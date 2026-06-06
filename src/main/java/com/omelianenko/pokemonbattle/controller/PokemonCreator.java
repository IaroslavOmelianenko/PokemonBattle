package com.omelianenko.pokemonbattle.controller;

import com.omelianenko.pokemonbattle.model.Element;
import com.omelianenko.pokemonbattle.model.Pokemon;
import com.omelianenko.pokemonbattle.model.spells.Spell;
import com.omelianenko.pokemonbattle.model.spells.SpellLibrary;
import com.omelianenko.pokemonbattle.util.ResourceReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PokemonCreator {

    private final Random random = new Random();
    List<String> names = ResourceReader.readFromResource("PokemonNames.txt");

    public Pokemon createPokemon() {

        String name = names.get(random.nextInt(names.size()));
        int level = random.nextInt(11);
        Element element = Element.values()[random.nextInt(Element.values().length)];
        int health = random.nextInt(40, 51);
        int baseDamage = random.nextInt(7, 11);

        List<Spell> spells = new ArrayList<>();
        spells.addAll(SpellLibrary.DamageSpells);
        spells.addAll(SpellLibrary.HealSpells);
        spells.addAll(SpellLibrary.DefenseSpells);

        Collections.shuffle(spells);
        List<Spell> selectedSpells = spells.subList(0, 2);
        List<Spell> pokemonSpells = new ArrayList<>();

        for (Spell spell : selectedSpells) {
            pokemonSpells.add(
                new Spell(spell.getName(), spell.getEffect(), spell.getElement(), spell.getTypes(),
                    spell.getCooldown()));
        }

        return new Pokemon(name, level, element, health, baseDamage, pokemonSpells);
    }
}
