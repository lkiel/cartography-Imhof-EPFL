package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.projection.Projection;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.Function;

/**
 * Classe permettant de dessiner un relief coloré ombré.
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public final class ReliefShader {

    private final Projection proj;
    private final DigitalElevationModel dem;
    private final Vector3 light;

    /**
     * Constructeur prenant en argument une projection, modèle digital d'élévation de terrain et un vecteur tridimensionnel
     * représentant la direction d'une source lumineuse.
     *
     * @param proj  la projection à utiliser
     * @param dem   le modèle numérique du terrain
     * @param light le vecteur lumière (indiquant la direction de la lumière)
     */
    public ReliefShader(Projection proj, DigitalElevationModel dem, Vector3 light) {
        this.proj = proj;
        this.dem = dem;
        this.light = light.normalized();
    }

    /**
     * Fonction retournant une image de type BufferedImage représentant un relief ombré,
     * dessiné à partir du point bas-gauche, haut-droit (déterminants le cadre de l'image), de la largeur et de la hauteur
     * de l'image à retourner et du rayon de floutage.
     *
     * @param bl     le point bas-gauche du cadre
     * @param tr     le point haut-droit du cadre
     * @param width  la largeur (en pixels) du relief à dessiner
     * @param height la hauteur (en pixels) du relief à dessiner
     * @return une BufferedImage représentant le relief ombré
     */
    public BufferedImage shadedRelief(Point bl, Point tr, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Function<Point, Point> coordChange = Point.alignedCoordinateChange(
                new Point(0, height), bl, new Point(width, 0), tr);

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                PointGeo p = proj.inverse(coordChange.apply(new Point(i, j)));
                Vector3 normalIJ = dem.normalAt(p);

                float cosineTheta = (float)light.scalarProduct(normalIJ);
                float rg = (cosineTheta + 1) / 2f;
                float b = (0.7f * cosineTheta + 1) / 2f;

                image.setRGB(i, j, new Color(rg, rg, b).getRGB());
            }
        }
        return image;
    }
}
