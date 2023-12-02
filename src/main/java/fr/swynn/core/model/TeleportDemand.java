package fr.swynn.core.model;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public record TeleportDemand(UUID sender, UUID target, BukkitRunnable timer) {
}
