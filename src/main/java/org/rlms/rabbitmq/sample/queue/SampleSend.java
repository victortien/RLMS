package org.rlms.rabbitmq.sample.queue;

import org.rlms.common.services.ServiceFacade;
import org.rlms.common.vo.CommonHashModel;
import org.rlms.rabbitmq.core.MessageType;

public class SampleSend {

    public static void main(String[] argv) {
        CommonHashModel<String, Object> messageContent = new CommonHashModel<>();
        messageContent.putDataToMap("SEND_VALUE", "this is the 332!!!");
        ServiceFacade.getRabbitMQService().sendMessage("cqq", MessageType.QUEUE, messageContent);
    }
}
