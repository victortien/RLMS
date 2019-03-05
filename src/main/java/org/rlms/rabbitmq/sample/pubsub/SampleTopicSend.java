package org.rlms.rabbitmq.sample.pubsub;

import org.rlms.rabbitmq.core.MessageType;

import org.rlms.common.services.ServiceFacade;
import org.rlms.common.vo.CommonHashModel;

public class SampleTopicSend {

    public static void main(String[] argv) {
        CommonHashModel<String, Object> messageContent = new CommonHashModel<>();
        messageContent.putDataToMap("SEND_VALUE", "this is the topic msg!!!");
        ServiceFacade.getRabbitMQService().sendMessage("ctt", MessageType.TOPIC, messageContent);
    }
}
