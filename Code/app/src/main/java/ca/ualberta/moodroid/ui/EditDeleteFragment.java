package ca.ualberta.moodroid.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.service.MoodEventService;

import static android.graphics.Color.parseColor;

/**
 * NOT FULLY IMPLEMENTED
 *
 * a fragment for editing data
 */
public class EditDeleteFragment extends AppCompatDialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_delete_fragment, null);
        Bundle bundle = getArguments();
        final String id = bundle.getString("eventId");

        builder.setView(view)
                .setTitle("Options")
                .setMessage("What would you like to do?")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MoodEventService moodEvent = new MoodEventService();
                        //gets the mood event by id
                        moodEvent.getEventWithId(id).addOnSuccessListener(new OnSuccessListener<MoodEventModel>() {
                            @Override
                            public void onSuccess(MoodEventModel moodEventModel) {
                                //For some reason internalid is set to null
                                moodEventModel.setInternalId(id);
                                Intent intent = new Intent(getActivity(), EditMoodDetail.class);
                                intent.putExtra("eventId", moodEventModel.getInternalId());
                                startActivity(intent);
                            }
                        });

                    }
                })
                .setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MoodEventService moodEvent = new MoodEventService();
                        //gets the mood event by id
                        moodEvent.getEventWithId(id).addOnSuccessListener(new OnSuccessListener<MoodEventModel>() {
                            @Override
                            public void onSuccess(MoodEventModel moodEventModel) {
                                //For some reason internalid is set to null
                                moodEventModel.setInternalId(id);
                                MoodEventRepository mood = new MoodEventRepository();
                                mood.delete(moodEventModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("RESULT/DELETE", "Model Deleted");
                                    }
                                });
                            }
                        });



                    }
                });
        return builder.create();
    }
}
