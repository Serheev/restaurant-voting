package com.serheev.restaurant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.serheev.restaurant.model.Dish;
import com.serheev.restaurant.model.Menu;
import com.serheev.restaurant.util.JsonUtil;
import lombok.experimental.UtilityClass;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;


@UtilityClass
public class MenuTestUtil {
    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT5_ID = 5;
    public static final int MENU1_ID = 1;
    public static final int MENU5_ID = 5;

    public static final List<Dish> restaurantIdOneActualDishes = List.of(
            new Dish("Wagyu beef steak", 400),
            new Dish("English marbled beef pie", 2000),
            new Dish("Fritatta with lobster and caviar", 1000),
            new Dish("Pizza \"Louis XIII\"", 12000)
    );

    public static final List<Dish> restaurantIdOneDishesUpdated = List.of(
            new Dish("Beef steak", 200),
            new Dish("Beef pie", 250),
            new Dish("Lobster and caviar", 100),
            new Dish("Pizza", 50)
    );

    public static final List<Dish> newDishes = List.of(
            new Dish("Borsch", 100),
            new Dish("Varenyk", 150)
    );

    public static Menu getToday() {
        return new Menu(MENU1_ID, LocalDate.now(), null, restaurantIdOneActualDishes);
    }

    public static Menu getNew() {
        return new Menu(MENU5_ID, LocalDate.now(), null, newDishes);
    }

    public static Menu getUpdated() {
        return new Menu(MENU1_ID, LocalDate.now(), null, restaurantIdOneDishesUpdated);
    }

    public static void assertEquals(Menu actual, Menu expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("restaurant").isEqualTo(expected);
    }

    public static void assertNoIdEquals(Menu actual, Menu expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("id", "restaurant").isEqualTo(expected);
    }

    public static Menu asMenu(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String jsonActual = mvcResult.getResponse().getContentAsString();
        return JsonUtil.readValue(jsonActual, Menu.class);
    }

    public static ResultMatcher jsonMatcher(Menu expected, BiConsumer<Menu, Menu> equalsAssertion) {
        return mvcResult -> equalsAssertion.accept(asMenu(mvcResult), expected);
    }
}
