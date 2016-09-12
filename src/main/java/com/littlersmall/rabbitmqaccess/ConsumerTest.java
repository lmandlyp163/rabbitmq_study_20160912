/**  
 * Project Name:rabbitmq-access  
 * File Name:ConsumerTest.java  
 * Package Name:com.littlersmall.rabbitmqaccess  
 * Date:2016年8月27日下午2:15:44  
 * Copyright (c) 2016,  All Rights Reserved.  
 *  
*/

package com.littlersmall.rabbitmqaccess;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.amqp.rabbit.support.MessagePropertiesConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

import com.littlersmall.rabbitmqaccess.common.DetailRes;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

/**
 * ClassName:ConsumerTest <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: 2016年8月27日 下午2:15:44 <br/>
 * 
 * @author hzlimao
 * @version
 * @since JDK 1.8
 * @see
 */
public class ConsumerTest {
	public final static CachingConnectionFactory connectionFactory;
    private MessageConsumer messageConsumer;
    private final static ConsumerTest consumerTest;
	static{
        String ip = "10.165.124.32";
        int port = 5672;
        String userName ="admin";
        String password = "f324fd2dwewe";
        System.out.println("ip:"+ip+"-----------------------");
        connectionFactory = new CachingConnectionFactory(ip, port);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(password);
        connectionFactory.setPublisherConfirms(true);
        consumerTest=new ConsumerTest();
	}
	public static void main(String[] args) {
		consumerTest.test();
	}
	public void test() {
		String SEARCH_QUEUE="search_queuename";//搜索数据推送的队列
		String SEARCH_EXCHANG="search_exchange";//搜索数据推送的队列
		String SEARCH_ROUTING="search_routingKey";//搜索数据推送的队列
		try {
			messageConsumer=consumerTest.buildMessageConsumer(SEARCH_EXCHANG, SEARCH_ROUTING, SEARCH_QUEUE, new SearchMessageProcess());
			messageConsumer.consume();
		} catch (IOException e) {
			e.printStackTrace();  
		}
	}

	public <T> MessageConsumer buildMessageConsumer(String exchange, String routingKey, final String queue,
			final MessageProcess<T> messageProcess) throws IOException {
		final Connection connection = connectionFactory.createConnection();
		// 1
		buildQueue(exchange, routingKey, queue, connection);
		// 2
		final MessagePropertiesConverter messagePropertiesConverter = new DefaultMessagePropertiesConverter();
		final MessageConverter messageConverter = new Jackson2JsonMessageConverter();
		// 3
		return new MessageConsumer() {
			QueueingConsumer consumer;
			{
				consumer = buildQueueConsumer(connection, queue);
			}

			@Override
			// 1 通过delivery获取原始数据
			// 2 将原始数据转换为特定类型的包
			// 3 处理数据
			// 4 手动发送ack确认
			public DetailRes consume() {
				QueueingConsumer.Delivery delivery = null;
				Channel channel = consumer.getChannel();
				try {
					// 1 如果没有消息会一直
					delivery = consumer.nextDelivery();
					Message message = new Message(delivery.getBody(), messagePropertiesConverter
							.toMessageProperties(delivery.getProperties(), delivery.getEnvelope(), "UTF-8"));
					// 2
					@SuppressWarnings("unchecked")
					T messageBean = (T) messageConverter.fromMessage(message);
					System.out.println(messageBean);
					// 3
					DetailRes detailRes = messageProcess.process(messageBean);
					// 4
					if (detailRes.isSuccess()) {
						channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
					} else {
						channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
					}
					return detailRes;
				} catch (InterruptedException e) {
					e.printStackTrace();
					return new DetailRes(false, "interrupted exception " + e.toString());
				} catch (IOException e) {
					e.printStackTrace();
					retry(delivery, channel);
					return new DetailRes(false, "io exception " + e.toString());
				} catch (ShutdownSignalException e) {
					e.printStackTrace();
					try {
						channel.close();
					} catch (IOException io) {
						io.printStackTrace();
					} catch (TimeoutException e1) {
						e1.printStackTrace();  
						
					}
					consumer = buildQueueConsumer(connection, queue);
					return new DetailRes(false, "shutdown exception " + e.toString());
				} catch (Exception e) {
					e.printStackTrace();
					retry(delivery, channel);
					return new DetailRes(false, "exception " + e.toString());
				}
			}
		};
	}
	public void retry(QueueingConsumer.Delivery delivery, Channel channel) {
        try {
            if (null != delivery) {
                channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	public void buildQueue(String exchange, String routingKey, final String queue, Connection connection)
			throws IOException {
		Channel channel = connection.createChannel(false);
		channel.exchangeDeclare(exchange, "direct", true, false, null);
		channel.queueDeclare(queue, true, false, false, null);
		channel.queueBind(queue, exchange, routingKey);
		try {
			channel.close();
		} catch (Exception e) {// Timeout
			e.printStackTrace();
		}
	}

	public QueueingConsumer buildQueueConsumer(Connection connection, String queue) {
		Channel channel = connection.createChannel(false);
		QueueingConsumer consumer = new QueueingConsumer(channel);

		try {
			// 通过 BasicQos 方法设置prefetchCount =
			// 1。这样RabbitMQ就会使得每个Consumer在同一个时间点最多处理一个Message。
			// 换句话说，在接收到该Consumer的ack前，他它不会将新的Message分发给它
			channel.basicQos(1);
			channel.basicConsume(queue, false, consumer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return consumer;
	}
}
