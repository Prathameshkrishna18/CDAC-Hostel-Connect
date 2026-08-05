package com.cdac.hostelconnect.service;

import com.cdac.hostelconnect.dto.FoodMenuRequest;
import com.cdac.hostelconnect.entity.FoodMenu;
import com.cdac.hostelconnect.entity.Hostel;
import com.cdac.hostelconnect.entity.User;
import com.cdac.hostelconnect.repository.FoodMenuRepository;
import com.cdac.hostelconnect.repository.HostelRepository;
import com.cdac.hostelconnect.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodMenuService {

    private final FoodMenuRepository foodMenuRepository;
    private final HostelRepository hostelRepository;
    private final UserRepository userRepository;

    public FoodMenuService(
            FoodMenuRepository foodMenuRepository,
            HostelRepository hostelRepository,
            UserRepository userRepository) {

        this.foodMenuRepository = foodMenuRepository;
        this.hostelRepository = hostelRepository;
        this.userRepository = userRepository;
    }

    // =====================================================
    // GET FOOD MENU
    // =====================================================

    public List<FoodMenu> getFoodMenus(
            Long hostelId,
            String ownerEmail) {

        Hostel hostel =
                getAuthorizedHostel(
                        hostelId,
                        ownerEmail
                );

        return foodMenuRepository
                .findByHostelId(hostel.getId());
    }


    // =====================================================
    // ADD FOOD MENU
    // =====================================================

    public FoodMenu addFoodMenu(
            Long hostelId,
            FoodMenuRequest request,
            String ownerEmail) {

        Hostel hostel =
                getAuthorizedHostel(
                        hostelId,
                        ownerEmail
                );

        FoodMenu foodMenu = new FoodMenu();

        foodMenu.setDay(
                request.getDay().trim()
        );

        foodMenu.setMealType(
                request.getMealType().trim()
        );

        foodMenu.setMenu(
                request.getMenu().trim()
        );

        foodMenu.setHostel(hostel);

        return foodMenuRepository.save(foodMenu);
    }


    // =====================================================
    // UPDATE FOOD MENU
    // =====================================================

    public FoodMenu updateFoodMenu(
            Long hostelId,
            Long foodId,
            FoodMenuRequest request,
            String ownerEmail) {

        Hostel hostel =
                getAuthorizedHostel(
                        hostelId,
                        ownerEmail
                );

        FoodMenu foodMenu =
                foodMenuRepository.findById(foodId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Food menu not found"
                                )
                        );

        if (!foodMenu.getHostel()
                .getId()
                .equals(hostel.getId())) {

            throw new RuntimeException(
                    "You are not authorized to modify this food menu"
            );
        }

        foodMenu.setDay(
                request.getDay().trim()
        );

        foodMenu.setMealType(
                request.getMealType().trim()
        );

        foodMenu.setMenu(
                request.getMenu().trim()
        );

        return foodMenuRepository.save(foodMenu);
    }


    // =====================================================
    // DELETE FOOD MENU
    // =====================================================

    public void deleteFoodMenu(
            Long hostelId,
            Long foodId,
            String ownerEmail) {

        Hostel hostel =
                getAuthorizedHostel(
                        hostelId,
                        ownerEmail
                );

        FoodMenu foodMenu =
                foodMenuRepository.findById(foodId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Food menu not found"
                                )
                        );

        if (!foodMenu.getHostel()
                .getId()
                .equals(hostel.getId())) {

            throw new RuntimeException(
                    "You are not authorized to delete this food menu"
            );
        }

        foodMenuRepository.delete(foodMenu);
    }


    // =====================================================
    // OWNER AUTHORIZATION
    // =====================================================

    private Hostel getAuthorizedHostel(
            Long hostelId,
            String ownerEmail) {

        User owner =
                userRepository.findByEmail(ownerEmail)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Owner not found"
                                )
                        );

        Hostel hostel =
                hostelRepository.findById(hostelId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Hostel not found"
                                )
                        );

        if (!hostel.getOwner()
                .getId()
                .equals(owner.getId())) {

            throw new RuntimeException(
                    "You are not authorized to modify this hostel"
            );
        }

        return hostel;
    }
}