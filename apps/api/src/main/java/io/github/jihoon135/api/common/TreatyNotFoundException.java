package io.github.jihoon135.api.common;

public class TreatyNotFoundException extends RuntimeException {

    public TreatyNotFoundException(String code) {
        super("Unknown treaty code: " + code);
    }
}
