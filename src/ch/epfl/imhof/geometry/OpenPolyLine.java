package ch.epfl.imhof.geometry;

import java.util.List;

/**
 * {@code public final class OpenPolyLine extends PolyLine}
 * <p>
 * Classe héritant de {@code PolyLine} implémentant une polyligne ouverte
 *
 * @author Clément Nussbaumer (250261)
 * @author Leandro Kieliger (246263)
 */
public final class OpenPolyLine extends PolyLine {
    private static final long serialVersionUID = 2015052700L;


    /**
     * Classe construisant une polyligne ouverte à partir d'une liste de
     * sommets fournie
     *
     * @param points - une liste de sommets composant la polyligne
     * @throws IllegalArgumentException si la liste de sommets est vide
     */
    public OpenPolyLine(List<Point> points) throws IllegalArgumentException {
        super(points);
    }

    @Override
    public boolean isClosed() {
        return false;
    }

}
