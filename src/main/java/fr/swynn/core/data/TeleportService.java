package fr.swynn.core.data;

import fr.swynn.core.model.TeleportDemand;

import java.util.UUID;

public interface TeleportService {

    void addDemand(TeleportDemand demand);

    TeleportDemand getDemand(UUID sender);
    void removeDemand(UUID sender);

}
