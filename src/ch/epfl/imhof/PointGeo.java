package ch.epfl.imhof;

/**
 * {@code public final class PointGeo}
 * <p>
 * Une classe immuable représentant un
 * point à la surface de la terre, exprimé en coordonnées sphériques.
 *
 * @author Clément Nussbaumer (250621)
 * @author Laurent Kieliger (246263)
 */
public final class PointGeo {

    private final double longitude;
    private final double latitude;

    /**
     * {@code public PointGeo(double longitude, double latitude) throws IllegalArgumentException}
     * <p>
     * Constructeur de la classe construisant un point géométrique caractérisé par une longitude et
     * une latitude.
     *
     * @param longitude - la longitude du point, exprimée en radians
     * @param latitude  - la latitude du point, exprimée en radians
     * @throws IllegalArgumentException si la longitude ou la latitude sont invalides, c-à-d si la
     *                                  longitude n'est pas dans l'intervalle [-π; π] ou si la
     *                                  latitude n'est pas dans l'intervalle [-π/2; π/2]
     */
    public PointGeo(double longitude, double latitude) throws IllegalArgumentException {
        if (longitude < -Math.PI || longitude > Math.PI || latitude < -Math.PI / 2 || latitude > Math.PI / 2)
            throw new IllegalArgumentException("La longitude et/ou la latitude fournie(s) n'existent pas");

        this.longitude = longitude;
        this.latitude = latitude;
    }

    /**
     * {@code public double longitude()}
     *
     * @return la longitude du point, exprimée en radians
     */
    public double longitude() {
        return longitude;
    }

    /**
     * {@code public double latitude()}
     *
     * @return la latitude du point, exprimée en radians
     */
    public double latitude() {
        return latitude;
    }
}
