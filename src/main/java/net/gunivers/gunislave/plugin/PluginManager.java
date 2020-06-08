package net.gunivers.gunislave.plugin;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PluginManager {

    /**
     * Load all plugins found in "plugin" folder.
     */
    public static void loadPlugins() {
        try {
            final String path = System.getProperty("user.dir") + File.separatorChar + "plugins";
            Files.list(Paths.get(path)).forEach(p -> loadPlugin(p));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load plugin at <i>path</i>.
     * @param path un path to a plugin
     */
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
