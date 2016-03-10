package ch.epfl.imhof.projection;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.geometry.Point;

/**
 * {@code public final class CH1903Projection implements Projection}
 * <p>
 * Classe héritant de l'interface {@code Projection} implémentant une projection équirectangulaire
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public final class CH1903Projection implements Projection {

    @Override
    public Point project(PointGeo point) {
        //Conversion  des angles en degrés pour l'utilisation de la formule
        double lambda = Math.toDegrees(point.longitude());
        double phi = Math.toDegrees(point.latitude());

        double lambda1 = (1 / 10000d) * ((lambda * 3600) - 26782.5);
        double phi1 = (1 / 10000d) * ((phi * 3600) - 169028.66);

        double lambda1SQ = lambda1 * lambda1;
        double phi1SQ = phi1 * phi1;

        double x = 600072.37
                + (211455.93 * lambda1)
                - (10938.51 * lambda1 * phi1)
                - (0.36 * lambda1 * phi1SQ)
                - (44.54 * lambda1SQ * lambda1);

        double y = 200147.07
                + (308807.95 * phi1)
                + (3745.25 * lambda1SQ)
                + (76.63 * phi1SQ)
                - (194.56 * lambda1SQ * phi1)
                + 119.79 * phi1SQ * phi1;

        return new Point(x, y);
    }

    @Override
    public PointGeo inverse(Point point) {
        double x = point.x();
        double y = point.y();

        double x1 = (x - 600000d) / (1000000d);
        double y1 = (y - 200000d) / (1000000d);

        double x1SQ = x1*x1;
        double y1SQ = y1*y1;

        double lambda0 = 2.6779094
                + (4.728982 * x1)
                + (0.791484 * x1 * y1)
                + (0.1306 * x1 * y1SQ)
                - (0.0436 * x1SQ * x1);

        double phi0 = 16.9023892
                + (3.238272 * y1)
                - (0.270978 * x1SQ)
                - (0.002528 * y1SQ)
                - (0.0447 * x1SQ * y1)
                - (0.0140 * y1SQ * y1);

        double lambda = Math.toRadians(lambda0 * (100 / 36d));
        double phi = Math.toRadians(phi0 * (100 / 36d));

        return new PointGeo(lambda, phi);
    }

}
