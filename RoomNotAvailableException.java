/**
 * Thrown when attempting to book a room that is not currently available.
 */
public class RoomNotAvailableException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a RoomNotAvailableException with the specified detail message.
     *
     * @param message the detail message
     */
    public RoomNotAvailableException(String message) {
        super(message);
    }
}
