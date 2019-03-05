package org.rlms.rabbitmq.sample.queue;

import org.rlms.common.services.ServiceFacade;

public class SampleReceive {

    public static void main(String[] argv) {
        ServiceFacade.getRabbitMQService().simpleReceiveMessage(FirstConsumer.class);
    }


}
