/**
 * Represents an administrator user with full system privileges.
 * Extends {@link User} and returns the role {@code "ADMIN"}.
 */
public class Admin extends User {
    /**
     * Constructs an Admin with the specified credentials.
     *
     * @param id       the unique user ID
     * @param username the login username
     * @param password the login password
     */
    public Admin(int id, String username, String password) {
        super(id, username, password);
    }

    /**
     * Returns the admin role identifier.
     *
     * @return {@code "ADMIN"}
     */
    public String getRole() {
        return "ADMIN";
    }
}
