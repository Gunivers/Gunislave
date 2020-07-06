package net.gunivers.gunislave;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import discord4j.core.DiscordClient;
import discord4j.core.event.EventDispatcher;
import discord4j.core.object.Invite;
import discord4j.core.object.Region;
import discord4j.core.object.entity.ApplicationInfo;
import discord4j.core.object.entity.Channel;
import discord4j.core.object.entity.Guild;
import discord4j.core.object.entity.GuildEmoji;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.Role;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.Webhook;
import discord4j.core.object.presence.Presence;
import discord4j.core.object.util.Snowflake;
import discord4j.core.spec.GuildCreateSpec;
import discord4j.core.spec.UserEditSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class GuniBot
{
	private final Function<Snowflake, Mono<Channel>> getChannelById;
	private final Function<Snowflake, Mono<Guild>> getGuildById;

	private final BiFunction<Snowflake, Snowflake, Mono<GuildEmoji>> getGuildEmojiById;
	private final BiFunction<Snowflake, Snowflake, Mono<Member>> getMemberById;
	private final BiFunction<Snowflake, Snowflake, Mono<Message>> getMessageById;
	private final BiFunction<Snowflake, Snowflake, Mono<Role>> getRoleById;

	private final Function<Snowflake, Mono<User>> getUserById;
	private final Function<Snowflake, Mono<Webhook>> getWebhookById;


	private final Supplier<Mono<ApplicationInfo>> getApplicationInfo;
	private final Supplier<Flux<Guild>> getGuilds;
	private final Supplier<Flux<User>> getUsers;
	private final Supplier<Flux<Region>> getRegions;
	private final Supplier<Mono<User>> getSelf;
	private final Supplier<Optional<Snowflake>> getSelfId;

	private final Supplier<Mono<Void>> login;
	private final Supplier<Mono<Void>> logout;

	private final Supplier<Boolean> isConnected;
	private final Supplier<Long> getResponseTime;

	private final Function<Consumer<? super GuildCreateSpec>, Mono<Guild>> createGuild;
	private final Function<Presence, Mono<Void>> updatePresence;
	private final Function<String, Mono<Invite>> getInvite;
	private final Function<Consumer<? super UserEditSpec>, Mono<User>> edit;
	private final Supplier<EventDispatcher> getEventDispatcher;

	public GuniBot(DiscordClient client)
	{
		this.getChannelById = client::getChannelById;
		this.getGuildById = client::getGuildById;
		this.getGuildEmojiById = client::getGuildEmojiById;
		this.getMemberById = client::getMemberById;
		this.getMessageById = client::getMessageById;
		this.getRoleById = client::getRoleById;
		this.getUserById = client::getUserById;
		this.getWebhookById = client::getWebhookById;

		this.getApplicationInfo = client::getApplicationInfo;
		this.getGuilds = client::getGuilds;
		this.getUsers = client::getUsers;
		this.getRegions = client::getRegions;
		this.getSelf = client::getSelf;
		this.getSelfId = client::getSelfId;

		this.login = client::login;
		this.logout = client::logout;

		this.isConnected = client::isConnected;
		this.getResponseTime = client::getResponseTime;

		this.createGuild = client::createGuild;
		this.updatePresence = client::updatePresence;
		this.getInvite = client::getInvite;
		this.edit = client::edit;
		this.getEventDispatcher = client::getEventDispatcher;
	}

	/** @see DiscordClient#getChannelById(Snowflake) */
	public Mono<Channel> getChannelById(final Snowflake channelId) {
		return this.getChannelById.apply(channelId); }

	/** @see DiscordClient#getGuildById(Snowflake) */
	public Mono<Guild> getGuildById(final Snowflake guildId) {
		return this.getGuildById.apply(guildId); }

	/** @see DiscordClient#getGuildEmojiById(Snowflake, Snowflake) */
	public Mono<GuildEmoji> getGuildEmojiById(final Snowflake guildId, final Snowflake emojiId) {
		return this.getGuildEmojiById.apply(guildId, emojiId); }

	/** @see DiscordClient#getMemberById(Snowflake, Snowflake) */
	public Mono<Member> getMemberById(final Snowflake guildId, final Snowflake userId) {
		return this.getMemberById.apply(guildId, userId); }

	/** @see DiscordClient#getMessageById(Snowflake, Snowflake) */
	public Mono<Message> getMessageById(final Snowflake channelId, final Snowflake messageId) {
		return this.getMessageById.apply(channelId, messageId); }

	/** @see DiscordClient#getRoleById(Snowflake, Snowflake) */
	public Mono<Role> getRoleById(final Snowflake guildId, final Snowflake roleId) {
		return this.getRoleById.apply(guildId, roleId); }

	/** @see DiscordClient#getUserById(Snowflake) */
	public Mono<User> getUserById(final Snowflake userId) {
		return this.getUserById.apply(userId); }

	/** @see DiscordClient#getWebhookById(Snowflake) */
	public Mono<Webhook> getWebhookById(final Snowflake webhookId) {
		return this.getWebhookById.apply(webhookId); }

	/** @see DiscordClient#getApplicationInfo() */
	public Mono<ApplicationInfo> getApplicationInfo() {
		return this.getApplicationInfo.get(); }

	/** @see DiscordClient#getGuilds() */
	public Flux<Guild> getGuilds() {
		return this.getGuilds.get(); }

	/** @see DiscordClient#getUsers() */
	public Flux<User> getUsers() {
		return this.getUsers.get(); }

	/** @see DiscordClient#getRegions() */
	public Flux<Region> getRegions() {
		return this.getRegions.get(); }

	/** @see DiscordClient#getSelf() */
	public Mono<User> getSelf() {
		return this.getSelf.get(); }

	/** @see DiscordClient#getSelfId() */
	public Optional<Snowflake> getSelfId() {
		return this.getSelfId.get(); }

	/** @see DiscordClient#login() */
	public Mono<Void> login() {
		return this.login.get(); }

	/** @see DiscordClient#logout() */
	public Mono<Void> logout() {
		return this.logout.get(); }

	/** @see DiscordClient#isConnected() */
	public boolean isConnected() {
		return this.isConnected.get(); }

	/** @see DiscordClient#getResponseTime() */
	public long getResponseTime() {
		return this.getResponseTime.get(); }

	/** @see DiscordClient#createGuild(Consumer) */
	public Mono<Guild> createGuild(final Consumer<? super GuildCreateSpec> spec) {
		return this.createGuild.apply(spec); }

	/** @see DiscordClient#updatePresence(Presence) */
	public Mono<Void> updatePresence(final Presence presence) {
		return this.updatePresence.apply(presence); }

	/** @see DiscordClient#getInvite(String) */
	public Mono<Invite> getInvite(final String inviteCode) {
		return this.getInvite.apply(inviteCode); }

	/** @see DiscordClient#edit(Consumer) */
	public Mono<User> edit(final Consumer<? super UserEditSpec> spec) {
		return this.edit.apply(spec); }

	/** @see DiscordClient#getEventDispatcher() */
	public EventDispatcher getEventDispatcher() {
		return this.getEventDispatcher.get(); }
}
