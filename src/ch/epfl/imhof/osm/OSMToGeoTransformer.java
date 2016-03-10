package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.imhof.*;
import ch.epfl.imhof.osm.OSMRelation.Member;
import ch.epfl.imhof.projection.*;
import ch.epfl.imhof.geometry.*;

/**
 * Classe responsable de la conversion de données OSM en carte de type Map.
 *
 * @author Clément Nussbaumer (250261)
 * @author Leandro Kieliger (246263)
 */
public final class OSMToGeoTransformer {
    private final Projection projection;
    private final static Set<String> POLYLINE_ATTRIBUTES = new HashSet<>(
            Arrays.asList("bridge", "highway", "layer", "man_made", "railway", "tunnel", "waterway"));

    private final static Set<String> POLYGON_ATTRIBUTES = new HashSet<>(
            Arrays.asList("building", "landuse", "layer", "leisure", "natural", "waterway"));

    private final static Set<String> AREA_ATTRIBUTES = new HashSet<>(
            Arrays.asList("aeroway", "amenity", "building", "harbour",
                    "historic", "landuse", "leisure", "man_made", "military",
                    "natural", "office", "place", "power", "public_transport",
                    "shop", "sport", "tourism", "water", "waterway", "wetland"));


    /**
     * {@code public OSMToGeoTransformer(Projection projection)}
     * <p>
     * Construit un convertisseur OSMToGeo utilisant la géométrie de la
     * projection
     *
     * @param projection définit la géométrie utilisée par le convertisseur
     */
    public OSMToGeoTransformer(Projection projection) {
        this.projection = projection;
    }

    /**
     * {@code public Map transform(OSMMap map)}
     * <p>
     * Convertit une carte OSMMap en carte géométrique projetée
     *
     * @param map - la carte à convertir
     * @return une carte géométrique projetée
     */
    public Map transform(OSMMap map) {

        Map.Builder mapBuilder = new Map.Builder();
        /*
         Conversion des chemins OSM en PolyLignes ou en PolyGones
         (ouverts/fermés)
         */
        for (OSMWay currentWay : map.ways()) {

            Attributes filteredAttributes;

            // Si le chemin est fermé et qu'il décrit une surface -> polygone sans trou
            if (currentWay.isClosed() && isAnArea(currentWay.attributes())) {
                filteredAttributes = currentWay.attributes().keepOnlyKeys(POLYGON_ATTRIBUTES);
                if (!filteredAttributes.isEmpty()) {
                    // Convertit le chemin en polyligne fermée et le passe en param. à un nouv. polygone
                    mapBuilder.addPolygon(new Attributed<>(
                            new Polygon((ClosedPolyLine) convertOSMWay(currentWay)), filteredAttributes));
                }
            }
            // Sinon -> polyligne ouverte ou fermée en fonction du type de chemin
            else {
                filteredAttributes = currentWay.attributes().keepOnlyKeys(POLYLINE_ATTRIBUTES);
                if (!filteredAttributes.isEmpty()) {
                    // Convertit le chemin en polyligne
                    mapBuilder.addPolyLine(new Attributed<>(
                            convertOSMWay(currentWay), filteredAttributes));
                }
            }
        }

        /*
         Conversion des relations OSM en Polygones attribués
         */
        for (OSMRelation currentRelation : map.relations()) {

            // On ne garde que les relations décrivant des multipolygones
            if (!currentRelation.hasAttribute("type") || !currentRelation.attributeValue("type").equals("multipolygon"))
                continue;

            Attributes filteredAttributes = currentRelation.attributes().keepOnlyKeys(POLYGON_ATTRIBUTES);

            if (!filteredAttributes.isEmpty())
                assemblePolygon(currentRelation, filteredAttributes).forEach(mapBuilder::addPolygon);
        }

        return mapBuilder.build();
    }

    /**
     * {@code private boolean isAnArea(Attributes a)}
     * <p>
     * Méthode vérifiant si les attributs d'une entité font qu'elle est considérée comme une surface
     *
     * @param a - les attributs caractérisan une entité
     * @return vrai si et seulement si les attributs font que l'entité peut être considérée comme une surface
     */
    private boolean isAnArea(Attributes a) {
        if (a.contains("area")) {
            switch (a.get("area")) {
                case "yes":
                    return true;
                case "1":
                    return true;
                case "true":
                    return true;
                default:
                    return false;
            }
        } else if (!a.keepOnlyKeys(AREA_ATTRIBUTES).isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * {@code private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role)}
     * <p>
     * Calcule et retourne l'ensemble des anneaux de la relation donnée ayant le
     * rôle spécifié
     *
     * @param relation - la relation à partir de laquelle la méthode calcule
     *                 l'ensemble des anneaux
     * @param role     - seuls les anneaux ayant le rôle spécifié seront retournés
     * @return Une {@code List<ClosedPolyLine>} représentant l'ensemble des
     * anneaux de la relation ou une {@code EmptyList} si le calcul
     * échoue
     */
    private List<ClosedPolyLine> ringsForRole(OSMRelation relation, String role) {

        List<OSMWay> filteredMemberList = new ArrayList<>();

        for (Member m : relation.members())
            if (m.type() == Member.Type.WAY && m.role().equals(role))
                filteredMemberList.add((OSMWay) m.member());

        return getClosedPolyLinesFromGraph(getGraphFromWays(filteredMemberList));
    }

    /**
     * {@code private List<ClosedPolyLine> getClosedPolyLinesFromGraph(Graph<OSMNode> g)}
     * <p>
     * Construit une liste de polylignes fermées à partir d'un graphe non
     * orienté donné
     *
     * @param g - un graphe non orienté
     * @return une liste de polylignes fermées
     */
    private List<ClosedPolyLine> getClosedPolyLinesFromGraph(Graph<OSMNode> g) {

        List<ClosedPolyLine> listOfClosedPolyLines = new ArrayList<>();
        Set<OSMNode> nodesToVisit = new HashSet<>(g.nodes());
        Set<OSMNode> setOfNeighbors;
        PolyLine.Builder polylineBuilder;
        OSMNode currentNode;

        while (nodesToVisit.iterator().hasNext()) {

            polylineBuilder = new PolyLine.Builder();
            // On récupère un premier noeud
            currentNode = nodesToVisit.iterator().next();
            polylineBuilder.addPoint(projection.project(currentNode.position()));
            nodesToVisit.remove(currentNode);

            /*
            Après avoir ajouté le noeud à la polyline en cours de construction, on parcourt les noeuds voisins
            de currentNode n'ayant pas encore été visités, on les ajoute à la polyligne et on les
            supprime alors des noeuds à visiter
             */
            do {
                setOfNeighbors = new HashSet<>(g.neighborsOf(currentNode));
                // On retourne une liste vide si le graphe n'est pas valide pour l'algorithme
                if (setOfNeighbors.size() != 2)
                    return Collections.emptyList();

                // On ne garde que les nodes voisins non visités
                setOfNeighbors.retainAll(nodesToVisit);
                if (setOfNeighbors.iterator().hasNext()) {
                    currentNode = setOfNeighbors.iterator().next();
                    polylineBuilder.addPoint(projection.project(currentNode.position()));
                    nodesToVisit.remove(currentNode);
                }
            } while (setOfNeighbors.iterator().hasNext());

            // Plus de noeuds à visiter sur cet anneau -> on le construit et on passe au suivant
            listOfClosedPolyLines.add(polylineBuilder.buildClosed());
        }
        return listOfClosedPolyLines;
    }

    /**
     * {@code private Graph<OSMNode> getGraphFromWays(List<OSMWay> waysList)}
     * <p>
     * Construit un graphe d'OSMNodes non orienté à partir d'une liste de
     * chemins
     *
     * @param waysList - la liste des chemins utilisés pour construire le graphe
     * @return un graphe non orienté d'OSMNodes
     */
    private Graph<OSMNode> getGraphFromWays(List<OSMWay> waysList) {
        Graph.Builder<OSMNode> graphBuilder = new Graph.Builder<>();

        // On ajoute à chaque node d'index > 0 une arrête la reliant à son voisin
        for (OSMWay w : waysList) {
            int index = -1;
            List<OSMNode> listOfNodes = w.nodes();
            for (OSMNode n : listOfNodes) {
                graphBuilder.addNode(n);
                if (++index > 0)
                    graphBuilder.addEdge(n, listOfNodes.get(index - 1));
            }
        }
        return graphBuilder.build();
    }

    /**
     * {@code private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes)}
     * <p>
     * Calcule et retourne la liste des polygones de la relation donnée, en leur
     * attachant les attributs donnés.
     *
     * @param relation   - la reation à inspecter
     * @param attributes - les attributs à attacher aux anneaux extérieurs
     * @return - une liste de Polygones attribués
     */
    private List<Attributed<Polygon>> assemblePolygon(OSMRelation relation, Attributes attributes) {

        List<Attributed<Polygon>> listOfAttributedPolygons = new ArrayList<>();
        List<ClosedPolyLine> remainingInnerRings = ringsForRole(relation, "inner");
        List<ClosedPolyLine> outerRings = ringsForRole(relation, "outer");
        List<ClosedPolyLine> holes;

        outerRings.sort(Comparator.comparing(ClosedPolyLine::area));

        for (ClosedPolyLine outerCPL : outerRings) {
            holes = new ArrayList<>();

            // On ajoute chaque anneau intérieur contenu dans l'anneau extérieur
            for (ClosedPolyLine innerCPL : remainingInnerRings)
                if (outerCPL.containsPoint(innerCPL.firstPoint()))
                    holes.add(innerCPL);

            if (holes.isEmpty()) {
                // On ajoute un polygone sans trou
                listOfAttributedPolygons.add(
                        new Attributed<>(new Polygon(outerCPL), attributes));
            } else {
                // On ajoute un polygone avec trou(s)
                listOfAttributedPolygons.add(
                        new Attributed<>(new Polygon(outerCPL, holes), attributes));
                remainingInnerRings.removeAll(holes);
            }
        }
        return listOfAttributedPolygons;
    }

    /**
     * {@code private PolyLine convertOSMWay(OSMWay osmWay)}
     * <p>
     * Convertit un chemin OSM en Polyligne ouverte ou fermée
     *
     * @param osmWay - le OSMWay à convertir
     * @return - un objet ClosedPolyLine ou OpenPolyLine
     */
    private PolyLine convertOSMWay(OSMWay osmWay) {

        PolyLine.Builder polyLineBuilder = new PolyLine.Builder();

        for (OSMNode n : osmWay.nonRepeatingNodes())
            polyLineBuilder.addPoint(projection.project(n.position()));

        return osmWay.isClosed() ? polyLineBuilder.buildClosed() : polyLineBuilder.buildOpen();
    }
}
