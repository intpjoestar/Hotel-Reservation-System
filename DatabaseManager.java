import java.sql.SQLException;
import java.util.List;

/**
 * Facade that delegates all database operations to dedicated DAO classes.
 * Each entity (User, Room, Guest, Booking) has its own DAO implementation
 * that encapsulates the JDBC logic for that entity.
 */
public class DatabaseManager {

    private final UserDao userDao;
    private final RoomDao roomDao;
    private final GuestDao guestDao;
    private final BookingDao bookingDao;

    public DatabaseManager() {
        this.userDao = new UserDao();
        this.roomDao = new RoomDao();
        this.guestDao = new GuestDao();
        this.bookingDao = new BookingDao(guestDao, roomDao);
    }

    public void connect() throws SQLException {
        DatabaseConnection.getConnection();
    }

    public void disconnect() throws SQLException {
        DatabaseConnection.close();
    }

    public boolean isConnected() {
        try {
            return DatabaseConnection.getConnection() != null;
        } catch (SQLException e) {
            return false;
        }
    }

    public Dao<User> getUserDao() {
        return userDao;
    }

    public RoomDao getRoomDao() {
        return roomDao;
    }

    public GuestDao getGuestDao() {
        return guestDao;
    }

    public BookingDao getBookingDao() {
        return bookingDao;
    }

    public void loadUsers(HotelSystem system) throws SQLException {
        List<User> users = userDao.getAll();
        for (User user : users) {
            system.addUser(user);
        }
    }

    public void loadRooms(HotelSystem system) throws SQLException {
        List<Room> rooms = roomDao.getAll();
        for (Room room : rooms) {
            system.addRoom(room);
        }
    }

    public void loadGuests(HotelSystem system) throws SQLException {
        List<Guest> guests = guestDao.getAll();
        for (Guest guest : guests) {
            system.addGuest(guest);
        }
    }

    public void loadBookings(HotelSystem system) throws SQLException {
        List<Booking> bookings = bookingDao.getAll();
        for (Booking booking : bookings) {
            system.getBookings().add(booking);
        }
    }

    public void saveBooking(Booking booking) throws SQLException {
        bookingDao.insert(booking);
    }

    public void saveGuest(Guest guest) throws SQLException {
        guestDao.insert(guest);
    }

    public void saveRoom(Room room) throws SQLException {
        roomDao.insert(room);
    }

    public void saveUser(User user) throws SQLException {
        userDao.insert(user);
    }

    public void cancelBookingInDB(int bookingId) throws SQLException {
        bookingDao.cancelBooking(bookingId);
    }

    public void checkOutInDB(int bookingId) throws SQLException {
        bookingDao.checkOut(bookingId);
    }

    public void updateRoomStatus(Room room) throws SQLException {
        roomDao.updateStatus(room);
    }

    public void updateBookingStatus(int bookingId, String status) throws SQLException {
        bookingDao.updateStatus(bookingId, status);
    }

    public void removeUserFromDB(int userId) throws SQLException {
        userDao.delete(userId);
    }

    public void removeRoomFromDB(int roomId) throws SQLException {
        roomDao.delete(roomId);
    }
}
