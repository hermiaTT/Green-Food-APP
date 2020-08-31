package com.byteme.greenfoodchallenge;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import CalculatorCode.Calculator;


public class SuggestedDietActivity extends AppCompatActivity {

    int meatId = 1;
    int vegId = 2;
    int balanceId = 3;
    Double CO2Result;
    Calculator calculator;
    TextView CO2_save_top_left;
    TextView CO2_save_top_right;
    TextView CO2_save_bottom_left;
    Double CO2_meat = 787.0;
    Double CO2_veggies = 299.0;
    Double CO2_balanced = 563.0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_diet);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        CO2_save_top_left = (TextView)findViewById(R.id.diet_text_left_top);
        CO2_save_top_right = (TextView)findViewById(R.id.diet_text_right_top);
        CO2_save_bottom_left = (TextView)findViewById(R.id.diet_text_left_bottom);

        ImageButton topLeftImageButton = (ImageButton)findViewById(R.id.left_image_button_top);
        final SharedPreferences mPrefs = getSharedPreferences("CalculatorData",MODE_PRIVATE);
        Bundle extras = getIntent().getExtras();


        //Strings used for concatenating to create diet difference sentence
        String string1 = getResources().getString(R.string.co2e_dif_start);
        String string3 = getResources().getString(R.string.kg);
        String string4 = getResources().getString(R.string.co2e_dif_end);

        // Check if the extras actually contain anything
        if(extras ==  null){
            // no calculator saved display default values??
            CO2_save_top_left.setText("");
            CO2_save_top_right.setText("");
            CO2_save_bottom_left.setText("");
        } else {
            String calculatorIdKey = extras.getString("calculatorId");
            Gson gson = new Gson();
            String json = mPrefs.getString(calculatorIdKey, "");
            calculator = gson.fromJson(json, Calculator.class); //get and return saved calculator
            CO2Result = calculator.getTotalCO2Emissions();


            if(CO2Result > CO2_meat){
                String stringMeat = (String.format("%.1f", CO2Result - CO2_meat));
                String combinedMeat = string1 + " " + stringMeat + string3 + " " + string4;

                CO2_save_top_left.setText(combinedMeat);
            }

            if(CO2Result > CO2_veggies){
                String stringVeg = (String.format("%.1f", CO2Result - CO2_veggies));
                String combinedVeg = string1 + " " + stringVeg + string3 + " " + string4;

                CO2_save_top_right.setText(combinedVeg);
            }

            if(CO2Result > CO2_balanced){
                String stringBal = (String.format("%.1f", CO2Result - CO2_balanced));
                String combinedBal = string1 + " " + stringBal + string3 + " " + string4;

                CO2_save_bottom_left.setText(combinedBal);
            }

            if(CO2Result <= CO2_meat){
                CO2_save_top_left.setText(R.string.better_diet);
            }

            if(CO2Result <= CO2_veggies){
                CO2_save_top_right.setText(R.string.better_diet);
            }

            if(CO2Result <= CO2_balanced){
                CO2_save_bottom_left.setText(R.string.better_diet);
            }
        }

        topLeftImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PopUpDialog(R.layout.activity_meat_pop_up);
                popUpDiet(meatId);
            }
        });

        ImageButton topRightImageButton = (ImageButton)findViewById(R.id.right_image_button_top);
        topRightImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PopUpDialog(R.layout.activity_veg_pop_up);
                popUpDiet(vegId);
            }
        });

        ImageButton bottomLeftImageButton = (ImageButton)findViewById(R.id.left_image_button_bottom);
        bottomLeftImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PopUpDialog(R.layout.activity_balance_pop_up);
                popUpDiet(balanceId);
            }
        });

    }

    public void PopUpDialog(int v) {
        final Dialog MyDialog = new Dialog(SuggestedDietActivity.this);
        MyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        MyDialog.setContentView(v);

        MyDialog.show();
    }

    // Method to pop up diey image
    public void popUpDiet(int id) {

        final AlertDialog popUpDialog = new AlertDialog.Builder(this).create();

        LayoutInflater pop = LayoutInflater.from(this);
        final View popView;
        if (id == 1) {
            popView = pop.inflate(R.layout.activity_meat_pop_up, null);
        } else if (id == 2) {
            popView = pop.inflate(R.layout.activity_veg_pop_up, null);
        } else {
            popView = pop.inflate(R.layout.activity_balance_pop_up, null);
        }
        popUpDialog.setView(popView);


        final Button cancel =  popView.findViewById(R.id.button_close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpDialog.dismiss();
            }
        });
        popUpDialog.show();
    }
}


