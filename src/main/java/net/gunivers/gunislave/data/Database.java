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
	private static Database DB;

	public static boolean init(DatabaseCredentials credentials)
	{
		if (DB != null)
			throw new IllegalStateException("DataBase already initialized");

		System.out.println("[DataBase] Initializing...");
		DB = new Database();

		boolean success = false;

		try
		{
			success = DB.connect(credentials);
		} catch (Throwable t)
		{
			t.printStackTrace();
		}

		if (success)
			System.out.println("[DataBase] Initialized!");
		else
			System.err.println("[DataBase] Couldn't connect to database");

		return success;
	}

	public static Database db() { return DB; }
	static Mono<Connection> connection() { return DB.pool.create(); }

	private ConnectionPool pool;

	private boolean connect(DatabaseCredentials credentials)
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

		this.pool = new ConnectionPool(ConnectionPoolConfiguration.builder(factory).build());
		return true;
	}

	public void disconnect()
	{
		System.out.println("[DataBase] Disconnecting...");
		this.pool.close();
		System.out.println("[DataBase] Disconnected!");
	}
}