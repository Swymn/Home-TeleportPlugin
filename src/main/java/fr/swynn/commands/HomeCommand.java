package fr.swynn.commands;

import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.HomeService;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {

    // Home service
    private static final HomeService SERVICE = HomeAndTPA.getInstance().getHomeService();

    // Home commande usage message
    private static final String HOME_COMMAND_USAGE = "Usage: /home <home_name>";
    // Home not found message
    private static final String HOME_NOT_FOUND = "Home not found";
    // Home teleported message
    private static final String HOME_TELEPORTED = "Teleported to home %s";

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
            final var home = SERVICE.getPlayerHome(player.getUniqueId(), homeName);

            if (home == null) {
                player.sendMessage(HOME_NOT_FOUND);
                return true;
            }

            final var world = Bukkit.getWorld(home.world());
            final var location = new Location(world, home.x(), home.y(), home.z(), home.yaw(), home.pitch());

            player.teleport(location);
            player.sendMessage(String.format(HOME_TELEPORTED, home.name()));
            return true;
        }

        return false;
    }
}
