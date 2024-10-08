package fr.swynn.core.impl;

import fr.swynn.core.data.TeleportService;
import fr.swynn.core.model.TeleportDemand;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MinecraftTeleportService implements TeleportService {

    private final Map<UUID, TeleportDemand> teleportDemands;

    public MinecraftTeleportService() {
        teleportDemands = new HashMap<>();
    }

    @Override
    public void addDemand(final TeleportDemand demand) {
        if (teleportDemands.containsKey(demand.sender())) {
            teleportDemands.get(demand.sender()).timer().cancel();
            teleportDemands.remove(demand.sender());
        }

        teleportDemands.put(demand.sender(), demand);
    }

    @Override
    public TeleportDemand getSenderDemand(final UUID sender) {
        return teleportDemands.get(sender);
    }

    @Override
    public List<TeleportDemand> getTargetDemand(UUID target) {
        return teleportDemands.values().stream()
                .filter(demand -> demand.target().equals(target))
                .toList();
    }

    @Override
    public void removeDemand(final UUID sender) {
        teleportDemands.remove(sender);
    }
}
