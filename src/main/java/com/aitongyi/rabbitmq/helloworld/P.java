/**
 * TODO
 * 
 */
package com.aitongyi.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
/**
 * ��Ϣ������
 * @author hushuang
 *
 */
public class P {

  private final static String QUEUE_NAME = "hello";

  public static void main(String[] argv) throws Exception {
	    // �������ӹ���  
      ConnectionFactory factory = new ConnectionFactory();  
//    ����RabbitMQ��ַ  
		factory.setHost("10.165.124.32");//:5672
		factory.setPort(5672);
		factory.setUsername("admin");
		factory.setPassword("f324fd2dwewe");
//    ����һ���µ�����  
      Connection connection = factory.newConnection();  
//    ����һ��Ƶ��  
      Channel channel = connection.createChannel();  
//    ����һ������ -- ��RabbitMQ�У������������ݵ��Եģ�һ���ݵȲ������ص�����������ִ����������Ӱ�����һ��ִ�е�Ӱ����ͬ����Ҳ����˵����������ڣ��ʹ�����������ڣ�������Ѿ����ڵĶ��в����κ�Ӱ�졣   
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
      String message = "Hello World!";  
//    ������Ϣ��������  
      channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));  
      System.out.println("P [x] Sent '" + message + "'");  
//    �ر�Ƶ��������  
      channel.close();  
      connection.close();  
  }
}