package com.lm.Qos_message_distribute;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * 消息分发
 *
 * @author
 * @create 2018-06-06 上午9:51
 **/
public class Qos_Distribute_C02 {

    private final static String QUEUE_NAME = "demo_01_queue01"; //消息队列名

    public static void main(String[] argv) throws Exception {

        //打开连接和创建频道，与发送端一样
        ConnectionFactory factory = new ConnectionFactory();
        //设置MabbitMQ所在主机ip或者主机名
        factory.setHost("127.0.0.1");
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.basicQos(1);//保证一次只分发一个


        System.out.println("Waiting for messages. To exit press CTRL+C");
        //创建队列消费者
//        QueueingConsumer consumer = new QueueingConsumer(channel);
        //指定消费队列
        // 创建队列消费者
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException, UnsupportedEncodingException {
                String message = new String(body, "UTF-8");

                System.out.println(" [x] Received '" + message + "'");
                try {
                    System.out.println("curtime:" + System.currentTimeMillis() + "Received '" + message + "'");
                    Thread.sleep(10000);//消费者2 睡10秒
                } catch (InterruptedException e) {

                } finally {
                    System.out.println(" [x] Done! at " + new Date().toLocaleString());
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.basicConsume(QUEUE_NAME, false, consumer);
    }

}
