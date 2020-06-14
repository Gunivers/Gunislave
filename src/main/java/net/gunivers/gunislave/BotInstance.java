package net.gunivers.gunislave;

import java.time.Duration;

import discord4j.core.DiscordClient;
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
	private DiscordClient botClient;
	private BotConfig config;

	/**
	 * créé le bot à partir du token donné et l'initialise.
	 *
	 * @param BotConfig la configuration du bot.
	 */
	public BotInstance(BotConfig config)
	{
		this.config = config;

		if (config.hasToken())
			throw new IllegalArgumentException("Vous devez indiquez votre token en argument !");

		System.out.println("Build Discord Client...");
		DiscordClientBuilder builder = new DiscordClientBuilder(config.token());

		// En cas de déconnection imprévue, tente de se reconnecter à l'infini (ie valeur maximale)
		builder.setRetryOptions(new RetryOptions(Duration.ofSeconds(30), Duration.ofMinutes(1), Integer.MAX_VALUE, Schedulers.single()));
		builder.setInitialPresence(Presence.doNotDisturb(Activity.watching("Démarrage...")));
		this.botClient = builder.build();

		EventDispatcher dispatcher = this.botClient.getEventDispatcher();

		// Initializing Events (nécessaire pour l'initialisation du bots et de ses données)
		dispatcher.on(ReadyEvent.class).take(1).subscribe(event ->
		{
			// code éxécuté qu'une seule fois lorsque le bot est connecté à discord
			this.botClient.updatePresence(Presence.online(Activity.listening("/help"))).subscribe();
		});
	}

	/**
	 * Connecte le bot en bloquant le thread
	 */
	public Mono<Void> login()
	{
		if (!this.botClient.isConnected())
			return this.botClient.login();
		else
			throw new IllegalStateException("The client is already connected!");
	}

	public Mono<Void> shutdown()
	{
		if (this.botClient.isConnected())
			return this.botClient.logout();
		else
			throw new IllegalStateException("The client is not connected!");
	}

	public DiscordClient getBotClient() { return this.botClient; }
	public BotConfig getConfig() { return this.config; }
}
