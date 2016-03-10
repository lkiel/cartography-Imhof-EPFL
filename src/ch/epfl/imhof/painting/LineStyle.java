package ch.epfl.imhof.painting;

import java.util.Arrays;

/**
 * Classe modélisant un style de ligne, en fonction de 5 paramètres : largeur de la ligne, couleur,
 * terminaison, jointure et schéma de traitillé.
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public final class LineStyle {

    private final float lineWidth;
    private final Color lineColor;
    private final LineCapStyle lineCap;
    private final LineJoinStyle lineJoin;
    private final float[] dashingPattern;

    /**
     * Enumeration des styles de terminaison de ligne
     */
    public enum LineCapStyle {
        Butt, Round, Square
    }

    /**
     * Enumeration des styles de jointures de ligne
     */
    public enum LineJoinStyle {
        Miter, Round, Bevel
    }

    /**
     * Constructeur principal initialisant chacun des attributs aux valeurs passées en argument
     * Lance une IllegalArgumentException si la largeur est négative ou si un des éléments
     * de la séquence d'alternance est négatif ou nul
     *
     * @param lineWidth      largeur de la ligne
     * @param lineColor      couleur de la ligne
     * @param lineCap        terminaison de la ligne
     * @param lineJoin       jointure des segments
     * @param dashingPattern schéma d'alternance des sections opaques et transparentes
     */
    public LineStyle(float lineWidth, Color lineColor, LineCapStyle lineCap, LineJoinStyle lineJoin, float... dashingPattern) {
        if (lineWidth < 0)
            throw new IllegalArgumentException("La largeur du trait est négative : " + lineWidth);
        for (float f : dashingPattern)
            if (f <= 0)
                throw new IllegalArgumentException("Un des éléments de la séquence d'alternance est négatif ou nul");

        this.lineWidth = lineWidth;
        this.lineColor = lineColor;
        this.lineCap = lineCap;
        this.lineJoin = lineJoin;
        this.dashingPattern = Arrays.copyOf(dashingPattern, dashingPattern.length);
    }

    /**
     * Constructeur initialisant une ligne avec terminaison "butt" et jointure "miter",
     * et prenant en argument la couleur et l'épaisseur de la ligne.
     *
     * @param lineWidth la largeur de la ligne
     * @param color     la couleur de la ligne
     */
    public LineStyle(float lineWidth, Color color) {
        this(lineWidth, color, LineCapStyle.Butt, LineJoinStyle.Miter);
    }

    /**
     * Méthode retournant un nouveau "LineStyle" dont l'épaisseur a été changée par celle passée en argument
     *
     * @param width la largeur
     * @return le nouveau LineStyle
     */
    public LineStyle withWidth(float width) {
        return new LineStyle(width, lineColor, lineCap, lineJoin, dashingPattern);
    }

    /**
     * Méthode retournant un nouveau "LineStyle" dont la couleur a été changée par celle passée en argument
     *
     * @param color la couleur
     * @return le nouveau LineStyle
     */
    public LineStyle withColor(Color color) {
        return new LineStyle(lineWidth, color, lineCap, lineJoin, dashingPattern);
    }

    /**
     * Méthode retournant un nouveau "LineStyle" dont la terminaison de ligne
     * a été changée par celle passée en argument
     *
     * @param cap la terminaison de ligne
     * @return le nouveau LineStyle
     */
    public LineStyle withCap(LineCapStyle cap) {
        return new LineStyle(lineWidth, lineColor, cap, lineJoin, dashingPattern);
    }

    /**
     * Méthode retournant un nouveau "LineStyle" dont la jointure des lignes
     * a été changée par celle passée en argument
     *
     * @param join la jointure de ligne
     * @return le nouveau LineStyle
     */
    public LineStyle withJoin(LineJoinStyle join) {
        return new LineStyle(lineWidth, lineColor, lineCap, join, dashingPattern);
    }

    /**
     * Méthode retournant un nouveau "LineStyle" dont le schéma de traitillé des lignes
     * a été changé par celui passé en argument
     *
     * @param pattern le schéma
     * @return le nouveau LineStyle
     */
    public LineStyle withPattern(float... pattern) {
        return new LineStyle(lineWidth, lineColor, lineCap, lineJoin, pattern);
    }

    /**
     * Méthode retournant la largeur de la ligne
     *
     * @return la largeur de la ligne
     */
    public float getLineWidth() {
        return lineWidth;
    }

    /**
     * Méthode retournant la couleur de la ligne
     *
     * @return la couleur de la ligne
     */
    public Color getLineColor() {
        return lineColor;
    }

    /**
     * Méthode retournant la terminaison de ligne
     *
     * @return la terminaison de ligne
     */
    public LineCapStyle getLineCap() {
        return lineCap;
    }

    /**
     * Méthode retournant la jointure des lignes
     *
     * @return la jointure des lignes
     */
    public LineJoinStyle getLineJoin() {
        return lineJoin;
    }

    /**
     * Méthode retournant le schéma de traitillés
     *
     * @return le schéma de traitillés ou null dans le cas d'un trait plein
     */
    public float[] getDashingPattern() {
        return (dashingPattern.length > 0) ? Arrays.copyOf(dashingPattern, dashingPattern.length) : null;
    }

}
