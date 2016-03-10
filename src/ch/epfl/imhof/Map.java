package ch.epfl.imhof;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * {@code public final class Map}
 * <p>
 * Classe immuable représentant une carte projetée, composée d'entités
 * géométriques attribuées. (A ne pas confondre avec l'interface map de la
 * bibliothèque Java)
 *
 * @author Clément Nussbaumer (250261)
 * @author Leandro Kieliger (246263)
 */
public final class Map implements Serializable{

    private static final long serialVersionUID = 2015052700L;
    private final List<Attributed<PolyLine>> mapPolyLines;
    private final List<Attributed<Polygon>> mapPolygons;

    /**
     * Bâtisseur public et imbriqué statiquement de la classe Map
     */
    public final static class Builder {

        private final List<Attributed<PolyLine>> builderPolyLines;
        private final List<Attributed<Polygon>> builderPolygons;

        /**
         * Constructeur du bâtisseur
         */
        public Builder() {
            builderPolyLines = new ArrayList<>();
            builderPolygons = new ArrayList<>();
        }

        /**
         * {@code public void addPolyLine(Attributed<PolyLine> newPolyLine)}
         * <p>
         * Ajoute une {@link PolyLine} attribuée, à la liste des polylignes qui
         * composeront l'objet {@link Map}
         *
         * @param newPolyLine - la nouvelle polyligne attribuée, à ajouter à la liste du
         *                    bâtisseur
         */
        public void addPolyLine(Attributed<PolyLine> newPolyLine) {
            builderPolyLines.add(newPolyLine);
        }

        /**
         * {@code public void addPolygon(Attributed<Polygon> newPolyGon)}
         * <p>
         * Ajoute un {@link Polygon} attribué, à la liste des polygones qui
         * composeront l'objet Map
         *
         * @param newPolygon - le nouveau polygone attribué, à ajouter à la liste du
         *                   bâtisseur
         */
        public void addPolygon(Attributed<Polygon> newPolygon) {
            builderPolygons.add(newPolygon);
        }

        /**
         * {@code public Map build()}
         * <p>
         *
         * @return une {@link Map} construite à partir des données du bâtisseur.
         */
        public Map build() {
            return new Map(builderPolyLines, builderPolygons);
        }
    }

    /**
     * {@code public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons)}
     * <p>
     * Constructeur de la classe Map
     *
     * @param polyLines - la liste des polylignes attribuées constituant la carte
     * @param polygons  - la liste des polygones attribués constituant la carte
     */
    public Map(List<Attributed<PolyLine>> polyLines, List<Attributed<Polygon>> polygons) {
        mapPolyLines = Collections.unmodifiableList(new ArrayList<>(polyLines));
        mapPolygons = Collections.unmodifiableList(new ArrayList<>(polygons));
    }

    /**
     * {@code public List<Attributed<PolyLine>> polyLines()}
     * <p>
     *
     * @return - la liste des polylignes de la carte
     */
    public List<Attributed<PolyLine>> polyLines() {
        return mapPolyLines;
    }

    /**
     * {@code public List<Attributed<Polygon>> polygons()}
     * <p>
     *
     * @return - la liste des polygones de la carte
     */
    public List<Attributed<Polygon>> polygons() {
        return mapPolygons;
    }
}
