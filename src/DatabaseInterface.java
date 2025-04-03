/**
 * The DatabaseInterface handles all data storing, deleting, updating and retrieving.
 * Should us toString to write different objects.
 *
 * @author Yihang Li
 * @version 4/1/2025
 */
public interface DatabaseInterface {

    /**
     * Saves an object to the database. Ex. Item item/ User user...
     *
     * @param obj The kind of object to be saved.
     */
    void save(Object obj);

    /**
     * Loads an object from the database using its unique ID.
     *
     * @param id The ID of the object.
     * @return The loaded object.
     */
    Object load(String id);

    /**
     * Deletes an object from the database using its unique ID.
     *
     * @param id The ID of the object to be deleted.
     * @return true if the object is successfully deleted, false otherwise.
     */
    boolean delete(String id);

    /**
     * Updates an existing object in the database.
     *
     * @param obj The object to be updated.
     */
    void update(Object obj);
}