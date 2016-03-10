package ch.epfl.imhof;

/**
 * Classe modélisant le concepteur de vecteur tri-dimensionnel.
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public final class Vector3 {

    private final double x, y, z;

    /**
     * Constructeur public du vecteur tri-dimensionnel, prenant en argument les coordonnées x,y,z.
     *
     * @param x la coordonnée x
     * @param y la coordonnée y
     * @param z la coordonnée z
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double x() {
        return x;
    }

    public double y() {
        return y;
    }

    public double z() { return z; }

    /**
     * Méthode calculant la norme du vecteur
     *
     * @return la norme du vecteur
     */
    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Méthode retournant un nouveau vecteur tri-dimensionnel normalisé
     *
     * @return un Vector3 normalisé
     */
    public Vector3 normalized() {
        double norm = this.norm();
        return new Vector3(x / norm, y / norm, z / norm);
    }

    /**
     * Méthode calculant le produit scalaire entre le vecteur auquel on l'applique et celui passé en argument
     *
     * @param that le vecteur avec lequel il faut calculer le produit vectoriel
     * @return le produit scalaire
     */
    public double scalarProduct(Vector3 that) {
        return (this.x * that.x + this.y * that.y + this.z * that.z);
    }
}
