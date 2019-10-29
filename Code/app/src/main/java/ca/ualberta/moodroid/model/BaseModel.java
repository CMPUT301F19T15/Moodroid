package ca.ualberta.moodroid.model;

abstract public class BaseModel implements ModelInterface {

    protected String internalId;

    public String getInternalId() {
        return internalId;
    }

    public void setInternalId(String internalId) {
        this.internalId = internalId;
    }
}
