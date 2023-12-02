package fr.swynn.commands;

import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.ConfigurationProvider;
import fr.swynn.core.data.TeleportService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportAcceptCommand implements CommandExecutor {

    // Teleport service
    private static final TeleportService TELEPORT_SERVICE;
    // Configuration provider
    private static final ConfigurationProvider CONFIGURATION_PROVIDER;
    // Empty demand message
    private static final String EMPTY_DEMAND_MESSAGE;

    static {
        TELEPORT_SERVICE = HomeAndTPA.getInstance().getTeleportService();
        CONFIGURATION_PROVIDER = HomeAndTPA.getInstance().getConfigurationProvider();

        EMPTY_DEMAND_MESSAGE = CONFIGURATION_PROVIDER.getString("teleport.messages.empty-demand");
    }

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
