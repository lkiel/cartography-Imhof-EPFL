package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe représentant le modèle numérique de terrain (MNT) stocké au format HGT.
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public final class HGTDigitalElevationModel implements DigitalElevationModel {

    private ShortBuffer hgtBuff;
    private final FileInputStream stream;
    private final Map<Integer, Vector3> normalMap;
    private final double s;
    private final double s_2;
    private final int latSW;
    private final int longSW;
    private final int sideDimension;

    /**
     * {@code public HGTDigitalElevationModel(File file) throws IOException}
     * <p>
     * Constructeur de la classe HGTDigitalElevationModel
     *
     * @param file - le fichier .hgt à charger
     * @throws IllegalArgumentException - si le fichier n'est pas valide au sens du type .hgt (format SRTM)
     * @throws IOException              si le fichier n'est pas accessible
     */
    public HGTDigitalElevationModel(File file) throws IOException, IllegalArgumentException {
        String fileName = file.getName();

        if (!fileName.matches("[NS]\\d{2}[EW]\\d{3}\\.hgt"))
            throw new IllegalArgumentException("Le nom du fichier n'est pas conforme au standard SRTM (hgt)");

        long length = file.length();
        double sqrtFile = Math.sqrt(length / 2d);
        if (sqrtFile % 1 != 0)
            throw new IllegalArgumentException("Le nombre de bytes est invalide");

        longSW = ((fileName.charAt(0) == 'N') ? 1 : -1) * Integer.parseInt(fileName.substring(4, 7));  //Longitude
        latSW = ((fileName.charAt(0) == 'N') ? 1 : -1) * Integer.parseInt(fileName.substring(1, 3));  //Latitude

        if (longSW < -180 || longSW > 180 || latSW < -90 || latSW > 90)
            throw new IllegalArgumentException("La longitude et/ou la latitude spécifiée par le fichier est invalide");

        normalMap = new HashMap<>();
        stream = new FileInputStream(file);
        sideDimension = (int) sqrtFile - 1;
        s = Earth.RADIUS * Math.toRadians(1 / (double) (sideDimension));
        s_2 = s * s * 8;
        hgtBuff = stream.getChannel().map(MapMode.READ_ONLY, 0, length).asShortBuffer();
    }

    @Override
    public void close() throws IOException {
        stream.close();
        hgtBuff = null;
    }

    @Override
    public Vector3 normalAt(PointGeo p) throws IllegalArgumentException {
        double pointLatitude = Math.toDegrees(p.latitude());
        double pointLongitude = Math.toDegrees(p.longitude());

        if (pointLatitude < latSW || pointLatitude > latSW + 1)
            throw new IllegalArgumentException("La latitude du point géométrique n'est pas comprise dans le fichier: " + pointLatitude);
        if (pointLongitude < longSW || pointLongitude > longSW + 1)
            throw new IllegalArgumentException("La longitude du point géométrique n'est pas comprise dans le fichier: " + pointLongitude);

        double preciseI = (pointLongitude - longSW) * sideDimension;
        double preciseJ = (pointLatitude - latSW) * sideDimension;

        int i = (int) preciseI;
        int j = (int) preciseJ;

        Vector3 topLeft = getVertexNormal(i, j + 1), topRight = getVertexNormal(i + 1, j + 1);
        Vector3 bottomLeft = getVertexNormal(i, j), bottomRight = getVertexNormal(i + 1, j);

        return interpolatedVector(bottomLeft, bottomRight, topLeft, topRight, preciseI - i, preciseJ - j);
    }

    /**
     * Méthode calculant le vecteur normal en un point du MNT. Ce vecteur correspond à la moyenne des
     * vecteurs normaux des surfaces triangulaires voisines du point passé en argument.
     *
     * @param i - la coordonnée selon l'axe i du fichier .hgt
     * @param j - la coordonnée selon l'axe j du fichier .hgt
     * @return Le vecteur normal en un point du MNT
     */
    private Vector3 getVertexNormal(int i, int j) {
        double h1 = bufferAt(i + 1, j) * 2, h2 = bufferAt(i + 1, j + 1), h3 = bufferAt(i, j + 1) * 2, h4 = bufferAt(i - 1, j + 1);
        double h5 = bufferAt(i - 1, j) * 2, h6 = bufferAt(i - 1, j - 1), h7 = bufferAt(i, j - 1) * 2, h8 = bufferAt(i + 1, j - 1);

        return normalMap.computeIfAbsent(indexOf(i, j), k ->
                new Vector3(s * (h5 + h6 + h4 - h1 - h2 - h8), s * (h7 + h6 + h8 - h3 - h2 - h4), s_2).normalized());
    }

    /**
     * Méthode calculant le vecteur résultant d'une interpolation bilinéaire en un point selon chaque
     * composante de 4 vecteurs
     *
     * @param bl - le vecteur bas gauche
     * @param br - le vecteur bas droite
     * @param tl - le vecteur haut gauche
     * @param tr - le vecteur haut droite
     * @param dx - l'espace en secondes d'arc entre le point et les deux vecteurs de gauche
     * @param dy - l'espace en secondes d'arc entre le point et les deux vecteurs du haut
     * @return Un vecteur interpolé
     */
    private Vector3 interpolatedVector(Vector3 bl, Vector3 br, Vector3 tl, Vector3 tr, double dx, double dy) {
        return new Vector3(
                bilinearInterpolation(bl.x(), br.x(), tl.x(), tr.x(), dx, dy), //selon x
                bilinearInterpolation(bl.y(), br.y(), tl.y(), tr.y(), dx, dy), //selon y
                bilinearInterpolation(bl.z(), br.z(), tl.z(), tr.z(), dx, dy));//selon z
    }

    /**
     * Méthode réalisant une interpolation bilinéaire en un point selon les 4 valeurs du carré
     *
     * @param bl - valeur bas gauche
     * @param br - valeur bas droite
     * @param tl - valeur haut gauche
     * @param tr - valeur haut droite
     * @param dx - l'espace en secondes d'arc entre le point et les deux vecteurs de gauche
     * @param dy - l'espace en secondes d'arc entre le point et les deux vecteurs du haut
     * @return La valeur interpolée
     */
    private double bilinearInterpolation(double bl, double br, double tl, double tr, double dx, double dy) {

    /* Schéma de l'interpolation
     * TL-----R2----TR
     * |             |
     * |< dx > p     |
     * |       ^     |
     * |      dy     |
     * |       v     |
     * BL-----R1----BR
     */
        double r1 = bl * (1 - dx) + br * dx;
        double r2 = tl * (1 - dx) + tr * dx;
        return r1 * (1 - dy) + r2 * dy;
    }

    /**
     * Méthode retournant l'élément dans le buffer spécifié par son index dans un tableau
     * bidimensionnel d'origine inférieure gauche
     *
     * @param i - coordonnée selon l'axe horizontal
     * @param j - coordonnée selon l'axe vertical
     * @return L'élément du buffer correspondant aux coordonnées
     */
    private short bufferAt(int i, int j) {
        return hgtBuff.get(indexOf(i, j));
    }

    /**
     * Méthode retournant la position de l'élément dans le buffer spécifié par ses
     * coordonnées dans un tableau bidimensionnel d'origine inférieure gauche
     *
     * @param i - coordonnée selon l'axe horizontal
     * @param j - coordonnée selon l'axe vertical
     * @return L'index de l'élément
     */
    private int indexOf(int i, int j) {
        return (sideDimension - j) * (sideDimension + 1) + i;
    }
}
