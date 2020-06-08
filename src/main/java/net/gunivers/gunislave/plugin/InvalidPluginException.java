package net.gunivers.gunislave.plugin;

public class InvalidPluginException extends RuntimeException {

    public InvalidPluginException(String format) {
        super(format);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
