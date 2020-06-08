package net.gunivers.gunislave.plugin;

import discord4j.core.DiscordClient;
import net.gunivers.gunislave.Main;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Set;

public class Plugin {

    private final URLClassLoader urlClassLoader;
    private final String name;
    private final Set<String> authors;
    private final Class<net.gunivers.net.Plugin> mainClass;
    private final String version;
    private final String description;

    Plugin(URLClassLoader urlClassLoader, Path fileName) {
        PluginYamlParser.PluginDescriptor descriptor = null;
        try {
            descriptor = PluginYamlParser.parse(urlClassLoader);
        } catch (IOException e) {
            e.printStackTrace();
            throw new InvalidPluginException(String.format("'plugin.yml' not found in %s.", fileName));
        }

        Class<net.gunivers.net.Plugin> tempClazz = null;
        try {
            tempClazz = (Class<net.gunivers.net.Plugin>) Class.forName(descriptor.main_class, true, urlClassLoader);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            this.mainClass = tempClazz;
            this.urlClassLoader = urlClassLoader;
            this.name = descriptor.name;
            this.authors = descriptor.authors;
            this.version = descriptor.version;
            this.description = descriptor.description;
        }
    }

    public void load() {
        Method method;
        try {
            method = mainClass.getDeclaredMethod("load", DiscordClient.class);
            Object instance = mainClass.getConstructor().newInstance();
            method.invoke(instance, Main.getBotInstance().getBotClient());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
