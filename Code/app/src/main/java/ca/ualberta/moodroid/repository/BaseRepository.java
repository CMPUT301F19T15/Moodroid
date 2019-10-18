package ca.ualberta.moodroid.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.moodroid.model.BaseModel;
import ca.ualberta.moodroid.model.ModelInterface;

public class BaseRepository implements RepositoryInterface {

    /**
     * These are configurable things
     */
    protected String collectionName;
    protected Class modelClass = BaseModel.class;


    protected CollectionReference collection;
    protected Query query;
    protected FirebaseFirestore db;

    public void BaseRepository(FirebaseFirestore db) {
        this.db = db;
        this.collection = this.db.collection(this.collectionName);
        if (this.collectionName == null) {
            Log.i("REPOSITORY", "Note, there is no collection name defined.");
        }
    }

    /**
     * This will reset the query class
     */
    protected void reset() {
        this.query = null;
        this.collection = this.db.collection(this.collectionName);

    }

    /**
     * Create a whereEqual query that returns the class so it can be chainable
     *
     * @param field
     * @param value
     * @return BaseRepository
     */
    public BaseRepository whereEqual(String field, String value) {
        if (this.query == null) {
            this.query = this.collection.whereEqualTo(field, value);
        } else {
            this.query.whereEqualTo(field, value);
        }
        return this;
    }


    public List<ModelInterface> get() {
        Task<QuerySnapshot> result;
        if (this.query == null) {
            result = this.collection.get();
        } else {
            result = this.query.get();
        }

        final List<ModelInterface> returning = new ArrayList<>();

        result.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ModelInterface data = (ModelInterface) document.toObject(modelClass);
                        returning.add(data);
                        Log.d("REPOSITORY", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d("REPOSITORY", "Error getting documents: ", task.getException());
                }
            }
        });

        return returning;
    }

    public void getAlways(EventListener<DocumentSnapshot> listener) {
        Log.d("REP", "NOT IMPLEMENTED");
    }

    public ModelInterface one(String id) {
        return new BaseModel();
    }


    public RepositoryInterface where(String field, String value) {
        return this;
    }

    public ModelInterface update(ModelInterface model) {
        return model;
    }

    public ModelInterface create(ModelInterface model) {
        return model;
    }

    public boolean delete(ModelInterface model) {
        return true;
    }
}
