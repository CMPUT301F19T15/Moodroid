package ca.ualberta.moodroid.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.service.UserService;

public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.ViewHolder> {
    private ArrayList<FollowRequestModel> requestList;
    UserService userService;
    static Context context;

    private OnListListener mOnListListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {


        public TextView requestText;
        public Button denyButton;
        public Button acceptButton;
        FollowListAdapter.OnListListener onListListener;


        public ViewHolder(@NonNull View itemView, OnListListener onListListener) {
            super(itemView);
            //get references to items in list
            this.onListListener = onListListener;
            itemView.setOnLongClickListener(this);
            this.requestText = itemView.findViewById(R.id.mood_list_view_moodText);
            this.denyButton = itemView.findViewById(R.id.follow_deny_btn);
            this.acceptButton = itemView.findViewById(R.id.follow_accept_btn);


        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    public FollowListAdapter(ArrayList<FollowRequestModel> requestList, UserService userService, OnListListener onListListener) {
        this.requestList = requestList;
        this.userService = userService;
        context = context;
        this.mOnListListener = onListListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //pass content view in
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_notification_content, parent, false);
        //view.setOnLongClickListener(new View.OnLongClickListener() {
        //    @Override
        //    public boolean onLongClick(View view) {
        ///////////////////////////////////////////////
////////////////////////////////go to fragment....delete from db using moodEventService.deleteEvent
        ////////////////////////////////////////////////////
        //        openEditDeleteDialog();
        //        return false;
        //    }
        //});


        ViewHolder viewHolder = new ViewHolder(view, mOnListListener);
        return viewHolder;
    }

    //

    public interface OnListListener {
        void onListClick(int position);
    }

    public void openEditDeleteDialog() {

        EditDeleteFragment editDeleteFragment = new EditDeleteFragment();
        editDeleteFragment.show(((MoodHistory) context).getSupportFragmentManager(), "Options");
    }

    //
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //assign values and display items in list
        // TODO: show background color base on state and hide buttons if state != undecided
        FollowRequestModel moodObject = requestList.get(position);

        if (moodObject.getState().equals(FollowRequestModel.REQUESTED_STATE)) {
            holder.requestText.setText("@" + moodObject.getRequesterUsername() + " requested to be friends on " + moodObject.getCreatedAt());
        } else if (moodObject.getState().equals(FollowRequestModel.ACCEPT_STATE)) {
            setAccepted(holder, moodObject);
        } else if (moodObject.getState().equals(FollowRequestModel.DENY_STATE)) {
            setDenied(holder, moodObject);
        } else {
            holder.requestText.setText("Request from @" + moodObject.getRequesterUsername());

            Log.e("NOTICATIONS/STATE", "Unknown state for " + moodObject.getInternalId() + " state=" + moodObject.getState());
        }

        holder.acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("NOTIFICATION/CLICK", "Accept clicked for: " + moodObject.getInternalId());
                moodObject.setState(FollowRequestModel.ACCEPT_STATE);
                userService.acceptFollowRequest(moodObject).addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean) {
                            setAccepted(holder, moodObject);
                        } else {
                            Log.e("NOTIFICATION/ACCEPT", "Could not accept the follow for " + moodObject.getInternalId());
                        }
                    }
                });
            }
        });

        holder.denyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("NOTIFICATION/CLICK", "Deny clicked for: " + moodObject.getInternalId());
                moodObject.setState(FollowRequestModel.DENY_STATE);
                userService.denyFollowRequest(moodObject).addOnSuccessListener(new OnSuccessListener<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean) {
                            setDenied(holder, moodObject);
                        } else {
                            Log.e("NOTIFICATION/DENY", "Could not deny the follow for " + moodObject.getInternalId());
                        }
                    }
                });
            }
        });
    }

    public void setAccepted(ViewHolder holder, FollowRequestModel request) {
        holder.requestText.setText("@" + request.getRequesterUsername() + " is following your moods.");
        holder.denyButton.setVisibility(View.INVISIBLE);
        holder.acceptButton.setVisibility(View.INVISIBLE);

    }

    public void setDenied(ViewHolder holder, FollowRequestModel request) {
        holder.requestText.setText("@" + request.getRequesterUsername() + " was declined.");
        holder.denyButton.setVisibility(View.INVISIBLE);
        holder.acceptButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        //return nr of items in list
        return requestList.size();
    }
}
