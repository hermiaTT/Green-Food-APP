package com.byteme.greenfoodchallenge;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

import CalculatorCode.Calculator;
import CalculatorCode.Food;
import CalculatorCode.FoodCategory;


public class QuestionActivity extends AppCompatActivity {

    private FoodCategory currentFoodCategory;
    int currentCategory = 0;
    Calculator calculator;
    ArrayList<NumberPicker> listOfNumberPickers;
    String calculatorIdKey = ""; //empty string
    String TIMES_PER_WEEK_STATE_KEY = "TimesPerWeekKey";
    String GENDER_STATE_KEY = "GenderKey";
    String CURRENT_CATEGORY_KEY = "CurrentCategoryKey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        final SharedPreferences mPrefs = getSharedPreferences("CalculatorData",MODE_PRIVATE);
        final TextView progressText = findViewById(R.id.progress);

        Bundle extras = getIntent().getExtras();
        // check if there is anything
        if(extras !=  null){
            calculatorIdKey = extras.getString("calculatorId",""); // if id doesn't exist use empty string
        }

        if (savedInstanceState != null) {
            //Initialize with saved data
            restoreSavedCalculatorState(savedInstanceState);
        }
        else if(mPrefs.contains(calculatorIdKey)){
            calculator = getSavedCalculatorObject(mPrefs);
        }
        // no saved instance, create a new calculator
        else {
            ArrayList<FoodCategory> foodBank = initializeFoodCategories();
            calculator = new Calculator(foodBank);

        }

        //test
        //Initialize
        // calculator = new Calculator(this, getIntent().getStringExtra("gender"));
        initializeNumberPickerArray();
        initializeText();
        progressText.setText((currentCategory+1) + "/" + calculator.getArrayLength());


        final Button nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {


                if (currentCategory < calculator.getArrayLength()) {
                    //save user input in Food object
                    saveInputs();
                    //Repopulate with next set of Foods
                    currentCategory++;
                    initializeText();

                    if (currentCategory + 1 != 6) {
                        progressText.setText((currentCategory + 1) + "/" + calculator.getArrayLength());
                    }
                }

                if (currentCategory == calculator.getArrayLength()) {
                    currentCategory--;  //(temporary fix)  there is a bug where if user presses android back button from the result page, they need to tap previous twice.
                    saveCalculatorObject(mPrefs);
                    double CO2Result = calculator.getTotalCO2Emissions();
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("key", CO2Result); // Pass the CO2 to ResultActivity
                    intent.putExtra("calculatorId",calculatorIdKey); // pass the calculator
                    startActivity(intent);
                }

            }
        });

        Button prevButton = findViewById(R.id.previous);
        prevButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (currentCategory > 0) {
                    //save user input in Food object
                    saveInputs();
                    //Repopulate with previous set of Foods
                    currentCategory--;
                    initializeText();
                    progressText.setText((currentCategory+1) + "/" + calculator.getArrayLength());
                }
            }
        });
    }

    private void saveInputs() {
        int currentNumberPicker = 0;
        NumberPicker currentItem;
        Food foodItem;
        currentFoodCategory = calculator.getFoodCategory(currentCategory);

        int maxSize = currentFoodCategory.getSize();
        //repopulate
        for (int i = 0; i < maxSize; i++, currentNumberPicker++) {
            currentItem = listOfNumberPickers.get(currentNumberPicker);
            foodItem = currentFoodCategory.getFood(i);
            int timesPerWeek = currentItem.getValue();
            foodItem.setTimesPerWeek(timesPerWeek);
        }
    }

    private void initializeNumberPickerArray() {

        listOfNumberPickers = new ArrayList<>();
        NumberPicker item0 = findViewById(R.id.item0);
        NumberPicker item1 = findViewById(R.id.item1);
        NumberPicker item2 = findViewById(R.id.item2);
        NumberPicker item3 = findViewById(R.id.item3);
        listOfNumberPickers.add(item0);
        listOfNumberPickers.add(item1);
        listOfNumberPickers.add(item2);
        listOfNumberPickers.add(item3);
    }

    private void initializeText() {
        int currentNumberPicker = 0;
        NumberPicker currentItem;
        Food foodItem;

        currentFoodCategory = calculator.getFoodCategory(currentCategory);

        int maxSize = currentFoodCategory.getSize();
        //repopulate
        for (int i = 0; i < maxSize; i++, currentNumberPicker++) {
            currentItem = listOfNumberPickers.get(currentNumberPicker);
            foodItem = currentFoodCategory.getFood(i);
            String foodName = foodItem.getNameOfFood();
            currentItem.setFoodName(foodName);
            int timesPerWeek = foodItem.getTimesPerWeek();
            currentItem.setValue(timesPerWeek);
        }
    }

    // initialize all food categories using strings from string.xml
    private ArrayList<FoodCategory> initializeFoodCategories() {
        ArrayList<FoodCategory> foodBank = new ArrayList<>();
        int defaultValue = 0;
        Resources res = getResources();
        TypedArray foodNameTypedArray = res.obtainTypedArray(R.array.FoodNames);
        TypedArray foodCO2eTypedArray = res.obtainTypedArray(R.array.FoodCO2e);
        int nameArrLength = foodNameTypedArray.length();
        //int cO2eArrLength = foodCO2eTypedArray.length();
        String[] foodNameArr;
        String[] foodCO2eArr;

        for (int i = 0; i < nameArrLength; i++) {
            int foodNameId = foodNameTypedArray.getResourceId(i, defaultValue);
            int foodCO2eId = foodCO2eTypedArray.getResourceId(i, defaultValue);

            if (foodNameId > defaultValue && foodCO2eId > defaultValue) {
                foodNameArr = res.getStringArray(foodNameId);
                foodCO2eArr = res.getStringArray(foodCO2eId);

                FoodCategory foodCategory = new FoodCategory();
                foodCategory.addFoodList(foodNameArr, foodCO2eArr);
                foodBank.add(foodCategory);
            } else {
                // something wrong with the XML
            }

        }

        foodNameTypedArray.recycle();
        foodCO2eTypedArray.recycle();
        return foodBank;
    }

    private void saveCalculatorObject(SharedPreferences mPrefs){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(calculator);
        //uses the date as a key
        if(calculatorIdKey.isEmpty()){
            Date date = new Date();
            calculatorIdKey = String.valueOf(date);
        }
        prefsEditor .putString(calculatorIdKey, json);
        prefsEditor.commit();
    }

    private Calculator getSavedCalculatorObject(SharedPreferences mPrefs){

        Gson gson = new Gson();
        String json = mPrefs.getString(calculatorIdKey, "");
        return gson.fromJson(json, Calculator.class); //get and return saved calculator
    }
    // From : https://developer.android.com/guide/components/activities/activity-lifecycle


    // invoked when the activity may be temporarily destroyed, save the instance state here
    @Override
    public void onSaveInstanceState (Bundle outState){
        saveInputs(); // save the inputs in the text box
        // save gender

        //save Food object data , cycle through Food Bank in calculator class.
        int timesPerWeek;
        ArrayList<Integer> foodTimesPerWeekArray = new ArrayList<>(); // should I use one big array or each category has their own array.
        for (int i = 0; i < calculator.getArrayLength(); i++) {
            FoodCategory foodCategory = calculator.getFoodCategory(i);
            for (int j = 0; j < foodCategory.getSize(); j++) {
                timesPerWeek = foodCategory.getFood(j).getTimesPerWeek(); // get the times per week for each food.
                foodTimesPerWeekArray.add(timesPerWeek);
            }
        }
        outState.putIntegerArrayList(TIMES_PER_WEEK_STATE_KEY, foodTimesPerWeekArray); // save times per week values
        outState.putInt(CURRENT_CATEGORY_KEY, currentCategory); // save category number
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    //this method restores the calculator and its values
    private void restoreSavedCalculatorState (Bundle savedInstanceState){

        //get previously saved items
        //get the Category that user stopped at.
        currentCategory = savedInstanceState.getInt(CURRENT_CATEGORY_KEY);
        // get gender
        String gender = savedInstanceState.getString(GENDER_STATE_KEY);
        ArrayList<FoodCategory> foodBank = initializeFoodCategories();
        calculator = new Calculator(foodBank); // create a new calculator object
        //get saved times per week
        ArrayList<Integer> foodTimesPerWeekArray = savedInstanceState.getIntegerArrayList(TIMES_PER_WEEK_STATE_KEY);
        // set all times per week .
        int timesPerWeekIterator = 0;

        if (!foodTimesPerWeekArray.isEmpty()) {
            for (int i = 0; i < calculator.getArrayLength(); i++) {
                FoodCategory foodCategory = calculator.getFoodCategory(i);
                for (int j = 0; j < foodCategory.getSize(); j++, timesPerWeekIterator++) {

                    int foodTimesPerWeek = foodTimesPerWeekArray.get(timesPerWeekIterator);
                    Food food = foodCategory.getFood(j);
                    food.setTimesPerWeek(foodTimesPerWeek);
                }
            }
        }
    }

}


