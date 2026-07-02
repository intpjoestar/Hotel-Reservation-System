import java.sql.SQLException;
import java.util.List;

public interface Dao<T> {

    T get(int id) throws SQLException;

    List<T> getAll() throws SQLException;

    void insert(T entity) throws SQLException;

    void update(T entity) throws SQLException;

    void delete(int id) throws SQLException;
}
