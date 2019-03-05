package org.rlms.rabbitmq.core;

import java.io.IOException;
import java.util.List;

import org.rlms.rabbitmq.vo.ConnectionInfo;

public class TopicEndPoint extends EndPoint {

    @Override
    protected void registerEndPoint(ConnectionInfo connectionInfo) throws IOException {
        boolean durable = false;
        boolean autoDelete = false;
        channel.exchangeDeclare(connectionInfo.getEndPointName(), "topic", durable, autoDelete, null);
    }

    @Override
    public String getBindingQueue(List<String> routingKeys) throws IOException {
        String queueName = channel.queueDeclare().getQueue();
        for(String bindingKey : routingKeys) {
            channel.queueBind(queueName, connectionInfo.getEndPointName(), bindingKey);
        }
        return queueName;
    }

}
