package fr.swynn.commands;

import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.ConfigurationProvider;
import fr.swynn.core.data.TeleportService;
import fr.swynn.core.model.TeleportDemand;
import fr.swynn.runnables.TeleportTimer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {

    // Teleport service
    private static final TeleportService TELEPORT_SERVICE;
    // Configuration provider
    private static final ConfigurationProvider CONFIGURATION_PROVIDER;

    // Request timeout
    private static final int REQUEST_TIMEOUT;

    // Usage message
    private static final String USAGE_MESSAGE;
    // Player cannot teleport to himself message
    private static final String CANNOT_TELEPORT_TO_YOURSELF_MESSAGE;
    // Player not found message
    private static final String PLAYER_NOT_FOUND_MESSAGE;

    // Already asked message
    private static final String ALREADY_ASKED_MESSAGE;
    // Demand sent message
    private static final String DEMAND_SENT_MESSAGE;
    // Demand received message
    private static final String DEMAND_RECEIVED_MESSAGE;

    static {
        TELEPORT_SERVICE = HomeAndTPA.getInstance().getTeleportService();
        CONFIGURATION_PROVIDER = HomeAndTPA.getInstance().getConfigurationProvider();

        REQUEST_TIMEOUT = CONFIGURATION_PROVIDER.getInt("teleport.timeout", 120);

        USAGE_MESSAGE = CONFIGURATION_PROVIDER.getString("teleport.messages.usage");
        PLAYER_NOT_FOUND_MESSAGE = CONFIGURATION_PROVIDER.getString("teleport.messages.player-not-found");
        ALREADY_ASKED_MESSAGE = CONFIGURATION_PROVIDER.getString("teleport.messages.already-demanded");
        DEMAND_SENT_MESSAGE = CONFIGURATION_PROVIDER.getString("teleport.messages.demand-sent");
        DEMAND_RECEIVED_MESSAGE = CONFIGURATION_PROVIDER.getString("teleport.messages.demand-received");
        CANNOT_TELEPORT_TO_YOURSELF_MESSAGE = CONFIGURATION_PROVIDER.getString("teleport.messages.cannot-teleport-to-yourself");
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        if (args.length == 0) {
            player.sendMessage(USAGE_MESSAGE);
            return false;
        }

        if (args.length == 1) {
            final var target = player.getServer().getPlayer(args[0]);

            if (target == null) {
                player.sendMessage(PLAYER_NOT_FOUND_MESSAGE);
                return false;
            }

            if (target.getUniqueId().equals(player.getUniqueId())) {
                player.sendMessage(CANNOT_TELEPORT_TO_YOURSELF_MESSAGE);
                return false;
            }

            if (isAlreadyAsked(player, target)) {
                player.sendMessage(ALREADY_ASKED_MESSAGE);
                return false;
            }

            final var timer = new TeleportTimer(player, target);

            player.sendMessage(DEMAND_SENT_MESSAGE.replace("%target%", target.getName()));
            target.sendMessage(DEMAND_RECEIVED_MESSAGE.replace("%target%", player.getName()).replace("%time%", String.valueOf(REQUEST_TIMEOUT)));

            timer.runTaskTimer(HomeAndTPA.getInstance(), 0, 20L);

            return true;
        }

        return false;
    }

    private boolean isAlreadyAsked(final Player sender, final Player target) {
        return TELEPORT_SERVICE.getSenderDemand(sender.getUniqueId()) != null
                && TELEPORT_SERVICE.getSenderDemand(sender.getUniqueId()).target().equals(target.getUniqueId());
    }
}
