package net.gunivers.gunislave.plugin;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import reactor.util.function.Tuple2;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;

public class PluginYamlParser {

    /**
     * Parse the plugin to a descriptor
     * @param classLoader the class loader of the plugin
     * @return a plugin descriptor
     * @throws IOException if the plugin doesn't have a 'plugin.yml' file
     */
    public static PluginDescriptor parse(URLClassLoader classLoader) throws IOException {
        //classLoader.getResource("plugin.yml").getFile().chars().forEach(c -> System.out.print((char)c));
        InputStream test = classLoader.getResourceAsStream("plugin.yml");
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        PluginDescriptor descriptor = om.readValue(test, PluginDescriptor.class);

        return descriptor;
    }

    /**
     * A descriptor for the plugin
     */
    static class PluginDescriptor {
        String name;
        Set<String> authors;
        String main_class;
        String version;
        String description;

        public void setName(String name) {
            this.name = name;
        }

        public void setAuthors(Set<String> authors) {
            this.authors = authors;
        }

        public void setMain_class(String main_class) {
            this.main_class = main_class;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public PluginDescriptor() {}
    }

}
