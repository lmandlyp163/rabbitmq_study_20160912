package com.lm.demo_01;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 生产者生产消息
 *
 * @author lm
 * @create 2018-06-04 下午8:10
 **/
public class P {

    private final static String QUEUE_NAME_O1 = "demo_01_queue01"; //消息队列名
    private final static String QUEUE_NAME_02 = "demo_01_queue02"; //消息队列名
    private final static String EXCHANGE_NAME = "RabbitMQ_exchange_demo_01"; //交换机名称
    private final static String ROUTING_KEY_NAME = "RabbitMQ_routingKey_demo_01"; //路由键

    public static void main(String[] argv) throws Exception {
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("java", "hello");
        map.put("RabbitMQ", "Hello");

        //1.打开连接和创建频道
        ConnectionFactory factory = new ConnectionFactory();
        //设置MabbitMQ所在主机ip或者主机名  127.0.0.1即localhost
        factory.setHost("127.0.0.1");
        //2.创建连接
        Connection connection = factory.newConnection();
        //3.创建频道
        Channel channel = connection.createChannel();
        //4.声明交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        //5.声明创建队列
        channel.queueDeclare(QUEUE_NAME_O1, false, false, false, null);
        channel.queueDeclare(QUEUE_NAME_02, false, false, false, null);
        //6.绑定队列和交换器
        channel.queueBind(QUEUE_NAME_O1, EXCHANGE_NAME, ROUTING_KEY_NAME);
        channel.queueBind(QUEUE_NAME_02, EXCHANGE_NAME, ROUTING_KEY_NAME);
        //7.往转发器上发送消息
        //发送的消息
        String message = JSON.toJSONString(map);
        channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println("发送数据："+JSON.toJSONString(message));
        //8.关闭信道
        channel.close();
        //9.关闭连接
        connection.close();
    }
}
