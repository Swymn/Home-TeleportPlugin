package fr.swynn.runnables;

import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.TeleportService;
import fr.swynn.core.model.TeleportDemand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TeleportTimer extends BukkitRunnable {

    // Teleport service
    private static final TeleportService TELEPORT_SERVICE = HomeAndTPA.getInstance().getTeleportService();
    // Expired message
    private static final String EXPIRED_MESSAGE_SENDER = "Your teleport request has expired.";
    private static final String EXPIRED_MESSAGE_TARGET = "The teleport request from %s has expired.";

    // Accepted message
    private static final String ACCEPTED_MESSAGE_SENDER = "Your teleport request has been accepted.";
    private static final String ACCEPTED_MESSAGE_TARGET = "The teleport request from %s has been accepted.";

    // Declined message
    private static final String DECLINED_MESSAGE_SENDER = "Your teleport request has been declined.";
    private static final String DECLINED_MESSAGE_TARGET = "The teleport request from %s has been declined.";

    // Teleportation request removed message
    private static final String TELEPORTATION_REQUEST_REMOVED_MESSAGE = "Teleportation request from %s removed.";

    // Timer delay
    private static final int DELAY = 120;

    // The sender
    private final Player sender;
    // The target
    private final Player target;

    // Timer task
    private int timer;

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

        playerTarget.sendMessage(TELEPORTATION_REQUEST_REMOVED_MESSAGE.formatted(sender.getName()));
    }

    @Override
    public void run() {
        if (timer == 0) {
            sender.sendMessage(EXPIRED_MESSAGE_SENDER);
            target.sendMessage(EXPIRED_MESSAGE_TARGET.formatted(sender.getName()));
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
        sender.sendMessage(ACCEPTED_MESSAGE_SENDER);
        target.sendMessage(ACCEPTED_MESSAGE_TARGET.formatted(sender.getName()));
        sender.teleport(target);
        cancel();
    }

    public void declineTeleport() {
        sender.sendMessage(DECLINED_MESSAGE_SENDER);
        target.sendMessage(DECLINED_MESSAGE_TARGET.formatted(sender.getName()));
        cancel();
    }
}
