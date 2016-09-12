/**
 * TODO
 * 
 */
package com.aitongyi.rabbitmq.helloworld;

import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * ��Ϣ������
 * 
 * @author hushuang
 * 
 */
public class C {

	private final static String QUEUE_NAME = "search_queue";

	public static void main(String[] argv) throws Exception {
			
	        // �������ӹ���  
	        ConnectionFactory factory = new ConnectionFactory();  
	        //	      ����RabbitMQ��ַ  
			factory.setHost("10.165.124.32");
			factory.setPort(5672);
			factory.setUsername("admin");
			factory.setPassword("f324fd2dwewe");
	        //	      ����һ���µ�����  
	        Connection connection = factory.newConnection();  
	        //	      ����һ��Ƶ��  
	        final Channel channel = connection.createChannel();  
	        //	      ����Ҫ��ע�Ķ��� -- ��RabbitMQ�У������������ݵ��Եģ�һ���ݵȲ������ص�����������ִ����������Ӱ�����һ��ִ�е�Ӱ����ͬ����Ҳ����˵����������ڣ��ʹ�����������ڣ�������Ѿ����ڵĶ��в����κ�Ӱ�졣  
	        channel.queueDeclare(QUEUE_NAME, false, false, false, null);  
	        System.out.println("C [*] Waiting for messages. To exit press CTRL+C");  
	        //	  DefaultConsumer��ʵ����Consumer�ӿڣ�ͨ������һ��Ƶ�������߷�����������Ҫ�Ǹ�Ƶ������Ϣ�����Ƶ��������Ϣ���ͻ�ִ�лص�����handleDelivery  
	        Consumer consumer = new DefaultConsumer(channel) {  
	            @Override  
	            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {  
	                String message = new String(body, "UTF-8");  
	                System.out.println("C [x] Received '" + message + "'"); 
	                try {
						System.out.println("");
					} catch (Exception e) {
						
					}finally {
	                    System.out.println("Worker1 [x] Done");
	                    // ��Ϣ�������ȷ��
	                    channel.basicAck(envelope.getDeliveryTag(), false);
	                }
	            }  
	        };  
	        //�Զ��ظ�����Ӧ�� -- RabbitMQ�е���Ϣȷ�ϻ���
	        channel.basicConsume(QUEUE_NAME, false, consumer);  
	}
	
	

	@Test
	public void test2(){
		Double[] array={2.0,3.0};
		getMax(array);
	}
	
	public <T extends Number> T getMax(T[] array){
	    T max = null;
	    for(T element : array){
	        max = element;
	        System.out.println(max);
	    }
	    return max;
	}
}
