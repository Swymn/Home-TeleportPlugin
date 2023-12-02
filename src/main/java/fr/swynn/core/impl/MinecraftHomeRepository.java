package fr.swynn.core.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.swynn.HomeAndTPA;
import fr.swynn.core.data.HomeRepository;
import fr.swynn.core.model.Home;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MinecraftHomeRepository implements HomeRepository {

    // The file name where the homes are stored.
    private static final String FILE_NAME;
    // The file path where the homes are stored.
    private static final String FILE_PATH;
    // The object mapper used to serialize/deserialize the homes.
    private static final ObjectMapper MAPPER;
    // Loggers
    private static final Logger LOGGER = HomeAndTPA.getInstance().getLogger();

    static {
        FILE_NAME = "homes.json";
        FILE_PATH = "plugins/HomeAndTPA/" + FILE_NAME;
        MAPPER = new ObjectMapper();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Home> getAllHomes() {
        try {
            return deserializeHomes();
        } catch (final HomeRepositoryException e) {
            LOGGER.severe("Unable to get all homes");
            return new ArrayList<>();
        }
    }

    /**
     * Deserialize the homes from the file.
     *
     * @return The list of homes
     * @throws HomeRepositoryException If an error occurs
     */
    private List<Home> deserializeHomes() throws HomeRepositoryException {
        try {
            initializeDataStorage();
            final var file = new File(FILE_PATH);
            final var type = MAPPER.getTypeFactory().constructCollectionType(List.class, Home.class);

            return MAPPER.readValue(file, type);
        } catch (final Exception e) {
            throw new HomeRepositoryException("Unable to deserialize homes");
        }
    }

    /**
     * Initialize the data storage.
     *
     * @throws HomeRepositoryException If we are unable to create the file / folder.
     */
    private void initializeDataStorage() throws HomeRepositoryException {
        final var file = new File(FILE_PATH);
        final var dir = file.getParentFile();

        createFolderIfNotExist(dir);
        createFileIfNotExist(file);
    }

    /**
     * Create the folder if it does not exist.
     *
     * @param dir The folder
     * @throws HomeRepositoryException If we are unable to create the folder.
     */
    private void createFolderIfNotExist(final File dir) throws HomeRepositoryException {
        if (!dir.exists() && (!dir.mkdirs())) {
            throw new HomeRepositoryException("Unable to create folder " + dir.getAbsolutePath());
        }
    }

    /**
     * Create the file if it does not exist.
     *
     * @param file The file
     * @throws HomeRepositoryException If we are unable to create the file.
     */
    private void createFileIfNotExist(final File file) throws HomeRepositoryException {
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new HomeRepositoryException("Unable to create file " + file.getAbsolutePath());
                }
            } catch (final Exception e) {
                throw new HomeRepositoryException("Unable to create file " + file.getAbsolutePath());
            }
        }
    }

    @Override
    public void saveAllHomes(final List<Home> homes) {
        try {
            serializeHomes(homes);
        } catch (final HomeRepositoryException e) {
            LOGGER.severe("Unable to save all homes");
        }
    }

    /**
     * Serialize the homes to the file.
     *
     * @param homes The list of homes
     * @throws HomeRepositoryException If an error occurs
     */
    private void serializeHomes(final List<Home> homes) throws HomeRepositoryException {
        try {
            initializeDataStorage();
            final var file = new File(FILE_PATH);

            MAPPER.writeValue(file, homes);
        } catch (final Exception e) {
            throw new HomeRepositoryException("Unable to serialize homes");
        }
    }
}
