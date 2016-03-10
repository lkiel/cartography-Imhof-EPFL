package ch.epfl.imhof.osm;

import ch.epfl.imhof.Attributes;
import ch.epfl.imhof.PointGeo;

/**
 * {@code public final class OSMNode extends OSMEntity}
 * <p>
 * Classe immuable et héritant de {@code OSMEntity} représentant les noeuds
 * OpenStreetMap
 *
 * @author Clément Nussbaumer (250261)
 * @author Leandro Kieliger (246263)
 */
public final class OSMNode extends OSMEntity {

    private final PointGeo pos;

    /**
     * {@code public final static class Builder extends OSMEntity.Builder}
     * <p>
     * Bâtisseur de la classe {@code OSMNode} héritant du bâtisseur d'
     * {@link OSMEntity}
     */
    public final static class Builder extends OSMEntity.Builder {
        private final PointGeo builderPosition;

        /**
         * {@code public Builder(long id, PointGeo position)}
         * <p>
         * Constructeur du bâtisseur
         *
         * @param id       - l'identifiant unique du noeud
         * @param position - la position du noeud
         */
        public Builder(long id, PointGeo position) {
            super(id);
            builderPosition = position;
        }

        /**
         * {@code public OSMNode build() throws IllegalStateException}
         * <p>
         * Construit un noeud OSM à partir des données du bâtisseur
         *
         * @return une noeud {@link OSMNode}
         * @throws IllegalStateException si l'entité est marquée comme étant incomplète. C'est le
         *                               cas si la méthode {@code setIncomplete()} a été appelée
         *                               précédemment sur l'objet.
         */
        public OSMNode build() throws IllegalStateException {
            if (isIncomplete())
                throw new IllegalStateException("Builder marqué comme incomplet");

            return new OSMNode(idBuilder, builderPosition, attributesBuilder.build());
        }
    }

    /**
     * {@code public OSMNode(long id, PointGeo position, Attributes attributes)}
     * <p>
     * Constructeur de la classe {@link OSMNode}, créant un noeud à partir d'un
     * identifiant unique, de sa position et de ses attributs
     *
     * @param id         - l'identifiant du noeud
     * @param position   - la position du noeud
     * @param attributes - les {@link Attributes} associés au noeud
     */
    public OSMNode(long id, PointGeo position, Attributes attributes) {
        super(id, attributes);
        pos = position;
    }

    /**
     * {@code public PointGeo position()}
     * <p>
     * Méthode retournant la position du noeud (un pointGeo)
     * @return la position du noeud
     */
    public PointGeo position() {
        return pos;
    }

}
