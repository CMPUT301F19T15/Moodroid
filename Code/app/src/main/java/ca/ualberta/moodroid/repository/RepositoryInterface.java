package ca.ualberta.moodroid.repository;

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

    // somehow return a streaming/callback or something to always send new data

    // Query provider
    public ModelInterface one();

    // query filtering
    public void where(String field, String operator, String value);

    public ModelInterface update(ModelInterface model);

    public ModelInterface create(ModelInterface model);

    public void delete(ModelInterface model);

}
