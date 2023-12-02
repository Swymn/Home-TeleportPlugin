package fr.swynn.commands;

import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.HomeService;
import fr.swynn.core.model.Home;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class SetHomeCommand implements CommandExecutor {

    // Home service
    private static final HomeService SERVICE = HomeAndTPA.getInstance().getHomeService();

    // Max home count
    private static final int MAX_HOME = 3;
    // Max home name length
    private static final int MAX_HOME_NAME_LENGTH = 36;

    // Home commande usage message
    private static final String HOME_COMMAND_USAGE = "Usage: /sethome <home_name>";
    // Max home count message
    private static final String MAX_HOME_COUNT = "You can't have more than " + MAX_HOME + " homes";
    // Home name too long message
    private static final String HOME_NAME_TOO_LONG = "Home name can't be longer than " + MAX_HOME_NAME_LENGTH + " characters";
    // Home already exists message
    private static final String HOME_ALREADY_EXISTS = "You already have a home with this name";
    // Weird player location message
    private static final String WEIRD_PLAYER_LOCATION = "You are not in a world";
    // Home created message
    private static final String HOME_CREATED = "Home created";


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
            final List<String> playerHomes = SERVICE.getPlayerHomeNames(player.getUniqueId());

            if (playerHomes.size() >= MAX_HOME) {
                player.sendMessage(MAX_HOME_COUNT);
                return true;
            }

            if (homeName.length() > MAX_HOME_NAME_LENGTH) {
                player.sendMessage(HOME_NAME_TOO_LONG);
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

            SERVICE.createPlayerHome(home);
            player.sendMessage(HOME_CREATED);
            return true;
        }

        return false;
    }
}
