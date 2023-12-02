package fr.swynn.core.data;

import fr.swynn.core.model.Home;

import java.util.List;

/**
 * Repository for managing homes persistence operations.
 */
public interface HomeRepository {

    /**
     * Get all the homes.
     * @return The list of homes
     */
    List<Home> getAllHomes();

    /**
     * Save all the homes.
     * @param homes The list of homes
     */
    void saveAllHomes(List<Home> homes);
}
