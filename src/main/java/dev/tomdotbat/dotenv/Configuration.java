package dev.tomdotbat.dotenv;

import dev.tomdotbat.dotenv.exception.BooleanFormatException;
import dev.tomdotbat.dotenv.exception.MissingKeyException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the Environment Configuration of an Application.
 */
public final class Configuration {
    /**
     * Creates a Configuration from the automatically located dotenv file.
     */
    public Configuration() {
        FILE = new EnvironmentFile(locateEnvironmentFile());
    }

    /**
     * Create a Configuration from the given File.
     * @param file the Environment File.
     */
    public Configuration(File file) {
        FILE = new EnvironmentFile(file);
    }

    /**
     * Create a Configuration from the File at the given Path.
     * @param path the path of the Environment File.
     */
    public Configuration(Path path) {
        FILE = new EnvironmentFile(path);
    }

    /**
     * Gets the value of a key from the Configuration.
     * @param key the key to get the value of.
     * @return the given key's value, or null if it doesn't exist.
     */
    public String getString(String key) {
        return SETTINGS.get(key);
    }

    /**
     * Gets the value of a key from the Configuration, returns the fallback if it doesn't exist.
     * @param key the key to get the value of.
     * @param fallback the fallback to use if the key isn't in the Configuration.
     * @return the given key's value, or the fallback if it couldn't be found.
     */
    public String getString(String key, String fallback) {
        final String VALUE = SETTINGS.get(key);
        return VALUE != null ? VALUE : fallback;
    }

    /**
     * Gets the value of a key from the Configuration as an int.
     * @param key the key to get the value of.
     * @return the given key's value as an int.
     * @throws NumberFormatException if the key isn't a valid int.
     * @throws MissingKeyException if the key couldn't be found.
     */
    public int getInteger(String key) throws NumberFormatException, MissingKeyException {
        final String VALUE = SETTINGS.get(key);
        if (VALUE == null)
            throw new MissingKeyException("The key \"" + key + "\" couldn't be found in the Configuration.");

        return Integer.parseInt(SETTINGS.get(key));

    }
    /**
     * Gets the value of a key from the Configuration as an int, returns the fallback if it doesn't exist or isn't a valid int.
     * @param key the key to get the value of.
     * @param fallback the fallback to use if the key isn't in the Configuration or isn't a valid int.
     * @return the given key's value as an int, or the fallback if it couldn't be found or isn't a valid int.
     */
    public int getInteger(String key, int fallback) {
        try {
            return getInteger(key);
        }
        catch (Exception ex) {
            return fallback;
        }
    }

    /**
     * Gets the value of a key from the Configuration as a boolean.
     * @param key the key to get the value of.
     * @return the given key's value as a boolean.
     * @throws BooleanFormatException if the key isn't a valid boolean.
     * @throws MissingKeyException if the key couldn't be found.
     */
    public boolean getBoolean(String key) throws BooleanFormatException, MissingKeyException {
        final String VALUE = SETTINGS.get(key);
        if (VALUE == null)
            throw new MissingKeyException("The key \"" + key + "\" couldn't be found in the Configuration.");

        return parseBoolean(VALUE);
    }

    /**
     * Gets the value of a key from the Configuration as a boolean, returns the fallback if it doesn't exist or isn't a valid boolean.
     * @param key the key to get the value of.
     * @param fallback the fallback to use if the key isn't in the Configuration or isn't a valid boolean.
     * @return the given key's value as a boolean, or the fallback if it couldn't be found or isn't a valid boolean.
     */
    public boolean getBoolean(String key, boolean fallback) {
        try {
            return getBoolean(key);
        }
        catch (Exception ex) {
            return fallback;
        }
    }

    /**
     * Sets the value of a String key in the Configuration.
     * @param key the key to set the value of
     * @param value the new value of the key.
     * @return the key's previous value, null if it didn't have one.
     */
    public String set(String key, String value) {
        return SETTINGS.put(key, value);
    }

    /**
     * Sets the value of an integer key in the Configuration.
     * @param key the key to set the value of
     * @param value the new value of the key.
     * @return the key's previous value, null if it didn't have one.
     */
    public String set(String key, int value) {
        return set(key, String.valueOf(value));
    }

    /**
     * Sets the value of a boolean key in the Configuration.
     * @param key the key to set the value of
     * @param value the new value of the key.
     * @return the key's previous value, null if it didn't have one.
     */
    public String set(String key, boolean value) {
        return set(key, value ? "true" : "false");
    }

    /**
     * Returns whether the value of the given String key is the same as the given value.
     * @param key the key to check the value of.
     * @param value the value to check against the key.
     * @param fallback the default state if the key doesn't have a value.
     * @return true if the key's value is the same as the value given.
     */
    public boolean keyEquals(String key, String value, boolean fallback) {
        final String VALUE = SETTINGS.get(key);
        if (VALUE == null) return fallback;
        return VALUE.equals(value);
    }

    /**
     * Returns whether the value of the given String key is the same as the given value.
     * @param key the key to check the value of.
     * @param value the value to check against the key.
     * @return true if the key's value is the same as the value given, false if the key doesn't exist.
     */
    public boolean keyEquals(String key, String value) {
        return keyEquals(key, value, false);
    }

    /**
     * Returns whether the value of the given integer key is the same as the given value.
     * @param key the key to check the value of.
     * @param value the value to check against the key.
     * @param fallback the default state if the key doesn't have a value.
     * @return true if the key's value is the same as the value given.
     */
    public boolean keyEquals(String key, int value, boolean fallback) {
        return keyEquals(key, String.valueOf(value), fallback);
    }

    /**
     * Returns whether the value of the given integer key is the same as the given value.
     * @param key the key to check the value of.
     * @param value the value to check against the key.
     * @return true if the key's value is the same as the value given, false if the key doesn't exist.
     */
    public boolean keyEquals(String key, int value) {
        return keyEquals(key, value, false);
    }

    /**
     * Returns whether the value of the given boolean key is the same as the given value.
     * @param key the key to check the value of.
     * @param value the value to check against the key.
     * @param fallback the default state if the key doesn't have a value.
     * @return true if the key's value is the same as the value given.
     */
    public boolean keyEquals(String key, boolean value, boolean fallback) {
        final String VALUE = SETTINGS.get(key);
        if (VALUE == null) return fallback;
        return VALUE.equalsIgnoreCase(String.valueOf(value));
    }

    /**
     * Returns whether the value of the given boolean key is the same as the given value.
     * @param key the key to check the value of.
     * @param value the value to check against the key.
     * @return true if the key's value is the same as the value given, false if the key doesn't exist.
     */
    public boolean keyEquals(String key, boolean value) {
        return keyEquals(key, value, false);
    }

    /**
     * Gets all keys from the Configuration.
     * @return every key in the Configuration.
     */
    public String[] getKeys() {
        return SETTINGS.keySet().toArray(new String[0]);
    }

    /**
     * Loads the settings from the Environment File.
     * @throws IOException if an error occurs when reading the file.
     */
    public void load() throws IOException {
        SETTINGS.clear();
        SETTINGS.putAll(FILE.load());
    }

    /**
     * Saves the settings to the Environment File.
     * @throws IOException if an error occurs when writing to the file.
     */
    public void save() throws IOException {
        FILE.save(SETTINGS);
    }

    private final EnvironmentFile FILE;
    private final Map<String, String> SETTINGS = new HashMap<>();

    /**
     * Locates the Environment File, firstly by looking in the working directory, then working up until it is found or the limit is reached.
     * If no Environment File is found, it will attempt to create one in the working directory.
     * @return the Environment File.
     */
    private Path locateEnvironmentFile() {
        File file = new File(".env");
        if (file.exists()) return file.toPath();

        for (int i = 1; !file.exists() && i < 3; i++)
            file = new File("../".repeat(i) + ".env");

        if (file.exists()) return file.toPath();

        file = new File(".env");

        try {
            file.createNewFile();
        }
        catch (Exception ignored) {}

        return file.toPath();
    }

    /**
     * Parses a boolean value from a String.
     * @param string the String to parse a boolean value from.
     * @return the boolean value of the String.
     * @throws BooleanFormatException if the String isn't in the format of a boolean value.
     */
    private boolean parseBoolean(String string) throws BooleanFormatException {
        if (string.equalsIgnoreCase("true")) return true;
        else if (string.equalsIgnoreCase("false")) return false;

        throw new BooleanFormatException("\"" + string + "\" cannot be parsed into a boolean value.");
    }
}