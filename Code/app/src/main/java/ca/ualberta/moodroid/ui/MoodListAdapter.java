package ca.ualberta.moodroid.ui;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.service.MoodEventService;

public class MoodListAdapter extends RecyclerView.Adapter<MoodListAdapter.ViewHolder>{
    private ArrayList<MoodEventModel> moodList;
    MoodEventService moodEvents;

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView moodText;
        public TextView dateText;
        public TextView timeText;
        public ImageView listItemBackgroundView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //get references to items in list
            imageView = itemView.findViewById(R.id.mood_list_view_image);
            moodText = itemView.findViewById(R.id.mood_list_view_moodText);
            dateText = itemView.findViewById(R.id.mood_list_view_dateText);
            timeText = itemView.findViewById(R.id.mood_list_view_timeText);
            listItemBackgroundView = itemView.findViewById(R.id.list_item_background);
        }
    }

    public MoodListAdapter(ArrayList<MoodEventModel> moodList, MoodEventService moodEventService){
        this.moodList = moodList;
        moodEvents = moodEventService;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //pass content view in
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mood_history_content, parent, false);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ///////////////////////////////////////////////
////////////////////////////////go to fragment....delete from db using moodEventService.deleteEvent
                ////////////////////////////////////////////////////
                return false;
            }
        });

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    //assign values and display items in list
        MoodEventModel moodObject = moodList.get(position);
        String moodStr = moodObject.getMoodName();
        //set emoji and background color
        if(moodStr.equals("Angry")){
            // holder.imageView.setImageResource(moodModel.getEmoji());  //change image to correct emoji for mood
            //holder.listItemBackgroundView.setBackground(moodModel.getColor());  //change color to correct color for mood
        } else if(moodStr.equals("Happy")){
            // holder.imageView.setImageResource(moodModel.getEmoji());  //get image from model....
            //holder.listItemBackgroundView.setBackground(moodModel.getColor());
        } else if(moodStr.equals("Sad")){
            // holder.imageView.setImageResource(moodModel.getEmoji());  //get image from model....
            //holder.listItemBackgroundView.setBackground(moodModel.getColor());
        } else if(moodStr.equals("Annoyed")){
            // holder.imageView.setImageResource(moodModel.getEmoji());  //get image from model....
            //holder.listItemBackgroundView.setBackground(moodModel.getColor());
        } else if(moodStr.equals("Sick")){
            // holder.imageView.setImageResource(moodModel.getEmoji());  //get image from model....
            //holder.listItemBackgroundView.setBackground(moodModel.getColor());
        }
        holder.moodText.setText(moodStr);
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
