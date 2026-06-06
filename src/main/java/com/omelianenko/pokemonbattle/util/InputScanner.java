package com.omelianenko.pokemonbattle.util;

import java.util.Optional;

public interface InputScanner {

    Optional<String> startAndReadInput();

    void closeInput();

}
