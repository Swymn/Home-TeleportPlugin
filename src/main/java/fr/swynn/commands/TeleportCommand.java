package fr.swynn.commands;

import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.TeleportService;
import fr.swynn.core.model.TeleportDemand;
import fr.swynn.runnables.TeleportTimer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {

    // Teleport service
    private static final TeleportService TELEPORT_SERVICE = HomeAndTPA.getInstance().getTeleportService();

    // Usage message
    private static final String USAGE_MESSAGE = "Usage: /tpa <player>";
    // Player not found message
    private static final String PLAYER_NOT_FOUND_MESSAGE = "Player not found.";

    // Already asked message
    private static final String ALREADY_ASKED_MESSAGE = "You already asked this player.";
    // Demand sent message
    private static final String DEMAND_SENT_MESSAGE = "Demand sent.";
    // Demand received message
    private static final String DEMAND_RECEIVED_MESSAGE = "You received a demand from %s. Do you want to accept it ? You have 1 minute";

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

            if (isAlreadyAsked(player, target)) {
                player.sendMessage(ALREADY_ASKED_MESSAGE);
                return false;
            }

            final var timer = new TeleportTimer(player, target);

            player.sendMessage(DEMAND_SENT_MESSAGE);
            target.sendMessage(DEMAND_RECEIVED_MESSAGE.formatted(player.getName()));

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
