package net.forgium.dotenv.exceptions;

/**
 * An Exception used by the Config when a key that isn't present was attempted to be accessed.
 */
public class MissingKeyException extends Exception {
    /**
     * Constructs a Missing Key Exception.
     * @param errorMessage the error message to show in the Exception.
     */
    public MissingKeyException(String errorMessage) {
        super(errorMessage);
    }
}