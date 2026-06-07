import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Entry point for the Hotel Reservation System application.
 * Initializes the database connection, loads persisted data, and launches
 * the login GUI on the Event Dispatch Thread.
 */
public class Main {

    /**
     * Application entry point.
     * <ol>
     *   <li>Sets the system look-and-feel</li>
     *   <li>Creates the core {@link HotelSystem} and {@link DatabaseManager}</li>
     *   <li>Connects to the database and loads all data</li>
     *   <li>Opens the {@link LoginFrame} on the EDT</li>
     * </ol>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Use default look and feel
        }

        HotelSystem system = new HotelSystem();
        DatabaseManager db = new DatabaseManager();

        try {
            db.connect();
            db.loadUsers(system);
            db.loadRooms(system);
            db.loadGuests(system);
            db.loadBookings(system);
        } catch (Exception e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame(system, db);
            }        });
    }
}
