package fr.swynn.commands;

import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.TeleportService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAcceptCommand implements CommandExecutor {

    // Teleport service
    private static final TeleportService TELEPORT_SERVICE = HomeAndTPA.getInstance().getTeleportService();
    // Empty demand message
    private static final String EMPTY_DEMAND_MESSAGE = "You don't have any demand.";

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {

        if (!(sender instanceof Player player)) {
            return false;
        }

        final var demands = TELEPORT_SERVICE.getTargetDemand(player.getUniqueId());

        if (demands.isEmpty()) {
            player.sendMessage(EMPTY_DEMAND_MESSAGE);
            return true;
        }

        final var demandToAccept = demands.get(demands.size() - 1);

        demandToAccept.timer().acceptTeleport();

        return false;
    }
}
