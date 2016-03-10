package ch.epfl.imhof;

import java.io.Serializable;

/**
 * {@code public final class Attributed<T>}
 * <p>
 * Classe immuable (si le type générique associé l'est également) gérant
 * une paire valeur/Attributes
 *
 * @param <T> - Le type d'objet à associer avec un {@code Attributes}
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public final class Attributed<T extends Serializable> implements Serializable {
    private final T value;
    private final Attributes attributes;
    private static final long serialVersionUID = 2015052700L;



    /**
     * {@code public Attributed(T value, Attributes attributes)}
     * <p>
     * Constructeur de la classe Attributed
     *
     * @param value      - l'objet auxquels s'appliquent les attributs
     * @param attributes - les attributs s'appliquant à l'objet
     */
    public Attributed(T value, Attributes attributes) {
        this.value = value;
        this.attributes = attributes;
    }

    /**
     * {@code public T value()}
     * <p>
     *
     * @return la valeur à laquelle les atributs sont attachés
     */
    public T value() {
        return value;
    }

    /**
     * {@code public Attributes attributes()}
     * <p>
     *
     * @return la table d'association clé-valeur contenant les attributs de l'objet
     * sur lequel elle est appelée
     */
    public Attributes attributes() {
        return attributes;
    }

    /**
     * {@code public boolean hasAttribute(String attributeName)}
     * <p>
     * Méthode retournant vrai si et seulement si l'argument attributeName fait partie des attributs de l'entité.
     *
     * @param attributeName - l'attribut à vérifier
     * @return vrai si et seulement si l'argument fait partie des attributs
     */
    public boolean hasAttribute(String attributeName) {
        return attributes.contains(attributeName);
    }

    /**
     * {@code public String attributeValue(String attributeName)}
     * <p>
     * Méthode retournant la valeur associée à l'attribut donné, si elle existe,
     * ou null si l'attribut donné n'existe pas pour cette entité.
     *
     * @param attributeName - l'attribut pour lequel on cherche la valeur
     * @return la valeur associée à l'attribut s'il existe, ou null autrement
     */
    public String attributeValue(String attributeName) {
        return attributes.get(attributeName);
    }

    /**
     * {@code public String attributeValue(String attributeName, String defaultValue)}
     * <p>
     * Méthode retournant la valeur associée à l'attribut donné,
     * la valeur par défaut si l'attribut n'existe pas pour cette entité
     *
     * @param attributeName - l'attribut dont on cherche la valeur
     * @param defaultValue  - la valeur à retourner à défaut de correspondance
     * @return la valeur associée à l'attribut donné, ou à
     * défaut la valeur par défaut
     */
    public String attributeValue(String attributeName, String defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }

    /**
     * {@code public int attributeValue(String attributeName, int defaultValue)}
     * <p>
     * Méthode retournant la valeur entière associée à l'attribut donné, ou qui
     * retourne la valeur par défaut si l'attribut n'existe pas ou si le nombre
     * n'est pas un entier valide
     *
     * @param attributeName - l'attribut dont on cherche la valeur
     * @param defaultValue  - la valeur à retourner à défaut de correspondance
     * @return un nombre entier associé à l'attribut spécifié ou la valeur de
     * retour à défaut de correspondance
     */
    public int attributeValue(String attributeName, int defaultValue) {
        return attributes.get(attributeName, defaultValue);
    }

}
