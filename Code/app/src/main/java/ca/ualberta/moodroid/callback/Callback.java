package ca.ualberta.moodroid.callback;

import java.util.List;

import ca.ualberta.moodroid.model.ModelInterface;

public interface Callback {

    void onCallback(List<ModelInterface> model);
}
