package fr.swynn.core.data;

import java.util.List;

public interface ConfigurationProvider {

    /**
     * Get a string from the configuration
     *
     * @param path The path of the string
     * @return The string
     */
    String getString(String path);

    /**
     * Get a string list from the configuration
     *
     * @param path The path of the string list
     * @return The string list
     */
    List<String> getStringList(String path);

    /**
     * Get an int from the configuration
     *
     * @param path The path of the int
     * @return The int
     */
    int getInt(String path, int defaultValue);

    /**
     * Get a double from the configuration
     * @param path The path of the double
     * @return The double
     */

    double getDouble(String path, double defaultValue);

}
