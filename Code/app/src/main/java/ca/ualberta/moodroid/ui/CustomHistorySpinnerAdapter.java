package ca.ualberta.moodroid.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ca.ualberta.moodroid.R;

/**
 * This class implements a custom list for our moods to be presentable to the user, it
 * uses an array adapter type function to make the spinner match our applications UI
 */
public class CustomHistorySpinnerAdapter extends ArrayAdapter {

    /**
     * The Emojis.
     */
    String[] emojis;
    /**
     * The Mood names.
     */
    String[] moodNames;
    /**
     * The Context.
     */
    Context context;

    /**
     * Instantiates a new Custom history spinner adapter.
     *
     * @param context   the context
     * @param emojis    the emojis
     * @param moodNames the mood names
     */
    public CustomHistorySpinnerAdapter(@NonNull Context context, String[] emojis, String[] moodNames) {
        super(context, R.layout.history_filter_spinner_row);
        this.emojis = emojis;
        this.moodNames = moodNames;
        this.context = context;

    }

    /**
     * This returns the number of items in the list.
     * @return the number of items in the list as an integer
     */
    @Override
    public int getCount() {
        return moodNames.length;
    }


    /**
     * This is where the views we added to the custom layout are initialized.
     * @param position
     * @param convertView
     * @param parent
     * @return a View object
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.history_filter_spinner_row, parent, false);
            viewHolder.moodText = (TextView) convertView.findViewById(R.id.spinner_text);
            viewHolder.emojiText = (TextView) convertView.findViewById(R.id.spinner_emoji);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.emojiText.setText(emojis[position]);
        viewHolder.moodText.setText(moodNames[position]);
        return convertView;
    }


    /**
     * This will show the dropdown list when the user taps on the spinner.
     * @param position
     * @param convertView
     * @param parent
     * @return a View object
     */
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }


    /**
     * The type View holder.
     */
    public static class ViewHolder{
        /**
         * The Mood text.
         */
        TextView moodText;
        /**
         * The Emoji text.
         */
        TextView emojiText;

    }













}
