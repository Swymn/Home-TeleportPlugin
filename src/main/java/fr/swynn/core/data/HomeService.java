package fr.swynn.core.data;

import fr.swynn.core.model.Home;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing player homes.
 */
public interface HomeService {

    /**
     * Get all the homes.
     * @return The list of homes
     */
    List<Home> getAllHomes();

    /**
     * Get all the home name for the given player.
     * @param playerUUID The player UUID
     * @return The list of home names
     */
    List<String> getPlayerHomeNames(UUID playerUUID);

    /**
     * Get the home for the given player and home name.
     * @param playerUUID The player UUID
     * @param homeName The home name
     * @return The home, or null if not found
     */
    Home getPlayerHome(UUID playerUUID, String homeName);

    /**
     * Create a new home for the given player.
     * @param home The home to create
     */
    void createPlayerHome(Home home);

    /**
     * Delete the home for the given player and home name.
     * @param playerUUID The player UUID
     * @param homeName The home name
     */
    void deletePlayerHome(UUID playerUUID, String homeName);

}
