package ch.epfl.imhof;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * {@code public final class Attributes}
 * <p>
 * Classe immuable gérant les attributs sous forme d'une table associative
 * clé-valeur de type String
 *
 * @author Clément Nussbaumer (250261)
 * @author Leandro Kieliger (246263)
 */
public final class Attributes implements Serializable {

    transient private final Map<String, String> attributesMap;
    private static final long serialVersionUID = 2015052700L;

    /**
     * {@code public final static class Builder}
     * <p>
     * Bâtisseur de la classe {@link Attributes}
     */
    public final static class Builder {
        private final Map<String, String> builderAttributesMap;

        /**
         * {@code public Builder()}
         * <p>
         * Constructeur du bâtisseur de la classe {@link Attributes}
         */
        public Builder() {
            builderAttributesMap = new HashMap<>();
        }

        /**
         * {@code public void put(String key, String value)}
         * <p>
         * Ajoute une valeur à la table associative
         *
         * @param key   - clé passée
         * @param value - valeur passée
         */
        public void put(String key, String value) {
            builderAttributesMap.put(key, value);
        }

        /**
         * {@code public Attributes build()}
         * <p>
         * Construit un objet de type {@link Attributes} à partir des données du
         * bâtisseur
         *
         * @return un objet {@link Attributes}
         */
        public Attributes build() {
            return new Attributes(builderAttributesMap);
        }
    }

    /**
     * {@code public Attributes(Map<String, String> attributes)}
     * <p>
     * Constructeur de la classe {@link Attributes}
     *
     * @param attributes - liste d'association clé-valeur de type {@link String}
     */
    public Attributes(Map<String, String> attributes) {
        attributesMap = Collections.unmodifiableMap(new HashMap<>(attributes));
    }

    /**
     * {@code public boolean isEmpty()}
     * <p>
     *
     * @return un booléen indiquant si la liste est vide
     */
    public boolean isEmpty() {
        return attributesMap.isEmpty();
    }

    /**
     * {@code public boolean contains(String key)}
     * <p>
     * Méthode testant si une clé passée en paramètre est contenue dans la table
     * associative
     *
     * @param key - clé donnée
     * @return un booléen indiquant le résultat
     */
    public boolean contains(String key) {
        return attributesMap.containsKey(key);
    }

    /**
     * {@code public String get(String key)}
     * <p>
     * Méthode retournant la valeur associée à la clé passée en paramètre
     *
     * @param key - la clé donnée
     * @return la valeur associée sous forme de {@link String} ou null si cette dernière
     * n'a pas pu être trouvée dans la table associative
     */
    public String get(String key) {
        return attributesMap.get(key);
    }

    /**
     * {@code public String get(String key, String defaultValue)}
     * <p>
     * Méthode retournant la valeur associée à la clé de type String passée en
     * paramètre
     *
     * @param key          - la clé donnée
     * @param defaultValue - la valeur à retourner à défaut de correspondance
     * @return String contenant la valeur associée à la clé passée en paramètre
     * ou la valeur par défaut si cette dernière n'a pas été trouvée
     * dans la table associative
     */

    public String get(String key, String defaultValue) {
        return attributesMap.getOrDefault(key, defaultValue);
    }

    /**
     * {@code public int get(String key, int defaultValue)}
     * <p>
     * Méthode retournant la valeur associée à la clé de type int passée en
     * paramètre
     *
     * @param key          - la clé donnée
     * @param defaultValue - la valeur à retourner à défaut de correspondance
     * @return int contenant la valeur associée à la clé passée en paramètre ou
     * la valeur par défaut si cette dernière n'a pas été trouvée das la
     * table associative ou si la valeur associé n'est pas un nombre
     * valide
     */
    public int get(String key, int defaultValue) {
        try {
            /*On réalise une conversion en String  pour que le résultat soit parsable par Integer.parseInt()
              évitant ainsi de tomber inutilement dans le catch à chaque fois que l'attribut cherché n'est pas présent
             */
            return Integer.parseInt(get(key, Integer.toString(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * {@code public Attributes keepOnlyKeys(Set<String> keysToKeep)}
     * <p>
     * Méthode retournant une version filtrée des attributs ne contenant que
     * ceux dont le nom figure dans l'ensemble passé en paramètre.
     *
     * @param keysToKeep - L'ensemble des clés à garder
     * @return un objet Attributes filtré selon l'ensemble des clés à garder
     */
    public Attributes keepOnlyKeys(Set<String> keysToKeep) {
        Map<String, String> newMap = new HashMap<>();
        attributesMap.keySet().stream().filter(keysToKeep::contains).forEach(k -> newMap.put(k, attributesMap.get(k)));
        return new Attributes(newMap);
    }

    /**
     * Méthode qui écrit la table d'attribut et qui est appelée lors de la sérialization.
     * @param oos Un flot sortant d'objet
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {

        oos.writeInt(attributesMap.size());

        for (Map.Entry<String, String> attribute : attributesMap.entrySet()) {
            oos.writeUTF(attribute.getKey());
            oos.writeUTF(attribute.getValue());
        }
    }

    /**
     * Méthode qui lit la table d'attributs sérializée, et qui remplace le champ final attributesMap par la version importée
     * @param ois un flot d'objets entrants
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void readObject(ObjectInputStream ois) throws IOException, NoSuchFieldException, IllegalAccessException {

        int length = ois.readInt();
        HashMap<String, String> tmpAttributesMap = new HashMap<>();
        for (int i = 0; i < length; i++)
            tmpAttributesMap.put(ois.readUTF(), ois.readUTF());

        Field f = Attributes.class.getDeclaredField("attributesMap");
        f.setAccessible(true);
        f.set(this, Collections.unmodifiableMap(tmpAttributesMap));

    }


}
