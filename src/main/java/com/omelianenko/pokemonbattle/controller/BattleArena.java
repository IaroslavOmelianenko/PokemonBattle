package com.omelianenko.pokemonbattle.controller;

import com.omelianenko.pokemonbattle.model.Pokemon;
import com.omelianenko.pokemonbattle.model.spells.Spell;
import com.omelianenko.pokemonbattle.util.InputScanner;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BattleArena {
    private static final String ATTACK_ACTION = "1";
    private static final String CAST_SPELL_ACTION = "2";
    private static final String ONE_ON_ONE = "1";
    private static final String TWO_ON_TWO = "2";

    private final PokemonCreator pokemonCreator;
    private final InputScanner inputScanner;

    public BattleArena(PokemonCreator pokemonCreator, InputScanner inputScanner) {
        this.pokemonCreator = pokemonCreator;
        this.inputScanner = inputScanner;
    }

    public void startBattle() {
        try {
            while (true) {
                System.out.println("Choose battle mode:");
                System.out.println(ONE_ON_ONE + ". 1 vs 1");
                System.out.println(TWO_ON_TWO + ". 2 vs 2");
                Optional<String> input = inputScanner.startAndReadInput();
                Optional<BattleMode> battleMode = input.flatMap(BattleMode::fromInput);
                if (battleMode.isPresent()) {
                    if (battleMode.get() == BattleMode.TWO_ON_TWO) {
                        startTwoOnTwoBattle();
                    } else {
                        startOneOnOneBattle();
                    }
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 1 or 2.");
                }
            }
        } finally {
            inputScanner.closeInput(); // ✅ Закрываем в одном месте
        }
    }

    private void startOneOnOneBattle() {
        Pokemon pokemon1 = pokemonCreator.createPokemon();
        Pokemon pokemon2 = pokemonCreator.createPokemon();
        System.out.println("[" + pokemon1.getName().toUpperCase() + " VS " + pokemon2.getName().toUpperCase() + "]");
        fight(pokemon1, pokemon2);
    }

    private void startTwoOnTwoBattle() {
        System.out.println("Create Team 1");
        Pokemon team1Member1 = pokemonCreator.createPokemon();
        Pokemon team1Member2 = pokemonCreator.createPokemon();
        System.out.println("Create Team 2");
        Pokemon team2Member1 = pokemonCreator.createPokemon();
        Pokemon team2Member2 = pokemonCreator.createPokemon();
        System.out.println("[" +
            team1Member1.getName().toUpperCase() + ", " + team1Member2.getName().toUpperCase() +
            " VS " +
            team2Member1.getName().toUpperCase() + ", " + team2Member2.getName().toUpperCase() + "]"
        );
        fightTwoOnTwo(team1Member1, team1Member2, team2Member1, team2Member2);
    }

    private void fightTwoOnTwo(Pokemon t1m1, Pokemon t1m2, Pokemon t2m1, Pokemon t2m2) {
        List<Pokemon> team1 = Arrays.asList(t1m1, t1m2);
        List<Pokemon> team2 = Arrays.asList(t2m1, t2m2);
        Pokemon attacker = t1m1;
        Pokemon defender = t2m1;

        while (isTeamAlive(team1) && isTeamAlive(team2)) {
            List<Pokemon> enemyTeam = team1.contains(attacker) ? team2 : team1;
            defender = enemyTeam.stream()
                .filter(p -> p.getHealth() > 0)
                .findFirst()
                .orElse(null);
            if (defender == null) break;

            System.out.println(attacker);
            System.out.println(defender);
            System.out.println(attacker.getName() + "'s turn");
            System.out.println(ATTACK_ACTION + ". Attack (" + attacker.getBaseDamage() + " dmg)");
            System.out.println(CAST_SPELL_ACTION + ". Cast spell");

            Optional<String> input = inputScanner.startAndReadInput();
            if (input.isPresent()) {
                switch (input.get()) {
                    case ATTACK_ACTION:
                        attacker.attack(defender);
                        break;
                    case CAST_SPELL_ACTION:
                        List<Spell> spells = attacker.getSpells();
                        boolean spellSelected = false;
                        while (!spellSelected) {
                            for (int i = 0; i < spells.size(); i++) {
                                Spell spell = spells.get(i);
                                System.out.println((i + 1) + ". " + spell.getName() + " (" + spell.getTypes() + ")" +
                                    (spell.isReady() ? "" : " - not ready"));
                            }
                            System.out.println("0. Cancel");
                            Optional<String> spellInput = inputScanner.startAndReadInput();
                            if (spellInput.isPresent()) {
                                try {
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
                                } catch (NumberFormatException e) {
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
            attacker = getNextAttacker(attacker, t1m1, t1m2, t2m1, t2m2);
        }
        declareTwoOnTwoWinner(t1m1, t1m2, t2m1, t2m2);
    }

    private boolean isTeamAlive(List<Pokemon> team) {
        return team.stream().anyMatch(p -> p.getHealth() > 0);
    }

    private Pokemon getNextAttacker(Pokemon current, Pokemon... all) {
        int startIndex = -1;
        for (int i = 0; i < all.length; i++) {
            if (all[i] == current) {
                startIndex = i;
                break;
            }
        }
        if (startIndex == -1) return current;
        for (int i = 1; i < all.length; i++) {
            int index = (startIndex + i) % all.length;
            if (all[index].getHealth() > 0) {
                return all[index];
            }
        }
        return current;
    }

    private void declareTwoOnTwoWinner(Pokemon t1m1, Pokemon t1m2, Pokemon t2m1, Pokemon t2m2) {
        boolean team1Alive = t1m1.getHealth() > 0 || t1m2.getHealth() > 0;
        boolean team2Alive = t2m1.getHealth() > 0 || t2m2.getHealth() > 0;
        if (team1Alive && !team2Alive) {
            System.out.println(t1m1.getName() + " and " + t1m2.getName() + " win!");
        } else if (team2Alive && !team1Alive) {
            System.out.println(t2m1.getName() + " and " + t2m2.getName() + " win!");
        } else {
            System.out.println("It's a draw!");
        }
    }

    public void fight(Pokemon pokemon1, Pokemon pokemon2) {
        Pokemon attacker = pokemon1;
        Pokemon defender = pokemon2;
        while (pokemon1.getHealth() > 0 && pokemon2.getHealth() > 0) {
            System.out.println(attacker);
            System.out.println(defender);
            System.out.println(attacker.getName() + "'s turn");
            System.out.println("1. Attack (" + attacker.getBaseDamage() + " dmg)");
            System.out.println("2. Cast spell");

            Optional<String> input = inputScanner.startAndReadInput();
            if (input.isPresent()) {
                switch (input.get()) {
                    case ATTACK_ACTION:
                        attacker.attack(defender);
                        break;
                    case CAST_SPELL_ACTION:
                        List<Spell> spells = attacker.getSpells();
                        boolean spellSelected = false;
                        while (!spellSelected) {
                            for (int i = 0; i < spells.size(); i++) {
                                System.out.println(
                                    (i + 1) + ". " + spells.get(i).getName() + " (" + spells.get(i).getTypes() + " SPELL)" +
                                        (spells.get(i).isReady() ? "" : " - not ready"));
                            }
                            System.out.println("0. Cancel");
                            Optional<String> spellInput = inputScanner.startAndReadInput();
                            if (spellInput.isPresent()) {
                                try {
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
                                } catch (NumberFormatException e) {
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
    }
}
