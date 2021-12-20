package com.serheev.restaurant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.serheev.restaurant.model.Restaurant;
import com.serheev.restaurant.util.JsonUtil;
import lombok.experimental.UtilityClass;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

@UtilityClass
public class RestaurantTestUtil {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingEqualsComparator(Restaurant.class);

    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int RESTAURANT3_ID = 3;
    public static final int RESTAURANT4_ID = 4;
    public static final int RESTAURANT5_ID = 5;
    public static final int NOT_FOUND = 10;
    public static final String SEARCH_STRING = "/by-name?name=meat";

    public static final Restaurant restaurant1 = new Restaurant(RESTAURANT1_ID, "Kuvshin", "Fedorova Street, 10, Kyiv (Kiev) 03150 Ukraine");
    public static final Restaurant restaurant2 = new Restaurant(RESTAURANT2_ID, "BEEF Meat & Wine", "Shota Rustaveli Street, 11, Kyiv (Kiev) 01001 Ukraine");
    public static final Restaurant restaurant3 = new Restaurant(RESTAURANT3_ID, "Genatsvale&Hinkali", "Dragomanova Street, 17, Kyiv (Kiev) 02068 Ukraine");
    public static final Restaurant restaurant4 = new Restaurant(RESTAURANT4_ID, "Musafir", "Saksaganskogo Street, 57А, Kyiv (Kiev) 02000 Ukraine");
    public static final Restaurant restaurant5 = new Restaurant(RESTAURANT5_ID, "Red Meat", "Shota Rustaveli Street, 18, Kyiv (Kiev) 01001 Ukraine");

    public static Restaurant getNew() {
        return new Restaurant("Sushi Bar", "Odessa");
    }

    public static Restaurant getUpdated() {
        return new Restaurant(RESTAURANT1_ID, "Read & Black", "Kyiv");
    }

    public static void assertEquals(Restaurant actual, Restaurant expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields().isEqualTo(expected);
    }

    public static void assertNoIdEquals(Restaurant actual, Restaurant expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }

    public static ResultMatcher jsonMatcher(Restaurant expected, BiConsumer<Restaurant, Restaurant> equalsAssertion) {
        return mvcResult -> equalsAssertion.accept(asRestaurant(mvcResult), expected);
    }

    public static Restaurant asRestaurant(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String jsonActual = mvcResult.getResponse().getContentAsString();
        return JsonUtil.readValue(jsonActual, Restaurant.class);
    }
}
