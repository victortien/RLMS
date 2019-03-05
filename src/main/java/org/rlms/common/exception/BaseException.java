package org.rlms.common.exception;

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return buildMessage(super.getMessage(), super.getCause());
    }

    public String buildMessage(String message, Throwable cause) {
        if (cause == null) {
            return message;
        }
        StringBuilder sb = new StringBuilder(64);
        if (message != null) {
            sb.append(message).append("; ");
        }
        sb.append("nested exception is ").append(cause).append("; ");

        StackTraceElement[] ste = cause.getStackTrace();
        for(int i = 0; i < ste.length; i++) {
            sb.append(ste[i] + System.lineSeparator());
        }
        return sb.toString();
    }

}
