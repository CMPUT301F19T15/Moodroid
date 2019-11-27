package ca.ualberta.moodroid.service;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * The storage service allows access to the storage in order to save, access and delete files.
 */

@Singleton
public class StorageService implements StorageInterface {

    /**
     * The Firebase storage and the storage reference.
     */
    private FirebaseStorage storage;
    private StorageReference reference;

    @Inject
    public StorageService() {
        this.storage = FirebaseStorage.getInstance();
        this.reference = storage.getReference();
    }


    /**
     * This allows files to be added to the storage.
     * @param refPath
     * @param filePath
     * @return the snapshot
     */
    public Task<UploadTask.TaskSnapshot> addFile(String refPath, Uri filePath) {
        this.reference = storage.getReference(); //reset reference pointer
        this.reference = reference.child(refPath);
        return this.reference.putFile(filePath).continueWith(new Continuation<UploadTask.TaskSnapshot, UploadTask.TaskSnapshot>() {
            @Override
            public UploadTask.TaskSnapshot then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                UploadTask.TaskSnapshot snapshot = null;
                if(task.isSuccessful()){
                    snapshot = task.getResult();
                }
                return snapshot;
            }
        });
    }

    /**
     * This returns the url for an image in the storage.
     * @return the url
     */
    public  Task<Uri> getUrl(){
        return reference.getDownloadUrl().continueWith(new Continuation<Uri, Uri>() {
            @Override
            public Uri then(@NonNull Task<Uri> task) throws Exception {
                Uri url = null;
                if(task.isSuccessful()){
                    url = task.getResult();
                }
                return url;
            }
        });
    }

    /**
     * This deletes a file in the storage.
     * @return
     */
    public Task<Void> delete(){
        return reference.delete().continueWith(new Continuation<Void, Void>() {
            @Override
            public Void then(@NonNull Task<Void> task) throws Exception {
                Log.d("DELETION/", "Photo deleted.");
                return null;
            }
        });
    }
}
