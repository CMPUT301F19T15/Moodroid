package ca.ualberta.moodroid.repository;

import com.google.android.gms.tasks.Task;

import java.util.List;

import ca.ualberta.moodroid.model.ModelInterface;

/**
 * The interface that must be adhered to in order to create a repository, a repository handles the ability to fetch, update, create, and delete data.
 *
 * @author Taylor Christie
 * @version v1
 */
public interface RepositoryInterface {

    // Query provider
    Task<List<ModelInterface>> get();

    Task<ModelInterface> one();

    // query filtering
    RepositoryInterface where(String field, String value);

    RepositoryInterface limit(int i);

    void reset();

    Task<ModelInterface> update(final ModelInterface model);

    Task<ModelInterface> create(ModelInterface model);

    Task<Void> delete(ModelInterface model);

    Class getModelClass();

}
