package net.gunivers.gunislave.util;

public class ParsingException extends Exception
{
	private static final long serialVersionUID = -2510389069329695021L;

	public ParsingException(String message) { super(message); }

	@Override public Throwable fillInStackTrace() { return this; }
}
