package ca.ualberta.moodroid.model;

/**
 * The interface that must be adhered to in order to create a model. A model is a strong representation of a concrete object in our code.
 * We want to be able to serialize the entire model to easily update/create entities through the repository.
 *
 * @author Taylor Christie
 * @version v1
 */
public interface ModelInterface {

    /**
     * Gets internal id.
     *
     * @return the internal id
     */
    public String getInternalId();

    /**
     * Sets internal id.
     *
     * @param internalId the internal id
     */
    public void setInternalId(String internalId);

}
