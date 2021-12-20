package com.serheev.restaurant.web;

import com.serheev.restaurant.MenuTestUtil;
import com.serheev.restaurant.model.Menu;
import com.serheev.restaurant.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.serheev.restaurant.MenuTestUtil.RESTAURANT1_ID;
import static com.serheev.restaurant.MenuTestUtil.RESTAURANT5_ID;
import static com.serheev.restaurant.MenuTestUtil.getNew;
import static com.serheev.restaurant.MenuTestUtil.getToday;
import static com.serheev.restaurant.MenuTestUtil.getUpdated;
import static com.serheev.restaurant.MenuTestUtil.jsonMatcher;
import static com.serheev.restaurant.UserTestUtil.ADMIN_MAIL;
import static com.serheev.restaurant.UserTestUtil.USER_MAIL;
import static com.serheev.restaurant.util.JsonUtil.writeValue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuControllerTest extends AbstractControllerTest {

    static final String ADMIN_URL = "/api/admin/restaurants/";
    static final String USER_URL = "/api/user/restaurants/";

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @WithUserDetails(value = USER_MAIL)
    void getTodayMenuByRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.get(USER_URL + RESTAURANT1_ID + "/today"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonMatcher(getToday(), MenuTestUtil::assertEquals));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void createTodayMenuByRestaurantId() throws Exception {
        Menu newMenu = getNew();
        perform(MockMvcRequestBuilders.post(ADMIN_URL + RESTAURANT5_ID + "/today")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(newMenu)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonMatcher(newMenu, MenuTestUtil::assertEquals));
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void updateTodayMenuByRestaurantId() throws Exception {
        Menu updated = getUpdated();
        perform(MockMvcRequestBuilders.put(ADMIN_URL + RESTAURANT1_ID + "/today")
                .contentType(MediaType.APPLICATION_JSON)
                .content(writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MenuTestUtil.assertEquals(updated, menuRepository.findById(RESTAURANT1_ID).orElseThrow());
    }

    @Test
    @WithUserDetails(value = ADMIN_MAIL)
    void deleteTodayMenuByRestaurantId() throws Exception {
        perform(MockMvcRequestBuilders.delete(ADMIN_URL + RESTAURANT1_ID + "/today"))
                .andExpect(status().isNoContent())
                .andDo(print());
        assertFalse(menuRepository.findById(RESTAURANT1_ID).isPresent());
    }
}