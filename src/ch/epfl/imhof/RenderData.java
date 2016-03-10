package ch.epfl.imhof;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;

/**
 * {@code public final class RenderData}
 * <p>
 * Classe utilitaire pour stocker les différentes composantes du rendu.
 * En l'occurence une image pour les données d'élévation, une pour les
 * données OSM et un dernière pour le rendu final
 *
 * @author Clément Nussbaumer (250621)
 * @author Laurent Kieliger (246263)
 */
public final class RenderData {
    private final BufferedImage rawHGT, rawOSM, shaded;

    public RenderData(BufferedImage rawHGT, BufferedImage rawOSM, BufferedImage shaded) {
        this.rawHGT = rawHGT;
        this.rawOSM = rawOSM;
        this.shaded = shaded;
    }

    public BufferedImage getRawHGT() {return rawHGT;}
    public BufferedImage getRawOSM() {return rawOSM;}
    public BufferedImage getShaded() {return shaded;}

    /**
     * Convertit la BufferedImage originale et retourne une javafx.scene.image.
     * @return Une image utilisable par javaFX
     */
    public Image getFXRawHGT() {return SwingFXUtils.toFXImage(rawHGT, null);}
    /**
     * Convertit la BufferedImage originale et retourne une javafx.scene.image.
     * @return Une image utilisable par javaFX
     */
    public Image getFXRawOSM() {return SwingFXUtils.toFXImage(rawOSM, null);}
    /**
     * Convertit la BufferedImage originale et retourne une javafx.scene.image.
     * @return Une image utilisable par javaFX
     */
    public Image getFXShaded() {return SwingFXUtils.toFXImage(shaded, null);}
}
