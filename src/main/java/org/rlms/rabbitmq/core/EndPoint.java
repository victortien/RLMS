package org.rlms.rabbitmq.core;

import java.io.IOException;
import java.util.List;

import org.rlms.common.exception.BaseException;
import org.rlms.rabbitmq.vo.ConnectionInfo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public abstract class EndPoint {

    protected Channel channel;
    protected Connection connection;
    protected ConnectionInfo connectionInfo;
    private boolean isAlive = false;

    private void init() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(connectionInfo.getHost());
            factory.setPort(connectionInfo.getPort());
            factory.setUsername(connectionInfo.getUserName());
            factory.setPassword(connectionInfo.getPassword());

            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.basicQos(1);
            isAlive = true;

            registerEndPoint(connectionInfo);
        } catch (IOException ex) {
            throw new BaseException(ex);
        }
    }

    public EndPoint of(ConnectionInfo messageInfo) {
        this.connectionInfo = messageInfo;
        init();
        return this;
    }

    protected abstract void registerEndPoint(ConnectionInfo connectionInfo) throws IOException;

    public abstract String getBindingQueue(List<String> routingKeys) throws IOException;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void close() {
        try {
            this.channel.close();
            this.connection.close();
            this.isAlive = false;
        } catch (IOException ex) {
            throw new BaseException(ex);
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

}
