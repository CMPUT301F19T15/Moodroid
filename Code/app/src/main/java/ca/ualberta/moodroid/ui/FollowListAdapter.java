package ca.ualberta.moodroid.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.FollowRequestModel;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.UserService;

/**
 * Adapter for the list of follow requests on the notification page. This code takes data from
 * requests sent to the user and displays them with some character.
 */
public class FollowListAdapter extends RecyclerView.Adapter<FollowListAdapter.ViewHolder> {
    private ArrayList<FollowRequestModel> requestList;
    /**
     * The User service type, used for handling data and situations with friends
     * and following
     */
    UserService userService;
    /**
     * The Context.
     */
    static Context context;

    private OnListListener mOnListListener;

    /**
     * The type View holder. A ViewHolder describes an item view and metadata about its place
     * within the RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {


        /**
         * The Request text from a different user, displayed in a text view.
         */
        public TextView requestText;
        /**
         * The deny button. A user can click this button to deny a follow request.
         */
        public Button denyButton;
        /**
         * The Accept button. A user can click this button to accept a follow request.
         */
        public Button acceptButton;

        public TextView dateText;

        /**
         * The On list listener, which is just a listener object for an item in a list, works like
         * a listener for a button.
         */
        public ImageView background;
        FollowListAdapter.OnListListener onListListener;


        /**
         * Instantiates a new View holder.
         *
         * @param itemView       the item view
         * @param onListListener the on list listener
         */
        public ViewHolder(@NonNull View itemView, OnListListener onListListener) {
            super(itemView);
            //get references to items in list
            this.onListListener = onListListener;
            itemView.setOnLongClickListener(this);
            this.requestText = itemView.findViewById(R.id.mood_list_view_moodText);
            this.denyButton = itemView.findViewById(R.id.follow_deny_btn);
            this.acceptButton = itemView.findViewById(R.id.follow_accept_btn);
            this.dateText = itemView.findViewById(R.id.mood_list_date);
            this.background = itemView.findViewById(R.id.list_item_background);

        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }

    /**
     * Instantiates a new Follow list adapter.
     *
     * @param requestList    the request list
     * @param userService    the user service
     * @param onListListener the on list listener
     */
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

    /**
     * The interface On list listener.
     */
    public interface OnListListener {
        /**
         * On list click.
         *
         * @param position the position
         */
        void onListClick(int position);
    }

    /**
     * Open edit delete dialog.
     */
    public void openEditDeleteDialog() {

        EditDeleteFragment editDeleteFragment = new EditDeleteFragment();
        editDeleteFragment.show(((MoodHistory) context).getSupportFragmentManager(), "Options");
    }

    /**
     * When a new list item is added, set the view parameters (color, text, buttons, ect) also setup the button actions individually for each list item
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //assign values and display items in list
        // TODO: show background color base on state and hide buttons if state != undecided
        FollowRequestModel moodObject = requestList.get(position);
        holder.dateText.setText(new SimpleDateFormat("MM/dd/yyyy hh:mm a").format(moodObject.dateObject()));
        if (moodObject.getRequesteeUsername().equals(AuthenticationService.getInstance().getUsername())) {
            if (moodObject.getState().equals(FollowRequestModel.REQUESTED_STATE)) {
                holder.requestText.setText("@" + moodObject.getRequesterUsername() + " requested to be friends on " + new SimpleDateFormat("MMMM dd yyyy").format(moodObject.dateObject()));
            } else if (moodObject.getState().equals(FollowRequestModel.ACCEPT_STATE)) {
                setAccepted(holder, moodObject);
            } else if (moodObject.getState().equals(FollowRequestModel.DENY_STATE)) {
                setDenied(holder, moodObject);
            } else {
                holder.requestText.setText("Request from @" + moodObject.getRequesterUsername());

                Log.e("NOTICATIONS/STATE", "Unknown state for " + moodObject.getInternalId() + " state=" + moodObject.getState());
            }
        } else if (moodObject.getRequesterUsername().equals(AuthenticationService.getInstance().getUsername())) {
            // this isn't our object, so we should be able to alter state
            holder.denyButton.setVisibility(View.INVISIBLE);
            holder.acceptButton.setVisibility(View.INVISIBLE);

            if (moodObject.getState().equals(FollowRequestModel.REQUESTED_STATE)) {
                holder.requestText.setText("Awaiting @" + moodObject.getRequesteeUsername() + "'s response.");
            } else {
                holder.requestText.setText("@" + moodObject.getRequesteeUsername() + " " + moodObject.getState() + " your follow request.");
            }
        } else {
            Log.e("NOTIFICATIONS/DATA", "user recieved a notification for a request not belonging to them...");
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

    /**
     * Sets a given item view to "accepted" mode, removing the buttons and changing the text.
     *
     * @param holder  the holder
     * @param request the request
     */
    public void setAccepted(ViewHolder holder, FollowRequestModel request) {
        holder.requestText.setText("@" + request.getRequesterUsername() + " is following your moods.");

        holder.denyButton.setVisibility(View.GONE);
        holder.acceptButton.setVisibility(View.GONE);
        holder.background.setBackgroundColor(Color.parseColor("#056608"));

    }

    /**
     * Sets a given item view to "denied" mode, removing buttons and changing the text.
     *
     * @param holder  the holder
     * @param request the request
     */
    public void setDenied(ViewHolder holder, FollowRequestModel request) {
        holder.requestText.setText("@" + request.getRequesterUsername() + " was declined.");

        holder.denyButton.setVisibility(View.GONE);
        holder.acceptButton.setVisibility(View.GONE);
        holder.background.setBackgroundColor(Color.parseColor("#8b0000"));

    }

    @Override
    public int getItemCount() {
        //return nr of items in list
        return requestList.size();
    }
}
