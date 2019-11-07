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

    /**
     * Get task.
     *
     * @return the task
     */
// Query provider
    Task<List<ModelInterface>> get();

    /**
     * One task.
     *
     * @return the task
     */
    Task<ModelInterface> one();

    /**
     * Where repository interface.
     *
     * @param field the field
     * @param value the value
     * @return the repository interface
     */
// query filtering
    RepositoryInterface where(String field, String value);

    /**
     * Limit repository interface.
     *
     * @param i the
     * @return the repository interface
     */
    RepositoryInterface limit(int i);

    /**
     * Reset.
     */
    void reset();

    /**
     * Update task.
     *
     * @param model the model
     * @return the task
     */
    Task<ModelInterface> update(final ModelInterface model);

    /**
     * Create task.
     *
     * @param model the model
     * @return the task
     */
    Task<ModelInterface> create(ModelInterface model);

    /**
     * Delete task.
     *
     * @param model the model
     * @return the task
     */
    Task<Void> delete(ModelInterface model);

    /**
     * Gets model class.
     *
     * @return the model class
     */
    Class getModelClass();

}
