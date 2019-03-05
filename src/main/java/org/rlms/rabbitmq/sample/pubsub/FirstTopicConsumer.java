package org.rlms.rabbitmq.sample.pubsub;

import org.rlms.common.util.LogUtil;
import org.rlms.common.vo.CommonHashModel;
import org.rlms.rabbitmq.consumer.BaseConsumer;

public class FirstTopicConsumer extends BaseConsumer<CommonHashModel<String, Object>, Void>{

    @Override
    public void onMessage(CommonHashModel<String, Object> messageContent) {
        LogUtil.info(" [CONSUMED TOPIC: {} ]", messageContent.getString("SEND_VALUE"));
    }

    @Override
    public Void getAcknowledge() {
        return null;
    }

}
