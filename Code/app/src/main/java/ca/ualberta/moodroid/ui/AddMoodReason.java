package ca.ualberta.moodroid.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.net.UrlEscapers;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import ca.ualberta.moodroid.R;
import ca.ualberta.moodroid.model.ModelInterface;
import ca.ualberta.moodroid.model.MoodEventModel;
import ca.ualberta.moodroid.model.MoodModel;
import ca.ualberta.moodroid.repository.MoodEventRepository;

import static android.view.View.GONE;

public class AddMoodReason extends AppCompatActivity {

////////in MAIN: delete the onclickListener and the photo button.......only put in to test this
    //////delete the layout file activity_add_photo_____temp and use correct one for add mood activity
    /////put some of this in service layer???
    /////change firebase storage permission ...public for now
    ////save link to image in mood event
    /////delete toolbar button and intent in MoodHistory activity (right one)


    private Button takePhotoButton;
    private Button savePhotoButton;
    private Button addLibraryPhotoButton;
    private ImageView photoView;
    private String photoFileName;
    private Uri filePath;
    TextView toolBarTextView;
    private final int PICK_IMAGE_FROM_LIBRARY_REQUEST = 1;
    private final int TAKE_PHOTO_REQUEST = 2;
    FirebaseStorage storage;
    StorageReference storageReference;  //points to uploaded file
    StorageReference imageRef;
    private String photoKey;
    private MoodEventModel myMood;
    String url;
    ImageView tempPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_reason);

        takePhotoButton = findViewById(R.id.take_photo_button);
        savePhotoButton = findViewById(R.id.save_photo_button);
        savePhotoButton.setVisibility(View.INVISIBLE);
        addLibraryPhotoButton = findViewById(R.id.add_photo_from_library_button);
        photoView = findViewById(R.id.show_photo_view);
        toolBarTextView = findViewById(R.id.toolbar_text_center);
        toolBarTextView.setText("Add Photo");
        String internalId;
        tempPhoto = findViewById(R.id.pic_show);

        internalId = "yeqnwNGPbU8kan3O1peZ";

        addLibraryPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
                //make save photo button visible once user has chosen photo to upload
                savePhotoButton.setVisibility(View.VISIBLE);
            }
        });

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
                //make save photo button visible once user has chosen photo to upload
                savePhotoButton.setVisibility(View.VISIBLE);
            }
        });


        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        savePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
                //TO DO: save url in firebase
            }
        });
    }

    private void choosePhoto(){
        //create intent to start image choose dialog that allows user to browse through photo library
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "choose photo"), PICK_IMAGE_FROM_LIBRARY_REQUEST);
    }

    private void takePhoto(){
        //take photo with phone's camera
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, TAKE_PHOTO_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //handle return intent
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == PICK_IMAGE_FROM_LIBRARY_REQUEST || requestCode == TAKE_PHOTO_REQUEST) && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            filePath = data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                photoView.setImageBitmap(bitmap);

            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    private void uploadPhoto(){
        if(filePath != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            //create random name and create folder automatically when image is uploaded
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddMoodReason.this, "Image saved.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddMoodReason.this, "Failed to save image. "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
            //get URL
            url =  ref.getDownloadUrl().toString();
        }
    }



//    private File createImageFile() throws IOException {
//        // Creates a unique image file name using a time stamp
//
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//        // Save a file: path for use with ACTION_VIEW intents
//        photoFileName = image.getAbsolutePath();
//        return image;
//    }


//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//            // Create the File where the photo should go
//            File photoFile = null;
//            try {
//                photoFile = createImageFile();
//            } catch (IOException ex) {
//                // Error occurred while creating the File
//
//            }
//            // Continue only if the File was successfully created
//            if (photoFile != null) {
//                Uri photoURI = FileProvider.getUriForFile(this,
//                        "com.example.android.fileprovider",
//                        photoFile);
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
//            }
//        }
//    }
}