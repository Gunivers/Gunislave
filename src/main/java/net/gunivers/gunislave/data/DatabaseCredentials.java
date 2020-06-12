package net.gunivers.gunislave.data;

public record DatabaseCredentials(String user, String host, int port, String password, String db)
{
	public String toURI() { return "jdbc:mysql://%s:%s/%s".formatted(this.host, this.port, this.db); }
}
