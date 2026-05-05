package com.omelianenko.pokemonbattle.model;

import com.omelianenko.pokemonbattle.model.spells.Spell;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Pokemon {

    private final String name;
    private final int level;
    private final Element element;
    private final int baseDamage;
    private final List<Spell> spells;
    @Setter
    private int health;
    @Setter
    private int defense;


    public Pokemon(String name, int level, Element element, int health, int baseDamage,
        List<Spell> spells) {
        this.name = name;
        this.level = level;
        this.element = element;
        this.health = health;
        this.baseDamage = baseDamage;
        this.spells = new ArrayList<>(spells);
        this.defense = 0;
    }

    public void attack(Pokemon opponent) {
        int damage = this.baseDamage + this.level - opponent.getDefense();
        opponent.setHealth(opponent.getHealth() - damage);
        System.out.println(
            this.getName() + " attacks " + opponent.getName() + " for " + damage + " dmg");
        System.out.println("(" + this.baseDamage + " base dmg, " + this.level + " dmg from lvl, "
            + opponent.getDefense() + " blocked by defence)");
    }

    public void useSpell(Pokemon opponent, Spell spell) {
        if (spell.isReady()) {
            switch (spell.getType()) {
                case ATTACK:
                    int damage = (int) (
                        spell.getEffect() * getElementEffectiveness(this.element, opponent.element)
                            + this.level);
                    opponent.setHealth(opponent.getHealth() - damage);
                    System.out.println(
                        this.getName() + " casts " + spell.getName() + " on " + opponent.getName()
                            + " for " + damage + " dmg");
                    break;
                case DEFENSE:
                    this.setDefense(this.getDefense() + spell.getEffect());
                    System.out.println(
                        this.getName() + " casts " + spell.getName() + " and increases defense by "
                            + spell.getEffect());
                    break;
                case HEAL:
                    this.setHealth(this.getHealth() + spell.getEffect());
                    System.out.println(
                        this.getName() + " casts " + spell.getName() + " and heals for "
                            + spell.getEffect());
                    break;
            }
            spell.setCurrentCooldown(spell.getCooldown());
        } else {
            System.out.println("Spell is not ready");
        }
    }


    public double getElementEffectiveness(Element attackerElement, Element defenderElement) {
        if (attackerElement == Element.WATER && defenderElement == Element.FIRE) {
            return 1.5;
        } else if (attackerElement == Element.FIRE && defenderElement == Element.EARTH) {
            return 1.5;
        } else if (attackerElement == Element.EARTH && defenderElement == Element.AIR) {
            return 1.5;
        } else if (attackerElement == Element.AIR && defenderElement == Element.WATER) {
            return 1.5;
        } else if (attackerElement == Element.WATER && defenderElement == Element.EARTH) {
            return 0.5;
        } else if (attackerElement == Element.FIRE && defenderElement == Element.WATER) {
            return 0.5;
        } else if (attackerElement == Element.EARTH && defenderElement == Element.FIRE) {
            return 0.5;
        } else if (attackerElement == Element.AIR && defenderElement == Element.EARTH) {
            return 0.5;
        } else {
            return 1;
        }
    }
}
