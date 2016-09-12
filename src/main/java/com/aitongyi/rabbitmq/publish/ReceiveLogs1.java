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
//�����ǰû�ж��б��󶨵�����������Ϣ������������Ϊû�������߼�����������Ϣ��������
public class ReceiveLogs1 {
	private static final String EXCHANGE_NAME = "logs";

	public static void main(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
		//��Java�ͻ��ˣ��ṩqueuedeclare()Ϊ���Ǵ���һ���ǳ־û����������Զ�ɾ���Ķ������ơ�
		String queueName = channel.queueDeclare().getQueue();
		//�������ǾͿ��Ծͽ����ǵĶ��и����������а�  ִ������δ������־�������Ὣ��Ϣ���ӵ����ǵĶ����С�
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