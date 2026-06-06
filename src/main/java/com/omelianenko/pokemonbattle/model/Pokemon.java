package com.omelianenko.pokemonbattle.model;

import com.omelianenko.pokemonbattle.model.spells.Spell;
import com.omelianenko.pokemonbattle.model.spells.SpellType;
import com.omelianenko.pokemonbattle.util.ElementEffectiveness;
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
        if (!spell.isReady()) {
            System.out.println("Spell is not ready. Cooldown: " + spell.getCurrentCooldown());
            return;
        }

        int damage = 0;
        int healAmount = 0;
        int defenseBoost = 0;

        boolean hasAttack = false;
        boolean hasHeal = false;
        boolean hasDefense = false;

        for (SpellType type : spell.getTypes()) {
            switch (type) {
                case ATTACK:
                    damage = (int) (
                        spell.getEffect() * ElementEffectiveness.getEffectiveness(this.element,
                            opponent.element)
                            + this.level);
                    hasAttack = true;
                    break;

                case HEAL:
                    healAmount = spell.getEffect();
                    hasHeal = true;
                    break;

                case DEFENSE:
                    defenseBoost = spell.getEffect();
                    hasDefense = true;
                    break;
            }
        }

        if (hasAttack) {
            opponent.setHealth(Math.max(0, opponent.getHealth() - damage));
        }
        if (hasHeal) {
            this.setHealth(this.getHealth() + healAmount);
        }
        if (hasDefense) {
            this.setDefense(this.getDefense() + defenseBoost);
        }

        StringBuilder log = new StringBuilder();
        log.append(this.getName()).append(" casts ").append(spell.getName());

        if (hasAttack) {
            log.append(" on ").append(opponent.getName()).append(" for ").append(damage)
                .append(" dmg");
        }

        if (hasHeal) {
            if (hasAttack) {
                log.append(" and heals for ").append(healAmount);
            } else {
                log.append(" and heals itself for ").append(healAmount);
            }
        }

        if (hasDefense) {
            if (hasAttack || hasHeal) {
                log.append(" and increases defense by ").append(defenseBoost);
            } else {
                log.append(" and increases defense by ").append(defenseBoost);
            }
        }

        log.append(".");
        System.out.println(log);

        spell.setCurrentCooldown(spell.getCooldown());
    }

    @Override
    public String toString() {
        StringBuilder pokemonStatusBuilder = new StringBuilder();

        pokemonStatusBuilder.append("-----------------------------\n");
        pokemonStatusBuilder.append("[").append(getName().toUpperCase()).append("] lvl: ")
            .append(getLevel()).append("\n");
        pokemonStatusBuilder.append("Health: ").append(getHealth())
            .append("; Dmg: ").append(getBaseDamage())
            .append("; Def: ").append(getDefense())
            .append("; Element: ").append(getElement()).append("\n");
        pokemonStatusBuilder.append("Spells: \n");

        for (Spell spell : getSpells()) {
            String cooldownStatus = spell.getCurrentCooldown() == 0
                ? "ready"
                : spell.getCurrentCooldown() + " turn" + (spell.getCurrentCooldown() > 1 ? "s"
                    : "");
            pokemonStatusBuilder.append("- ").append(spell.getName())
                .append(" - cd: ").append(cooldownStatus).append("\n");
        }

        pokemonStatusBuilder.append("-----------------------------");

        return pokemonStatusBuilder.toString();
    }
}
