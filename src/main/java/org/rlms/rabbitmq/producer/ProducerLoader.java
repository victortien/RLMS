package org.rlms.rabbitmq.producer;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.rlms.common.exception.BaseException;
import org.rlms.rabbitmq.core.EndPoint;
import org.rlms.rabbitmq.core.EndPointFactory;
import org.rlms.rabbitmq.core.MessageType;
import org.rlms.rabbitmq.util.MessageUtil;
import org.rlms.rabbitmq.vo.ConnectionInfo;

import com.rabbitmq.client.MessageProperties;

public class ProducerLoader {

    private MessageType type;

    private ConnectionInfo messageInfo;

    private List<String> routingKeys;

    private EndPoint endPoint;

    private Serializable messageContent;

    private ProducerLoader(String endPointName, MessageType type, List<String> routingKeys, Serializable messageContent) {
        this.type = type;
        this.messageContent = messageContent;
        this.routingKeys = routingKeys;
        this.messageInfo = MessageUtil.getConnectionInfo(endPointName);
        this.endPoint = EndPointFactory.getEndPoint(type).of(messageInfo);
    }

    public static ProducerLoader decorate(String endPointName, MessageType type, List<String> routingKeys, Serializable messageContent) {
        return new ProducerLoader(endPointName, type, routingKeys, messageContent);
    }

    public void sendMessage() {
        try {
            if(MessageType.QUEUE.equals(type)) {
                endPoint.getChannel().basicPublish("", messageInfo.getEndPointName(), MessageProperties.PERSISTENT_TEXT_PLAIN, SerializationUtils.serialize(messageContent));
            }
            if(MessageType.TOPIC.equals(type)) {
                for(String routingKey : routingKeys) {
                    endPoint.getChannel().basicPublish(messageInfo.getEndPointName(), routingKey, MessageProperties.TEXT_PLAIN, SerializationUtils.serialize(messageContent));
                }
            }
        } catch (IOException ex) {
            throw new BaseException(ex);
        } finally {
            endPoint.close();
        }
    }

}
