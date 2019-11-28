package ca.ualberta.moodroid.service;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

import ca.ualberta.moodroid.model.MoodEventModel;

/**
 * The storage interface.
 */

public interface StorageInterface {
    /**
     * This allows files to be added to the storage.
     * @param refPath
     * @param filePath
     * @return
     */
    public Task<UploadTask.TaskSnapshot> addFile(String refPath, Uri filePath);

    /**
     * This returns the url for an image in the storage.
     * @return
     */
    public  Task<Uri> getUrl();

    /**
     * This deletes a file in the storage.
     * @return
     */
    public Task<Void> delete();
//
//
//






//    /**
//     * Get the storage path by url.
//     * @param url
//     * @return
//     */
//    public String getStoragePath(String url);

    /**
     * Get the storage reference by url.
     * @param url
     * @return
     */
    public StorageReference getStorageReference(String url);

//    public StorageReference getReference();

//    public void setReference(StorageReference reference);

    /**
     * Delete an item in the storage by Storage reference.
     * @param ref
     * @return
     */
    public Task<Void> deleteByReference(StorageReference ref);

//    /**
//     * Delete an item in the storage using the current reference.
//     * @return
//     */
//    public Task<Void> deleteItem();
}
