package com.byteme.greenfoodchallenge;

import android.net.Uri;

public class Restaurant implements Comparable<Restaurant> {

    private String restaurantName;
    private String mealName;
    private String description;
    private String location;
    private String protein;
    private String foodImage;
    private Long timeStamp;

    public Restaurant() {
        // for firebase to recreate a Restaurant object
    }
    public Restaurant(String restaurantName, String mealName, String description, String location, String protein, String foodImage,Long timeStamp) {
        this.restaurantName = restaurantName;
        this.mealName = mealName;
        this.description = description;
        this.location = location;
        this.protein = protein;
        this.foodImage = foodImage;
        this.timeStamp = timeStamp;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public String getMealName(){
        return mealName;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation(){
        return location;
    }

    public String getProtein(){
        return protein;
    }

    public String getFoodImage() {
        return foodImage;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    @Override
    public int compareTo(Restaurant restaurant) {
        if (getTimeStamp() == null || restaurant.getTimeStamp() == null) {
            return 0;
        }
        return getTimeStamp().compareTo(restaurant.getTimeStamp());
    }
}
