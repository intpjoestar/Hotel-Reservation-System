import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GuestDao implements Dao<Guest> {

    @Override
    public Guest get(int id) throws SQLException {
        String sql = "SELECT * FROM guests WHERE id=?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapGuest(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<Guest> getAll() throws SQLException {
        List<Guest> guests = new ArrayList<Guest>();
        String sql = "SELECT * FROM guests";
        Connection conn = DatabaseConnection.getConnection();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                guests.add(mapGuest(rs));
            }
        }
        return guests;
    }

    @Override
    public void insert(Guest guest) throws SQLException {
        String sql = "INSERT INTO guests (name, national_id, phone) VALUES (?, ?, ?)";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, guest.getName());
            ps.setString(2, guest.getNationalId());
            ps.setString(3, guest.getPhone());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Guest guest) throws SQLException {
        String sql = "UPDATE guests SET name=?, national_id=?, phone=? WHERE id=?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, guest.getName());
            ps.setString(2, guest.getNationalId());
            ps.setString(3, guest.getPhone());
            ps.setInt(4, guest.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM guests WHERE id=?";
        Connection conn = DatabaseConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private Guest mapGuest(ResultSet rs) throws SQLException {
        return new Guest(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("national_id"),
            rs.getString("phone")
        );
    }
}
