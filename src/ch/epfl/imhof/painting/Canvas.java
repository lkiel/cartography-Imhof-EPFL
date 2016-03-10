package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

/**
 * {@code public interface Canvas}
 * <p>
 * Interface représentant une toile
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public interface Canvas {

    /**
     * {@code public void drawPolyLine(PolyLine p, LineStyle s)}
     * <p>
     * Dessine sur la toile la polyligne donnée selon le style spécifié
     *
     * @param p - la polyligne à dessiner
     * @param s - le style de la polyligne
     */
    void drawPolyLine(PolyLine p, LineStyle s);

    /**
     * {@code public void drawPolygon(Polygon p, Color c)}
     * <p>
     * Dessine sur la toile le polygone donné selon la couleur spécifiée
     *
     * @param p - le polygone à dessiner
     * @param c - la couleur de remplissage
     */
    void drawPolygon(Polygon p, Color c);
}
