package fr.swynn.runnables;

import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.ConfigurationProvider;
import fr.swynn.core.data.TeleportService;
import fr.swynn.core.model.TeleportDemand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TeleportTimer extends BukkitRunnable {

    // Teleport service
    private static final TeleportService TELEPORT_SERVICE;
    // Configuration provider
    private static final ConfigurationProvider CONFIGURATION_PROVIDER;

    // Prefix config
    private static final String TARGET_PREFIX = "%target%";
    private static final String SENDER_PREFIX = "%sender%";

    // Expired message
    private static final String EXPIRED_MESSAGE_SENDER;
    private static final String EXPIRED_MESSAGE_TARGET;

    // Accepted message
    private static final String ACCEPTED_MESSAGE_SENDER;
    private static final String ACCEPTED_MESSAGE_TARGET;

    // Declined message
    private static final String DECLINED_MESSAGE_SENDER;
    private static final String DECLINED_MESSAGE_TARGET;

    // Teleportation request removed message
    private static final String TELEPORTATION_REQUEST_REMOVED_MESSAGE;

    // Timer delay
    private static final int DELAY;

    // The sender
    private final Player sender;
    // The target
    private final Player target;

    // Timer task
    private int timer;

    static {
        TELEPORT_SERVICE = HomeAndTPA.getInstance().getTeleportService();
        CONFIGURATION_PROVIDER = HomeAndTPA.getInstance().getConfigurationProvider();

        DELAY = CONFIGURATION_PROVIDER.getInt("teleport.timeout", 120);

        EXPIRED_MESSAGE_SENDER = CONFIGURATION_PROVIDER.getString("teleport.messages.demand-expired-sender");
        EXPIRED_MESSAGE_TARGET = CONFIGURATION_PROVIDER.getString("teleport.messages.demand-expired-target");

        ACCEPTED_MESSAGE_SENDER = CONFIGURATION_PROVIDER.getString("teleport.messages.demand-accepted-sender");
        ACCEPTED_MESSAGE_TARGET = CONFIGURATION_PROVIDER.getString("teleport.messages.demand-accepted-target");

        DECLINED_MESSAGE_SENDER = CONFIGURATION_PROVIDER.getString("teleport.messages.demand-denied-sender");
        DECLINED_MESSAGE_TARGET = CONFIGURATION_PROVIDER.getString("teleport.messages.demand-denied-target");

        TELEPORTATION_REQUEST_REMOVED_MESSAGE = CONFIGURATION_PROVIDER.getString("teleport.messages.demand-removed");
    }

    public TeleportTimer(final Player sender, final Player target) {
        timer = DELAY;
        this.sender = sender;
        this.target = target;

        removeOtherDemandIfExist(sender);

        final var demand = new TeleportDemand(sender.getUniqueId(), target.getUniqueId(), this);
        TELEPORT_SERVICE.addDemand(demand);
    }

    /**
     * Check if the sender already asked the target
     * @param sender The sender
     */
    private void removeOtherDemandIfExist(final Player sender) {
        final var demand = TELEPORT_SERVICE.getSenderDemand(sender.getUniqueId());

        if (demand == null) {
            return;
        }

        final var demandTarget = demand.target();
        final var playerTarget = sender.getServer().getPlayer(demandTarget);

        if (playerTarget == null) {
            return;
        }

        playerTarget.sendMessage(TELEPORTATION_REQUEST_REMOVED_MESSAGE.replace(SENDER_PREFIX, sender.getName()));
    }

    @Override
    public void run() {
        if (timer == 0) {
            sender.sendMessage(EXPIRED_MESSAGE_SENDER);
            target.sendMessage(EXPIRED_MESSAGE_TARGET.replace(TARGET_PREFIX, sender.getName()));
            cancel();
        } else {
            timer--;
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        TELEPORT_SERVICE.removeDemand(sender.getUniqueId());
        super.cancel();
    }

    public void acceptTeleport() {
        sender.sendMessage(ACCEPTED_MESSAGE_SENDER.replace(TARGET_PREFIX, target.getName()));
        target.sendMessage(ACCEPTED_MESSAGE_TARGET.replace(SENDER_PREFIX, sender.getName()));
        sender.teleport(target);
        cancel();
    }

    public void declineTeleport() {
        sender.sendMessage(DECLINED_MESSAGE_SENDER.replace(TARGET_PREFIX, target.getName()));
        target.sendMessage(DECLINED_MESSAGE_TARGET.replace(SENDER_PREFIX, sender.getName()));
        cancel();
    }
}
