package org.rlms.rabbitmq.vo;

import java.util.List;

import org.rlms.rabbitmq.consumer.BaseConsumer;

public class ConsumerInitVO<M, A> {

    private Class<? extends BaseConsumer<M, A>> consumerClz;

    private Class<?>[] constructorClz;

    private Object[] constructors;

    private List<String> routingKeys;

    public Class<? extends BaseConsumer<M, A>> getConsumerClz() {
        return consumerClz;
    }

    public void setConsumerClz(Class<? extends BaseConsumer<M, A>> consumerClz) {
        this.consumerClz = consumerClz;
    }

    public Class<?>[] getConstructorClz() {
        return constructorClz;
    }

    public void setConstructorClz(Class<?>... constructorClz) {
        this.constructorClz = constructorClz;
    }

    public Object[] getConstructors() {
        return constructors;
    }

    public void setConstructors(Object... constructors) {
        this.constructors = constructors;
    }

    public List<String> getRoutingKeys() {
        return routingKeys;
    }

    public void setRoutingKeys(List<String> routingKeys) {
        this.routingKeys = routingKeys;
    }

}
