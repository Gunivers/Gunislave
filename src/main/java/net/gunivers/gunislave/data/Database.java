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
	private static boolean ENABLED;

	public final static boolean init(DatabaseCredentials credentials) throws IllegalStateException
	{
		if (!ENABLED)
			throw new IllegalStateException("Cannot initialize a disabled database");

		if (INITIALIZED)
			throw new IllegalStateException("Database already initialized");

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

	private final static boolean connect(DatabaseCredentials credentials)
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

	public final static void disconnect()
	{
		System.out.println("[DataBase] Disconnecting...");
		POOL.close();
		System.out.println("[DataBase] Disconnected!");
	}

	public final static void enable()  { Database.setEnabled(true);  }
	public final static void disable() { Database.setEnabled(false); }
	public final static void setEnabled(boolean enabled) throws IllegalStateException
	{
		if (INITIALIZED)
			throw new IllegalStateException("Cannot enable nor disable database after initialization");

		ENABLED = enabled;
	}

	final static Mono<Connection> connection()	{ return POOL.create(); }
	public final static boolean isInitialized()	{ return INITIALIZED; }
	public final static boolean isEnabled()		{ return ENABLED; }
}