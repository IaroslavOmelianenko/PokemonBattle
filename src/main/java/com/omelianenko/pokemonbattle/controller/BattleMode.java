package com.omelianenko.pokemonbattle.controller;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;

@Getter
public enum BattleMode {
    ONE_ON_ONE("1"),
    TWO_ON_TWO("2");

    private final String input;

    BattleMode(String input) {
        this.input = input;
    }

    public static Optional<BattleMode> fromInput(String input) {
        return Arrays.stream(values())
            .filter(mode -> mode.input.equals(input))
            .findFirst();
    }
}
