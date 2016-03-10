package ch.epfl.imhof.geometry;

import java.io.Serializable;
import java.util.function.Function;

/**
 * {@code public final class Point}
 * <p>
 * Classe implémentant la notion de point géométrique à la surface de la terre,
 * caractérisé par ses coordonnées, selon le système CH1903
 *
 * @author Clément Nussbaumer (250621)
 * @author Leandro Kieliger (246263)
 */
public final class Point implements Serializable {

    private static final long serialVersionUID = 2015052700L;
    private final double x;
    private final double y;

    /**
     * {@code public Point(double x, double y)}
     * <p>
     * Classe construisant un point géométrique caractérisé par ses coordonnées
     * x et y passées en paramètre selon la convention CH1903
     *
     * @param x - la coordonnée selon l'axe horizontal, en mètres et selon la
     *          norme CH1903
     * @param y - la coordonnée selon l'axe horizontal, en mètres et selon la
     *          norme CH1903
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * {@code public double x()}
     * <p>
     * Méthode retournant la valeur de la coordonnée horizontale
     *
     * @return - retourne la valeur de la coordonnée horizontale
     */
    public double x() {
        return x;
    }

    /**
     * {@code public double y()}
     * <p>
     * Méthode reoturnant la valeur de la coordonnée verticale
     *
     * @return - retourne la valeur de la coordonnée verticale
     */
    public double y() {
        return y;
    }

    /**
     * {@code public static Function<Point, Point> alignedCoordinateChange(Point b1, Point r1, Point b2, Point r2)}
     * <p>
     *
     * Cette méthode calcule à partir de deux paires de points données le changement de coordonnées correspondant
     *
     * @param b1 - le premier Point dans le système de coordonnées de départ
     * @param r1 - le premier Point dans le système de coordonnées d' arrivée
     * @param b2 - le deuxième Point dans le système de coordonnées de départ
     * @param r2 - le deuxième Point dans le système de coordonnées d'arrivée
     * @return Une expression lambda de type {@link Function} représentant le changement de coordonnées.
     * Cette fonction prend un Point supposé être contenu dans le système de coordonnées de départ et retourne
     * le Point correspondant dans le système d'arrivée
     * @throws IllegalArgumentException - Dans le cas où les deux points se situent sur une même ligne verticale
     * ou horizontale, rendant le calcul de la transformation impossible
     */
    public static Function<Point, Point> alignedCoordinateChange(Point b1, Point r1, Point b2, Point r2){

        //On profite pour tester les 4 points dans le cas où les coordonnées selon le deuxième repère sont fausses également
        if(b1.x == b2.x || b1.y == b2.y ||  r1.x == r2.x || r1.y == r2.y)
            throw new IllegalArgumentException("Impossible de calculer le changement de coordonnées," +
                " les points sont situés sur une même ligne horizontale ou verticale.");

        double dilatationX = (r2.x - r1.x) / (b2.x - b1.x);
        double dilatationY = (r2.y - r1.y) / (b2.y - b1.y);
        double translationX = (r1.x - (dilatationX * b1.x));
        double translationY = (r1.y - (dilatationY * b1.y));

        return p -> new Point(p.x * dilatationX + translationX, p.y * dilatationY + translationY);
    }

}