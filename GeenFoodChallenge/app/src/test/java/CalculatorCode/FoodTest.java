package CalculatorCode;

import org.junit.Test;

import static org.junit.Assert.*;

public class FoodTest {

    @Test
    public void getNameOfFood() {
        String name = "Peanut Butter";
        double co2 = 2.5;
        Food food = new Food(name,co2); // test value
        assertEquals(name,food.getNameOfFood());
        assertNotEquals("Beef",food.getNameOfFood());
    }

    @Test
    public void setNameOfFood() {
        String name = "Peanut Butter";
        double co2 = 2.5;
        Food food = new Food(name,co2); // test value

        String newName = "Beef";
        food.setNameOfFood(newName); // change the name
        assertNotEquals(name,food.getNameOfFood());
        assertEquals(newName,food.getNameOfFood());
    }

    @Test
    public void getTimesPerWeek() {
        String name = "Peanut Butter";
        double co2 = 2.5;
        Food food = new Food(name,co2); // test value
        assertEquals(0,food.getTimesPerWeek());
        int timesPerWeek = 5;
        food.setTimesPerWeek(5);
        assertEquals(timesPerWeek,food.getTimesPerWeek());

    }

    @Test
    public void setTimesPerWeek() {
        String name = "Peanut Butter";
        double co2 = 2.5;
        Food food = new Food(name,co2); // test value
        int timesPerWeek = 5;
        food.setTimesPerWeek(timesPerWeek);
        assertEquals(timesPerWeek,food.getTimesPerWeek());
        assertNotEquals(0,food.getTimesPerWeek());
    }

    @Test
    public void getCo2PerServing() {
        String name = "Peanut Butter";
        double co2 = 2.5;
        Food food = new Food(name,co2); // test value
        String co2String = Double.toString(co2);
        String actualCo2String = Double.toString(food.getCo2PerServing());

        assertEquals(co2String,actualCo2String);

    }

    @Test
    public void setCo2PerServing() {

        String name = "Peanut Butter";
        double co2 = 2.5;
        Food food = new Food(name,co2); // test value
        double newCo2 = 3.5;
        food.setCo2PerServing(newCo2);
        //double to double comparison is deprecated
        String co2String = Double.toString(co2);
        String newCo2String = Double.toString(newCo2);
        String actualCo2String = Double.toString(food.getCo2PerServing());
        assertEquals(newCo2String,actualCo2String);
        assertNotEquals(co2String,actualCo2String);
    }
}