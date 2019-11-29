package ca.ualberta.moodroid.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import javax.inject.Inject;

import ca.ualberta.moodroid.ContextGrabber;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.service.MoodEventService;
import ca.ualberta.moodroid.service.StorageService;

/**
 * the function of this class is to create a simple fragment that appears after the user taps
 * a mood event in their list, which allows the user to choose to either edit or delete
 * a mood event.
 * <p>
 * a fragment for editing data
 */
public class EditDeleteFragment extends AppCompatDialogFragment {


    /**
     * The Mood event.
     */
    @Inject
    MoodEventService moodEvent;

    /**
     * The Storage service.
     */
    @Inject
    StorageService storageService;

    /**
     * The Ref.
     */
    StorageReference ref;

    /**
     * The interface On input listener.
     */
    public interface OnInputListener {
        /**
         * Delete callback.
         *
         * @param eventId the event id
         */
        void deleteCallback(String eventId);
    }

    /**
     * The On input listener.
     */
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
                                ref = null;
                                if (moodEventModel.getReasonImageUrl() != null) {
                                    ref = storageService.getStorageReference(moodEventModel.getReasonImageUrl());
                                }
                                moodEvent.deleteEvent(moodEventModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        if (ref != null) {
                                            storageService.deleteByReference(ref).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d("FRAGMENT/DELETEPHOTO", "Photo deleted.");
                                                    onInputListener.deleteCallback(id);
                                                }
                                            });
                                        } else {
                                            onInputListener.deleteCallback(id);
                                        }
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
