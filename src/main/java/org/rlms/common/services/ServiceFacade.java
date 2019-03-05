package org.rlms.common.services;

import org.rlms.lucene.core.LuceneSession;
import org.rlms.lucene.services.LuceneService;
import org.rlms.rabbitmq.services.RabbitMQService;

public class ServiceFacade {

    private ServiceFacade() {
    }

    public static LuceneService getLuceneService(LuceneSession session) {
        return session.getLuceneService();
    }

    public static RabbitMQService getRabbitMQService() {
        return new RabbitMQService();
    }

}
