package com.ncinga.timer.exceptions;

public class RefreshTokenHasExpired extends Exception {
    public RefreshTokenHasExpired(String message) {
        super(message);
    }
}
