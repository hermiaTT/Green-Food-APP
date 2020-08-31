package com.byteme.greenfoodchallenge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class ResultActivity extends AppCompatActivity {

    TextView CO2ResultText;
    TextView CO2Text;
    Button nextStatButton;
    Button editDietButton;
    TextView dietPlanText;
    TextView pledgeText;
    TextView equivalentText;
    Button goHomeButton;
    Double CO2Result;
    String calculatorId;
    int randomNum = ThreadLocalRandom.current().nextInt(0, 7);
    SeekBar seekBar;
    int joinedChallenge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_result);

        // Get the saved value to determine if user has joined the challenge or not
        SharedPreferences sp = getSharedPreferences("myprefs", Activity.MODE_PRIVATE);
        joinedChallenge = sp.getInt("firstTime", 0);


        CO2ResultText = findViewById(R.id.CO2_value);
        CO2Text = findViewById(R.id.CO2_text);
        equivalentText = findViewById(R.id.equivalent_value);
        dietPlanText = findViewById(R.id.diet_plan_text);
        pledgeText = findViewById(R.id.pledge_text);
        goHomeButton = findViewById(R.id.home_button);
        nextStatButton = findViewById(R.id.new_stat_button);
        editDietButton = findViewById(R.id.edit_button);

        // Get the CO2 result from QuestionActivity
        Bundle extras = getIntent().getExtras();

        // Check if the extras actually contain anything
        if(extras ==  null){
            CO2Result = null;
        }
        else {
            nextStatButton.setVisibility(View.VISIBLE);
            calculatorId = extras.getString("calculatorId"); // get calculator object;
            CO2Result = extras.getDouble("key");


            // Show messages if CO2 is greater than 0
            if(CO2Result > 0){
                String CO2ResultTextString = String.format("%.0f", CO2Result) + getResources().getString(R.string.per_year);
                CO2ResultText.setText(CO2ResultTextString);    // Round to one decimal place

                // Use spannable string to style different parts of a textview differently
                // In this case I want to include "diet plans" as a link in my sentence.
                SpannableString ss = new SpannableString(getResources().getString(R.string.encouragement_text));
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        Intent goToDietIntent = new Intent(getApplicationContext(),SuggestedDietActivity.class);
                        goToDietIntent.putExtra("calculatorId",calculatorId);
                        startActivity(goToDietIntent);
                    }
                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setUnderlineText(false);
                    }
                };
                ss.setSpan(clickableSpan, ss.length() - "diet plans.".length(), ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                dietPlanText.setText(ss);
                dietPlanText.setMovementMethod(LinkMovementMethod.getInstance());
                dietPlanText.setLinkTextColor(getResources().getColor(R.color.greenFood));


                // If user has already joined the challenge, ask if they want to update their pledge.
                if(joinedChallenge==1){
                    SpannableString ss_pledge = new SpannableString(getResources().getString(R.string.pledge_text2));
                    ClickableSpan clickableSpan_pledge = new ClickableSpan() {
                        @Override
                        public void onClick(View textView) {
                            Intent goToPledge = new Intent(getApplicationContext(),PledgeActivity.class);
                            startActivity(goToPledge);
                        }
                        @Override
                        public void updateDrawState(TextPaint ds) {
                            super.updateDrawState(ds);
                            ds.setUnderlineText(false);
                        }
                    };
                    ss_pledge.setSpan(clickableSpan_pledge, ss_pledge.length() - "Update your pledge.".length(), ss_pledge.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    pledgeText.setText(ss_pledge);
                    pledgeText.setMovementMethod(LinkMovementMethod.getInstance());
                    pledgeText.setLinkTextColor(getResources().getColor(R.color.greenFood));
                }
                else {
                    pledgeText.setText(getResources().getString(R.string.pledge_text));
                }

                equivalentText.setText(showRandomEquivalence(CO2Result, randomNum));
            }

            else if (CO2Result == 0){
                dietPlanText.setText("");
                equivalentText.setText("");
                CO2Text.setText(getResources().getString(R.string.no_result));
                CO2ResultText.setText("");
                nextStatButton.setVisibility(View.GONE);
            }

        }

        goHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToStartIntent = new Intent(getApplicationContext(),CarbonCalculatorActivity.class);
                startActivity(goToStartIntent);
            }
        });

        editDietButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToGenderIntent = new Intent(getApplicationContext(),QuestionActivity.class);
                goToGenderIntent.putExtra("calculatorId",calculatorId);
                startActivity(goToGenderIntent);
            }
        });

        nextStatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CO2Result != null){
                    randomNum++;
                    equivalentText.setText(showRandomEquivalence(CO2Result, randomNum));
                }
            }
        });


        seekBar=(SeekBar)findViewById(R.id.seekbar);
        seekBar.setMax(0);
        seekBar.setProgress(0);
        seekBar.setMax(3000);
        final double progressValue = Double.valueOf(CO2Result);
        seekBar.setProgress((int) progressValue);
        seekBar.setEnabled(false);

    }


    @SuppressLint("DefaultLocale")
    public String showRandomEquivalence(double co2e, int statNum){

        double milesDriven = (co2e*2451)/1000;
        double gallonsGasoline = (co2e*90)/1000;
        double poundsCole = (co2e*875)/1000;
        double homeEnergyPerYear = (co2e*0.086)/1000;
        double barrelsOil = (co2e*1.9)/1000;
        double propaneCylindersUsed = (co2e*32.7)/1000;
        double acresForestPerYear = (co2e*0.952)/1000;

        ArrayList<String> stats = new ArrayList<String>();
        stats.add("This is equivalent to driving " + String.format("%.1f", milesDriven) + " miles (on average)" );
        stats.add("This is equivalent to using " + String.format("%.1f", barrelsOil) + " barrels of oil");
        stats.add("This is equivalent to using " + String.format("%.1f", gallonsGasoline) + " gallons of gasoline");
        stats.add("This is equivalent to " + String.format("%.2f", acresForestPerYear) + " acres of U.S forest per year");
        stats.add("This is equivalent to " + String.format("%.2f", homeEnergyPerYear) + " homes' energy use per year");
        stats.add("This is equivalent to burning " + String.format("%.1f", poundsCole) + " pounds of coal");
        stats.add("This is equivalent to using " + String.format("%.1f", propaneCylindersUsed) + " propane cylinders");

        return stats.get(statNum % stats.size());
    }

    @Override
    public void onBackPressed() {
        Intent goToStartIntent = new Intent(getApplicationContext(),CarbonCalculatorActivity.class);
        startActivity(goToStartIntent);
    }
}
