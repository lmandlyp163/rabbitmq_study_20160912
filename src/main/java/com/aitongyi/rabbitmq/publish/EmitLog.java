/**
 * TODO
 * 
 */
package com.aitongyi.rabbitmq.publish;

/**
 * @author hushuang
 *
 */

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


		/*direct ��ֱ����
		topic �����⣩
		headers �����⣩
		fanout ���ַ���Ҳ�з���Ϊ�ȳ��ġ�*/
        //�������Ĺ����У����ǽ�ʹ�á�fanout�����ʹ���һ������Ϊ logs�Ľ�������
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

//		�ַ���Ϣ
		for(int i = 0 ; i < 5; i++){
			String message = "Hello World! " + i;
			/*��һ���������ǽ����������ơ�������롰�����ַ�������ʾʹ��Ĭ�ϵ������������� 
				�ڶ��������ǡ�routingKey��·������ 	*/
			//�����ṩһ�����ַ�����routingkey�����Ĺ��ܱ��������ķַ����ʹ����ˡ�
			 channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
		     System.out.println(" [x] Sent '" + message + "'");
		}
        channel.close();
        connection.close();
    }
}
