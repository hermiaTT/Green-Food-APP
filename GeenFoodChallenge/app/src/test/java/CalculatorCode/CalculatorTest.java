
package CalculatorCode;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class CalculatorTest {


    @Test
    public void getArrayLength() {
        FoodCategory foodList = new FoodCategory();
        foodList.addFood("Beef", 100);
        foodList.addFood("Chicken", 200);
        ArrayList<FoodCategory> foodBank = new ArrayList<>();
        foodBank.add(foodList);

        Calculator calculator = new Calculator(foodBank);
        assertEquals(1, calculator.getArrayLength());
    }

    @Test
    public void setArrayLength() {
        FoodCategory foodList = new FoodCategory();
        foodList.addFood("Beef", 100);
        foodList.addFood("Chicken", 200);
        ArrayList<FoodCategory> foodBank = new ArrayList<>();
        foodBank.add(foodList);

        Calculator calculator = new Calculator(foodBank);

        calculator.setArrayLength(0);
        assertEquals(0,calculator.getArrayLength());
        calculator.setArrayLength(foodBank.size());
        assertEquals(1,calculator.getArrayLength());
    }


    @Test
    public void getTotalCO2Emissions() {
        FoodCategory foodList = new FoodCategory();
        foodList.addFood("Beef", 1);
        foodList.addFood("Chicken", 2);
        ArrayList<FoodCategory> foodBank = new ArrayList<>();
        foodBank.add(foodList);
        foodList.getFood(0).setTimesPerWeek(1);
        foodList.getFood(1).setTimesPerWeek(1);
        Calculator calculator = new Calculator(foodBank);
        assertEquals(468, calculator.getTotalCO2Emissions(),10);
    }

    @Test
    public void getTotalCO2Vancouver() {
        FoodCategory foodList = new FoodCategory();
        foodList.addFood("Beef", 100);
        foodList.addFood("Chicken", 200);

        FoodCategory foodList2 = new FoodCategory();
        foodList2.addFood("Fruit",100);
        foodList2.addFood("Veggies",100);

        FoodCategory foodList3 = new FoodCategory();
        foodList2.addFood("Fruit",100);
        foodList2.addFood("Veggies",100);
        ArrayList<FoodCategory> foodBank = new ArrayList<>();
        foodBank.add(foodList);
        foodBank.add(foodList2);
        foodBank.add(foodList3);

        Calculator calculator = new Calculator(foodBank);

        double testco2 = calculator.getTotalCO2Emissions() *631486;

        assertEquals(testco2, calculator.getTotalCO2Vancouver(),10);
    }


    @Test
    public void getFoodCategory() {
        FoodCategory foodList = new FoodCategory();
        foodList.addFood("Beef", 100);
        foodList.addFood("Chicken", 200);

        FoodCategory foodList2 = new FoodCategory();
        foodList2.addFood("Fruit",100);
        foodList2.addFood("Veggies",100);

        ArrayList<FoodCategory> foodBank = new ArrayList<>();
        foodBank.add(foodList);
        foodBank.add(foodList2);

        Calculator calculator = new Calculator(foodBank);

        assertSame(foodList2,calculator.getFoodCategory(1));
        assertNotSame(foodList2,calculator.getFoodCategory(0));


    }
}