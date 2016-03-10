package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;

/**
 * {@code public final class OSMWay extends OSMEntity}
 * <p>
 * Classe immuable héritant de {@code OSMEntity} représentant des chemins
 * OpenStreetMap
 *
 * @author Clément Nussbaumer (250261)
 * @author Leandro Kieliger (246263)
 */
public final class OSMWay extends OSMEntity {

    private final List<OSMNode> nodesList;

    /**
     * {@code public final static class Builder extends OSMEntity.Builder}
     * <p>
     * Bâtisseur de la classe {@code OSMWay}, héritant du bâtisseur d' {@link OSMEntity}
     */
    public final static class Builder extends OSMEntity.Builder {
        private final List<OSMNode> builderNodesList;

        /**
         * {@code public Builder(long id)}
         * <p>
         * Constructeur du Builder
         *
         * @param id - l'identifiant unique du chemin
         */
        public Builder(long id) {
            super(id);
            builderNodesList = new ArrayList<>();
        }

        /**
         * {@code public void addNode(OSMNode newNode)}
         * <p>
         * Ajoute un noeud à la fin de la liste des noeuds du bâtisseur
         *
         * @param newNode - le nouveau noeud à ajouter
         */
        public void addNode(OSMNode newNode) {
            builderNodesList.add(newNode);
        }

        /**
         * {@code public boolean isIncomplete()}
         * <p>
         * retourne un booléen indiquant si l'entité est marquée comme
         * incomplète, ou si la liste de noeuds contient moins de deux éléments.
         */
        @Override
        public boolean isIncomplete() {
            return (super.isIncomplete() || builderNodesList.size() < 2);
        }

        /**
         * {@code public OSMWay build() throws IllegalStateException}
         * <p>
         * Construit un chemin OSM à partir des données du bâtisseur
         *
         * @return le chemin construit
         * @throws IllegalStateException si l'entité est considérée comme incomplète. C'est le cas
         *                               si la méthode {@code setIncomplete} a été appelée
         *                               précédemment sur le bâtisseur ou si le nombre de noeuds
         *                               composant le chemin est inférieur à 2
         */
        public OSMWay build() throws IllegalStateException {

            if (isIncomplete())
                throw new IllegalStateException("Chemin incomplet");

            return new OSMWay(idBuilder, builderNodesList, attributesBuilder.build());
        }
    }

    /**
     * {@code public OSMWay(long id, List<OSMNode> nodes, Attributes attributes)}
     * <p>
     * Constructeur de la classe {@link OSMWay}
     *
     * @param id         - l'identifiant unique de l'entité
     * @param nodes      - une liste de noeuds formant le chemin
     * @param attributes - les attributs associés au chemin
     * @throws IllegalArgumentException si la liste de noeuds comporte moins de deux éléments
     */
    public OSMWay(long id, List<OSMNode> nodes, Attributes attributes) throws IllegalArgumentException {

        super(id, attributes);

        if (nodes.size() < 2)
            throw new IllegalArgumentException("Moins de deux noeuds dans la liste");

        nodesList = Collections.unmodifiableList(new ArrayList<>(nodes));

    }

    /**
     * {@code public int nodesCount()}
     * <p>
     *
     * @return le nombre de noeuds composant le chemin
     */
    public int nodesCount() {
        return nodesList.size();
    }

    /**
     * {@code public List<OSMNode> nodes()}
     * <p>
     *
     * @return la liste de noeuds composant le chemin
     */
    public List<OSMNode> nodes() {
        return nodesList;
    }

    /**
     * {@code public List<OSMNode> nonRepeatingNodes()}
     * <p>
     * Retourne la liste des noeuds du chemin sans le dernier noeud, lorsque
     * celui-ci est identique au premier noeud
     */
    public List<OSMNode> nonRepeatingNodes() {
        return isClosed() ?
                nodesList.subList(0, nodesCount() - 1) :
                nodesList;
    }

    /**
     * {@code public OSMNode firstNode()}
     * <p>
     *
     * @return le premier noeud du chemin
     */
    public OSMNode firstNode() {
        return nodesList.get(0);
    }

    /**
     * {@code public OSMNode lastNode()}
     * <p>
     *
     * @return le dernier noeud du chemin
     */
    public OSMNode lastNode() {
        return nodesList.get(nodesCount() - 1);
    }

    /**
     * {@code public boolean isClosed()}
     * <p>
     *
     * @return Retourne vrai si le chemin est fermé, c.-à-d. si le premier et le
     * dernier noeud sont identiques
     */
    public boolean isClosed() {
        return firstNode().equals(lastNode());
    }
}
