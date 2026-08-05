package com.cdac.hostelconnect.dto;

import jakarta.validation.constraints.NotBlank;

public class FoodMenuRequest {

    @NotBlank(message = "Day is required")
    private String day;

    @NotBlank(message = "Meal type is required")
    private String mealType;

    @NotBlank(message = "Menu is required")
    private String menu;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}