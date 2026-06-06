package com.omelianenko.pokemonbattle.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.omelianenko.pokemonbattle.model.spells.Spell;
import com.omelianenko.pokemonbattle.model.spells.SpellType;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PokemonTest {

    @Test
    void attack_ShouldReduceOpponentHealth() {
        Pokemon attacker = new Pokemon("Pikachu", 5, Element.AIR, 50, 8,
            new java.util.ArrayList<>());

        Pokemon defender = new Pokemon("Bulbasaur", 3, Element.EARTH, 40, 7,
            new java.util.ArrayList<>());
        defender.setDefense(2);

        attacker.attack(defender);

        int expectedDamage = 8 + 5 - 2; // baseDamage + level - defense
        assertEquals(40 - expectedDamage, defender.getHealth());
    }

    @Test
    void useSpell_AttackSpell_ShouldApplyDamageWithEffectiveness() {
        Pokemon attacker = new Pokemon("Charmander", 5, Element.FIRE, 50, 8,
            java.util.Collections.emptyList());
        Pokemon defender = new Pokemon("Squirtle", 5, Element.WATER, 40, 8,
            java.util.Collections.emptyList());

        Spell fireball = new Spell("Fireball", 10, Element.FIRE, Set.of(SpellType.ATTACK), 2);
        fireball.setCurrentCooldown(0); // ready

        attacker.useSpell(defender, fireball);

        // FIRE vs WATER = 0.5 effectiveness
        double effectiveness = 0.5;
        int expectedDamage = (int) ((10 * effectiveness) + 5); // effect * effectiveness + level
        assertEquals(40 - expectedDamage, defender.getHealth());
    }

    @Test
    void useSpell_DefenseSpell_ShouldIncreaseDefense() {
        Pokemon pokemon = new Pokemon("Onix", 1, Element.EARTH, 50, 5,
            java.util.Collections.emptyList());
        Spell armor = new Spell("Armor", 5, Element.EARTH, Set.of(SpellType.DEFENSE), 1);

        pokemon.useSpell(pokemon, armor);

        assertEquals(5, pokemon.getDefense());
    }

    @Test
    void useSpell_HealSpell_ShouldIncreaseHealth() {
        Pokemon pokemon = new Pokemon("Chansey", 10, Element.AIR, 40, 5,
            java.util.Collections.emptyList());
        Spell heal = new Spell("Heal", 10, Element.AIR, Set.of(SpellType.HEAL), 1);

        pokemon.useSpell(pokemon, heal);

        assertEquals(50, pokemon.getHealth());
    }

    @Test
    void useSpell_OnCooldown_ShouldNotApplyEffect() {
        Pokemon attacker = new Pokemon("Mage", 1, Element.WATER, 50, 5,
            java.util.Collections.emptyList());
        Spell spell = new Spell("Blink", 5, Element.AIR, Set.of(SpellType.ATTACK), 2);
        spell.setCurrentCooldown(1); // not ready

        attacker.useSpell(attacker, spell);

        // No effect should be applied
        assertEquals(0, attacker.getDefense());
    }
}

