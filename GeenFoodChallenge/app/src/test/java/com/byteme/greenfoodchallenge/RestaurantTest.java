package com.byteme.greenfoodchallenge;

import org.junit.Test;

import static org.junit.Assert.*;

public class RestaurantTest {
    @Test
    public void getRestaurantName() {
        long time = 1;
        Restaurant testRes = new Restaurant("name","mealname","description","location","protein","path",time);

        assertEquals("name", testRes.getRestaurantName());
        assertNotEquals("notthis",testRes.getRestaurantName());
    }

    @Test
    public void getMealName() {
        long time = 1;
        Restaurant testRes = new Restaurant("name","mealname","description","location","protein","path",time);

        assertEquals("mealname",testRes.getMealName());
        assertNotEquals("notthis", testRes.getMealName());
    }

    @Test
    public void getDescription() {
        long time = 1;
        Restaurant testRes = new Restaurant("name","mealname","description","location","protein","path",time);

        assertEquals("description", testRes.getDescription());
        assertNotEquals("notthis", testRes.getDescription());
    }

    @Test
    public void getLocation() {
        long time = 1;
        Restaurant testRes = new Restaurant("name","mealname","description","location","protein","path",time);

        assertEquals("location", testRes.getLocation());
        assertNotEquals("notthis", testRes.getLocation());
    }

    @Test
    public void getProtein() {
        long time = 1;
        Restaurant testRes = new Restaurant("name","mealname","description","location","protein","path",time);

        assertEquals("protein", testRes.getProtein());
        assertNotEquals("notthis", testRes.getProtein());
    }

    @Test
    public void getFoodImage() {
        long time = 1;
        Restaurant testRes = new Restaurant("name","mealname","description","location","protein","path",time);

        assertEquals("path", testRes.getFoodImage());
        assertNotEquals("notPath", testRes.getFoodImage());
    }

    @Test
    public void getTimeStamp(){
        long time = 1;
        Restaurant testRes = new Restaurant("name","mealname","description","location","protein","path",time);
        assertEquals(time, testRes.getTimeStamp().longValue());
        assertNotEquals(0, testRes.getTimeStamp().longValue());
    }
}