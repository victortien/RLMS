package org.rlms.rabbitmq.core;

import org.rlms.common.util.PropertiesUtil;

public class RabbitConst {

    private RabbitConst() {
    }

    public static final String CONFIG_FILE = "rabbitmq";

    public static final String CONFIG_HOST = PropertiesUtil.getResourceBundle("rabbitmq.host", CONFIG_FILE);

    public static final String CONFIG_PORT = PropertiesUtil.getResourceBundle("rabbitmq.port", CONFIG_FILE);

    public static final String CONFIG_USER = PropertiesUtil.getResourceBundle("rabbitmq.username", CONFIG_FILE);

    public static final String CONFIG_PASSWORD = PropertiesUtil.getResourceBundle("rabbitmq.password", CONFIG_FILE);

    public static final String COUSUMER_PACKAGE = "org.rlms.rabbitmq.consumer";
}
