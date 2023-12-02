package fr.swynn.core.model;

import fr.swynn.runnables.TeleportTimer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public record TeleportDemand(UUID sender, UUID target, TeleportTimer timer) {
}
