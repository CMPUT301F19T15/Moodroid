package ca.ualberta.moodroid.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import ca.ualberta.moodroid.callback.Callback;
import ca.ualberta.moodroid.exception.repository.MissingCollectionNameException;
import ca.ualberta.moodroid.exception.repository.NoDataException;
import ca.ualberta.moodroid.model.BaseModel;
import ca.ualberta.moodroid.model.ModelInterface;

abstract class BaseRepository implements RepositoryInterface {

    /**
     * These are configurable things
     */
    protected String collectionName;
    protected Class modelClass = ModelInterface.class;


    protected CollectionReference collection;
    protected Query query;
    protected FirebaseFirestore db;

    public BaseRepository(String collectionName, Class cls) {
        this.db = FirebaseFirestore.getInstance();
        this.collectionName = collectionName;
        this.modelClass = cls;
        this.collection = this.db.collection(collectionName);

    }

    /**
     * This will reset the query class
     */
    public void reset() {
        this.query = null;
        this.collection = this.db.collection(this.collectionName);


    }

    protected Query getQuery() {
        if (this.query == null) {
            // CollectionReference extends Query, so this is fine.
            this.query = this.collection;
        }
        return this.query;
    }

    /**
     * Create a whereEqual query that returns the class so it can be chainable
     *
     * @param field
     * @param value
     * @return BaseRepository
     */
    public RepositoryInterface where(String field, String value) {

        this.getQuery().whereEqualTo(field, value);

        return this;
    }

    public RepositoryInterface limit(int i) {
        this.getQuery().limit(i);

        return this;
    }


    public Task<List<ModelInterface>> get() {

        final Class<ModelInterface> modelClass = this.getModelClass();

        return this.getQuery().get().continueWith(new Continuation<QuerySnapshot, List<ModelInterface>>() {
            @Override
            public List<ModelInterface> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                List<ModelInterface> ret = new ArrayList<ModelInterface>();
                for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                    ModelInterface m = doc.toObject(modelClass);
                    m.setInternalId(doc.getId());
                    ret.add(m);
                }

                return ret;
            }
        });


    }


    public Task<ModelInterface> update(final ModelInterface model) {
        return this.collection.document(model.getInternalId()).set(model).continueWith(new Continuation<Void, ModelInterface>() {
            @Override
            public ModelInterface then(@NonNull Task<Void> task) throws Exception {
                return model;
            }
        });
    }

    public Task<String> create(final ModelInterface model) {
        System.out.println("CREATE:" + this.db);
        System.out.println("CREATE:" + this.collection);
        return this.collection.add(model)
                .continueWith(new Continuation<DocumentReference, String>() {
                    @Override
                    public String then(@NonNull Task<DocumentReference> task) {
                        DocumentReference doc = task.getResult();
                        return doc.getId();
                    }
                });
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        Log.d("REPOSITORY", "Document written with ID: " + documentReference.getId());
//                        model.setInternalId(documentReference.getId());
//
//                    }
//                });
        // ensure the internalId is set
    }

    public boolean delete(ModelInterface model) {
        return true;
//        return this.collection.document(model.getInternalId()).delete();
    }

    public Class getModelClass() {
        return this.modelClass;
    }

    public void setModelClass(Class modelClass) {
        this.modelClass = modelClass;
    }
}
