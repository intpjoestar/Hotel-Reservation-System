/**
 * Represents a hotel guest with personal identification and contact information.
 */
public class Guest {
    private int id;
    private String name;
    private String nationalId;
    private String phone;

    /**
     * Constructs a Guest with the specified details.
     *
     * @param id         the unique guest ID
     * @param name       the full name of the guest
     * @param nationalId the national identification number
     * @param phone      the phone number
     */
    public Guest(int id, String name, String nationalId, String phone) {
        this.id = id;
        this.name = name;
        this.nationalId = nationalId;
        this.phone = phone;
    }

    /**
     * Returns the unique guest ID.
     *
     * @return the guest ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the guest's full name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the national identification number.
     *
     * @return the national ID
     */
    public String getNationalId() {
        return nationalId;
    }

    /**
     * Returns the phone number.
     *
     * @return the phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets the guest's full name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the phone number.
     *
     * @param phone the new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Returns a string representation of this guest.
     *
     * @return a string in the format {@code Guest[name, ID=nationalId]}
     */
    public String toString() {
        return "Guest[" + name + ", ID=" + nationalId + "]";
    }
}
