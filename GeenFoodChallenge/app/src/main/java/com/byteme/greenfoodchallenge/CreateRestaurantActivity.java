package com.byteme.greenfoodchallenge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import pledgeCode.FirebaseMethods;

public class CreateRestaurantActivity extends AppCompatActivity {

    private Button buttonUpload;
    private ImageView takePhoto;
    private ImageView capturedPicture;
    private String municipalitySpinnerValue;
    private String proteinSpinnerValue;
    private ProgressDialog progressBar;
    private EditText restaurantNameTextField;
    private  EditText mealNameTextField;
    private EditText descriptionTextField;

    //camera
    private String mCurrentPhotoPath;
    private Uri filePath;
    static final int REQUEST_TAKE_PHOTO = 1;
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    private String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if(savedInstanceState != null)
        {
            restoreUserValues(savedInstanceState);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_restaurant);
        //initialize firebase
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        userId = FirebaseMethods.getFireBaseCurrentUser().getUid();

        //initialize buttons and text fields
        takePhoto = findViewById(R.id.takePhoto);
        capturedPicture = findViewById(R.id.capturedPicture);
        restaurantNameTextField = findViewById(R.id.restaurantName);
        mealNameTextField = findViewById(R.id.mealName);
        descriptionTextField = findViewById(R.id.mealDescription);
        buttonUpload = findViewById(R.id.upload);

        // Municipality Dropdown
        final Spinner municipalitySpinner = findViewById(R.id.municipality_spinner);
        municipalitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                municipalitySpinnerValue = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.municipality_array, R.layout.municipality_spinner);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        municipalitySpinner.setAdapter(adapter);

        // protein dropdown
        final Spinner proteinSpinner =  findViewById(R.id.protein_spinner);

        proteinSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                proteinSpinnerValue = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        final ArrayAdapter<CharSequence> proteinAdapter = ArrayAdapter.createFromResource(this,
                R.array.protein_array, R.layout.municipality_spinner);
        proteinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        proteinSpinner.setAdapter(proteinAdapter);

        //set listeners
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // prepare for a progress bar dialog
                progressBar = new ProgressDialog(v.getContext());
                progressBar.setCancelable(true);
                progressBar.setMessage("In Progress ...");
                progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressBar.setProgress(0);
                progressBar.setMax(100);
                String restaurantName = restaurantNameTextField.getText().toString();
                String mealName = mealNameTextField.getText().toString();
                String description = descriptionTextField.getText().toString();

                String location = municipalitySpinnerValue;
                String protein = proteinSpinnerValue;
                long unixTime = System.currentTimeMillis();
                String fireBaseImage = "No Image";

                if(restaurantName .equalsIgnoreCase("")){
                    Toast.makeText(CreateRestaurantActivity.this, "Restaurant Name is required!", Toast.LENGTH_SHORT).show();
                    restaurantNameTextField.setError( "Restaurant Name is required!" );
                }
                else if(mealName.equalsIgnoreCase("")){
                    Toast.makeText(CreateRestaurantActivity.this, "Name of Dish is required!", Toast.LENGTH_SHORT).show();
                    mealNameTextField.setError("Name of Dish is required!");
                }
                else{
                    if(mCurrentPhotoPath != null)
                    {
                        fireBaseImage = uploadImage(userId);
                        progressBar.show();
                    }
                    else{
                        Toast.makeText(CreateRestaurantActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                        // After submitting, go back to the Restaurant page
                        Restaurant currentRestaurant = new Restaurant(restaurantName,mealName,description,location,protein,fireBaseImage,unixTime);
                        FirebaseMethods.saveRestaurant(currentRestaurant);
                        Intent goBack = new Intent(getApplicationContext(), CarbonCalculatorActivity.class);
                        goBack.putExtra("fragmentToLoad", 2); // Restaurant Fragment value is 2
                        startActivity(goBack);
                    }
                }

            }
        });
    }

    // save the image's path, because if user takes a long time in the camera app, this activity maybe destroyed.
    @Override
    public void onSaveInstanceState (Bundle outState) {
        outState.putString("ImagePath",mCurrentPhotoPath);
        super.onSaveInstanceState(outState);
    }
    //restore values due to destroying the activity
    private void restoreUserValues (Bundle savedInstanceState){
        mCurrentPhotoPath = savedInstanceState.getString("ImagePath");

    }

    // code taken from https://developer.android.com/training/camera/photobasics#java
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.byteme.greenfoodchallenge.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    //after camera session ends this runs
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            setPic();
        }
    }

    //create a file to put the image
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //set picture
    private void setPic() {
        // Get the dimensions of the View
        int targetW = (int) getResources().getDimension(R.dimen.photo_previewSize);
        int targetH = (int) getResources().getDimension(R.dimen.photo_previewSize);
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        Log.d("PATHPHOTO",mCurrentPhotoPath);
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        capturedPicture.setImageBitmap(bitmap);
    }

    //firebase uploading image and return the path
    private String uploadImage(String userId) {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // File or Blob
        Uri file = Uri.fromFile(new File(mCurrentPhotoPath));
        //get the file name from the path
        String filename = file.getLastPathSegment();

        // Create a reference to 'images/mountains.jpg'
        String referencePath = "images/" +userId+"/" + filename;
        StorageReference mountainImagesRef = storageRef.child(referencePath);

        // Create the file metadata
        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        // Upload file and metadata to the path 'images/mountains.jpg'
        UploadTask uploadTask = storageRef.child(referencePath).putFile(file, metadata);
        // Listen for state changes, errors, and completion of the upload.
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                //System.out.println("Upload is paused");
                Toast.makeText(CreateRestaurantActivity.this, "Upload Paused", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(CreateRestaurantActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                // ...
                Toast.makeText(CreateRestaurantActivity.this, "Upload Successful", Toast.LENGTH_SHORT).show();
                Intent goBack = new Intent(getApplicationContext(), CarbonCalculatorActivity.class);
                goBack.putExtra("fragmentToLoad", 2); // Restaurant Fragment value is 2
                startActivity(goBack);
            }
        });
        return referencePath;
    }


}
