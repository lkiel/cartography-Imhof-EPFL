
package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * {@code public final class EquirectangularProjection implements Projection}
 * <p>
 * Classe héritant de l'interface {@code Projection} implémentant une projection équirectangulaire
 * 
 * @author Clément Nussbaumer	(250261)
 * @author Leandro Kieliger		(246263)
 */
public final class EquirectangularProjection implements Projection{
    @Override
    public Point project(PointGeo point) {
        return new Point(point.longitude(), point.latitude());
    }

    @Override
    public PointGeo inverse(Point point) {
        return new PointGeo(point.x(), point.y());
    }

}
