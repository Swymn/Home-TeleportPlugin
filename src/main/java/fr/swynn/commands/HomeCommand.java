package fr.swynn.commands;

import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.ConfigurationProvider;
import fr.swynn.core.data.HomeService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {

    // Home service
    private static final HomeService HOME_SERVICE;
    // Configuration provider
    private static final ConfigurationProvider CONFIGURATION_PROVIDER;

    // Home commande usage message
    private static final String HOME_COMMAND_USAGE;
    // Home not found message
    private static final String HOME_NOT_FOUND;
    // Home teleported message
    private static final String HOME_TELEPORTED;

    static {
        HOME_SERVICE = HomeAndTPA.getInstance().getHomeService();
        CONFIGURATION_PROVIDER = HomeAndTPA.getInstance().getConfigurationProvider();

        HOME_COMMAND_USAGE = CONFIGURATION_PROVIDER.getString("home.messages.home-usage");
        HOME_NOT_FOUND = CONFIGURATION_PROVIDER.getString("home.messages.home-not-found");
        HOME_TELEPORTED = CONFIGURATION_PROVIDER.getString("home.messages.home-teleported");
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(HOME_COMMAND_USAGE);
            return false;
        }

        if (args.length == 1) {
            final var homeName = args[0];
            final var home = HOME_SERVICE.getPlayerHome(player.getUniqueId(), homeName);

            if (home == null) {
                player.sendMessage(HOME_NOT_FOUND);
                return true;
            }

            final var world = Bukkit.getWorld(home.world());
            final var location = new Location(world, home.x(), home.y(), home.z(), home.yaw(), home.pitch());

            player.teleport(location);
            player.sendMessage(HOME_TELEPORTED.replace("%name%", homeName));
            return true;
        }

        return false;
    }
}
