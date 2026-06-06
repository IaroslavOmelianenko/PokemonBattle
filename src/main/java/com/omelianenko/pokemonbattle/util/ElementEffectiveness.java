package com.omelianenko.pokemonbattle.util;

import com.omelianenko.pokemonbattle.model.Element;

public class ElementEffectiveness {
    public static double getEffectiveness(Element attackerElement, Element defenderElement) {
        // Strong vs
        if (isStrongAgainst(attackerElement, defenderElement)) {
            return 1.5;
        }
        // Weak vs
        if (isWeakAgainst(attackerElement, defenderElement)) {
            return 0.5;
        }
        // Neutral
        return 1.0;
    }

    private static boolean isStrongAgainst(Element attacker, Element defender) {
        return (attacker == Element.WATER && defender == Element.FIRE) ||
            (attacker == Element.FIRE && defender == Element.EARTH) ||
            (attacker == Element.EARTH && defender == Element.AIR) ||
            (attacker == Element.AIR && defender == Element.WATER);
    }

    private static boolean isWeakAgainst(Element attacker, Element defender) {
        return (attacker == Element.WATER && defender == Element.EARTH) ||
            (attacker == Element.FIRE && defender == Element.WATER) ||
            (attacker == Element.EARTH && defender == Element.FIRE) ||
            (attacker == Element.AIR && defender == Element.EARTH);
    }
}
