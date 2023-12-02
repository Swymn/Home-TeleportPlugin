package fr.swynn.commands;

import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.ConfigurationProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {

    // Configuration provider
    private static final ConfigurationProvider CONFIGURATION_PROVIDER;

    // Spawn coordinates
    private static final int SPAWN_X;
    private static final int SPAWN_Y;
    private static final int SPAWN_Z;

    // Teleported message
    private static final String TELEPORTED_MESSAGE;

    static {
        CONFIGURATION_PROVIDER = HomeAndTPA.getInstance().getConfigurationProvider();

        SPAWN_X = CONFIGURATION_PROVIDER.getInt("spawn.coordinates.x", 0);
        SPAWN_Y = CONFIGURATION_PROVIDER.getInt("spawn.coordinates.y", 0);
        SPAWN_Z = CONFIGURATION_PROVIDER.getInt("spawn.coordinates.z", 0);

        TELEPORTED_MESSAGE = CONFIGURATION_PROVIDER.getString("spawn.messages.teleported");
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        final var world = Bukkit.getWorld("world");
        final var location = new Location(world, SPAWN_X, SPAWN_Y, SPAWN_Z);

        player.sendMessage(TELEPORTED_MESSAGE);
        player.teleport(location);
        return true;
    }
}
