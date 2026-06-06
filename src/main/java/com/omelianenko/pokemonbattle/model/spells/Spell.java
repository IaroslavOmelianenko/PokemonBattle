package com.omelianenko.pokemonbattle.model.spells;

import com.omelianenko.pokemonbattle.model.Element;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Spell {

    private final String name;
    private final int effect;
    private final Element element;
    private final Set<SpellType> types;
    private final int cooldown;
    @Setter
    private int currentCooldown;

    public Spell(
        String name,
        int effect,
        Element element,
        Set<SpellType> types,
        int cooldown
    ) {
        this.name = name;
        this.effect = effect;
        this.element = element;
        this.types = types;
        this.cooldown = cooldown;
        this.currentCooldown = 0;
    }

    public boolean isReady() {
        return currentCooldown == 0;
    }
}
