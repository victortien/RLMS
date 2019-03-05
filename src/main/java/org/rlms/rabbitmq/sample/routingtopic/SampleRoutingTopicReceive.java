package org.rlms.rabbitmq.sample.routingtopic;

import java.util.Arrays;

import org.rlms.common.services.ServiceFacade;
import org.rlms.common.vo.CommonHashModel;
import org.rlms.rabbitmq.vo.ConsumerInitVO;

public class SampleRoutingTopicReceive {

    public static void main(String[] argv) {
        ConsumerInitVO<CommonHashModel<String, Object>, Void> consumerInitVO = new ConsumerInitVO<>();
        consumerInitVO.setConsumerClz(FirstRoutingTopicConsumer.class);
        consumerInitVO.setConstructorClz(String.class);
        consumerInitVO.setConstructors("Im a RoutingTopic");
        consumerInitVO.setRoutingKeys(Arrays.asList("route_1"));

        ServiceFacade.getRabbitMQService().receiveMessage(consumerInitVO);
    }

}
