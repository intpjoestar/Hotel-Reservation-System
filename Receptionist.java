/**
 * Represents a receptionist user with limited system privileges.
 * Extends {@link User} and returns the role {@code "RECEPTIONIST"}.
 */
public class Receptionist extends User {
    /**
     * Constructs a Receptionist with the specified credentials.
     *
     * @param id       the unique user ID
     * @param username the login username
     * @param password the login password
     */
    public Receptionist(int id, String username, String password) {
        super(id, username, password);
    }

    /**
     * Returns the receptionist role identifier.
     *
     * @return {@code "RECEPTIONIST"}
     */
    public String getRole() {
        return "RECEPTIONIST";
    }
}
