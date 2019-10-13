package ca.ualberta.moodroid.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Query;

import java.util.List;

import ca.ualberta.moodroid.model.ModelInterface;

/**
 * The interface that must be adhered to in order to create a repository, a repository handles the ability to fetch, update, create, and delete data.
 * <p>
 * TODO: requires a static get, and a streaming (realtime) get.
 *
 * @author Taylor Christie
 * @version v1
 */
public interface RepositoryInterface {

    // Query provider
    public List<ModelInterface> get();

    public void getAlways(EventListener<DocumentSnapshot> listener);

    // Query provider
    public ModelInterface one(String id);

    // query filtering
    public RepositoryInterface where(String field, String value);

    public ModelInterface update(ModelInterface model);

    public ModelInterface create(ModelInterface model);

    public boolean delete(ModelInterface model);

}
