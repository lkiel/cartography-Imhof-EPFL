package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * {@code public final class ClosedPolyLine extends PolyLine}
 * <p>
 * Classe héritant de {@code PolyLine} implémentant une polyligne fermée, dans
 * laquelle le premier et le dernier noeud sont identiques
 *
 * @author Clément Nussbaumer (250261)
 * @author Leandro Kieliger (246263)
 */
public final class ClosedPolyLine extends PolyLine {
    private static final long serialVersionUID = 2015052700L;


    /**
     * {@code public ClosedPolyLine(List<Point> points) throws IllegalArgumentException}
     * <p>
     * Crée une polyligne à partir d'une liste de sommets fournie
     *
     * @param points - une liste de sommets constituant la polyligne
     * @throws IllegalArgumentException - si la liste des sommets est vide
     */
    public ClosedPolyLine(List<Point> points) throws IllegalArgumentException {
        super(points);
    }

    @Override
    public boolean isClosed() {
        return true;
    }

    /**
     * {@code public double area()}
     * <p>
     * Méthode calculant l'aire non signée de la polyligne
     *
     * @return - aire non signée de la polyligne
     */
    public double area() {
        double sum = 0;
        for (int i = 0; i < points().size(); i++)
            sum += getPtGen(i).x() * (getPtGen(i + 1).y() - getPtGen(i - 1).y());

        return Math.abs(sum / 2d);
    }

    /**
     * {@code public boolean containsPoint(Point p)}
     * <p>
     * Méthode déterminant si un point est contenu dans la polyligne fermée ou
     * non.
     *
     * @param p - le point qui va être testé
     * @return - <code>true</code> si et seulement si la polyligne contient le
     * point en question
     */
    public boolean containsPoint(Point p) {
        int index = 0;
        for (int i = 0; i < points().size(); i++) {
            if (getPtGen(i).y() <= p.y()) {
                if (getPtGen(i + 1).y() > p.y() && isAtLeft(getPtGen(i), getPtGen(i + 1), p))
                    index++;
            } else {
                if (getPtGen(i + 1).y() <= p.y() && isAtLeft(getPtGen(i + 1), getPtGen(i), p))
                    index--;
            }
        }
        return (index != 0);
    }

    /**
     * {@code private boolean isAtLeft(Point a, Point b, Point p)}
     * <p>
     * Méthode déterminant si un point p est à gauche d'un segment créé par les
     * points a et b
     *
     * @param a - point de départ du segment
     * @param b - point d'arrivée du segment
     * @param p - point qui va être testé
     * @return <code>true</code> si et seulement si le point p est à gauche du
     * segment créé par les points a et b
     */
    private boolean isAtLeft(Point a, Point b, Point p) {
        return (a.x() - p.x()) * (b.y() - p.y()) > (b.x() - p.x()) * (a.y() - p.y());
    }

    /**
     * {@code private Point getPtGen(int indexGen)}
     * <p>
     * Méthode retournant un point de la liste à partir d'un index généralisé
     *
     * @param indexGen - l'index généralisé utilisé pour retourner le sommet d'indice
     *                 valide
     * @return - un {@link Point} étant un sommet de la polyligne fermée
     */
    private Point getPtGen(int indexGen) {
        return points().get(Math.floorMod(indexGen, points().size()));
    }

}
