package org.rlms.rabbitmq.consumer;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.rlms.rabbitmq.core.MessageType;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RabbitConsumer {
    String endPointName();
    MessageType messageType();
    boolean isDisposible();
}
