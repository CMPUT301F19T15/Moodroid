package ca.ualberta.moodroid.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;

import ca.ualberta.moodroid.MainActivity;
import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodRepository;
import ca.ualberta.moodroid.service.MoodEventService;

public class MoodListAdapter extends RecyclerView.Adapter<MoodListAdapter.ViewHolder> {
    private ArrayList<MoodEventModel> moodList;
    MoodEventService moodEvents;
    private MoodRepository moods;
    static Context context;

    private OnListListener mOnListListener;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        public TextView emojiView;
        public TextView moodText;
        public TextView dateText;
        public TextView timeText;
        public ImageView listItemBackgroundView;
        OnListListener onListListener;


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
        }

        @Override
        public boolean onLongClick(View view) {
            onListListener.onListClick(getAdapterPosition());
            return false;
        }
    }

    public MoodListAdapter(ArrayList<MoodEventModel> moodList, MoodEventService moodEventService, OnListListener onListListener) {
        this.moodList = moodList;
        moodEvents = moodEventService;
        context = context;
        this.moods = new MoodRepository();


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
        // TODO: don't show until mood is loaded
        MoodEventModel moodObject = moodList.get(position);
        String moodStr = moodObject.getMoodName();
        this.moods.where("name", moodStr).one().addOnSuccessListener(new OnSuccessListener<ModelInterface>() {
            @Override
            public void onSuccess(ModelInterface modelInterface) {
                MoodModel mood = (MoodModel) modelInterface;

                holder.listItemBackgroundView.setBackground(new ColorDrawable(Color.parseColor(mood.getColor())));
                holder.moodText.setText(moodStr);
                holder.emojiView.setText(mood.getEmoji());
            }
        });

        //set emoji and background color
        //extract date, time from date object
        SimpleDateFormat simpleDate = new SimpleDateFormat("dd/mm/yyyy");
        SimpleDateFormat simpleTime = new SimpleDateFormat("hh:mm");
        String dateStr = simpleDate.format(moodObject.getDatetime());
        holder.dateText.setText(dateStr);
        String timeStr = simpleTime.format(moodObject.getDatetime());
        holder.timeText.setText(timeStr);
    }

    @Override
    public int getItemCount() {
        //return nr of items in list
        return moodList.size();
    }
}
