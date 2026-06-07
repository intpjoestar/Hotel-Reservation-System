import java.time.LocalDate;

/**
 * Represents a hotel booking that associates a guest with a room for a
 * specific date range. Maintains its own lifecycle status.
 */
public class Booking {
    private int id;
    private Guest guest;
    private Room room;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private String status;

    /**
     * Constructs a new Booking with an initial status of {@code "ACTIVE"}.
     *
     * @param id       the unique booking ID
     * @param guest    the guest making the booking
     * @param room     the room being booked
     * @param checkIn  the check-in date
     * @param checkOut the check-out date
     */
    public Booking(int id, Guest guest, Room room, LocalDate checkIn, LocalDate checkOut) {
        this.id = id;
        this.guest = guest;
        this.room = room;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = "ACTIVE";
    }

    /**
     * Returns the unique booking ID.
     *
     * @return the booking ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the guest associated with this booking.
     *
     * @return the guest
     */
    public Guest getGuest() {
        return guest;
    }

    /**
     * Returns the room associated with this booking.
     *
     * @return the room
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Returns the check-in date.
     *
     * @return the check-in date
     */
    public LocalDate getCheckIn() {
        return checkIn;
    }

    /**
     * Returns the check-out date.
     *
     * @return the check-out date
     */
    public LocalDate getCheckOut() {
        return checkOut;
    }

    /**
     * Returns the current booking status.
     *
     * @return the status (ACTIVE, CHECKED_IN, CHECKED_OUT, CANCELLED)
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the booking status.
     *
     * @param status the new status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns a string representation of this booking.
     *
     * @return a string in the format {@code Booking[id, GuestName, Room 101, 2025-06-01 to 2025-06-05, ACTIVE]}
     */
    public String toString() {
        return "Booking[" + id + ", " + guest.getName()
            + ", Room " + room.getRoomNumber()
            + ", " + checkIn + " to " + checkOut
            + ", " + status + "]";
    }
}
