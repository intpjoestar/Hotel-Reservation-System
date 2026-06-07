/**
 * Defines a contract for add/remove operations in the hotel reservation system.
 * Implemented by {@link HotelSystem} to provide uniform management of entities.
 */
public interface Manageable {
    /**
     * Adds a new entity to the system.
     */
    void add();

    /**
     * Removes an entity from the system.
     */
    void remove();
}
