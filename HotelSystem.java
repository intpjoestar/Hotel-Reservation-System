import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Core system class that manages users, rooms, guests, and bookings.
 * Implements the {@link Manageable} interface and provides business logic
 * for login, registration, and reservation workflows.
 */
public class HotelSystem implements Manageable {

    private ArrayList<User> users;
    private ArrayList<Room> rooms;
    private ArrayList<Guest> guests;
    private ArrayList<Booking> bookings;
    private User loggedInUser;

    /**
     * Constructs an empty HotelSystem with no data.
     */
    public HotelSystem() {
        users = new ArrayList<User>();
        rooms = new ArrayList<Room>();
        guests = new ArrayList<Guest>();
        bookings = new ArrayList<Booking>();
        loggedInUser = null;
    }

    /**
     * Authenticates a user with the given credentials.
     *
     * @param username the login username
     * @param password the login password
     * @return the authenticated User
     * @throws InvalidLoginException if credentials do not match any user
     */
    public User login(String username, String password) throws InvalidLoginException {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                loggedInUser = user;
                return user;
            }
        }
        throw new InvalidLoginException("Wrong username or password.");
    }

    /**
     * Logs out the currently logged-in user.
     */
    public void logout() {
        loggedInUser = null;
    }

    /**
     * Creates a new booking for a guest in a specified room.
     *
     * @param guest    the guest making the booking
     * @param room     the room to book
     * @param checkIn  the check-in date
     * @param checkOut the check-out date
     * @return the newly created Booking
     * @throws RoomNotAvailableException if the room is not available
     */
    public Booking addBooking(Guest guest, Room room, LocalDate checkIn, LocalDate checkOut)
            throws RoomNotAvailableException {
        if (!room.isAvailable()) {
            throw new RoomNotAvailableException("Room " + room.getRoomNumber() + " is not available.");
        }
        int id = bookings.size() + 1;
        Booking booking = new Booking(id, guest, room, checkIn, checkOut);
        bookings.add(booking);
        return booking;
    }

    /**
     * Cancels a booking by its ID, marking it as CANCELLED and setting the
     * associated room back to AVAILABLE.
     *
     * @param bookingId the ID of the booking to cancel
     * @throws BookingNotFoundException if no booking with the given ID exists
     */
    public void cancelBooking(int bookingId) throws BookingNotFoundException {
        Booking booking = findBookingById(bookingId);
        booking.setStatus("CANCELLED");
        booking.getRoom().setStatus("AVAILABLE");
    }

    /**
     * Checks in a guest for an active booking, marking the room as OCCUPIED.
     *
     * @param bookingId the ID of the booking to check in
     * @throws BookingNotFoundException if no booking with the given ID exists
     */
    public void checkIn(int bookingId) throws BookingNotFoundException {
        Booking booking = findBookingById(bookingId);
        booking.getRoom().setStatus("OCCUPIED");
    }

    /**
     * Checks out a guest, marking the booking as CHECKED_OUT and the room as AVAILABLE.
     *
     * @param bookingId the ID of the booking to check out
     * @throws BookingNotFoundException if no booking with the given ID exists
     */
    public void checkOut(int bookingId) throws BookingNotFoundException {
        Booking booking = findBookingById(bookingId);
        booking.setStatus("CHECKED_OUT");
        booking.getRoom().setStatus("AVAILABLE");
    }

    /**
     * Finds a booking by its ID.
     *
     * @param id the booking ID to search for
     * @return the matching Booking
     * @throws BookingNotFoundException if no booking with the given ID exists
     */
    private Booking findBookingById(int id) throws BookingNotFoundException {
        for (Booking booking : bookings) {
            if (booking.getId() == id) {
                return booking;
            }
        }
        throw new BookingNotFoundException("Booking with ID " + id + " not found.");
    }

    /**
     * Adds a new user to the system.
     *
     * @param user the User to add
     */
    public void addUser(User user) {
        users.add(user);
    }

    /**
     * Removes a user by their ID.
     *
     * @param userId the ID of the user to remove
     */
    public void removeUser(int userId) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == userId) {
                users.remove(i);
                break;
            }
        }
    }

    /**
     * Adds a new room to the system.
     *
     * @param room the Room to add
     */
    public void addRoom(Room room) {
        rooms.add(room);
    }

    /**
     * Removes a room by its ID.
     *
     * @param roomId the ID of the room to remove
     */
    public void removeRoom(int roomId) {
        for (int i = 0; i < rooms.size(); i++) {
            if (rooms.get(i).getId() == roomId) {
                rooms.remove(i);
                break;
            }
        }
    }

    /**
     * Adds a new guest to the system.
     *
     * @param guest the Guest to add
     */
    public void addGuest(Guest guest) {
        guests.add(guest);
    }

    /**
     * Returns the list of all users.
     *
     * @return the user list
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Returns the list of all rooms.
     *
     * @return the room list
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * Returns the list of all guests.
     *
     * @return the guest list
     */
    public ArrayList<Guest> getGuests() {
        return guests;
    }

    /**
     * Returns the list of all bookings.
     *
     * @return the booking list
     */
    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    /**
     * Returns the currently logged-in user.
     *
     * @return the logged-in user, or {@code null} if no user is logged in
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }

    /**
     * Placeholder for generic add operation.
     * Prints a message directing to use specific add methods.
     */
    public void add() {
        System.out.println("Use specific add methods.");
    }

    /**
     * Placeholder for generic remove operation.
     * Prints a message directing to use specific remove methods.
     */
    public void remove() {
        System.out.println("Use specific remove methods.");
    }
}
