package com.byteme.greenfoodchallenge;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import GlideCode.GlideApp;
import pledgeCode.FirebaseMethods;

public class RestaurantDetailsActivity extends AppCompatActivity {

    ImageView foodImage;
    TextView restaurantName;
    TextView location;
    TextView mealName;
    TextView protein;
    TextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);

        foodImage = findViewById(R.id.foodImage);
        restaurantName = findViewById(R.id.restaurantName);
        location = findViewById(R.id.location);
        mealName = findViewById(R.id.mealName);
        protein = findViewById(R.id.protein);
        description = findViewById(R.id.description);



        Bundle mBundle = getIntent().getExtras();
        if (mBundle != null) {
            String imagePath = mBundle.getString("Image");
            StorageReference storageReference = FirebaseMethods.getRestaurantImage(imagePath);
            GlideApp.with(this)
                    .load(storageReference)
                    .placeholder(R.drawable.no_image)
                    .into(foodImage);

            restaurantName.setText(mBundle.getString("Restaurant"));
            location.setText(mBundle.getString("Location"));
            mealName.setText(mBundle.getString("Meal"));
            protein.setText(mBundle.getString("Protein"));
            description.setText(mBundle.getString("Description"));
        }
    }
}
