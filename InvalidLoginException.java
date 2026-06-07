/**
 * Thrown when login credentials do not match any registered user.
 */
public class InvalidLoginException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an InvalidLoginException with the specified detail message.
     *
     * @param message the detail message
     */
    public InvalidLoginException(String message) {
        super(message);
    }
}
