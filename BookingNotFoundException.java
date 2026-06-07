/**
 * Thrown when attempting to find or operate on a booking that does not exist.
 */
public class BookingNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a BookingNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public BookingNotFoundException(String message) {
        super(message);
    }
}
