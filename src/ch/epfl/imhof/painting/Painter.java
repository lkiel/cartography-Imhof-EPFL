package ch.epfl.imhof.painting;

import ch.epfl.imhof.Attributed;
import ch.epfl.imhof.Map;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;
import ch.epfl.imhof.painting.LineStyle.*;

import java.util.function.Predicate;

/**
 * Interface fonctionnelle modélisant le concept de peintres en Java
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public interface Painter {

    /**
     * Méthode abstraite prenant une carte et une toile en argument; elle dessine la carte sur la toile.
     *
     * @param m la carte
     * @param c la toile
     */
    void drawMap(Map m, Canvas c);

    /**
     * Méthode retournant un peintre dessinant tous les polygones de la carte qu'il reçoit
     * dans la couleur passée en argument
     *
     * @param c la couleur des polygones
     * @return le peintre de polygones
     */
    static Painter polygon(Color c) {
        return (m, cvs) -> {
            for (Attributed<Polygon> attrP : m.polygons())
                cvs.drawPolygon(attrP.value(), c);
        };
    }

    /**
     * Méthode retournant un peintre dessinant les pourtours de l'enveloppe et des trous des polygones
     * de la carte qu'elle reçoit, selon le style de ligne passé dans les arguments
     *
     * @param lineWidth      largeur de la ligne
     * @param lineColor      couleur de la ligne
     * @param lineCap        terminaison des lignes
     * @param lineJoin       jointure des lignes
     * @param dashingPattern schéma de traitillé des lignes
     * @return le peintre de lignes "extérieures"
     */
    static Painter outline(float lineWidth, Color lineColor, LineCapStyle lineCap, LineJoinStyle lineJoin, float... dashingPattern) {
        return Painter.outline(new LineStyle(lineWidth, lineColor, lineCap, lineJoin, dashingPattern));
    }

    /**
     * Méthode retournant un peintre dessinant les pourtours de l'enveloppe et des trous des polygones
     * de la carte qu'elle reçoit, selon la largeur et la couleur de ligne passés en argument,
     * les autres paramètre étant fixés par défaut
     *
     * @param lineWidth largeur de la ligne
     * @param lineColor couleur de la ligne
     * @return le peintre de lignes "extérieures"
     */
    static Painter outline(float lineWidth, Color lineColor) {
        return Painter.outline(new LineStyle(lineWidth, lineColor));
    }

    /**
     * Méthode retournant un peintre dessinant les pourtours de l'enveloppe et des trous des polygones
     * de la carte qu'elle reçoit, selon le LineStyle passé en argument
     *
     * @param s le LineStyle
     * @return la painter
     */
    static Painter outline(LineStyle s) {
        return (m, cvs) -> {
            for (Attributed<Polygon> attrP : m.polygons()) {
                cvs.drawPolyLine(attrP.value().shell(), s);
                for (PolyLine p : attrP.value().holes())
                    cvs.drawPolyLine(p, s);
            }
        };
    }

    /**
     * Méthode retournant un peintre dessinant les lignes/traits des polylignes
     * de la carte qu'elle reçoit, selon le style de ligne passé dans les arguments
     *
     * @param lineWidth      largeur de la ligne
     * @param lineColor      couleur de la ligne
     * @param lineCap        terminaison de la ligne
     * @param lineJoin       jointure de la ligne
     * @param dashingPattern schéma de traitillés
     * @return le peintre de polyligne
     */
    static Painter line(float lineWidth, Color lineColor, LineCapStyle lineCap, LineJoinStyle lineJoin, float... dashingPattern) {
        return Painter.line(new LineStyle(lineWidth, lineColor, lineCap, lineJoin, dashingPattern));
    }

    /**
     * Méthode retournant un peintre dessinant les lignes/traits des polylignes
     * de la carte qu'elle reçoit, selon la largeur et la couleur de ligne passés en argument,
     * les autres paramètre étant fixés par défaut
     *
     * @param lineWidth largeur de la ligne
     * @param lineColor couleur de la ligne
     * @return le peintre de polygones
     */
    static Painter line(float lineWidth, Color lineColor) {
        return Painter.line(new LineStyle(lineWidth, lineColor));
    }

    /**
     * Méthode retournant un peintre dessinant les pourtours de l'enveloppe et des trous des polygones
     * de la carte qu'elle reçoit, selon le LineStyle passé en argument
     *
     * @param s le LineStyle
     * @return le peintre associé
     */
    static Painter line(LineStyle s) {
        return (m, cvs) -> {
            for (Attributed<PolyLine> attrPL : m.polyLines())
                cvs.drawPolyLine(attrPL.value(), s);
        };
    }

    /**
     * Méthode par défaut retournant un peintre qui ne peint que les entités satisfaisants à la condition
     * passée en argument, condition de type : {@code Predicate<Attributed<?>>}
     *
     * @param predicate la condition
     * @return le nouveau peintre
     */
    default Painter when(Predicate<Attributed<?>> predicate) {
        return (m, cvs) -> {
            Map.Builder mapB = new Map.Builder();
            m.polygons().stream().filter(predicate).forEach(mapB::addPolygon);
            m.polyLines().stream().filter(predicate).forEach(mapB::addPolyLine);
            drawMap(mapB.build(), cvs);
        };
    }

    /**
     * Méthode par défaut retournant un peintre qui dessine d'abord le peintre passé en argument,
     * puis le peintre auquel on applique cette méthode
     *
     * @param that la peintre dessiné "sous" le peintre auquel on applique cette méthode
     * @return le nouveau peintre
     */
    default Painter above(Painter that) {
        return (m, cvs) -> {
            that.drawMap(m, cvs);
            this.drawMap(m, cvs);
        };
    }

    /**
     * Méthode par défaut retournant un peintre qui dessine couche par couche la carte sur le canvas
     *
     * @return le peintre "à étages"
     */
    default Painter layered() {
        Painter p = when(Filters.onLayer(-5));
        for (int i = -4; i <= 5; i++)
            p = when(Filters.onLayer(i)).above(p);

        return p;
    }
}
