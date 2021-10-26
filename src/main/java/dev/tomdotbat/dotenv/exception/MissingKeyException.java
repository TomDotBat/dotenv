package dev.tomdotbat.dotenv.exception;

/**
 * An Exception used when the Configuration tries to access a key that isn't present.
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