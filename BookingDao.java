import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingDao implements Dao<Booking> {

    private final GuestDao guestDao;
    private final RoomDao roomDao;

    public BookingDao(GuestDao guestDao, RoomDao roomDao) {
        this.guestDao = guestDao;
        this.roomDao = roomDao;
    }

    @Override
    public Booking get(int id) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE id=?";
        Map<Integer, Guest> guestMap = loadGuestMap();
        Map<Integer, Room> roomMap = loadRoomMap();
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapBooking(rs, guestMap, roomMap);
                }
            }
        }
        return null;
    }

    @Override
    public List<Booking> getAll() throws SQLException {
        List<Booking> bookings = new ArrayList<Booking>();
        Map<Integer, Guest> guestMap = loadGuestMap();
        Map<Integer, Room> roomMap = loadRoomMap();
        String sql = "SELECT * FROM bookings";
        Connection conn = DatabaseConnection.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Booking booking = mapBooking(rs, guestMap, roomMap);
                if (booking != null) {
                    bookings.add(booking);
                }
            }
        }
        return bookings;
    }

    @Override
    public void insert(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings (guest_id, room_id, check_in, check_out, status) VALUES (?, ?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, booking.getGuest().getId());
            ps.setInt(2, booking.getRoom().getId());
            ps.setDate(3, Date.valueOf(booking.getCheckIn()));
            ps.setDate(4, Date.valueOf(booking.getCheckOut()));
            ps.setString(5, booking.getStatus());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Booking booking) throws SQLException {
        String sql = "UPDATE bookings SET guest_id=?, room_id=?, check_in=?, check_out=?, status=? WHERE id=?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, booking.getGuest().getId());
            ps.setInt(2, booking.getRoom().getId());
            ps.setDate(3, Date.valueOf(booking.getCheckIn()));
            ps.setDate(4, Date.valueOf(booking.getCheckOut()));
            ps.setString(5, booking.getStatus());
            ps.setInt(6, booking.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM bookings WHERE id=?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void cancelBooking(int bookingId) throws SQLException {
        updateStatus(bookingId, "CANCELLED");
    }

    public void checkOut(int bookingId) throws SQLException {
        updateStatus(bookingId, "CHECKED_OUT");
    }

    public void updateStatus(int bookingId, String status) throws SQLException {
        String sql = "UPDATE bookings SET status=? WHERE id=?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        }
    }

    private Map<Integer, Guest> loadGuestMap() throws SQLException {
        Map<Integer, Guest> map = new HashMap<Integer, Guest>();
        for (Guest g : guestDao.getAll()) {
            map.put(g.getId(), g);
        }
        return map;
    }

    private Map<Integer, Room> loadRoomMap() throws SQLException {
        Map<Integer, Room> map = new HashMap<Integer, Room>();
        for (Room r : roomDao.getAll()) {
            map.put(r.getId(), r);
        }
        return map;
    }

    private Booking mapBooking(ResultSet rs, Map<Integer, Guest> guestMap, Map<Integer, Room> roomMap) throws SQLException {
        int id = rs.getInt("id");
        int guestId = rs.getInt("guest_id");
        int roomId = rs.getInt("room_id");
        String checkInStr = rs.getString("check_in");
        String checkOutStr = rs.getString("check_out");
        String status = rs.getString("status");

        Guest guest = guestMap.get(guestId);
        Room room = roomMap.get(roomId);
        if (guest != null && room != null) {
            Booking booking = new Booking(id, guest, room,
                LocalDate.parse(checkInStr), LocalDate.parse(checkOutStr));
            booking.setStatus(status);
            return booking;
        }
        return null;
    }
}
