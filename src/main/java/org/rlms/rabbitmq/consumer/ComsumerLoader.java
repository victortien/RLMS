package org.rlms.rabbitmq.consumer;

import java.util.Set;

import org.reflections.Reflections;
import org.rlms.common.exception.BaseException;
import org.rlms.rabbitmq.core.RabbitConst;

public class ComsumerLoader {

    private ComsumerLoader() {}

    private static final ComsumerLoader instance = new ComsumerLoader();

    public static ComsumerLoader getInstance() {
        return instance;
    }

    public void startup () {
        try {
            Reflections reflections = new Reflections(RabbitConst.COUSUMER_PACKAGE);
            Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(RabbitConsumer.class);
            for (Class<?> consumerClz : annotated) {
                RabbitConsumer config = consumerClz.getAnnotation(RabbitConsumer.class);

                ConsumerConfig messageInfo = new ConsumerConfig();
                messageInfo.setEndPointName(config.endPointName());

                consumerClz.getConstructor(ConsumerConfig.class).newInstance(messageInfo);
            }
        } catch (Exception ex) {
            throw new BaseException(ex);
        }
    }

}
