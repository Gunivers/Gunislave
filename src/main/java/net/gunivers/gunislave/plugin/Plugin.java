package net.gunivers.gunislave.plugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;

import net.gunivers.gunislave.Main;
import net.gunivers.gunislave.plugin.PluginYamlParser.PluginDescriptor;

public class Plugin
{
	@SuppressWarnings("unchecked")
	public static Optional<Plugin> load(URLClassLoader loader, Path file) throws InvalidPluginException
	{
		PluginDescriptor descriptor;

		try
		{
			descriptor = PluginYamlParser.parse(loader);
		} catch (IOException e)
		{
			e.printStackTrace();
			throw new InvalidPluginException(String.format("'plugin.yml' not found in %s.", file));
		}

		net.gunivers.net.Plugin plugin;

		try
		{
			Class<? extends net.gunivers.net.Plugin> clazz = (Class<? extends net.gunivers.net.Plugin>) Class.forName(descriptor.main_class, true, loader);
			plugin = clazz.getConstructor().newInstance();
			//plugin.load(Main.getBotInstance().getBotClient());
		}
		catch (ClassCastException | ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e)
		{
			throw new InvalidPluginException(e.getMessage());
		}
		catch (Throwable t) // A single plugin crashing while loading should not make the whole instance crash
		{
			t.printStackTrace();
			return Optional.empty();
		}

		return Optional.of(new Plugin(plugin, descriptor));
	}

	/** The name of the plugin */
	private final String name;
	/** A list of authors of the plugin */
	private final Set<String> authors;
	/** The version of the plugin */
	private final String version;
	/** The description of the plugin */
	private final String description;

	private Plugin(net.gunivers.net.Plugin plugin, PluginDescriptor metadata)
	{
		this.name = metadata.name;
		this.authors = metadata.authors;
		this.version = metadata.version;
		this.description = metadata.description;
	}

	public String name() { return this.name; }
	public String version() { return this.version; }
	public String description() { return this.description; }
	public Set<String> authors() { return this.authors; }
}
