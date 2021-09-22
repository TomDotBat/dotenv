package net.forgium.dotenv.exceptions;

/**
 * An Exception used by the Config when a Boolean was failed to be parsed from a String.
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