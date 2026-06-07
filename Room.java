/**
 * Represents a hotel room with its number, type, nightly price, and
 * availability status.
 */
public class Room {
    private int id;
    private String roomNumber;
    private String type;
    private double price;
    private String status;

    /**
     * Constructs a new Room with the specified attributes.
     * The initial status is set to {@code "AVAILABLE"}.
     *
     * @param id         the unique room ID
     * @param roomNumber the room number (e.g. {@code "101"})
     * @param type       the room type (e.g. {@code "Single"}, {@code "Double"}, {@code "Suite"})
     * @param price      the nightly price in dollars
     */
    public Room(int id, String roomNumber, String type, double price) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.type = type;
        this.price = price;
        this.status = "AVAILABLE";
    }

    /**
     * Returns the unique room ID.
     *
     * @return the room ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the room number.
     *
     * @return the room number string
     */
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * Returns the room type.
     *
     * @return the type (Single, Double, Suite)
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the nightly price.
     *
     * @return the price in dollars
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the current occupancy status.
     *
     * @return the status string (e.g. AVAILABLE, OCCUPIED)
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the occupancy status.
     *
     * @param status the new status (AVAILABLE, OCCUPIED, etc.)
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Sets the nightly price.
     *
     * @param price the new price in dollars
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Checks whether the room is currently available.
     *
     * @return {@code true} if status equals {@code "AVAILABLE"}
     */
    public boolean isAvailable() {
        return "AVAILABLE".equals(status);
    }

    /**
     * Returns a string representation of this room.
     *
     * @return a string in the format {@code Room[101, Single, $80.00, AVAILABLE]}
     */
    public String toString() {
        return "Room[" + roomNumber + ", " + type + ", $" + price + ", " + status + "]";
    }
}
