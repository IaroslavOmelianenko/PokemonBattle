package com.omelianenko.pokemonbattle.model.spells;

import com.omelianenko.pokemonbattle.model.Element;
import java.util.List;

public class SpellLibrary {

    public static final List<Spell> DamageSpells = List.of(
        new Spell("Fireball", 12, Element.FIRE, SpellType.ATTACK, 3),
        new Spell("Frostbolt", 8, Element.WATER, SpellType.ATTACK, 1),
        new Spell("Earth Slam", 15, Element.EARTH, SpellType.ATTACK, 4),
        new Spell("Lightning Strike", 12, Element.AIR, SpellType.ATTACK, 2)
    );

    public static final List<Spell> DefenseSpells = List.of(
        new Spell("Shield", 5, Element.EARTH, SpellType.DEFENSE, 2),
        new Spell("Barrier", 10, Element.WATER, SpellType.DEFENSE, 3)
    );

    public static final List<Spell> HealSpells = List.of(
        new Spell("Healing Touch", 8, Element.WATER, SpellType.HEAL, 2),
        new Spell("Restoration", 12, Element.EARTH, SpellType.HEAL, 3)
    );

}
