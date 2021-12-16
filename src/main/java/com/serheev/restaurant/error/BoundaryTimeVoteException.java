package com.serheev.restaurant.error;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;

import static org.springframework.boot.web.error.ErrorAttributeOptions.Include.MESSAGE;

public class BoundaryTimeVoteException extends AppException {
    public BoundaryTimeVoteException(String message) {
        super(HttpStatus.FORBIDDEN, message, ErrorAttributeOptions.of(MESSAGE));
    }
}
