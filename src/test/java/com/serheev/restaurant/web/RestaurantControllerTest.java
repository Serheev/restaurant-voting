package com.serheev.restaurant.web;

import com.serheev.restaurant.RestaurantTestUtil;
import com.serheev.restaurant.model.Restaurant;
import com.serheev.restaurant.repository.RestaurantRepository;
import com.serheev.restaurant.util.JsonUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.serheev.restaurant.MenuTestUtil.RESTAURANT1_ID;
import static com.serheev.restaurant.RestaurantTestUtil.*;
import static com.serheev.restaurant.UserTestUtil.ADMIN_MAIL;
import static com.serheev.restaurant.UserTestUtil.USER_MAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {

    static final String ADMIN_URL = "/api/admin/restaurants/";
    static final String USER_URL = "/api/user/restaurants/";

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void allRestaurantsShouldBeRead() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant1, restaurant2, restaurant3, restaurant4, restaurant5));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void shouldBeNotFoundException() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_URL + NOT_FOUND))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getRestaurantById() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_URL + RESTAURANT3_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonMatcher(restaurant3, RestaurantTestUtil::assertNoIdEquals));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createNewRestaurant() throws Exception {
        Restaurant created = getNew();
        perform(MockMvcRequestBuilders.post(ADMIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created)))
                .andExpect(status().isCreated())
                .andExpect(jsonMatcher(created, RestaurantTestUtil::assertNoIdEquals));
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void shouldBeForbiddenForUser() throws Exception {
        Restaurant newRestaurant = getNew();
        perform(MockMvcRequestBuilders.post(ADMIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newRestaurant)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateRestaurantById() throws Exception {
        Restaurant updated = getUpdated();
        perform(MockMvcRequestBuilders.put(ADMIN_URL + RESTAURANT1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());
        RestaurantTestUtil.assertEquals(updated, restaurantRepository.findById(RESTAURANT1_ID).orElseThrow());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteRestaurantById() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_URL + RESTAURANT1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        Assertions.assertFalse(restaurantRepository.findById(RESTAURANT1_ID).isPresent());
    }

    @Test
    @WithUserDetails(value = USER_MAIL)
    void allRestaurantsByNameShouldBeFound() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_URL + SEARCH_STRING))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE))
                .andExpect(RESTAURANT_MATCHER.contentJson(restaurant2, restaurant5));
    }
}