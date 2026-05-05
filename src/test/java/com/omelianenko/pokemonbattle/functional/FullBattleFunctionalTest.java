package com.omelianenko.pokemonbattle.functional;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.omelianenko.pokemonbattle.controller.BattleArena;
import com.omelianenko.pokemonbattle.controller.PokemonCreator;
import com.omelianenko.pokemonbattle.util.ConsoleInputScanner;
import com.omelianenko.pokemonbattle.view.PokemonConsoleStatusView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class FullBattleFunctionalTest {
    private final InputStream originalSystemIn = System.in;
    private final PrintStream originalSystemOut = System.out;

    private ByteArrayOutputStream capturedOutput;

    @BeforeEach
    void setUp() {
        capturedOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOutput));
    }

    @AfterEach
    void tearDown() {
        // Always restore original streams
        System.setIn(originalSystemIn);
        System.setOut(originalSystemOut);
    }

    @Test
    @DisplayName("fullBattle functional")
    void fullBattle_ShouldRunToEndAndDeclareWinner() {
        // GIVEN: Simulate user input - always choose "Attack"
        String simulatedInput = """
            1
            1
            1
            1
            1
            1
            """; // Sufficient for a short battle

        // Redirect System.in to simulate user typing
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Use real components to test full integration
        PokemonCreator creator = new PokemonCreator();
        PokemonConsoleStatusView statusView = new PokemonConsoleStatusView();
        ConsoleInputScanner inputScanner = new ConsoleInputScanner(); // Real scanner
        BattleArena arena = new BattleArena(creator, statusView, inputScanner);

        arena.startBattle();

        String output = capturedOutput.toString().toLowerCase();

        // Verify key stages of the battle
        assertTrue(output.contains("vs"),
            "Battle header should display both Pokemon names with 'VS'");

        assertTrue(output.contains("attacks"),
            "Attack actions should be logged in the output");

        assertTrue(output.contains("wins!"),
            "Final output must declare a winner with 'wins!'");

        // Ensure multiple turns occurred (not an instant end)
        long newLineCount = output.chars().filter(ch -> ch == '\n').count();
        assertTrue(newLineCount > 8,
            "Battle output should include multiple turns and status updates. Got: " + newLineCount);
    }
}
