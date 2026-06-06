package com.omelianenko.pokemonbattle.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.omelianenko.pokemonbattle.controller.BattleArena;
import com.omelianenko.pokemonbattle.controller.PokemonCreator;
import com.omelianenko.pokemonbattle.model.Element;
import com.omelianenko.pokemonbattle.model.Pokemon;
import com.omelianenko.pokemonbattle.model.spells.Spell;
import com.omelianenko.pokemonbattle.model.spells.SpellType;
import com.omelianenko.pokemonbattle.util.InputScanner;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FullBattleFunctionalTest {

    @Mock
    private PokemonCreator pokemonCreator;

    @Mock
    private InputScanner inputScanner;

    private BattleArena battleArena;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
        battleArena = new BattleArena(pokemonCreator, inputScanner);
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    @DisplayName("BattleArena should handle 1v1 battle with spell casting and healing")
    void battleArena_ShouldHandleFightWithHealingSpell() {

        // Heal: 15 HP
        Spell healSpell = new Spell("Heal", 20, Element.AIR, Set.of(SpellType.HEAL), 2);

        // Mage
        Pokemon mage = new Pokemon("Mage", 5, Element.AIR, 60, 5, List.of(healSpell));

        // Warrior
        Pokemon warrior = new Pokemon("Warrior", 6, Element.FIRE, 40, 8, List.of());

        when(pokemonCreator.createPokemon())
            .thenReturn(mage)
            .thenReturn(warrior);

        // === Симулируем ввод пользователя ===
        // Этап 1: выбрать 1v1
        // Этап 2: Mage выбирает "2" → Cast spell → "1" → Heal
        // Этап 3: Warrior атакует
        // Этап 4: Mage атакует
        // Этап 5: Warrior атакует
        // Этап 6: Mage снова лечится
        // Этап 7: Mage атакует → убивает Warrior
        when(inputScanner.startAndReadInput())
            .thenReturn(Optional.of("1"))  // 1v1
            .thenReturn(Optional.of("2"))  // Cast spell
            .thenReturn(Optional.of("1"))  // Choose Heal
            .thenReturn(Optional.of("1"))  // Warrior attacks
            .thenReturn(Optional.of("1"))  // Mage attacks
            .thenReturn(Optional.of("1"))  // Warrior attacks
            .thenReturn(Optional.of("2"))  // Mage casts spell
            .thenReturn(Optional.of("1"))  // Choose Heal
            .thenReturn(Optional.of("1")); // Mage attacks → final blow

        // === Запускаем бой ===
        battleArena.startBattle();

        // === Проверяем вывод ===
        String output = outContent.toString();

        // Проверяем начало боя
        assertThat(output).contains("[MAGE VS WARRIOR]");

        assertThat(output).contains("Mage's turn");

        // Проверяем, что Warrior атаковал
        assertThat(output).contains("Warrior attacks Mage");

        // Проверяем победителя
        assertThat(output).contains("Mage wins!");

        // Дополнительно: проверить, что не было ошибок
        assertThat(output).doesNotContain("Invalid input");
        assertThat(output).doesNotContain("null");
    }

    @Test
    @DisplayName("BattleArena should handle 2v2 team battle and declare correct winner")
    void battleArena_ShouldHandleTwoOnTwoBattle() {
        // === Команда 1: Fast и Tank ===
        Pokemon fast = new Pokemon("Fast", 4, Element.AIR, 30, 10, List.of());
        Pokemon tank = new Pokemon("Tank", 7, Element.EARTH, 60, 5, List.of());

        // Команда 2: Mage и Archer (оба слабее)
        Pokemon enemyMage = new Pokemon("EnemyMage", 5, Element.WATER, 35, 6, List.of());
        Pokemon archer = new Pokemon("Archer", 4, Element.AIR, 25, 7, List.of());

        // Мокаем создание покемонов (по порядку)
        when(pokemonCreator.createPokemon())
            .thenReturn(fast).thenReturn(tank)      // Team 1
            .thenReturn(enemyMage).thenReturn(archer); // Team 2

        // Ввод: выбрать 2v2, Fast атакует, остальные тоже атакуют
        when(inputScanner.startAndReadInput())
            .thenReturn(Optional.of("2"))  // 2v2
            .thenReturn(Optional.of("1"))  // Fast attacks
            .thenReturn(Optional.of("1"))  // Tank attacks
            .thenReturn(Optional.of("1"))  // EnemyMage attacks
            .thenReturn(Optional.of("1"))  // Archer attacks
            .thenReturn(Optional.of("1"))  // Fast attacks
            .thenReturn(Optional.of("1"))
            .thenReturn(Optional.of("1"))
            .thenReturn(Optional.of("1"));

        // Запускаем
        battleArena.startBattle();

        // Проверяем
        String output = outContent.toString();

        assertThat(output).contains("Fast and Tank win!");
    }
}
