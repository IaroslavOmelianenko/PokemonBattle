package com.omelianenko.pokemonbattle.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.omelianenko.pokemonbattle.model.Element;
import com.omelianenko.pokemonbattle.model.Pokemon;
import com.omelianenko.pokemonbattle.model.spells.Spell;
import com.omelianenko.pokemonbattle.model.spells.SpellType;
import com.omelianenko.pokemonbattle.util.InputScanner;
import com.omelianenko.pokemonbattle.view.PokemonConsoleStatusView;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BattleArenaTest {

    private PokemonCreator pokemonCreator;
    private PokemonConsoleStatusView statusView;
    private InputScanner inputScanner;
    private BattleArena battleArena;

    @BeforeEach
    void setUp() {
        pokemonCreator = mock(PokemonCreator.class);
        statusView = mock(PokemonConsoleStatusView.class);
        inputScanner = mock(InputScanner.class);

        battleArena = new BattleArena(pokemonCreator, statusView, inputScanner);
    }

    @Test
    @DisplayName("startBattle: 2 pokemons created")
    void startBattle_ShouldCreateTwoPokemonsAndStartFight() {
        Pokemon pokemon1 = new Pokemon("Pikachu", 5, Element.AIR, 50, 8,
            new ArrayList<>());
        Pokemon pokemon2 = new Pokemon("Bulbasaur", 4, Element.EARTH, 45, 7,
            new ArrayList<>());

        when(pokemonCreator.createPokemon()).thenReturn(pokemon1).thenReturn(pokemon2);
        when(inputScanner.startAndReadInput())
            .thenReturn(Optional.of("1")) // Attack
            .thenReturn(Optional.of("1"))
            .thenReturn(Optional.of("1"))
            .thenReturn(Optional.of("1"));

        battleArena.startBattle();

        verify(pokemonCreator, times(2)).createPokemon();
        verify(statusView, atLeastOnce()).showPokemonStatus(any(Pokemon.class));
    }

    @Test
    @DisplayName("fight: changing pokemon roles every turn")
    void fight_ShouldAlternateTurnsAndDeclareWinner() {
        Pokemon pokemon1 = mock(Pokemon.class);
        Pokemon pokemon2 = mock(Pokemon.class);

        when(pokemon1.getName()).thenReturn("Pikachu");
        when(pokemon2.getName()).thenReturn("Bulbasaur");
        when(pokemon1.getHealth()).thenReturn(10).thenReturn(10);
        when(pokemon2.getHealth()).thenReturn(20).thenReturn(5).thenReturn(0);

        when(inputScanner.startAndReadInput())
            .thenReturn(Optional.of("1"))
            .thenReturn(Optional.of("1"));

        battleArena.fight(pokemon1, pokemon2);

        verify(pokemon1).attack(pokemon2);
        verify(pokemon2).attack(pokemon1);
        verify(inputScanner).closeInput();
    }

    @Test
    @DisplayName("fight: changing pokemon roles every turn")
    void fight_WhenSpellSelected_ShouldUseSpell() {
        Pokemon pokemon1 = mock(Pokemon.class);
        Pokemon pokemon2 = mock(Pokemon.class);
        Spell spell = new Spell("Fireball", 10, Element.FIRE, SpellType.ATTACK, 2);

        when(pokemon1.getName()).thenReturn("P1");
        when(pokemon2.getName()).thenReturn("P2");
        when(pokemon1.getHealth()).thenReturn(30).thenReturn(30);
        when(pokemon2.getHealth()).thenReturn(20).thenReturn(5).thenReturn(0);
        when(pokemon1.getSpells()).thenReturn(List.of(spell));
        when(pokemon1.getBaseDamage()).thenReturn(5);

        when(inputScanner.startAndReadInput())
            .thenReturn(Optional.of("2")) // Cast spell
            .thenReturn(Optional.of("1"))
            .thenReturn(Optional.of("1")); // Then attack

        battleArena.fight(pokemon1, pokemon2);

        verify(pokemon1).useSpell(eq(pokemon2), eq(spell));
    }

    @Test
    @DisplayName("fight: invalid input error")
    void fight_WhenInvalidInput_ShouldShowErrorAndRetry() {
        Pokemon pokemon1 = new Pokemon("Pikachu", 1, Element.AIR, 50, 8,
            new ArrayList<>());
        Pokemon pokemon2 = new Pokemon("Bulbasaur", 1, Element.EARTH, 45, 7,
            new ArrayList<>());

        when(inputScanner.startAndReadInput())
            .thenReturn(Optional.of("3")) // invalid
            .thenReturn(Optional.of("1")); // valid

        battleArena.fight(pokemon1, pokemon2);
    }
}
