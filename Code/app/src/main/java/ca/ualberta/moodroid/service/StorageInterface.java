package ca.ualberta.moodroid.service;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * The storage interface.
 */
public interface StorageInterface {
    /**
     * This allows files to be added to the storage.
     *
     * @param refPath  the ref path
     * @param filePath the file path
     * @return task
     */
    public Task<UploadTask.TaskSnapshot> addFile(String refPath, Uri filePath);

    /**
     * This returns the url for an image in the storage.
     *
     * @return url
     */
    public  Task<Uri> getUrl();

    /**
     * This deletes a file in the storage.
     *
     * @return task
     */
    public Task<Void> delete();

    /**
     * Get the storage reference by url.
     *
     * @param url the url
     * @return storage reference
     */
    public StorageReference getStorageReference(String url);

    /**
     * Delete an item in the storage by Storage reference.
     *
     * @param ref the ref
     * @return task
     */
    public Task<Void> deleteByReference(StorageReference ref);

}
