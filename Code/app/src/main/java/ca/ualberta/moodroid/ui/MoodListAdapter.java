package ca.ualberta.moodroid.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ca.ualberta.moodroid.MainActivity;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodRepository;
import ca.ualberta.moodroid.service.MoodEventService;

/**
 * This is a custom adapter meant for displaying moods according to the assignment spec. It takes
 * the mood data the user creates and displays it with its colour, emoji, and title.
 */
public class MoodListAdapter extends RecyclerView.Adapter<MoodListAdapter.ViewHolder> {
    private ArrayList<MoodEventModel> moodList;
    /**
     * The mood events and their unique data for display.
     */
    MoodEventService moodEvents;
    private List<MoodModel> moods;
    private Boolean showUsername;
    /**
     * The Context.
     */
    static Context context;

    private OnListListener mOnListListener;

    /**
     * The type View holder.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

        /**
         * The field where the moods emoji will be displayed.
         */
        public TextView emojiView;
        /**
         * The field where the the moods title will be displayed.
         */
        public TextView moodText;
        /**
         * The field where the mood date will be displayed.
         */
        public TextView dateText;
        /**
         * Same as above but for time.
         */
        public TextView timeText;
        /**
         * The List item background view.
         */
        public ImageView listItemBackgroundView;
        /**
         * Same as an onClickListener but for items in a custom array display.
         */
        OnListListener onListListener;


        /**
         * Instantiates a new View holder.
         *
         * @param itemView       the item view
         * @param onListListener the on list listener
         */
        public ViewHolder(@NonNull View itemView, OnListListener onListListener) {
            super(itemView);
            //get references to items in list
            emojiView = itemView.findViewById(R.id.mood_list_view_emoji);
            moodText = itemView.findViewById(R.id.mood_list_view_moodText);
            dateText = itemView.findViewById(R.id.mood_list_view_dateText);
            timeText = itemView.findViewById(R.id.mood_list_view_timeText);
            listItemBackgroundView = itemView.findViewById(R.id.list_item_background);

            this.onListListener = onListListener;
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            onListListener.onListClick(getAdapterPosition());
            return false;
        }

        @Override
        public void onClick(View view) {
            onListListener.onShortClick(getAdapterPosition());
        }
    }

    /**
     * Instantiates a new Mood list adapter.
     *
     * @param moodList       the mood list
     * @param moods          the moods
     * @param showUsername   the show username
     * @param onListListener the on list listener
     */
    public MoodListAdapter(ArrayList<MoodEventModel> moodList, List<MoodModel> moods, Boolean showUsername, OnListListener onListListener) {
        this.moodList = moodList;
        this.showUsername = showUsername;
        context = context;
        this.moods = moods;


        this.mOnListListener = onListListener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //pass content view in
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mood_history_content, parent, false);
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
        void onShortClick(int position);
    }

    /**
     * Open edit delete dialog.
     */
    public void openEditDeleteDialog() {

        EditDeleteFragment editDeleteFragment = new EditDeleteFragment();
        editDeleteFragment.show(((MoodHistory) context).getSupportFragmentManager(), "Options");
    }

    //
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //assign values and display items in list
        // TODO: don't show until mood is loaded
        MoodEventModel moodObject = moodList.get(position);
        String moodStr = moodObject.getMoodName();

        if (this.showUsername) {
            holder.moodText.setText("@" + moodObject.getUsername() + "\n" + moodStr);

        } else {
            holder.moodText.setText(moodStr);
        }

        Log.d("MOODLIST/MOOD", "Mood Name: " + moodStr + " Mood Position: " + position);
        MoodModel mood = new MoodModel();

        for (MoodModel m : moods) {
            if (m.getName().equals(moodStr)) {
                mood = m;
            }
        }

        Log.d("MOODLIST/MOOD", mood.getName() + mood.getColor());
        holder.listItemBackgroundView.setBackground(new ColorDrawable(Color.parseColor(mood.getColor())));
        holder.emojiView.setText(mood.getEmoji());


        //set emoji and background color
        //extract date, time from date object
        String dateStr = moodObject.getDatetime().split(" ")[0];
        holder.dateText.setText(dateStr);
        String timeStr = moodObject.getDatetime().split(" ")[1];
        holder.timeText.setText(timeStr);

    }

    @Override
    public int getItemCount() {
        //return nr of items in list
        return moodList.size();
    }
}
