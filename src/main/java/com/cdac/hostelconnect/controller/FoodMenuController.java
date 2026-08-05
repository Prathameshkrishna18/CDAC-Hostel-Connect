package com.cdac.hostelconnect.controller;

import com.cdac.hostelconnect.dto.FoodMenuRequest;
import com.cdac.hostelconnect.entity.FoodMenu;
import com.cdac.hostelconnect.service.FoodMenuService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owner/hostels")
public class FoodMenuController {

    private final FoodMenuService foodMenuService;

    public FoodMenuController(
            FoodMenuService foodMenuService) {

        this.foodMenuService = foodMenuService;
    }


    // =====================================================
    // GET FOOD MENUS
    // =====================================================

    @GetMapping("/{hostelId}/food-menu")
    public ResponseEntity<List<FoodMenu>> getFoodMenus(
            @PathVariable Long hostelId,
            Authentication authentication) {

        return ResponseEntity.ok(
                foodMenuService.getFoodMenus(
                        hostelId,
                        authentication.getName()
                )
        );
    }


    // =====================================================
    // ADD FOOD MENU
    // =====================================================

    @PostMapping("/{hostelId}/food-menu")
    public ResponseEntity<FoodMenu> addFoodMenu(
            @PathVariable Long hostelId,
            @Valid @RequestBody FoodMenuRequest request,
            Authentication authentication) {

        FoodMenu foodMenu =
                foodMenuService.addFoodMenu(
                        hostelId,
                        request,
                        authentication.getName()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(foodMenu);
    }


    // =====================================================
    // UPDATE FOOD MENU
    // =====================================================

    @PutMapping("/{hostelId}/food-menu/{foodId}")
    public ResponseEntity<FoodMenu> updateFoodMenu(
            @PathVariable Long hostelId,
            @PathVariable Long foodId,
            @Valid @RequestBody FoodMenuRequest request,
            Authentication authentication) {

        return ResponseEntity.ok(
                foodMenuService.updateFoodMenu(
                        hostelId,
                        foodId,
                        request,
                        authentication.getName()
                )
        );
    }


    // =====================================================
    // DELETE FOOD MENU
    // =====================================================

    @DeleteMapping("/{hostelId}/food-menu/{foodId}")
    public ResponseEntity<Void> deleteFoodMenu(
            @PathVariable Long hostelId,
            @PathVariable Long foodId,
            Authentication authentication) {

        foodMenuService.deleteFoodMenu(
                hostelId,
                foodId,
                authentication.getName()
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}