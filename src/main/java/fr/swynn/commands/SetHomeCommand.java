package fr.swynn.commands;

import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.ConfigurationProvider;
import fr.swynn.core.data.HomeService;
import fr.swynn.core.model.Home;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetHomeCommand implements CommandExecutor {

    // Home service
    private static final HomeService HOME_SERVICE;
    // Configuration provider
    private static final ConfigurationProvider CONFIGURATION_PROVIDER;

    // Max home count
    private static final int MAX_HOME;
    // Max home name length
    private static final int MAX_HOME_NAME_LENGTH;

    // Home commande usage message
    private static final String HOME_COMMAND_USAGE;
    // Max home count message
    private static final String MAX_HOME_COUNT;
    // Home name too long message
    private static final String HOME_NAME_TOO_LONG;
    // Home already exists message
    private static final String HOME_ALREADY_EXISTS;
    // Weird player location message
    private static final String WEIRD_PLAYER_LOCATION;
    // Home created message
    private static final String HOME_CREATED;

    static {
        HOME_SERVICE = HomeAndTPA.getInstance().getHomeService();
        CONFIGURATION_PROVIDER = HomeAndTPA.getInstance().getConfigurationProvider();

        MAX_HOME = CONFIGURATION_PROVIDER.getInt("home.max-home-count", 3);
        MAX_HOME_NAME_LENGTH = CONFIGURATION_PROVIDER.getInt("home.name-length-limit", 36);

        HOME_COMMAND_USAGE = CONFIGURATION_PROVIDER.getString("home.messages.sethome-usage");
        MAX_HOME_COUNT = CONFIGURATION_PROVIDER.getString("home.messages.sethome-limit");
        HOME_NAME_TOO_LONG = CONFIGURATION_PROVIDER.getString("home.messages.sethome-name-too-long");
        HOME_ALREADY_EXISTS = CONFIGURATION_PROVIDER.getString("home.messages.sethome-already-exists");
        WEIRD_PLAYER_LOCATION = CONFIGURATION_PROVIDER.getString("home.messages.sethome-weird-location");
        HOME_CREATED = CONFIGURATION_PROVIDER.getString("home.messages.sethome-created");
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
            final String homeName = args[0];
            final List<String> playerHomes = HOME_SERVICE.getPlayerHomeNames(player.getUniqueId());

            if (playerHomes.size() >= MAX_HOME) {
                player.sendMessage(MAX_HOME_COUNT.replace("%limit%", String.valueOf(MAX_HOME)));
                return true;
            }

            if (homeName.length() > MAX_HOME_NAME_LENGTH) {
                player.sendMessage(HOME_NAME_TOO_LONG.replace("%max%", String.valueOf(MAX_HOME_NAME_LENGTH)));
                return true;
            }

            if (playerHomes.contains(homeName)) {
                player.sendMessage(HOME_ALREADY_EXISTS);
                return true;
            }

            final var playerLocation = player.getLocation();
            if (playerLocation.getWorld() == null) {
                player.sendMessage(WEIRD_PLAYER_LOCATION);
                return false;
            }

            final var home = new Home(player.getUniqueId().toString(), homeName, playerLocation.getWorld().getName(), playerLocation.getX(), playerLocation.getY(), playerLocation.getZ(), playerLocation.getYaw(), playerLocation.getPitch());

            HOME_SERVICE.createPlayerHome(home);
            player.sendMessage(HOME_CREATED.replace("%name%", homeName));
            return true;
        }

        return false;
    }
}
