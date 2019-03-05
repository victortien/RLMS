package org.rlms.rabbitmq.sample.queue;

import org.rlms.common.util.LogUtil;
import org.rlms.common.vo.CommonHashModel;
import org.rlms.rabbitmq.consumer.BaseConsumer;
import org.rlms.rabbitmq.consumer.RabbitConsumer;
import org.rlms.rabbitmq.core.MessageType;

@RabbitConsumer(endPointName = "cqq", isDisposible = true, messageType = MessageType.QUEUE)
public class FirstConsumer extends BaseConsumer<CommonHashModel<String, Object>, String> {

    @Override
    public void onMessage(CommonHashModel<String, Object> messageContent) {
        LogUtil.info(" [CONSUMED: {} ]", messageContent.getString("SEND_VALUE"));
    }

    @Override
    public String getAcknowledge() {
        return "DONE!!";
    }

}
