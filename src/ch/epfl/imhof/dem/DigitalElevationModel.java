package ch.epfl.imhof.dem;

import ch.epfl.imhof.PointGeo;
import ch.epfl.imhof.Vector3;

/**
 * {@code public interface DigitalElevationModel extends AutoCloseable}
 * <p>
 * Interface modélisant un modèle numérique du terrain.
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public interface DigitalElevationModel extends AutoCloseable {

    @Override
    void close() throws Exception;

    /**
     * {@code Vector3 normalAt(PointGeo p) throws IllegalArgumentException}
     * <p>
     * Calcule le vecteur normal à la terre en le point spécifié.
     * @param p - le point en coordonnées WGS 84
     * @return Un vecteur normal
     * @throws IllegalArgumentException si le point spécifié ne fait pas partie de la zone
     * couverte par le fichier MNT
     */
    Vector3 normalAt(PointGeo p) throws IllegalArgumentException;
}
