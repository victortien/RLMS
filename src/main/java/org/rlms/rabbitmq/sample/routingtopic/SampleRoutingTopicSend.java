package org.rlms.rabbitmq.sample.routingtopic;

import java.util.Arrays;

import org.rlms.common.services.ServiceFacade;
import org.rlms.common.util.LogUtil;
import org.rlms.common.vo.CommonHashModel;
import org.rlms.rabbitmq.core.MessageType;

public class SampleRoutingTopicSend {

    public static void main(String[] argv) {
        CommonHashModel<String, Object> messageContent = new CommonHashModel<>();

        for(int i=0; i<100; i++) {
            LogUtil.info("already send: " + i);
            messageContent.putDataToMap("SEND_VALUE", "this is for : " + i + "!!!");
            ServiceFacade.getRabbitMQService().sendMessage("routingtopic", MessageType.TOPIC, Arrays.asList("route_1","route_2"), messageContent);
        }

    }
}
