package ca.ualberta.moodroid.ui;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.ui.IconGenerator;

import java.util.List;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;

import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;
import ca.ualberta.moodroid.service.AuthenticationService;
import ca.ualberta.moodroid.service.MoodEventService;

public class MarkerIconGenerator extends Map {

    @Override
    protected void startMyMap(boolean isRestore){
        if (!isRestore) {                                               // set to current location
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(53.631611,-113.323975), 10));
        }

        final IconGenerator iconFactory = new IconGenerator(this);

        MoodEventRepository moodEvents = new MoodEventRepository();
        // get all model interfaces (moodeventModel) then change it to a moodeventModel and get the location
        moodEvents.where("username", "bryce").get().addOnSuccessListener(new OnSuccessListener<List<ModelInterface>>() {
            @Override
            public void onSuccess(List<ModelInterface> modelInterfaces) {
                for(ModelInterface m: modelInterfaces) {
                    MoodEventModel event = (MoodEventModel) m;
                    Log.d("MARKER", "NEW EVENT LOCATION: "+event.getLocation());

                    addIcon(iconFactory, "Bryce", new LatLng(event.getLocation().getLatitude(),event.getLocation().getLongitude()));

                }
            }
        });

    }

    private void addIcon(IconGenerator iconFactory, CharSequence text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        getMap().addMarker(markerOptions);
    }

    private CharSequence makeCharSequence() {
        String prefix = "Mixing ";
        String suffix = "different fonts";
        String sequence = prefix + suffix;
        SpannableStringBuilder ssb = new SpannableStringBuilder(sequence);
        ssb.setSpan(new StyleSpan(ITALIC), 0, prefix.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.setSpan(new StyleSpan(BOLD), prefix.length(), sequence.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

}
