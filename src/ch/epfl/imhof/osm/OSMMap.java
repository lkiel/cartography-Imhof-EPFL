package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@code public final class OSMMap}
 * <p>
 * Classe représentant une carte OpenStreetMaps, autrement dit un ensemble de
 * chemins et de relations
 *
 * @author Clément Nussbaumer (250261)
 * @author Leandro Kieliger (246263)
 */
public final class OSMMap {

    private final List<OSMWay> waysList;
    private final List<OSMRelation> relsList;

    /**
     * {@code public final static class Builder}
     * <p>
     * Bâtisseur de la classe OSMMap
     */
    public final static class Builder {

        private final Map<Long, OSMNode> builderNodesMap;
        private final Map<Long, OSMWay> builderWaysMap;
        private final Map<Long, OSMRelation> builderRelsMap;

        /**
         * {@code public Builder()}
         * <p>
         * Constructeur du bâtisseur de la classe OSMMap
         */
        public Builder() {
            builderNodesMap = new HashMap<>();
            builderWaysMap = new HashMap<>();
            builderRelsMap = new HashMap<>();
        }

        /**
         * {@code public void addNode(OSMNode newNode)}
         * <p>
         * Ajoute un {@link OSMNode} au bâtisseur et le stocke dans une
         * {@link HashMap} pour permettre par la suite un accès rapide donné
         * l'id.
         *
         * @param newNode - Le noeud à ajouter
         */
        public void addNode(OSMNode newNode) {
            builderNodesMap.put(newNode.id(), newNode);
        }

        /**
         * {@code public OSMNode nodeForId(long id)}
         * <p>
         *
         * @param id - l'identifiant unique OSM
         * @return Le noeud correspondant à l'id passé en paramètre ou null si
         * l'id n'est pas trouvé
         */
        public OSMNode nodeForId(long id) {
            return builderNodesMap.get(id);
        }

        /**
         * {@code public void addWay(OSMWay newWay)}
         * <p>
         * Ajoute un {@link OSMWay} au bâtisseur et le stocke dans une
         * {@link HashMap} pour permettre par la suite un accès rapide donné
         * l'id
         *
         * @param newWay - Le chemin à ajouter
         */
        public void addWay(OSMWay newWay) {
            builderWaysMap.put(newWay.id(), newWay);
        }

        /**
         * {@code public OSMWay wayForId(long id)}
         * <p>
         * Retourne le chemin associé à l'id passé en argument
         * @param id - l'identifiant unique OSM
         * @return le chemin correspondant à l'id passé en paramètre ou null si
         * l'id n'est pas trouvé
         */
        public OSMWay wayForId(long id) {
            return builderWaysMap.get(id);
        }

        /**
         * {@code public void addRelation(OSMRelation newRelation)}
         * <p>
         * Ajoute une {@link OSMRelation} au bâtisseur et la stocke dans une
         * {@link HashMap} pour permettre par la suite un accès rapide donné
         * l'id
         *
         * @param newRelation - La relation à ajouter
         */
        public void addRelation(OSMRelation newRelation) {
            builderRelsMap.put(newRelation.id(), newRelation);
        }

        /**
         * {@code public OSMRelation relationForId(long id)}
         * <p>
         *
         * @param id - l'identifiant unique OSM
         * @return la relation correspondante à l'id passé en paramètre ou null
         * si l'id n'est pas trouvé
         */
        public OSMRelation relationForId(long id) {
            return builderRelsMap.get(id);
        }

        /**
         * {@code public OSMMap build()}
         * <p>
         * Construit une {@code OSMMap} à partir des données du bâtisseur
         *
         * @return un objet de type {@link OSMMap}
         */
        public OSMMap build() {
            return new OSMMap(builderWaysMap.values(), builderRelsMap.values());
        }
    }

    /**
     * {@code public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations)}
     * <p>
     * Constructeur de la classe OSMMap
     *
     * @param ways      - La liste des chemins constituant l'entité, passé sous forme
     *                  d'une {@link Collection}
     * @param relations - La liste des relations constituant l'entité, passé sour la
     *                  forme d'une {@link Collection}
     */
    public OSMMap(Collection<OSMWay> ways, Collection<OSMRelation> relations) {
        waysList = Collections.unmodifiableList(new ArrayList<>(ways));
        relsList = Collections.unmodifiableList(new ArrayList<>(relations));
    }

    /**
     * {@code public List<OSMWay> ways()}
     * <p>
     *
     * @return la liste des chemins de la carte
     */
    public List<OSMWay> ways() {
        return waysList;
    }

    /**
     * {@code public List<OSMRelation> relations()}
     * <p>
     *
     * @return la liste des relations de la carte
     */
    public List<OSMRelation> relations() {
        return relsList;
    }
}
