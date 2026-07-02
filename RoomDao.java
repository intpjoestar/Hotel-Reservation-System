import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoomDao implements Dao<Room> {

    @Override
    public Room get(int id) throws SQLException {
        String sql = "SELECT * FROM rooms WHERE id=?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRoom(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Room> getAll() throws SQLException {
        List<Room> rooms = new ArrayList<Room>();
        String sql = "SELECT * FROM rooms";
        Connection conn = DatabaseConnection.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                rooms.add(mapRoom(rs));
            }
        }
        return rooms;
    }

    @Override
    public void insert(Room room) throws SQLException {
        String sql = "INSERT INTO rooms (room_number, type, price, status) VALUES (?, ?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getType());
            ps.setDouble(3, room.getPrice());
            ps.setString(4, room.getStatus());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Room room) throws SQLException {
        String sql = "UPDATE rooms SET room_number=?, type=?, price=?, status=? WHERE id=?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getRoomNumber());
            ps.setString(2, room.getType());
            ps.setDouble(3, room.getPrice());
            ps.setString(4, room.getStatus());
            ps.setInt(5, room.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM rooms WHERE id=?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public void updateStatus(Room room) throws SQLException {
        String sql = "UPDATE rooms SET status=? WHERE id=?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, room.getStatus());
            ps.setInt(2, room.getId());
            ps.executeUpdate();
        }
    }

    private Room mapRoom(ResultSet rs) throws SQLException {
        Room room = new Room(
            rs.getInt("id"),
            rs.getString("room_number"),
            rs.getString("type"),
            rs.getDouble("price")
        );
        room.setStatus(rs.getString("status"));
        return room;
    }
}
