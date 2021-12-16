package com.serheev.restaurant.util;

import com.serheev.restaurant.error.BoundaryTimeVoteException;
import lombok.experimental.UtilityClass;

import java.time.LocalTime;

@UtilityClass
public class VoteUtil {
    private static final LocalTime BOUNDARY_TIME = LocalTime.of(11, 0);

    public static void ifChangeAvailable(LocalTime time) {
        if (time.isAfter(BOUNDARY_TIME)) {
            throw new BoundaryTimeVoteException("It's too late to change your voice today");
        }
    }
}
