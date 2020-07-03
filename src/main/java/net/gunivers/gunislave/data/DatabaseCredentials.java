package net.gunivers.gunislave.data;

public record DatabaseCredentials(String user, String host, int port, String password, String database)
{
}
