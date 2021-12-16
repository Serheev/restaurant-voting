package com.serheev.restaurant.web;

import com.serheev.restaurant.error.NotFoundException;
import com.serheev.restaurant.model.Restaurant;
import com.serheev.restaurant.repository.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static com.serheev.restaurant.util.ValidationUtil.assureIdConsistent;
import static com.serheev.restaurant.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = RestaurantController.URL, produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
@Slf4j
@Tag(name = "Restaurant Controller")
public class RestaurantController {
    static final String URL = "/api";

    private final RestaurantRepository restaurantRepository;

    @Operation(summary = "Get list of restaurants")
    @GetMapping(value = "/user/restaurants", produces = MediaTypes.HAL_JSON_VALUE)
    public List<Restaurant> getAll() {
        log.info("getAll");
        return restaurantRepository.findAll();
    }

    @Operation(summary = "Get restaurant with id")
    @GetMapping(value = "/user/restaurants/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public Restaurant get(@PathVariable int id) {
        log.info("get {}", id);
        return restaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("Restaurant Not Found: id=" + id));
    }

    @Operation(summary = "Add a new restaurant", description = "This can be only done by an admin.")
    @PostMapping(value = "/admin/restaurants", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Restaurant> createWithLocation(@Valid @RequestBody Restaurant restaurant) {
        log.info("create {}", restaurant);
        checkNew(restaurant);
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @Operation(summary = "Update restaurant profile", description = "This can be only done by an admin.")
    @PutMapping(value = "/admin/restaurants/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody Restaurant restaurant, @PathVariable int id) {
        log.info("update {} with id={}", restaurant, id);
        assureIdConsistent(restaurant, id);
        restaurantRepository.save(restaurant);
    }

    @Operation(summary = "Remove the restaurant by id", description = "This can be only done by an admin.")
    @DeleteMapping("/admin/restaurants/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        restaurantRepository.deleteById(id);
    }

    @Operation(summary = "Get all restaurants by name", description = "Non strict search by string, ignoring case.")
    @GetMapping(value = "/user/restaurants/by-name", produces = MediaTypes.HAL_JSON_VALUE)
    public List<Restaurant> getAllByName(@RequestParam String name) {
        log.info("getAllByName {}", name);
        return restaurantRepository.findAllByNameIsContainingIgnoreCase(name);
    }
}
