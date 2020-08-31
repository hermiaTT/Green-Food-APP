package CalculatorCode;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class FoodCategoryTest {

    @Test
    public void addFoodList() {
        FoodCategory foodCategory = new FoodCategory();
        ArrayList<String> foodNames = new ArrayList<>();
        ArrayList<String> co2Values = new ArrayList<>();
        foodNames.add("Peanut Butter");
        foodNames.add("Beef");
        foodNames.add("Tofu");
        co2Values.add("2.5");
        co2Values.add("1.0");
        co2Values.add("2.5");
        for(int i = 0 ; i < foodNames.size(); i++){
            Double co2 = Double.valueOf(co2Values.get(i));
            System.out.println(co2);
            Food food = new Food(foodNames.get(i),co2);
            foodCategory.addFood(foodNames.get(i),co2);

            //compare food with food from food category
            Food  foodFromCategory = foodCategory.getFood(i);
            assertEquals(food.getNameOfFood(),foodFromCategory.getNameOfFood());

            //double to double comparison is deprecated
            String co2String = Double.toString(food.getCo2PerServing());
            String actualCo2String = Double.toString(foodFromCategory.getCo2PerServing());
            assertEquals(co2String,actualCo2String);

            assertEquals(food.getTimesPerWeek(),foodFromCategory.getTimesPerWeek());
        }
    }

    @Test
    public void getFood() { //addFood method is also tested here.
        FoodCategory foodCategory = new FoodCategory();
        String name = "Peanut Butter";
        double co2 = 2.5;
        Food food = new Food(name,co2);
        foodCategory.addFood(name,co2);
        int index = 0;
        Food  foodFromCategory = foodCategory.getFood(index);
        //compare food with food from food category
        assertEquals(food.getNameOfFood(),foodFromCategory.getNameOfFood());

        //double to double comparison is deprecated
        String co2String = Double.toString(food.getCo2PerServing());
        String actualCo2String = Double.toString(foodFromCategory.getCo2PerServing());
        assertEquals(co2String,actualCo2String);

        assertEquals(food.getTimesPerWeek(),foodFromCategory.getTimesPerWeek());
    }

    @Test
    public void getSize() {
        FoodCategory foodCategory = new FoodCategory();
        int size = 4;
        assertEquals(size,foodCategory.getSize());
    }
}