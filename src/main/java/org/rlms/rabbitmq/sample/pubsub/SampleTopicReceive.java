package org.rlms.rabbitmq.sample.pubsub;

import org.rlms.common.services.ServiceFacade;
import org.rlms.common.vo.CommonHashModel;
import org.rlms.rabbitmq.core.MessageType;
import org.rlms.rabbitmq.vo.ConsumerInitVO;

public class SampleTopicReceive {

    public static void main(String[] argv) {

        ConsumerInitVO<CommonHashModel<String, Object>, Void> consumerInitVO = new ConsumerInitVO<>();
        consumerInitVO.setConsumerClz(FirstTopicConsumer.class);

        ServiceFacade.getRabbitMQService().receiveMessage("ctt", MessageType.TOPIC, false, consumerInitVO);
    }
}
