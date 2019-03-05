package org.rlms.rabbitmq.core;

import java.io.IOException;
import java.util.List;

import org.rlms.rabbitmq.vo.ConnectionInfo;

public class QueueEndPoint extends EndPoint {

    @Override
    protected void registerEndPoint(ConnectionInfo connectionInfo) throws IOException {
        boolean durable = true;
        boolean exclusive = false;
        boolean autoDelete = false;
        channel.queueDeclare(connectionInfo.getEndPointName(), durable, exclusive, autoDelete, null);
    }

    @Override
    public String getBindingQueue(List<String> routingKeys) throws IOException {
        return connectionInfo.getEndPointName();
    }

}
