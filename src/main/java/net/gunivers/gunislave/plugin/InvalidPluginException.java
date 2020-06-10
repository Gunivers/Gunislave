package net.gunivers.gunislave.plugin;

public class InvalidPluginException extends Exception
{
	private static final long serialVersionUID = 3639558604486225492L;

	public InvalidPluginException(String format)
	{
		super(format);
	}

	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
}
