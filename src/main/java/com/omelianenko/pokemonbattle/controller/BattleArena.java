package com.omelianenko.pokemonbattle.controller;

import com.omelianenko.pokemonbattle.model.Pokemon;
import com.omelianenko.pokemonbattle.model.spells.Spell;
import com.omelianenko.pokemonbattle.util.InputScanner;
import com.omelianenko.pokemonbattle.view.PokemonConsoleStatusView;
import java.util.List;
import java.util.Optional;

public class BattleArena {

    private final PokemonCreator pokemonCreator;
    private final PokemonConsoleStatusView pokemonConsoleStatusView;
    private final InputScanner inputScanner;

    public BattleArena(PokemonCreator pokemonCreator,
        PokemonConsoleStatusView pokemonConsoleStatusView, InputScanner inputScanner) {
        this.pokemonCreator = pokemonCreator;
        this.pokemonConsoleStatusView = pokemonConsoleStatusView;
        this.inputScanner = inputScanner;
    }

    public void startBattle() {
        Pokemon pokemon1 = pokemonCreator.createPokemon();
        Pokemon pokemon2 = pokemonCreator.createPokemon();
        System.out.println("[" + pokemon1.getName().toUpperCase() + " VS " + pokemon2.getName().toUpperCase() + "]");
        fight(pokemon1, pokemon2);
    }

    public void fight(Pokemon pokemon1, Pokemon pokemon2) {
        Pokemon attacker = pokemon1;
        Pokemon defender = pokemon2;
        while (pokemon1.getHealth() > 0 && pokemon2.getHealth() > 0) {
            pokemonConsoleStatusView.showPokemonStatus(attacker);
            pokemonConsoleStatusView.showPokemonStatus(defender);
            System.out.println(attacker.getName() + "'s turn");
            System.out.println("1. Attack (" + attacker.getBaseDamage() + " dmg)");
            System.out.println("2. Cast spell");
            Optional<String> input = inputScanner.startAndReadInput();
            if (input.isPresent()) {
                switch (input.get()) {
                    case "1":
                        attacker.attack(defender);
                        break;
                    case "2":
                        List<Spell> spells = attacker.getSpells();
                        boolean spellSelected = false;
                        while (!spellSelected) {
                            for (int i = 0; i < spells.size(); i++) {
                                System.out.println(
                                    (i + 1) + ". " + spells.get(i).getName() + " (" + spells.get(i)
                                        .getType() + " SPELL)" + (spells.get(i).isReady() ? ""
                                        : " - not ready"));
                            }
                            System.out.println("0. Cancel");
                            Optional<String> spellInput = inputScanner.startAndReadInput();
                            if (spellInput.isPresent()) {
                                int spellIndex = Integer.parseInt(spellInput.get());
                                if (spellIndex == 0) {
                                    attacker.attack(defender);
                                    spellSelected = true;
                                } else if (spellIndex > 0 && spellIndex <= spells.size()) {
                                    Spell spell = spells.get(spellIndex - 1);
                                    attacker.useSpell(defender, spell);
                                    spellSelected = true;
                                } else {
                                    System.out.println("Invalid input");
                                }
                            } else {
                                System.out.println("Invalid input");
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid input");
                }
            } else {
                System.out.println("Invalid input");
            }
            Pokemon temp = attacker;
            attacker = defender;
            defender = temp;
        }
        if (pokemon1.getHealth() > 0) {
            System.out.println(pokemon1.getName() + " wins!");
        } else {
            System.out.println(pokemon2.getName() + " wins!");
        }
        inputScanner.closeInput();
    }
}
