package fr.swynn.core.impl;

import fr.swynn.core.data.ConfigurationProvider;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class MinecraftConfigurationProvider implements ConfigurationProvider {

    private final FileConfiguration configuration;

    public MinecraftConfigurationProvider(final FileConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getString(final String path) {
        return configuration.getString(path, "§cSomething went wrong while trying to get the message, please contact an administrator.§r");
    }

    @Override
    public List<String> getStringList(final String path) {
        return configuration.getStringList(path);
    }

    @Override
    public int getInt(final String path, final int defaultValue) {
        return configuration.getInt(path, defaultValue);
    }

    @Override
    public double getDouble(final String path, final double defaultValue) {
        return configuration.getDouble(path, defaultValue);
    }
}
