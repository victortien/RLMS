package org.rlms.rabbitmq.consumer;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.SerializationUtils;
import org.rlms.common.exception.BaseException;
import org.rlms.common.util.LogUtil;
import org.rlms.rabbitmq.core.EndPoint;
import org.rlms.rabbitmq.core.EndPointFactory;
import org.rlms.rabbitmq.util.MessageUtil;
import org.rlms.rabbitmq.vo.ConnectionInfo;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public abstract class BaseConsumer<M, A> {

    private ConnectionInfo connectionInfo;

    private EndPoint endPoint;

    private ConsumerConfig config;

    private Lock lock = new ReentrantLock();

    public void initConfig(ConsumerConfig config) {
        this.config = config;
        initEndPoint();
        initConsumer();
    }

    private void initEndPoint() {
        connectionInfo = MessageUtil.getConnectionInfo(config.getEndPointName());
        endPoint = EndPointFactory.getEndPoint(config.getType()).of(connectionInfo);
    }

    private void initConsumer() {
        try {
            boolean autoAck = false ;
            Channel channel = endPoint.getChannel();

            channel.basicConsume(endPoint.getBindingQueue(config.getRoutingKeys()), autoAck, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, BasicProperties properties, byte[] messageBody) throws IOException {
                    lock.lock();
                    try {
                        M messageContent = SerializationUtils.deserialize(messageBody);
                        onMessage(messageContent);

                        if(endPoint.isAlive())
                            channel.basicAck(envelope.getDeliveryTag(), false);

                        if(config.isDisposible() && endPoint.isAlive())
                            endPoint.close();

                    } catch (Exception ex) {
                        LogUtil.error(ex.getMessage());
                        throw ex;
                    } finally {
                        lock.unlock();
                    }
                }
            });
        } catch (IOException ex) {
            throw new BaseException(ex);
        }
    }

    public abstract void onMessage(M messageContent);

    public abstract A getAcknowledge();

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public EndPoint getEndPoint() {
        return endPoint;
    }

    public ConsumerConfig getConfig() {
        return config;
    }

}
