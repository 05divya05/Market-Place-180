/**
 * The ImageHandlerInterface handles uploading, retrieving, editing, deleting, and downloading images.
 * Buyers cannot modify images, they can only download images.
 *
 * @author Yihang Li
 * @version 4/1/2025
 */
public interface ImageHandlerInterface {

    /**
     * Uploads an image and associates it with an item.
     *
     * @param itemID The ID of the item.
     * @param imagePath The file path of the image to be uploaded.
     */
    void uploadImage(String itemID, String imagePath);

    /**
     * Retrieves the path of an image associated with an item.
     *
     * @param itemID The ID of the item.
     * @return The file path of the image.
     */
    String getImagePath(String itemID);

    /**
     * Deletes an image associated with an item.
     *
     * @param itemID The ID of the item.
     * @return true if deletion is successful, false otherwise.
     */
    boolean deleteImage(String itemID);

    /**
     * Downloads an image associated with a specific item.
     *
     * @param itemID The ID of the item.
     * @param savePath The file path where the image will be saved.
     * @return true if the download is successful, false otherwise.
     */
    boolean downloadImage(String itemID, String savePath);

    /**
     * Allows a seller to edit or update the image associated with their item.
     *
     * @param itemID The ID of the item.
     * @param sellerID The ID of the seller requesting the update.
     * @param newImagePath The new file path of the image to replace the old one.
     * @return true if the update is successful, false otherwise.
     */
    boolean editImage(String itemID, String sellerID, String newImagePath);
}