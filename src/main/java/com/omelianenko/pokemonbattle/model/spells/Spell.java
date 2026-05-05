package com.omelianenko.pokemonbattle.model.spells;

import com.omelianenko.pokemonbattle.model.Element;
import com.omelianenko.pokemonbattle.model.Pokemon;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Spell {

    private final String name;
    private final int effect;
    private final Element element;
    private final SpellType type;
    private final int cooldown;
    @Setter
    private int currentCooldown;

    public Spell(
        String name,
        int effect,
        Element element,
        SpellType type,
        int cooldown
    ) {
        this.name = name;
        this.effect = effect;
        this.element = element;
        this.type = type;
        this.cooldown = cooldown;
        this.currentCooldown = 0;
    }

    public boolean isReady() {
        return currentCooldown == 0;
    }
}
