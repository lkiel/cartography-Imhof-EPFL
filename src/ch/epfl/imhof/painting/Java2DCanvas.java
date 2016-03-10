package ch.epfl.imhof.painting;

import ch.epfl.imhof.geometry.ClosedPolyLine;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.geometry.PolyLine;
import ch.epfl.imhof.geometry.Polygon;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.function.Function;

import static java.awt.RenderingHints.*;

/**
 * {@code public class Java2DCanvas implements Canvas}
 * <p>
 * Mise en oeuvre du concept de toile en utilisant en partie les fonctionnalités de Java2D
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public final class Java2DCanvas implements Canvas {

    private final Function<Point, Point> coordChange;
    private final BufferedImage image;
    private final Graphics2D ctx;

    /**
     * {@code public Java2DCanvas(Point bl, Point tr, int width, int height, int dpi, Color bc)}
     * <p>
     * Construit une toile sur la base des paramètres donnés
     *
     * @param bl     - le point inférieur gauche de l'image
     * @param tr     - le point supérieur droit
     * @param width  - la largeur en pixels
     * @param height - la hauteur en pixels
     * @param dpi    - la résolution utilisée pour tracer les lignes
     * @param bc     - la couleur de fond par défaut si rien de supplémentaire n'est dessiné sur la toile
     */
    public Java2DCanvas(Point bl, Point tr, int width, int height, int dpi, Color bc) {

        double dilatation = dpi / 72d;

        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        ctx = image.createGraphics();
        ctx.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        ctx.setColor(Color.convertColor(bc));
        ctx.fillRect(0, 0, width, height);
        ctx.translate(width / 2d, height / 2d);
        ctx.scale(dilatation, dilatation);
        coordChange = Point.alignedCoordinateChange(
                bl, new Point((-width / 2d) / dilatation, (height / 2d) / dilatation),
                tr, new Point((width / 2d) / dilatation, (-height / 2d) / dilatation));
    }

    @Override
    public void drawPolyLine(PolyLine p, LineStyle s) {
        Path2D polylinePath = createPathFromPolyLine(p);
        if (p.isClosed()) polylinePath.closePath();
        ctx.setColor(Color.convertColor(s.getLineColor()));
        ctx.setStroke(new BasicStroke(s.getLineWidth(), s.getLineCap().ordinal(), s.getLineJoin().ordinal(), 10.0f, s.getDashingPattern(), 0));
        ctx.draw(polylinePath);
    }

    @Override
    public void drawPolygon(Polygon p, Color c) {
        Path2D shellPath = createPathFromPolyLine(p.shell());
        shellPath.closePath();
        Area polygonArea = new Area(shellPath);

        for (ClosedPolyLine cpl : p.holes()) {
            Path2D holePath = createPathFromPolyLine(cpl);
            holePath.closePath();
            polygonArea.subtract(new Area(holePath));
        }
        ctx.setColor(Color.convertColor(c));
        ctx.fill(polygonArea);
    }

    /**
     * Méthode créant un Path2D à partir d'une polyligne
     *
     * @param p - la polyligne qu'il faut convertir en chemin
     * @return Un objet Path2D utilisé par la toile
     */
    private Path2D createPathFromPolyLine(PolyLine p) {
        Path2D path = new Path2D.Double();
        Point firstPoint = coordChange.apply(p.firstPoint());
        path.moveTo(firstPoint.x(), firstPoint.y());

        for (Point pt : p.points().subList(1, p.points().size())) {
            Point nextPoint = coordChange.apply(pt);
            path.lineTo(nextPoint.x(), nextPoint.y());
        }
        return path;
    }

    /**
     * {@code public BufferedImage image()}
     * <p>
     *
     * @return l'image générée sur la toile
     */
    public BufferedImage image() {
        return image;
    }

    /**
     * {@code public static BufferedImage findDifferences(BufferedImage image, BufferedImage reference)}
     * <p>
     * Méthode utilitaire prenant deux images, une source et une de référence, et peint en rouge dans l'image de source
     * tous les pixels qui diffèrent de l'image de référence
     *
     * @param image     - l'image qui sera éventuellement modifiée
     * @param reference - l'image de référence utilisée pour la comparaison
     * @return Une image indiquant les pixels différents en rouge
     */
    public static BufferedImage findDifferences(BufferedImage image, BufferedImage reference) {
        int diffs = 0;
        for (int i = 0; i < reference.getWidth(); i++) {
            for (int j = 0; j < reference.getHeight(); j++) {
                if (image.getRGB(i, j) != reference.getRGB(i, j)) {
                    reference.setRGB(i, j, 0xFF_00_00);
                    diffs++;
                }
            }
        }
        System.out.println(diffs + " different pixels found.");
        return reference;
    }
}
