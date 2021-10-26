package dev.tomdotbat.dotenv.exception;

/**
 * An Exception used by the Configuration when a Boolean fails to be parsed from a String.
 */
public class BooleanFormatException extends Exception {
    /**
     * Constructs a Boolean Format Exception.
     * @param errorMessage the error message to show in the Exception.
     */
    public BooleanFormatException(String errorMessage) {
        super(errorMessage);
    }
}