package com.cdac.hostelconnect.dto.owner;

import jakarta.validation.constraints.NotBlank;

public class OwnerFoodMenuRequest {

    @NotBlank
    private String day;

    @NotBlank
    private String mealType;

    @NotBlank
    private String menu;

    public OwnerFoodMenuRequest() {
    }

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