package fr.swynn.core.impl;

import fr.swynn.core.data.HomeService;
import fr.swynn.core.model.Home;

import java.util.List;
import java.util.UUID;

public class MinecraftHomeService implements HomeService {

    // Cache of homes
    private final List<Home> homes;

    /**
     * Constructor. Initialize the cache of homes.
     * @param homes The list of homes
     */
    public MinecraftHomeService(final List<Home> homes) {
        this.homes = homes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Home> getAllHomes() {
        return homes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getPlayerHomeNames(UUID playerUUID) {
        return homes.stream()
                .filter(home -> home.playerUUID().equals(playerUUID.toString()))
                .map(Home::name)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Home getPlayerHome(UUID playerUUID, String homeName) {
        return homes.stream()
                .filter(home -> home.playerUUID().equals(playerUUID.toString()))
                .filter(home -> home.name().equals(homeName))
                .findFirst()
                .orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createPlayerHome(Home home) {
        homes.add(home);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deletePlayerHome(UUID playerUUID, String homeName) {
        homes.removeIf(home -> home.playerUUID().equals(playerUUID.toString()) && home.name().equals(homeName));
    }
}
