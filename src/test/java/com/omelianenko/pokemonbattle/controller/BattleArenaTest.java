package com.omelianenko.pokemonbattle.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.omelianenko.pokemonbattle.model.Element;
import com.omelianenko.pokemonbattle.model.Pokemon;
import com.omelianenko.pokemonbattle.model.spells.Spell;
import com.omelianenko.pokemonbattle.util.InputScanner;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BattleArenaTest {

    @Mock
    private PokemonCreator pokemonCreator;

    @Mock
    private InputScanner inputScanner;

    @InjectMocks
    private BattleArena battleArena;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }


    @Test
    @DisplayName("startBattle: should show 1v1 battle setup")
    void startBattle_ShouldShowOneOnOneSetup() {
        // Arrange
        Pokemon p1 = mock(Pokemon.class);
        Pokemon p2 = mock(Pokemon.class);

        when(p1.getName()).thenReturn("Pikachu");
        when(p2.getName()).thenReturn("Bulbasaur");

        when(pokemonCreator.createPokemon()).thenReturn(p1).thenReturn(p2);
        when(inputScanner.startAndReadInput()).thenReturn(Optional.of("1"));

        // Act
        battleArena.startBattle();

        // Assert
        assertThat(outContent.toString()).contains("PIKACHU VS BULBASAUR");
        verify(pokemonCreator, times(2)).createPokemon();
    }


    @Test
    @DisplayName("startBattle: should start 2v2 battle and show team names")
    void startBattle_ShouldStartTwoOnTwoBattleAndShowTeamNames() {
        // Arrange
        Pokemon t1m1 = mock(Pokemon.class);
        Pokemon t1m2 = mock(Pokemon.class);
        Pokemon t2m1 = mock(Pokemon.class);
        Pokemon t2m2 = mock(Pokemon.class);

        when(t1m1.getName()).thenReturn("A");
        when(t1m2.getName()).thenReturn("B");
        when(t2m1.getName()).thenReturn("C");
        when(t2m2.getName()).thenReturn("D");

        when(pokemonCreator.createPokemon())
            .thenReturn(t1m1).thenReturn(t1m2)
            .thenReturn(t2m1).thenReturn(t2m2);

        when(inputScanner.startAndReadInput()).thenReturn(Optional.of("2"));

        // Capture output
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        try {
            // Act
            battleArena.startBattle();
        } finally {
            System.setOut(originalOut);
        }

        // Assert
        assertThat(outContent.toString()).contains("A, B VS C, D");
        verify(pokemonCreator, times(4)).createPokemon();
    }


    @Test
    @DisplayName("fight: should handle simple attack and declare winner with real Pokemon")
    void fight_ShouldHandleAttackAndDeclareWinner_WithRealPokemon() {
        // Arrange
        List<Spell> noSpells = Collections.emptyList();

        // Сделаем так, что Pikachu быстрее убьёт
        Pokemon pikachu = new Pokemon("Pikachu", 5, Element.AIR, 50, 10, noSpells);
        Pokemon bulbasaur = new Pokemon("Bulbasaur", 4, Element.EARTH, 50, 5, noSpells);

        when(inputScanner.startAndReadInput())
            .thenReturn(Optional.of("1"))
            .thenReturn(Optional.of("1"))
            .thenReturn(Optional.of("1"));

        // Act
        battleArena.fight(pikachu, bulbasaur);

        // Assert
        assertThat(outContent.toString()).contains("Pikachu wins!");
    }


    @Test
    @DisplayName("startBattle: should declare correct team winner in 2v2 battle")
    void startBattle_ShouldDeclareCorrectTeamWinnerInTwoOnTwoBattle() {
        // Arrange
        Pokemon t1m1 = mock(Pokemon.class);
        Pokemon t1m2 = mock(Pokemon.class);
        Pokemon t2m1 = mock(Pokemon.class);
        Pokemon t2m2 = mock(Pokemon.class);

        when(t1m1.getName()).thenReturn("T1M1");
        when(t1m2.getName()).thenReturn("T1M2");
        when(t2m1.getName()).thenReturn("T2M1");
        when(t2m2.getName()).thenReturn("T2M2");

        lenient().when(t1m1.getHealth()).thenReturn(10);
        lenient().when(t1m2.getHealth()).thenReturn(0);
        lenient().when(t2m1.getHealth()).thenReturn(0);
        lenient().when(t2m2.getHealth()).thenReturn(0);


        when(pokemonCreator.createPokemon())
            .thenReturn(t1m1)
            .thenReturn(t1m2)
            .thenReturn(t2m1)
            .thenReturn(t2m2);

        when(inputScanner.startAndReadInput())
            .thenReturn(Optional.of("2"))
            .thenReturn(Optional.of("1"));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            battleArena.startBattle();
        } finally {
            System.setOut(originalOut);
        }

        // Assert
        assertThat(outContent.toString()).contains("T1M1 and T1M2 win!");
        verify(pokemonCreator, times(4)).createPokemon();
    }

    @Test
    @DisplayName("startBattle: should close input scanner at the end")
    void startBattle_ShouldCloseInputScanner() {
        // Arrange
        Pokemon p1 = mock(Pokemon.class);
        Pokemon p2 = mock(Pokemon.class);

        when(p1.getName()).thenReturn("Pikachu");
        when(p2.getName()).thenReturn("Bulbasaur");

        when(inputScanner.startAndReadInput()).thenReturn(Optional.of("1"));
        when(pokemonCreator.createPokemon()).thenReturn(p1).thenReturn(p2);

        // Act
        battleArena.startBattle();

        // Assert
        verify(inputScanner).closeInput();
    }
}
