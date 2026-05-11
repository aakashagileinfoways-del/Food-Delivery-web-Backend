package com.fooddelivery.service.impl;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fooddelivery.dto.request.MenuRequest;
import com.fooddelivery.dto.response.MenuResponse;
import com.fooddelivery.entity.MenuItem;
import com.fooddelivery.entity.Restaurant;
import com.fooddelivery.enums.FoodCategory;
import com.fooddelivery.repository.MenuRepository;
import com.fooddelivery.repository.RestaurantRepository;


@ExtendWith(MockitoExtension.class)
public class MenuServiceImplTest {
    
@Mock private MenuRepository menuRepository;
@Mock private RestaurantRepository restaurantRepository;


@InjectMocks
private MenuServiceImpl menuService;

    @Test
    void testAddItem_success() {
    
Restaurant restaurant = new Restaurant();
restaurant.setId(1L);
restaurant.setStatus(com.fooddelivery.enums.RestaurantStatus.APPROVED);

Mockito.when(restaurantRepository.findById(1L))
        .thenReturn(Optional.of(restaurant));

// create a MenuRequest object with test data
MenuRequest request = new MenuRequest();
request.setName("Burger");
request.setDescription("Test Description");
request.setPrice(150.0);
request.setCategory(com.fooddelivery.enums.FoodCategory.MAIN_COURSE);

// call the addItem method
  Mockito.when(menuRepository.save(Mockito.any(MenuItem.class)))
            .thenAnswer(inv -> inv.getArgument(0));

    // --- Act ---
    MenuResponse response = menuService.addItem(1L, request);

    // --- Assert ---
    org.assertj.core.api.Assertions.assertThat(response.getName()).isEqualTo("Burger");
    org.assertj.core.api.Assertions.assertThat(response.getPrice()).isEqualTo(150.0);

    // Verify save called
    Mockito.verify(menuRepository).save(Mockito.any(MenuItem.class));
        
    }

    @Test
    void testGetItemById() {


    }

    @Test
    void testGetMenuByRestaurant() {
    Restaurant restaurant = new Restaurant();
    restaurant.setId(1L);
    restaurant.setStatus(com.fooddelivery.enums.RestaurantStatus.APPROVED);

    Mockito.when(restaurantRepository.findById(1L))
            .thenReturn(Optional.of(restaurant));

            MenuItem menu1 = new MenuItem();
            menu1.setId(10L);
            menu1.setRestaurant(restaurant);
            menu1.setCategory(com.fooddelivery.enums.FoodCategory.MAIN_COURSE);
            menu1.setAvailable(true);

            MenuItem menu2 = new MenuItem();
            menu2.setId(20L);
            menu2.setRestaurant(restaurant);
            menu2.setCategory(com.fooddelivery.enums.FoodCategory.STARTER);
            menu2.setAvailable(false);


            Mockito.when(menuRepository.findByRestaurantId(1L))
                    .thenReturn(java.util.List.of(menu1, menu2));
         
    // --- Act ---
    List<MenuResponse> result = menuService.getMenuByRestaurant(1L);

    // --- Assert ---
     Assertions.assertThat(result).hasSize(1);
Assertions.assertThat(
        FoodCategory.valueOf(result.get(0).getCategory())
).isEqualTo(FoodCategory.MAIN_COURSE);
    }

    @Test
    void testUpdateAvailability() {

    }

    @Test
    void updateItem() {
     
    
    }

}
