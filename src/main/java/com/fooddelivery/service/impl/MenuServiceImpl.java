    package com.fooddelivery.service.impl;

    import java.util.List;
    import java.util.concurrent.TimeUnit;

    // import org.apache.kafka.common.protocol.types.Field.Str;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.redis.core.StringRedisTemplate;
    import org.springframework.stereotype.Service;

    import com.fooddelivery.dto.request.MenuRequest;
    import com.fooddelivery.dto.response.MenuResponse;
    import com.fooddelivery.entity.MenuItem;
    import com.fooddelivery.entity.Restaurant;
    import com.fooddelivery.enums.RestaurantStatus;
    import com.fooddelivery.repository.MenuRepository;
    import com.fooddelivery.repository.RestaurantRepository;
    import com.fooddelivery.service.MenuService;

    import lombok.RequiredArgsConstructor;
    import tools.jackson.databind.ObjectMapper;

    @Service
    @RequiredArgsConstructor
    public class MenuServiceImpl implements MenuService {

        private final MenuRepository menuRepository;
        private final RestaurantRepository restaurantRepository;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;
        // @Override
        // public MenuResponse addItem(Long restaurantId, MenuRequest request) {

        //     Restaurant restaurant = restaurantRepository.findById(restaurantId)
        //             .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        //     MenuItem item = new MenuItem();
        //     item.setName(request.getName());
        //     item.setDescription(request.getDescription());
        //     item.setPrice(request.getPrice());
        //     item.setCategory(request.getCategory());
        //     item.setAvailable(true);
        //     item.setRestaurant(restaurant);

        //     menuRepository.save(item);

        //     return mapToResponse(item);
        // }


            @Override
    public MenuResponse addItem(Long restaurantId, MenuRequest request) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        if (restaurant.getStatus() != RestaurantStatus.APPROVED) {
            throw new RuntimeException("Cannot add menu items. Restaurant is not approved.");
        }

        MenuItem item = new MenuItem();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setPrice(request.getPrice());
        item.setCategory(request.getCategory());
        item.setAvailable(true);
        item.setRestaurant(restaurant);

        menuRepository.save(item);

        return mapToResponse(item);
    }




    @Override
    public List<MenuResponse> getMenuByRestaurant(Long restaurantId) {

        String key = "menu:restaurant:" + restaurantId;

        try {
            // 🔥 1. Check Redis
            String cachedData = redisTemplate.opsForValue().get(key);

            if (cachedData != null) {
                System.out.println("🔥 Menu from Redis");
                return objectMapper.readValue(
                        cachedData,
                        objectMapper.getTypeFactory()
                                .constructCollectionType(List.class, MenuResponse.class)
                );
            }

            // 💾 2. Fetch from DB
            System.out.println("💾 Menu from DB");

            List<MenuResponse> response = menuRepository.findByRestaurantId(restaurantId)
                    .stream()
                    .map(this::mapToResponse)
                    .toList();

            // 🔥 3. Save to Redis (10 min)
            String json = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(key, json, 10, TimeUnit.MINUTES);

            return response;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
        public MenuResponse updateItem(Long id, MenuRequest request) {

            MenuItem item = menuRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            if (item.getRestaurant() == null || item.getRestaurant().getStatus() != RestaurantStatus.APPROVED) {
                throw new RuntimeException("Cannot update menu item. Restaurant is not approved.");
            }

            item.setName(request.getName());
            item.setDescription(request.getDescription());
            item.setPrice(request.getPrice());
            item.setCategory(request.getCategory());

            menuRepository.save(item);

            return mapToResponse(item);
        }

        @Override
        public void deleteItem(Long id) {
            menuRepository.deleteById(id);
        }

        private MenuResponse mapToResponse(MenuItem item) {
            return new MenuResponse(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getPrice(),
                    item.getAvailable(),
                    item.getCategory().name(),
                    item.getRestaurant().getId()
            );
        }

        @Override
        public List<MenuResponse> searchAvailableDishes(String cuisineType, Double minRating) {
            String cuisine = (cuisineType == null || cuisineType.isBlank()) ? null : cuisineType;

            return restaurantRepository.findAll().stream()
                    .filter(r -> r.getStatus() == RestaurantStatus.APPROVED)
                    .filter(Restaurant::isActive)
                    .filter(r -> cuisine == null || (r.getCuisineType() != null && r.getCuisineType().equalsIgnoreCase(cuisine)))
                    .filter(r -> minRating == null || (r.getRating() != null && r.getRating() >= minRating))
                    .flatMap(r -> menuRepository.findByRestaurantId(r.getId()).stream())
                    .filter(MenuItem::getAvailable)
                    .map(this::mapToResponse)
                    .toList();
        }


        @Override
    public void updateAvailability(Long id, Boolean available) {

        MenuItem item = menuRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (item.getRestaurant() == null || item.getRestaurant().getStatus() != RestaurantStatus.APPROVED) {
            throw new RuntimeException("Cannot update availability. Restaurant is not approved.");
        }

        item.setAvailable(available);
        menuRepository.save(item);
    }
    }