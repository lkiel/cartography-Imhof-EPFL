package ch.epfl.imhof.painting;

import ch.epfl.imhof.Attributed;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * {@code public final class Filters}
 * <p>
 * Classe servant à construire des expressions lambdas nécessaires au système de filtre utilisé pour
 * générer le rendu visuel de la carte
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public final class Filters {


    private Filters() {
    }

    /**
     * {@code public static Predicate<Attributed<?>> tagged(String key)}
     * <p>
     *
     * @param key - L'attribut dont on désire tester la présence
     * @return Une expression lambda de type {@link Predicate} testant si l'objet attribué
     * (de type {@code Attributed} qu'elle reçoit possède l'attribut 'key' donné en argument de cette méthode
     */
    public static Predicate<Attributed<?>> tagged(String key) {
        return a -> a.hasAttribute(key);
    }

    /**
     * {@code public static Predicate<Attributed<?>> tagged(String key, String value1, String... values)}
     * <p>
     *
     * @param key    - L'attribut dont on désire tester la présence
     * @param value1 - La valeur dont on désire vérifier la présence dans le cas où l'objet attribué
     *               possède bel et bien l'attribut passé précédemment
     * @param values - Un nombre arbitraire de valeurs supplémentaires à tester
     * @return Une expression lambda de type {@link Predicate} testant si l'objet attribué
     * (de type {@code Attributed}) qu'elle reçoit possède l'attribut 'key' donné en argument de cette méthode.
     * Si c'est le cas, elle teste également que la valeur associée à l'attribut 'key' est une des valeurs données
     * en argument
     */
    public static Predicate<Attributed<?>> tagged(String key, String value1, String... values) {

        Set<String> valuesSet = new HashSet<>();
        valuesSet.add(value1);
        Collections.addAll(valuesSet, values);

        return a -> valuesSet.contains(a.attributeValue(key));
    }

    /**
     * {@code public static Predicate<Attributed<?>> onLayer(int layerNumber)}
     * <p>
     *
     * @param layerNumber - Le numéro représentant la couche à tester
     * @return Une expression lambda de type {@link Predicate} testant si l'objet attribué (de type {@code Attributed})
     * se trouve bel est bien sur la même couche que celle passée en argument. Si l'objet attribué de possède pas l'attribut
     * 'layer' ou que ce dernier est un nombre invalide, il est considéré comme se trouvant sur la couche 0
     */
    public static Predicate<Attributed<?>> onLayer(int layerNumber) {

        return a -> (layerNumber == a.attributeValue("layer", 0));
    }
}
