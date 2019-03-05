package org.rlms.rabbitmq.services;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.rlms.common.exception.BaseException;
import org.rlms.common.util.LogUtil;
import org.rlms.common.util.StreamUtil;
import org.rlms.common.vo.CommonHashModel;
import org.rlms.rabbitmq.consumer.BaseConsumer;
import org.rlms.rabbitmq.consumer.ConsumerConfig;
import org.rlms.rabbitmq.consumer.RabbitConsumer;
import org.rlms.rabbitmq.core.EndPoint;
import org.rlms.rabbitmq.core.EndPointFactory;
import org.rlms.rabbitmq.core.MessageType;
import org.rlms.rabbitmq.producer.ProducerLoader;
import org.rlms.rabbitmq.util.MessageUtil;
import org.rlms.rabbitmq.vo.ConnectionInfo;
import org.rlms.rabbitmq.vo.ConsumerInitVO;

import com.rabbitmq.client.Channel;

public class RabbitMQService {

    public void sendMessage(String endPointName, MessageType type, Serializable messageContent) {
        sendMessage(endPointName, type, Arrays.asList(""), messageContent);
    }

    public void sendMessage(String endPointName, MessageType type, List<String> routingKeys, Serializable messageContent) {
        try {
            ProducerLoader.decorate(endPointName, type, routingKeys, messageContent).sendMessage();
        } catch (Exception ex) {
            LogUtil.error("Send Message fail, with : " + ex.getMessage());
            throw new BaseException(ex);
        }
    }

    public <M, A> BaseConsumer<M, A> simpleReceiveMessage(Class<? extends BaseConsumer<M, A>> consumerClz) {
        ConsumerInitVO<M, A> consumerInitVO = new ConsumerInitVO<>();
        consumerInitVO.setConsumerClz(consumerClz);
        return receiveMessage(consumerInitVO);
    }

    public <M, A> BaseConsumer<M, A> receiveMessage(ConsumerInitVO<M, A> consumerInitVO) {
        try {
            RabbitConsumer anno = consumerInitVO.getConsumerClz().getAnnotation(RabbitConsumer.class);

            ConsumerConfig config = new ConsumerConfig();
            config.setType(anno.messageType());
            config.setDisposible(anno.isDisposible());
            config.setEndPointName(anno.endPointName());
            config.setRoutingKeys(CollectionUtils.isNotEmpty(consumerInitVO.getRoutingKeys()) ? consumerInitVO.getRoutingKeys() : Arrays.asList(""));

            BaseConsumer<M, A> consumer = consumerInitVO.getConsumerClz().getConstructor(consumerInitVO.getConstructorClz()).newInstance(consumerInitVO.getConstructors());
            consumer.initConfig(config);
            return consumer;
        } catch (Exception ex) {
            LogUtil.error("init Consumer fail, with : " + ex.getMessage());
            throw new BaseException(ex);
        }
    }

    public <M, A> BaseConsumer<M, A> receiveMessage(String endPointName, MessageType type, boolean isDisposible, ConsumerInitVO<M, A> consumerInitVO) {
        try {
            ConsumerConfig config = new ConsumerConfig();
            config.setType(type);
            config.setDisposible(isDisposible);
            config.setEndPointName(endPointName);
            config.setRoutingKeys(CollectionUtils.isNotEmpty(consumerInitVO.getRoutingKeys()) ? consumerInitVO.getRoutingKeys() : Arrays.asList(""));

            BaseConsumer<M, A> consumer = consumerInitVO.getConsumerClz().getConstructor(consumerInitVO.getConstructorClz()).newInstance(consumerInitVO.getConstructors());
            consumer.initConfig(config);
            return consumer;
        } catch (Exception ex) {
            LogUtil.error("init Consumer fail, with : " + ex.getMessage());
            throw new BaseException(ex);
        }
    }

    public void initEndPoints(CommonHashModel<String, MessageType> endPoints) {
        StreamUtil.ofNullable(endPoints.getkeySet()).forEach(endPointName -> {
            ConnectionInfo connection = MessageUtil.getConnectionInfo(endPointName);

            MessageType type = endPoints.getDataFromMap(endPointName);
            EndPoint endPoint = EndPointFactory.getEndPoint(type).of(connection);
            endPoint.close();
        });
    }

    public void deleteQueues(List<String> queues, boolean ifUnused, boolean ifEmpty) {
        StreamUtil.ofNullable(queues).forEach(queue -> {
            try {
                ConnectionInfo connection = MessageUtil.getConnectionInfo(queue);
                EndPoint endPoint = EndPointFactory.getEndPoint(MessageType.QUEUE).of(connection);
                Channel channel = endPoint.getChannel();
                channel.queueDelete(queue, ifUnused, ifEmpty);
                endPoint.close();
            } catch (IOException e) {
                throw new BaseException("delete queue: " + queue + " fail. ", e);
            }
        });
    }
}
