package org.rlms.rabbitmq.consumer;

import java.util.List;

import org.rlms.rabbitmq.core.MessageType;

public class ConsumerConfig {

    private MessageType type;

    private String endPointName;

    private boolean isDisposible;

    private List<String> routingKeys;

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getEndPointName() {
        return endPointName;
    }

    public void setEndPointName(String endPointName) {
        this.endPointName = endPointName;
    }

    public boolean isDisposible() {
        return isDisposible;
    }

    public void setDisposible(boolean isDisposible) {
        this.isDisposible = isDisposible;
    }

    public List<String> getRoutingKeys() {
        return routingKeys;
    }

    public void setRoutingKeys(List<String> routingKeys) {
        this.routingKeys = routingKeys;
    }

}
