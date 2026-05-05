package com.omelianenko.pokemonbattle.view;

import com.omelianenko.pokemonbattle.model.Pokemon;
import com.omelianenko.pokemonbattle.model.spells.Spell;
import java.util.List;

public class PokemonConsoleStatusView {

    public void showPokemonStatus(Pokemon pokemon){
        System.out.println("-----------------------------");
        System.out.println("[" + pokemon.getName().toUpperCase() + "] lvl: " + pokemon.getLevel());
        System.out.println("Health: " + pokemon.getHealth() + "; Dmg: " + pokemon.getBaseDamage() + "; Def: " + pokemon.getDefense() + "; Element: " + pokemon.getElement());
        System.out.println("Spells: ");
        List<Spell> spells = pokemon.getSpells();
        for (Spell spell : spells){
            System.out.println("- " + spell.getName() + " - cd: " + (spell.getCurrentCooldown() == 0 ? "ready" : (spell.getCurrentCooldown()) + " turn"));
        }
        System.out.println("-----------------------------");
    }
}
