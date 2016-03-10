package ch.epfl.imhof.painting;

/**
 * {@code public final class Color}
 * <p>
 * Classe représentant une couleur, décrite par ses composantes de rouge, de vert et de bleu.
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public final class Color {

    private final double r, g, b;

    /**
     * Couleur rouge (pure)
     */
    public final static Color RED = new Color(1, 0, 0);

    /**
     * Couleur verte (pure)
     */
    public final static Color GREEN = new Color(0, 1, 0);

    /**
     * Couleur bleue (pure)
     */
    public final static Color BLUE = new Color(0, 0, 1);

    /**
     * Couleur noire
     */
    public final static Color BLACK = new Color(0, 0, 0);

    /**
     * Couleur blanche
     */
    public final static Color WHITE = new Color(1, 1, 1);

    /**
     * Constructeur privé initialisant les variables r, g, b.
     *
     * @param r proportion de rouge
     * @param g proportion de vert
     * @param b proportion de bleu
     */
    private Color(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * {@code public static Color rgb(float r, float g, float b)}
     * <p>
     * Méthode prenant en argument les trois couleurs individuelles, exprimées entre 0 et 1,
     * et construisant la couleur correspondante.
     *
     * @param r proportion de rouge
     * @param g proportion de vert
     * @param b proportion de bleu
     * @return la couleur correspondante
     */
    public static Color rgb(double r, double g, double b) {
        if (!(0d <= r && r <= 1d))
            throw new IllegalArgumentException("Composante rouge invalide: " + r);
        if (!(0d <= r && r <= 1d))
            throw new IllegalArgumentException("Composante verte invalide: " + g);
        if (!(0d <= r && r <= 1d))
            throw new IllegalArgumentException("Composante bleue invalide: " + b);

        return new Color(r, g, b);
    }

    /**
     * {@code public static Color rgb(int packedRGB)}
     * <p>
     * Méthode prenant en arguments les trois composantes individuelles « empaquetées » dans
     * un entier de type int, la composante rouge se trouvant dans les bits 23 à 16,
     * la composante verte dans les bits 15 à 8 et la composante bleue dans les bits 7 à 0.
     *
     * @param packedRGB l'entier empaquetant les trois couleurs
     * @return la couleur associée à l'entier
     */
    public static Color rgb(int packedRGB) {
        return new Color(
                ((packedRGB >>> 16) & 0xFF) / 255d,
                ((packedRGB >>> 8) & 0xFF) / 255d,
                (packedRGB & 0xFF) / 255d
        );
    }

    /**
     * {@code public static Color gray(double p)}
     * <p>
     * Méthode construisant une couleur grise avec la proportion passée en argument
     *
     * @param p proportion de la couleur grise
     * @return la couleur grise correspondante
     */
    public static Color gray(double p) {
        if (!(0d <= p && p <= 1d))
            throw new IllegalArgumentException("Proportion pour la couleur grise invalide: " + p);
        return new Color(p, p, p);
    }

    /**
     * {@code public Color multiplyColors(Color a, Color b)}
     * <p>
     * Méthode permettant de multiplier deux couleurs entre elles, en multipliant les composantes une à une,
     * et retournant la couleur multipliée
     *
     * @param a la couleur a
     * @param b la couleur b
     * @return une nouvelle couleur mélangée
     */
    public static Color multiplyColors(Color a, Color b) {
        return new Color(
                a.r * b.r,
                a.g * b.g,
                a.b * b.b
        );
    }

    /**
     * {@code public java.awt.Color convertColor(Color c)}
     * <p>
     * <p>
     * Méthode permettant de convertir une couleur de type ch.epfl.imhof.painting.Color en couleur
     * java.awt.Color, à partir d'une couleur passée en argument
     *
     * @param c la couleur à convertir
     * @return la couleur de type "java"
     */
    public static java.awt.Color convertColor(Color c) {
        return new java.awt.Color((float) c.r, (float) c.g, (float) c.b);
    }

}
