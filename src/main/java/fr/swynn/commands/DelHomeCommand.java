package fr.swynn.commands;

import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.ConfigurationProvider;
import fr.swynn.core.data.HomeService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class DelHomeCommand implements CommandExecutor {

    // Home service
    private static final HomeService HOME_SERVICE;
    // Configuration provider
    private static final ConfigurationProvider CONFIGURATION_PROVIDER;

    // Home commande usage message
    private static final String DELHOME_COMMAND_USAGE;
    // Home not found message
    private static final String DELHOME_NOT_FOUND;
    // Home deleted message
    private static final String DELHOME_DELETED;

    static {
        HOME_SERVICE = HomeAndTPA.getInstance().getHomeService();
        CONFIGURATION_PROVIDER = HomeAndTPA.getInstance().getConfigurationProvider();

        DELHOME_COMMAND_USAGE = CONFIGURATION_PROVIDER.getString("home.messages.delhome-usage");
        DELHOME_NOT_FOUND = CONFIGURATION_PROVIDER.getString("home.messages.home-not-found");
        DELHOME_DELETED = CONFIGURATION_PROVIDER.getString("home.messages.delhome-deleted");
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (!(sender instanceof Player player)) return false;

        if (args.length == 0) {
            player.sendMessage(DELHOME_COMMAND_USAGE);
            return false;
        }

        if (args.length == 1) {
            final String homeName = args[0];
            final List<String> playerHomList = HOME_SERVICE.getPlayerHomeNames(player.getUniqueId());

            if (!playerHomList.contains(homeName)) {
                player.sendMessage(DELHOME_NOT_FOUND);
                return false;
            }

            HOME_SERVICE.deletePlayerHome(player.getUniqueId(), homeName);

            player.sendMessage(DELHOME_DELETED.replace("%name%", homeName));
            return true;
        }

        return false;

    }
}
