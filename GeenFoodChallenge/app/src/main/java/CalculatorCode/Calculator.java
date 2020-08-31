package CalculatorCode;

import android.content.Context;

import java.util.ArrayList;
/*
 The calculator class has a food bank that stores all the food categories , it takes data from the food bank and calculates the total co2 emissions
 */
public class Calculator {

    private ArrayList<FoodCategory> foodBank;
    private int arrLength;
    private int VANCOUVER_POPULATION = 631486; // 2016 population from Wikipedia
    private int NUM_WEEKS_IN_YEAR = 52;
    private FoodCategory foodlist;
    private double totalCO2Emissions;

    public Calculator(ArrayList<FoodCategory> foodBank){
        this.foodBank = foodBank;
        setArrayLength(foodBank.size());

    }

    public int getArrayLength(){
        return this.arrLength;
    }

    public void setArrayLength(int arrLength){
        this.arrLength = arrLength;
    }


    // Calculate the total CO2e from the 5 FoodCategory ArrayLists
    public double getTotalCO2Emissions() {
        totalCO2Emissions = 0;
        Food foodItem;

        // Loop through the ArrayList with the 5 Food Categories
        // For each Food Category, loop through the food items and get the CO2e and timesPerWeek
        // Add up the total CO2 emissions for every food item
        // Returns the total CO2 emissions per year
        for(int foodCatIndex=0; foodCatIndex<foodBank.size(); foodCatIndex++){
            foodlist = foodBank.get(foodCatIndex);
            for(int foodNameIndex=0; foodNameIndex<foodlist.getSize(); foodNameIndex++){
                foodItem = foodlist.getFood(foodNameIndex);
                totalCO2Emissions += foodItem.getCo2PerServing() * foodItem.getTimesPerWeek();
            }
        }
        return totalCO2Emissions * NUM_WEEKS_IN_YEAR;
    }

    // Assuming everyone's CO2e is the same, estimate the total annual CO2e for all of Vancouver
    public double getTotalCO2Vancouver(){
        return getTotalCO2Emissions() * VANCOUVER_POPULATION;
    }


    public void setTotalCO2Emissions(int totalCO2Emissions) {
        this.totalCO2Emissions = totalCO2Emissions;
    }


    //get food category from a list
    public FoodCategory getFoodCategory(int index) {
        if(index < arrLength)
            return foodBank.get(index);
        else
            return foodBank.get(arrLength-1);
    }


    //Method to calculate c02 emissions

    //Method to display the result so the user can understand how much co2 they are usuing
}
