package ca.ualberta.moodroid.model;

/**
 * Base model used for operations with the repository
 * <p>
 * This is an abstract class that extends to other types of models that make up the
 * bulk of the repo. Implements ModelInterface which forces any model type to have an internal ID.
 */
abstract public class BaseModel implements ModelInterface {

    /**
     * The Internal id. Unique to all model objects in firebase and the primary reference
     * to all said objects.
     */
    protected String internalId;

    /**
     * Internal ID used by firestore so we can update/delete objects
     *
     * @return Firestore Document ID
     */

    /**
     *  getInternalId
     *
     *  a simple getter method for fetching a unique mood ID
     */
    public String getInternalId() {
        return internalId;
    }

    /**
     * setInternalID
     * @param internalId the internal id
     *
     * sets this objects internal ID with the given parameter
     */

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }
}
