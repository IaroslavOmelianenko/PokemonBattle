package com.omelianenko.pokemonbattle.integration;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.omelianenko.pokemonbattle.controller.BattleArena;
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

public class BattleArenaPokemonIntegrationTest {

    private InputScanner inputScanner;
    private PokemonConsoleStatusView statusView;
    private BattleArena battleArena;

    @BeforeEach
    void setUp() {
        inputScanner = mock(InputScanner.class);
        statusView = mock(PokemonConsoleStatusView.class);
        battleArena = new BattleArena(null, statusView, inputScanner);
    }

    @Test
    @DisplayName("battleArena: interaction between pokemons")
    void battleArena_ShouldCorrectlyHandleInteractionBetweenRealPokemons() {

        List<Spell> spells = new ArrayList<>();
        spells.add(new Spell("Heal", 10, Element.AIR, SpellType.HEAL, 2));
        spells.add(new Spell("Fireball", 15, Element.FIRE, SpellType.ATTACK, 3));

        Pokemon pokemon1 = new Pokemon("P1", 3, Element.FIRE, 40, 6, spells);
        Pokemon pokemon2 = new Pokemon("P2", 4, Element.EARTH, 50, 8, new ArrayList<>());

        // Input simulation: P1 healing -> P2 attack -> P1 attack
        when(inputScanner.startAndReadInput())
            .thenReturn(Optional.of("2")) // Cast spell
            .thenReturn(Optional.of("1")) // Choose "Heal"
            .thenReturn(Optional.of("1")) // P2 attacks
            .thenReturn(Optional.of("1")); // P1 attacks

        battleArena.fight(pokemon1, pokemon2);

        verify(statusView, atLeast(2)).showPokemonStatus(pokemon1);
        verify(statusView, atLeast(2)).showPokemonStatus(pokemon2);

        // - P1 should heal for 10 HP (up to max)
        // - P2 deals 8 + 4 = 12 damage (no defense)
        // - P1 deals 6 + 3 = 9 damage in return
    }
}
