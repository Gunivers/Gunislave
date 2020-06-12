package net.gunivers.gunislave.data;

import java.beans.PropertyVetoException;
import java.sql.Connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import reactor.core.publisher.Mono;

public class Database
{
	private static Database DB;

	public static boolean init(DatabaseCredentials credentials)
	{
		if (DB != null)
			throw new IllegalStateException("Database already initialized");

		System.out.println("[Database] Initializing...");
		DB = new Database();

		boolean success = DB.connect(credentials);

		if (success)
			System.out.println("[Database] Initialized!");
		else
			System.err.println("[Database] Couldn't connect to database");

		return success;
	}

	public static Database db() { return DB; }
	public static Mono<Connection> connection() { return Database.db().getConnection(); }

	private ComboPooledDataSource dataSource;
	private Mono<Connection> connection;

	private boolean connect(DatabaseCredentials credentials)
	{
		try
		{
			this.dataSource = new ComboPooledDataSource();
			this.dataSource.setDriverClass("com.mysql.jdbc.Driver");
			this.dataSource.setJdbcUrl(credentials.toURI());
			this.dataSource.setUser(credentials.user());
			this.dataSource.setPassword(credentials.password());
			this.dataSource.setMaxIdleTime(300000);
			this.dataSource.setMaxPoolSize(15);

			this.connection = Mono.fromCallable(this.dataSource::getConnection);
		} catch (PropertyVetoException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public void disconnect()
	{
		System.out.println("[Database] Disconnecting...");
		this.dataSource.close();
		System.out.println("[Database] Disconnected!");
	}

	public Mono<Connection> getConnection() { return this.connection; }
}
