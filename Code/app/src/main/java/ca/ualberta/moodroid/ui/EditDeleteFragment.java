package ca
        .ualberta.moodroid.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import javax.inject.Inject;

import ca.ualberta.moodroid.ContextGrabber;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.repository.MoodRepository;
import ca.ualberta.moodroid.service.MoodEventService;

import static android.graphics.Color.parseColor;

/**
 * a fragment for editing data
 */
public class EditDeleteFragment extends AppCompatDialogFragment {


    @Inject
    MoodEventService moodEvent;


    public interface OnInputListener {
        void deleteCallback(String eventId);
    }

    public OnInputListener onInputListener;

    /**
     * Create and display the dialog for mood event actions
     *
     * @param savedInstanceState
     * @return
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        ContextGrabber.get().di().inject(EditDeleteFragment.this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_delete_fragment, null);
        Bundle bundle = getArguments();
        final String id = bundle.getString("eventId");

        return new AlertDialog.Builder(getActivity()).setView(view)
                .setTitle("Options")
                .setMessage("What would you like to do?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //gets the mood event by id
                        Intent intent = new Intent(getActivity(), EditMoodDetail.class);
                        intent.putExtra("eventId", id);
                        intent.putExtra("emoji", bundle.getString("emoji"));
                        intent.putExtra("mood_name", bundle.getString("mood_name"));
                        intent.putExtra("hex", bundle.getString("hex"));
                        startActivity(intent);
                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //gets the mood event by id
                        moodEvent.getEventWithId(id).addOnSuccessListener(new OnSuccessListener<MoodEventModel>() {
                            @Override
                            public void onSuccess(MoodEventModel moodEventModel) {
                                moodEvent.deleteEvent(moodEventModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        onInputListener.deleteCallback(id);
                                    }
                                });
                            }
                        });

                    }
                }).create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            /* this line is main difference for fragment to fragment communication & fragment to activity communication
            fragment to fragment: onInputListener = (OnInputListener) getTargetFragment();
            fragment to activity: onInputListener = (OnInputListener) getActivity();
             */
            onInputListener = (OnInputListener) getActivity();
            Log.d("EDITDELETEFRAG", "onAttach: " + onInputListener);
        } catch (ClassCastException e) {
            Log.d("EDITDELETEFRAG", "onAttach: ClassCastException : " + e.getMessage());
        }
    }
}
