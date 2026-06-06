package com.omelianenko.pokemonbattle;

import com.omelianenko.pokemonbattle.controller.BattleArena;
import com.omelianenko.pokemonbattle.controller.PokemonCreator;
import com.omelianenko.pokemonbattle.util.ConsoleInputScanner;
import com.omelianenko.pokemonbattle.view.PokemonConsoleStatusView;

public class Main {

    public static void main(String[] args) {
        try {
            PokemonCreator pokemonCreator = new PokemonCreator();
            ConsoleInputScanner consoleInputScanner = new ConsoleInputScanner();
            BattleArena battleArena = new BattleArena(pokemonCreator, consoleInputScanner);
            battleArena.startBattle();
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }

    }
}
