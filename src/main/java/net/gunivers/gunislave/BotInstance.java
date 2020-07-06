package net.gunivers.gunislave;

import java.time.Duration;

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
	 * @param BotConfig la configuration du bot.
	 */
	public BotInstance(BotConfig config)
	{
		if (config.hasToken())
			throw new IllegalArgumentException("Vous devez indiquez votre token en argument !");

		System.out.println("Build Discord Client...");
		DiscordClientBuilder builder = new DiscordClientBuilder(config.token());

		// En cas de déconnection imprévue, tente de se reconnecter à l'infini (ie valeur maximale)
		builder.setRetryOptions(new RetryOptions(Duration.ofSeconds(30), Duration.ofMinutes(1), Integer.MAX_VALUE, Schedulers.single()));
		builder.setInitialPresence(Presence.doNotDisturb(Activity.watching("Démarrage...")));
		this.bot = new GuniBot(builder.build());

		EventDispatcher dispatcher = this.bot.getEventDispatcher();

		// Initializing Events (nécessaire pour l'initialisation du bots et de ses données)
		dispatcher.on(ReadyEvent.class).take(1).subscribe(event ->
		{
			// code éxécuté qu'une seule fois lorsque le bot est connecté à discord
			this.bot.updatePresence(Presence.online(Activity.listening("/help"))).subscribe();
		});
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
