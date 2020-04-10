package net.gunivers.gunislave;

import java.time.Duration;

import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.event.EventDispatcher;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;
import discord4j.gateway.retry.RetryOptions;
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

		if (config.getToken() == null)
			throw new IllegalArgumentException("Vous devez indiquez votre token en argument !");
		else
		{
			System.out.println("Build Discord Client...");
			DiscordClientBuilder builder = new DiscordClientBuilder(config.getToken());

			// En cas de déconnection imprévue, tente de se reconnecter à l'infini (1000 fois de suite)
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
	}

	/**
	 * Connecte le bot en bloquant le thread
	 */
	public void loginBlock()
	{
		if (!this.botClient.isConnected())
			this.botClient.login().block();
		else
			throw new IllegalStateException("The client is already connected!");
	}

	/**
	 * Connecte le bot en libérant le thread.<br>
	 * ATTENTION : Tout les threads créé par le bot sont des daemons. Si le thread
	 * principal meurt, la jvm s'arrète !
	 */
	public void loginSubscribe()
	{
		if (!this.botClient.isConnected())
			this.botClient.login().subscribe();
		else
			throw new IllegalStateException("The client is already connected!");
	}

	public void shutdown()
	{
		if (this.botClient.isConnected())
			this.botClient.logout().subscribe();
		else
			throw new IllegalStateException("The client is not connected!");
	}

	public DiscordClient getBotClient() { return this.botClient; }
	public BotConfig getConfig() { return this.config; }
}
