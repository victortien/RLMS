package org.rlms.rabbitmq.core;

import java.util.EnumMap;

import org.rlms.common.exception.BaseException;

public class EndPointFactory {

    private EndPointFactory() {
    }

    public static EndPoint getEndPoint(MessageType type) {
        try {
            EnumMap<MessageType, Class<? extends EndPoint>> endPointMap = new EnumMap<>(MessageType.class);
            endPointMap.put(MessageType.TOPIC, TopicEndPoint.class);
            endPointMap.put(MessageType.QUEUE, QueueEndPoint.class);
            return endPointMap.get(type).newInstance();
        } catch (Exception e) {
            throw new BaseException(e);
        }
    }

}
