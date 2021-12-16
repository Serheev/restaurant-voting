package com.serheev.restaurant.web;

import com.serheev.restaurant.error.NotFoundException;
import com.serheev.restaurant.model.Menu;
import com.serheev.restaurant.model.Restaurant;
import com.serheev.restaurant.repository.MenuRepository;
import com.serheev.restaurant.repository.RestaurantRepository;
import com.serheev.restaurant.to.MenuTo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;

import static com.serheev.restaurant.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = MenuController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Tag(name = "Menu Controller")
public class MenuController {
    static final String URL = "/api";

    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;

    @Operation(summary = "Get today's menu for restaurant with id")
    @GetMapping(value = "/user/restaurants/{restaurantId}/today", produces = MediaTypes.HAL_JSON_VALUE)
    public Menu get(@PathVariable int restaurantId) {
        log.info("get {}", restaurantId);
        return getTodayMenu(restaurantId);
    }

    @Operation(summary = "Add today's menu for restaurant with id")
    @PostMapping(value = "/admin/restaurants/{restaurantId}/today", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Menu> createTodayMenuWithLocation(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        log.info("create today's {} for restaurant {}", menuTo, restaurantId);
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException("Restaurant Not Found: id=" + restaurantId));
        Menu created = createNewMenuWithDishesToday(menuTo);
        checkNew(created);
        created.setRestaurant(restaurant);
        menuRepository.save(created);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL)
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "Update today's menu for restaurant with id")
    @PutMapping(value = "/admin/restaurants/{restaurantId}/today", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateToday(@Valid @RequestBody MenuTo menuTo, @PathVariable int restaurantId) {
        log.info("update today's menu{} for restaurant {}", menuTo, restaurantId);
        Menu menu = getTodayMenu(restaurantId);
        menu.setDishes(menuTo.getDishes());
        menuRepository.save(menu);
    }

    @Operation(summary = "Delete today's menu for restaurant with id")
    @DeleteMapping(value = "/admin/restaurants/{restaurantId}/today")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteToday(@PathVariable int restaurantId) {
        log.info("delete today's menu for restaurant {}", restaurantId);
        menuRepository.deleteById(getMenuByRestaurantId(restaurantId));
    }

    private Menu getTodayMenu(int restaurantId) {
        return getByDateMenu(restaurantId, LocalDate.now());
    }

    private Menu getByDateMenu(int restaurantId, LocalDate date) {
        restaurantRepository.findById(restaurantId).orElseThrow(() -> new NotFoundException("Restaurant Not Found: id=" + restaurantId));
        return menuRepository.findMenuByDate(restaurantId, date).orElseThrow(() -> new NotFoundException("Not found menu with date=" + date));
    }

    private Menu createNewMenuWithDishesToday(MenuTo menuTo) {
        return new Menu(LocalDate.now(), null, menuTo.getDishes());
    }

    private int getMenuByRestaurantId(int restaurantId) {
        return getTodayMenu(restaurantId).getId();
    }
}
