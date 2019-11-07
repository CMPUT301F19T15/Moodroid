package ca.ualberta.moodroid.model;

/**
 * Base model used for operations with the repository
 */
abstract public class BaseModel implements ModelInterface {

    /**
     * The Internal id.
     */
    protected String internalId;

    /**
     * Internal ID used by firestore so we can update/delete objects
     *
     * @return Firestore Document ID
     */
    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }
}
