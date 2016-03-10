package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * {@code public interface Projection}
 * <p>
 * Interface introduisant les méthodes de projection selon les différents
 * systèmes de coordonnées
 *
 * @author Clément Nussbaumer (250261)
 * @author Leandro Kieliger (246263)
 */
public interface Projection {

    /**
     * {@code public Point project(PointGeo point)}
     *
     * @param point - un point en coordonnées géodésiques
     * @return un point en coordonnées CH1903 depuis les coordonnées géodésiques
     */
    Point project(PointGeo point);

    /**
     * {@code public PointGeo inverse(Point point)}
     *
     * @param point - un point en coordonnées CH1903
     * @return un point en coodonnées géodésiques depuis les coordonnées CH1903
     */
    PointGeo inverse(Point point);
}
