/**
 * TODO
 * 
 */
package com.aitongyi.rabbitmq.publish;

/**
 * @author hushuang
 *
 */
import com.rabbitmq.client.*;

import java.io.IOException;
//如果当前没有队列被绑定到交换器，消息将被丢弃，因为没有消费者监听，这条消息将被丢弃
public class ReceiveLogs1 {
	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		//在Java客户端，提供queuedeclare()为我们创建一个非持久化、独立、自动删除的队列名称。
		String queueName = channel.queueDeclare().getQueue();
		//现在我们就可以就将我们的队列跟交换器进行绑定  执行完这段代码后，日志交换器会将消息添加到我们的队列中。
		channel.queueBind(queueName, EXCHANGE_NAME, "");

		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
				String message = new String(body, "UTF-8");
				System.out.println(" [x] Received '" + message + "'");
			}
		};
		channel.basicConsume(queueName, true, consumer);
	}
}
