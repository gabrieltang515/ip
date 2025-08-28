package atlas;


/**
 * Application-specific exception used to signal user input or processing
 * errors that should be shown nicely to the user.
 */
public class AtlasException extends Exception {
    public AtlasException(String message) {
        super(message);
    }
}
