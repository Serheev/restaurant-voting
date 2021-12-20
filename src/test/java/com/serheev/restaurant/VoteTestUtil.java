package com.serheev.restaurant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.serheev.restaurant.model.Vote;
import com.serheev.restaurant.util.JsonUtil;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.UnsupportedEncodingException;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.assertThat;

public class VoteTestUtil {
    public static final int RESTAURANT1_ID = 1;
    public static final int RESTAURANT2_ID = 2;
    public static final int USER1_ID = 1;
    public static final int USER2_ID = 2;

    public static void assertEquals(Vote actual, Vote expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("user").isEqualTo(expected);
    }

    public static void assertNoIdEquals(Vote actual, Vote expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("id", "user").isEqualTo(expected);
    }

    public static Vote asMenu(MvcResult mvcResult) throws UnsupportedEncodingException, JsonProcessingException {
        String jsonActual = mvcResult.getResponse().getContentAsString();
        return JsonUtil.readValue(jsonActual, Vote.class);
    }

    public static ResultMatcher jsonMatcher(Vote expected, BiConsumer<Vote, Vote> equalsAssertion) {
        return mvcResult -> equalsAssertion.accept(asMenu(mvcResult), expected);
    }
}
