package CalculatorCode;

import java.util.ArrayList;

import CalculatorCode.Food;
/*
 The foodCategoty class , keeps a list of foods that are related.
 */
public class FoodCategory {
    private ArrayList<Food> ListOfFoods;
    private int MAX_SIZE_OF_LIST = 4;

    public FoodCategory() {
        ListOfFoods = new ArrayList<>();

    }
    public void addFood(String name,double co2PerServing){

        if(ListOfFoods.size() < MAX_SIZE_OF_LIST){
            Food newFood = new Food(name,co2PerServing);
            ListOfFoods.add(newFood);
        }
        else{
            throw new ArrayIndexOutOfBoundsException("Array is full");
        }
    }
    public void addFoodList(String[] foodArr,String[] foodCO2eArr){

        for(int i = 0; i<MAX_SIZE_OF_LIST ; i++) {
            double co2e = Double.valueOf(foodCO2eArr[i]);
            addFood(foodArr[i],co2e);
        }
    }
    public Food getFood(int index){
        return ListOfFoods.get(index);
    }
    public int getSize(){
        return ListOfFoods.size();
    }

}
