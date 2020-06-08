package net.gunivers.gunislave.plugin;

import discord4j.core.DiscordClient;
import net.gunivers.gunislave.Main;
import reactor.util.function.Tuple2;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class PluginManager {

    public static void loadPlugins() {
        try {
            final String path = System.getProperty("user.dir") + File.separatorChar + "plugins";
            Files.list(Paths.get(path)).forEach(p -> loadPlugin(p));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadPlugin(Path path) {
        //System.out.println(path);
        try {
            URLClassLoader child = new URLClassLoader(
                    new URL[] {path.toUri().toURL()},
                    PluginManager.class.getClassLoader()
            );
            Plugin plugin = new Plugin(child, path.getFileName());
            plugin.load();
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }


    }

}
