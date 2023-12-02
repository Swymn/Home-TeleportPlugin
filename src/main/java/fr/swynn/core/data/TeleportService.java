package fr.swynn.core.data;

import fr.swynn.core.model.TeleportDemand;

import java.util.List;
import java.util.UUID;

public interface TeleportService {

    void addDemand(TeleportDemand demand);

    TeleportDemand getSenderDemand(UUID sender);
    List<TeleportDemand> getTargetDemand(UUID target);
    void removeDemand(UUID sender);

}
