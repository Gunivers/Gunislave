package net.gunivers.gunislave.data;

import static io.r2dbc.spi.ConnectionFactoryOptions.DATABASE;
import static io.r2dbc.spi.ConnectionFactoryOptions.DRIVER;
import static io.r2dbc.spi.ConnectionFactoryOptions.HOST;
import static io.r2dbc.spi.ConnectionFactoryOptions.PASSWORD;
import static io.r2dbc.spi.ConnectionFactoryOptions.PORT;
import static io.r2dbc.spi.ConnectionFactoryOptions.PROTOCOL;
import static io.r2dbc.spi.ConnectionFactoryOptions.SSL;
import static io.r2dbc.spi.ConnectionFactoryOptions.USER;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import reactor.core.publisher.Mono;

public final class Database
{
	private static ConnectionPool POOL;
	private static boolean INITIALIZED;

	public static boolean init(DatabaseCredentials credentials)
	{
		if (INITIALIZED)
			throw new IllegalStateException("DataBase already initialized");

		System.out.println("[DataBase] Initializing...");

		boolean success = false;

		try
		{
			success = Database.connect(credentials);
		} catch (Throwable t)
		{
			t.printStackTrace();
		}

		if (success)
		{
			System.out.println("[DataBase] Initialized!");
			INITIALIZED = true;
		}
		else
			System.err.println("[DataBase] Couldn't connect to database");

		return success;
	}

	private static boolean connect(DatabaseCredentials credentials)
	{
		ConnectionFactory factory = ConnectionFactories.get(ConnectionFactoryOptions.builder()
			.option(DRIVER, "mysql")
			.option(PROTOCOL, "mysql")
			.option(HOST, credentials.host())
			.option(PORT, credentials.port())
			.option(USER, credentials.user())
			.option(PASSWORD, credentials.password())
			.option(DATABASE, credentials.database())
			.option(SSL, false)
			.build());

		POOL = new ConnectionPool(ConnectionPoolConfiguration.builder(factory).build());
		return true;
	}

	public static void disconnect()
	{
		System.out.println("[DataBase] Disconnecting...");
		POOL.close();
		System.out.println("[DataBase] Disconnected!");
	}

	static Mono<Connection> connection() { return POOL.create(); }
}