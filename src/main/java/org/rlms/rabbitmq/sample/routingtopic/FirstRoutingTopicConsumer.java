package org.rlms.rabbitmq.sample.routingtopic;

import org.rlms.common.util.LogUtil;
import org.rlms.common.vo.CommonHashModel;
import org.rlms.rabbitmq.consumer.BaseConsumer;
import org.rlms.rabbitmq.consumer.RabbitConsumer;
import org.rlms.rabbitmq.core.MessageType;

@RabbitConsumer(endPointName = "routingtopic", isDisposible = false, messageType = MessageType.TOPIC)
public class FirstRoutingTopicConsumer extends BaseConsumer<CommonHashModel<String, Object>, Void> {

    private String consturct;

    public FirstRoutingTopicConsumer(String consturct) {
        this.consturct = consturct;
    }

    @Override
    public void onMessage(CommonHashModel<String, Object> messageContent) {
        LogUtil.info(" [CONSUMED ROUTING TOPIC: {}, consturct:{} ]", messageContent.getString("SEND_VALUE"), consturct);
    }

    @Override
    public Void getAcknowledge() {
        return null;
    }

}
