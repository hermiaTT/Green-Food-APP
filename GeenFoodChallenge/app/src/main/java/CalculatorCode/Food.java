package CalculatorCode;

/*
 The food class keeps the name, co2 emissions and the amount user eats  of each individual food
 */
public class Food {

    private String nameOfFood;
    private int timesPerWeek;
    private double co2PerServing;

    // the constructor for Food Class
    public Food(String nameOfFood, double co2PerServing) {
        this.nameOfFood = nameOfFood;
        this.co2PerServing = co2PerServing;
        timesPerWeek = 0;
    }

    public String getNameOfFood() {
        return nameOfFood;
    }

    public void setNameOfFood(String nameOfFood) {
        this.nameOfFood = nameOfFood;
    }

    public int getTimesPerWeek() {
        return timesPerWeek;
    }

    public void setTimesPerWeek(int timesPerWeek) {
        this.timesPerWeek = timesPerWeek;
    }

    public double getCo2PerServing() {
        return co2PerServing;
    }

    public void setCo2PerServing(double co2PerServing) {
        this.co2PerServing = co2PerServing;
    }


}
