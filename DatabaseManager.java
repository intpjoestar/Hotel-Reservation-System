import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/**
 * Handles all JDBC database operations for the hotel reservation system.
 * Connects to a MySQL database and provides load/save/update/delete methods
 * for users, rooms, guests, and bookings.
 */
public class DatabaseManager {

    private static final String URL = "jdbc:mysql://localhost:3306/hotel_db";
    private static final String USER = "root";
    private static final String PASS = "1234";

    private Connection connection;

    /**
     * Establishes a connection to the MySQL database.
     *
     * @throws SQLException if a database access error occurs
     */
    public void connect() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Closes the current database connection.
     *
     * @throws SQLException if a database access error occurs
     */
    public void disconnect() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Checks whether the database connection is established.
     *
     * @return {@code true} if connected, {@code false} otherwise
     */
    public boolean isConnected() {
        return connection != null;
    }

    /**
     * Loads all users from the database and adds them to the system.
     * Users with role {@code "ADMIN"} are instantiated as {@link Admin},
     * all others as {@link Receptionist}.
     *
     * @param system the HotelSystem to populate
     * @throws SQLException if a database access error occurs
     */
    public void loadUsers(HotelSystem system) throws SQLException {
        String sql = "SELECT * FROM users";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String role = rs.getString("role");
                if ("ADMIN".equals(role)) {
                    system.addUser(new Admin(id, username, password));
                } else {
                    system.addUser(new Receptionist(id, username, password));
                }
            }
        }
    }

    /**
     * Loads all rooms from the database and adds them to the system.
     *
     * @param system the HotelSystem to populate
     * @throws SQLException if a database access error occurs
     */
    public void loadRooms(HotelSystem system) throws SQLException {
        String sql = "SELECT * FROM rooms";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Room room = new Room(
                    rs.getInt("id"),
                    rs.getString("room_number"),
                    rs.getString("type"),
                    rs.getDouble("price")
                );
                room.setStatus(rs.getString("status"));
                system.addRoom(room);
            }
        }
    }

    /**
     * Loads all guests from the database and adds them to the system.
     *
     * @param system the HotelSystem to populate
     * @throws SQLException if a database access error occurs
     */
    public void loadGuests(HotelSystem system) throws SQLException {
        String sql = "SELECT * FROM guests";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Guest guest = new Guest(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("national_id"),
                    rs.getString("phone")
                );
                system.addGuest(guest);
            }
        }
    }

    /**
     * Loads all bookings from the database and adds them to the system.
     * Resolves guest and room references from the already-loaded data.
     *
     * @param system the HotelSystem to populate
     * @throws SQLException if a database access error occurs
     */
    public void loadBookings(HotelSystem system) throws SQLException {
        String sql = "SELECT * FROM bookings";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                int guestId = rs.getInt("guest_id");
                int roomId = rs.getInt("room_id");
                String checkInStr = rs.getString("check_in");
                String checkOutStr = rs.getString("check_out");
                String status = rs.getString("status");

                Guest foundGuest = findGuestById(system, guestId);
                Room foundRoom = findRoomById(system, roomId);

                if (foundGuest != null && foundRoom != null) {
                    Booking booking = new Booking(id, foundGuest, foundRoom,
                        LocalDate.parse(checkInStr), LocalDate.parse(checkOutStr));
                    booking.setStatus(status);
                    system.getBookings().add(booking);
                }
            }
        }
    }

    /**
     * Finds a guest by ID from the already-loaded system data.
     *
     * @param system  the HotelSystem containing guest data
     * @param guestId the guest ID to search for
     * @return the matching Guest, or {@code null} if not found
     */
    private Guest findGuestById(HotelSystem system, int guestId) {
        for (Guest g : system.getGuests()) {
            if (g.getId() == guestId) {
                return g;
            }
        }
        return null;
    }

    /**
     * Finds a room by ID from the already-loaded system data.
     *
     * @param system the HotelSystem containing room data
     * @param roomId the room ID to search for
     * @return the matching Room, or {@code null} if not found
     */
    private Room findRoomById(HotelSystem system, int roomId) {
        for (Room r : system.getRooms()) {
            if (r.getId() == roomId) {
                return r;
            }
        }
        return null;
    }

    /**
     * Inserts a new booking record into the database.
     *
     * @param booking the Booking to save
     * @throws SQLException if a database access error occurs
     */
    public void saveBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (guest_id, room_id, check_in, check_out, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, booking.getGuest().getId());
            ps.setInt(2, booking.getRoom().getId());
            ps.setDate(3, Date.valueOf(booking.getCheckIn()));
            ps.setDate(4, Date.valueOf(booking.getCheckOut()));
            ps.setString(5, booking.getStatus());
            ps.executeUpdate();
        }
    }

    /**
     * Inserts a new guest record into the database.
     *
     * @param guest the Guest to save
     * @throws SQLException if a database access error occurs
     */
    public void saveGuest(Guest guest) throws SQLException {
        String sql = "INSERT INTO guests (name, national_id, phone) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, guest.getName());
            ps.setString(2, guest.getNationalId());
            ps.setString(3, guest.getPhone());
            ps.executeUpdate();
        }
    }

    /**
     * Inserts a new room record into the database.
     *
     * @param room the Room to save
     * @throws SQLException if a database access error occurs
     */
    public void saveRoom(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (room_number, type, price, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getType());
            ps.setDouble(3, room.getPrice());
            ps.setString(4, room.getStatus());
            ps.executeUpdate();
        }
    }

    /**
     * Inserts a new user record into the database.
     *
     * @param user the User to save
     * @throws SQLException if a database access error occurs
     */
    public void saveUser(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            ps.executeUpdate();
        }
    }

    /**
     * Updates a booking's status to CANCELLED in the database.
     *
     * @param bookingId the ID of the booking to cancel
     * @throws SQLException if a database access error occurs
     */
    public void cancelBookingInDB(int bookingId) throws SQLException {
        String sql = "UPDATE bookings SET status='CANCELLED' WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        }
    }

    /**
     * Updates a booking's status to CHECKED_OUT in the database.
     *
     * @param bookingId the ID of the booking to check out
     * @throws SQLException if a database access error occurs
     */
    public void checkOutInDB(int bookingId) throws SQLException {
        String sql = "UPDATE bookings SET status='CHECKED_OUT' WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ps.executeUpdate();
        }
    }

    /**
     * Updates a room's status in the database to match its current in-memory status.
     *
     * @param room the Room whose status to persist
     * @throws SQLException if a database access error occurs
     */
    public void updateRoomStatus(Room room) throws SQLException {
        String sql = "UPDATE rooms SET status=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, room.getStatus());
            ps.setInt(2, room.getId());
            ps.executeUpdate();
        }
    }

    /**
     * Updates a booking's status to the given value in the database.
     *
     * @param bookingId the ID of the booking to update
     * @param status    the new status value
     * @throws SQLException if a database access error occurs
     */
    public void updateBookingStatus(int bookingId, String status) throws SQLException {
        String sql = "UPDATE bookings SET status=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        }
    }

    /**
     * Deletes a user record from the database.
     *
     * @param userId the ID of the user to delete
     * @throws SQLException if a database access error occurs
     */
    public void removeUserFromDB(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    /**
     * Deletes a room record from the database.
     *
     * @param roomId the ID of the room to delete
     * @throws SQLException if a database access error occurs
     */
    public void removeRoomFromDB(int roomId) throws SQLException {
        String sql = "DELETE FROM rooms WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.executeUpdate();
        }
    }
}
