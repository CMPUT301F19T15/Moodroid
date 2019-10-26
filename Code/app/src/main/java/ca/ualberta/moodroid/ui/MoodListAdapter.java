package ca.ualberta.moodroid.ui;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ca.ualberta.moodroid.R;

public class MoodListAdapter extends RecyclerView.Adapter<MoodListAdapter.ViewHolder>{
    private ArrayList<DELEEEEEEEETEtest> moodList;


    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView moodText;
        public TextView dateText;
        public TextView timeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //get references to items in list
            imageView = itemView.findViewById(R.id.mood_list_view_image);
            moodText = itemView.findViewById(R.id.mood_list_view_moodText);
            dateText = itemView.findViewById(R.id.mood_list_view_dateText);
            timeText = itemView.findViewById(R.id.mood_list_view_timeText);

        }
    }

    public MoodListAdapter(ArrayList<DELEEEEEEEETEtest> mockLIIIST){
        moodList = mockLIIIST;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //pass content view in
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mood_history_content, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    //assign values to items in list
        DELEEEEEEEETEtest moodObject = moodList.get(position);

        holder.imageView.setImageResource(moodObject.getImageRes());  ////change to correct mood method to get image
        holder.moodText.setText(moodObject.getMoodstr());  ///change
        holder.dateText.setText(moodObject.getDatestr());  ///change
        holder.timeText.setText(moodObject.getTimestr());  ///change
    }

    @Override
    public int getItemCount() {
        //return nr of items in list
        return moodList.size();
    }
}
