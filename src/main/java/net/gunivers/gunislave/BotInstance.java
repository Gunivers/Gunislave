package net.gunivers.gunislave;

import java.time.Duration;

import net.gunivers.gunislave.data.Database;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.gateway.retry.RetryOptions;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class BotInstance
{
	private GuniBot bot;

	/**
	 * créé le bot à partir du token donné et l'initialise.
	 *
	 * @param config la configuration du bot.
	 */
	public BotInstance(BotConfig config)
	{
		System.out.println("Build Discord Client...");
		DiscordClientBuilder builder = new DiscordClientBuilder(config.token());

		// Try infinite reconnection (ie maximal value)
		builder.setRetryOptions(new RetryOptions(Duration.ofSeconds(30), Duration.ofMinutes(1), Integer.MAX_VALUE, Schedulers.single()));
		builder.setInitialPresence(Presence.doNotDisturb(Activity.watching("Launching...")));
		this.bot = new GuniBot(builder.build());

		if (Database.isEnabled() && !Database.init(config.credentials()))
			throw new ExceptionInInitializerError("Could not initialize database");

		EventDispatcher dispatcher = this.bot.getEventDispatcher();

		// Initializing Events (mandatory for bot initialization)
		dispatcher
			.on(ReadyEvent.class)
			.take(1)
			.flatMap(event -> this.bot.updatePresence(Presence.online(Activity.listening("/help"))))
			.subscribe();
	}

	/**
	 * Connecte le bot en bloquant le thread
	 */
	public Mono<Void> login()
	{
		if (!this.bot.isConnected())
			return this.bot.login();
		else
			throw new IllegalStateException("The client is already connected!");
	}

	public Mono<Void> shutdown()
	{
		if (this.bot.isConnected())
			return this.bot.logout();
		else
			throw new IllegalStateException("The client is not connected!");
	}

	public GuniBot getBotClient() { return this.bot; }
}
