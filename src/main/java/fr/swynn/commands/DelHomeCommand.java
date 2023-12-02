package fr.swynn.commands;

import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.HomeService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class DelHomeCommand implements CommandExecutor {

    // Home service
    private static final HomeService SERVICE = HomeAndTPA.getInstance().getHomeService();

    // Home commande usage message
    private static final String DELHOME_COMMAND_USAGE = "Usage: /delhome <home_name>";
    // Home not found message
    private static final String DELHOME_NOT_FOUND = "Home not found";
    // Home deleted message
    private static final String DELHOME_DELETED = "Home deleted";

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (!(sender instanceof Player player)) return false;

        if (args.length == 0) {
            player.sendMessage(DELHOME_COMMAND_USAGE);
            return false;
        }

        if (args.length == 1) {
            final String homeName = args[0];
            final List<String> playerHomList = SERVICE.getPlayerHomeNames(player.getUniqueId());

            if (!playerHomList.contains(homeName)) {
                player.sendMessage(DELHOME_NOT_FOUND);
                return false;
            }

            SERVICE.deletePlayerHome(player.getUniqueId(), homeName);

            player.sendMessage(DELHOME_DELETED);
            return true;
        }

        return false;

    }
}
