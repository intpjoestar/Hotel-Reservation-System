/**
 * Abstract base class representing a system user.
 * Provides common attributes (id, username, password) and defines the contract
 * for role-specific behavior via the abstract {@link #getRole()} method.
 *
 * @see Admin
 * @see Receptionist
 */
public abstract class User {
    private int id;
    private String username;
    private String password;

    /**
     * Constructs a User with the specified credentials.
     *
     * @param id       the unique user ID
     * @param username the login username
     * @param password the login password
     */
    public User(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the unique user ID.
     *
     * @return the user ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the login username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the login password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the login username.
     *
     * @param username the new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the login password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the role identifier for this user.
     * Subclasses must override this to provide their specific role.
     *
     * @return {@code "ADMIN"} for admin users, {@code "RECEPTIONIST"} for receptionists
     */
    public abstract String getRole();

    /**
     * Returns a string representation of this user.
     *
     * @return a string in the format {@code User[username, role=ROLE]}
     */
    public String toString() {
        return "User[" + username + ", role=" + getRole() + "]";
    }
}
