package com.omelianenko.pokemonbattle.util;


import java.util.Optional;
import java.util.Scanner;

public class ConsoleInputScanner implements InputScanner {

    private final Scanner scanner;

    public ConsoleInputScanner() {
        this.scanner = new Scanner(System.in);
    }

    @Override
    public Optional<String> startAndReadInput() {
        if (scanner.hasNextLine()) {
            String input = scanner.nextLine().trim();
            return Optional.ofNullable(input.isEmpty() ? null : input.toLowerCase());
        }
        return Optional.empty();
    }

    @Override
    public void closeInput() {
        scanner.close();
    }

}
