package com.cdac.hostelconnect.dto;

public class FoodMenuResponse {

    private Long id;
    private String day;
    private String mealType;
    private String menu;

    public FoodMenuResponse(
            Long id,
            String day,
            String mealType,
            String menu) {

        this.id = id;
        this.day = day;
        this.mealType = mealType;
        this.menu = menu;
    }

    public Long getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public String getMealType() {
        return mealType;
    }

    public String getMenu() {
        return menu;
    }
}