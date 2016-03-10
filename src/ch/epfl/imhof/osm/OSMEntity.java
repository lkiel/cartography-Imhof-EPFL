package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;

/**
 * {@code public abstract class OSMEntity}
 * <p>
 * Classe mère de toutes les classes représentant les entités OSM
 *
 * @author Clément Nussbaumer (250261)
 * @author Leandro Kieliger (246263)
 */
public abstract class OSMEntity {

    private final long id;
    private final Attributes attributes;

    /**
     * {@code public abstract static class Builder}
     * <p>
     * Bâtisseur abstrait de la classe OSMEntity
     */
    public abstract static class Builder {
        
        protected final long idBuilder;
        protected final Attributes.Builder attributesBuilder;
        private boolean isIncomplete;

        /**
         * {@code public Builder(long id)}
         * <p>
         * Constructeur du bâtisseur
         *
         * @param id - l'identifiant unique OSM de l'entité.
         */
        public Builder(long id) {
            isIncomplete = false;
            idBuilder = id;
            attributesBuilder = new Attributes.Builder();
        }

        /**
         * {@code public void setAttribute(String key, String value)}
         * <p>
         * Ajoute l'association clé-valeur des paramètres donnés à l'ensemble
         * des {@link Attributes} de l'entité en cours de construction. Si un
         * attribut de même nom a déjà été ajouté auparavant, sa valeur est
         * remplacée par la nouvelle
         *
         * @param key   - l'attribut
         * @param value - la valeur à associer à l'attribut
         */
        public void setAttribute(String key, String value) {
            attributesBuilder.put(key, value);
        }

        /**
         * {@code public void setIncomplete()}
         * <p>
         * Si cette méthode est appelée, l'entité est alors définie comme
         * incomplète
         */
        public void setIncomplete() {
            isIncomplete = true;
        }

        /**
         * {@code public boolean isIncomplete()}
         * <p>
         *
         * @return un booléen indiquant si l'entité est incomplète
         */
        public boolean isIncomplete() {
            return isIncomplete;
        }
    }

    /**
     * {@code public OSMEntity(long id, Attributes attributes)}
     * <p>
     * Constructeur de la classe OSMEntity
     *
     * @param id         - l'identifiant unique OSM de l'entité
     * @param attributes - la liste des attributs qui caractérisent l'entité
     */
    public OSMEntity(long id, Attributes attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    /**
     * {@code public long id()}
     * <p>
     *
     * @return l'identifiant unique OSM de l'entité
     */
    public long id() {
        return id;
    }

    /**
     * {@code public Attributes attributes()}
     * <p>
     *
     * @return l'ensemble des attributs qui caractérisent l'entité
     */
    public Attributes attributes() {
        return attributes;
    }

    /**
     * {@code public boolean hasAttribute(String key)}
     * <p>
     * Teste si l'entité possède l'attribut passé en argument
     *
     * @param key - l'attribut à tester
     * @return un booléen indiquant le résultat du test
     */
    public boolean hasAttribute(String key) {
        return attributes.contains(key);
    }

    /**
     * {@code public String attributeValue(String key)}
     * <p>
     * Retourne la valeur de l'attribut donné en paramètre
     *
     * @param key - l'attribut dont on cherche la valeur
     * @return la valeur associée ou null si l'attribut n'existe pas dans la
     * liste des attributs de l'entité
     */
    public String attributeValue(String key) {
        return attributes.get(key);
    }
}
