package ca.ualberta.moodroid.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.moodroid.model.ModelInterface;

/**
 * The base repository all repositories use to query FireStore.
 *
 * @author Taylor Christie
 * @version v1
 */
abstract class BaseRepository implements RepositoryInterface {

    /**
     * These are configurable things
     */
    protected String collectionName;
    protected Class modelClass;


    protected CollectionReference collection;
    protected Query query;
    protected FirebaseFirestore db;


    /**
     * Build the repository by the collection/table name and the model class
     *
     * @param collectionName name of the firestore collection
     * @param cls            the class of the concrete model
     */
    public BaseRepository(String collectionName, Class cls) {
        this.db = FirebaseFirestore.getInstance();
        this.collectionName = collectionName;
        this.modelClass = cls;
        this.collection = this.db.collection(collectionName);

    }

    /**
     * This will reset the query class so we can run multiple queries
     */
    public void reset() {
        this.query = null;
        this.collection = this.db.collection(this.collectionName);


    }

    /**
     * Get the current query
     *
     * @return
     */
    protected Query getQuery() {
        if (this.query == null) {
            // CollectionReference extends Query, so this is fine.
            this.query = this.collection;
        }
        return this.query;
    }

    /**
     * Set the current query
     *
     * @param q
     */
    protected void setQuery(Query q) {
        this.query = q;
    }

    /**
     * Create a where query that returns the class so it can be chainable
     *
     * @param field the field you want to search for
     * @param value the value you want it to match
     * @return BaseRepository
     */
    public RepositoryInterface where(String field, String value) {

        this.setQuery(this.getQuery().whereEqualTo(field, value));

        return this;
    }

    /**
     * Limit the number of query results when using get
     *
     * @param i
     * @return
     */
    public RepositoryInterface limit(int i) {
        this.setQuery(this.getQuery().limit(i));

        return this;
    }


    /**
     * Get the results from your query. If no conditions set, it will return all values
     *
     * @return a asyncronous list of models which need to be cast to their proper model
     */
    public Task<List<ModelInterface>> get() {

        final Class<ModelInterface> modelClass = this.getModelClass();

        final RepositoryInterface r = this;

        return this.getQuery().get().continueWith(new Continuation<QuerySnapshot, List<ModelInterface>>() {
            @Override
            public List<ModelInterface> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                r.reset();
                List<ModelInterface> ret = new ArrayList<ModelInterface>();
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    Log.d("REPO/GET", doc.getId());
                    ModelInterface m = doc.toObject(modelClass);
                    m.setInternalId(doc.getId());
                    ret.add(m);
                }

                return ret;
            }
        });
    }

    /**
     * Get a single result from your query
     *
     * @return
     */
    public Task<ModelInterface> one() {
        final Class<ModelInterface> modelClass = this.getModelClass();
        final RepositoryInterface r = this;

        return this.getQuery().limit(1).get().continueWith(new Continuation<QuerySnapshot, ModelInterface>() {
            @Override
            public ModelInterface then(@NonNull Task<QuerySnapshot> task) throws Exception {
                r.reset();
                DocumentSnapshot doc = task.getResult().getDocuments().get(0);

                ModelInterface m = doc.toObject(modelClass);
                m.setInternalId(doc.getId());

                return m;
            }
        });
    }

    public Task<ModelInterface> find(String id) {
        final Class<ModelInterface> modelClass = this.getModelClass();
        return this.collection.document(id).get().continueWith(new Continuation<DocumentSnapshot, ModelInterface>() {
            @Override
            public ModelInterface then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                return task.getResult().toObject(modelClass);
            }

        });
    }


    /**
     * Update an existing model.
     * TODO: will not check if the internalId is set. It should throw an error if it is not set.
     *
     * @param model
     * @return
     */
    public Task<ModelInterface> update(final ModelInterface model) {
        Log.d("REPOSITORY/UPDATE", "Updating " + model.getInternalId());
        return this.collection.document(model.getInternalId()).set(model).continueWith(new Continuation<Void, ModelInterface>() {
            @Override
            public ModelInterface then(@NonNull Task<Void> task) throws Exception {
                if (task.isSuccessful()) {

                }
                Log.d("REPO/UPDATE", model.getInternalId());

                return model;
            }
        });
    }

    /**
     * Create a new object in firestore from a model. It will return the model with the internalId
     *
     * @param model
     * @return
     */
    public Task<ModelInterface> create(final ModelInterface model) {
        return this.collection.add(model)
                .continueWith(new Continuation<DocumentReference, ModelInterface>() {
                    @Override
                    public ModelInterface then(@NonNull Task<DocumentReference> task) {
                        DocumentReference doc = task.getResult();
                        Log.d("REPO/CREATE", doc.getId());
                        model.setInternalId(doc.getId());

                        return model;
                    }
                });
    }

    public Task<ModelInterface> create(final ModelInterface model, final String docId) {
        return this.collection.document(docId).set(model)
                .continueWith(new Continuation<Void, ModelInterface>() {
                    @Override
                    public ModelInterface then(@NonNull Task<Void> task) {
                        model.setInternalId(docId);
                        return model;
                    }
                });
    }

    /**
     * Delete an entry in firestore
     *
     * @param model
     * @return
     */
    public Task<Void> delete(ModelInterface model) {
        Log.d("REPO/DELETE", model.getInternalId());
        return this.collection.document(model.getInternalId()).delete();
    }

    /**
     * Used to get the model class
     *
     * @return
     */
    public Class getModelClass() {
        return this.modelClass;
    }

}
