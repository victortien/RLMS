package org.rlms.rabbitmq.util;

import org.rlms.common.util.MathUtil;
import org.rlms.rabbitmq.core.RabbitConst;
import org.rlms.rabbitmq.vo.ConnectionInfo;

public class MessageUtil {

    private MessageUtil() {}

    public static ConnectionInfo getConnectionInfo(String endPointName) {
        ConnectionInfo messageInfo = new ConnectionInfo();
        messageInfo.setEndPointName(endPointName);
        messageInfo.setHost(RabbitConst.CONFIG_HOST);
        messageInfo.setPort(MathUtil.toBigDecimal(RabbitConst.CONFIG_PORT).intValue());
        messageInfo.setUserName(RabbitConst.CONFIG_USER);
        messageInfo.setPassword(RabbitConst.CONFIG_PASSWORD);
        return messageInfo;
    }

}
