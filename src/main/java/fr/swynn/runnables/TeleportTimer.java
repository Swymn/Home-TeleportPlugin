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
    private static final String EXPIRED_MESSAGE = "Your teleport request has expired.";

    // Timer delay
    private static final int DELAY = 20 * 60;

    // The sender
    private final Player player;

    // Timer task
    private int timer;

    public TeleportTimer(final Player sender, final UUID target) {
        timer = DELAY;
        player = sender;
        final var demand = new TeleportDemand(sender.getUniqueId(), target, this);
        TELEPORT_SERVICE.addDemand(demand);
    }

    @Override
    public void run() {
        if (timer == 0) {
            cancel();
        } else {
            timer--;
        }
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        player.sendMessage(EXPIRED_MESSAGE);
        TELEPORT_SERVICE.removeDemand(player.getUniqueId());
        super.cancel();
    }
}
