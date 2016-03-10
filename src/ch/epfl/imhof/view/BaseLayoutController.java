package ch.epfl.imhof.view;

import ch.epfl.imhof.Main;
import ch.epfl.imhof.RenderData;

import javax.imageio.ImageIO;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Classe représentant le contrôleur pour l'interface de base
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public class BaseLayoutController {
    private Main mainApp;
    private static final String IMAGE_TYPE = "jpeg";

    /**
     * Appelée par l'application principale pour qu'elle puisse donner au contrôleur
     * une référence à elle-meme
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Gère l'appui sur l'item de sauvegarde des images dans le menu principal
     */
    public void handleSaveOfRenderedImages(){
        RenderData maps = mainApp.getRenderedData();
        if(maps != null) {
            String outputPath = mainApp.getOutputPath();
            try(BufferedOutputStream hgtStream = new BufferedOutputStream(new FileOutputStream(new File(outputPath + "\\rawHGT."+IMAGE_TYPE)));
                BufferedOutputStream osmStream = new BufferedOutputStream(new FileOutputStream(new File(outputPath + "\\rawOSM."+IMAGE_TYPE)));
                BufferedOutputStream renderStream = new BufferedOutputStream(new FileOutputStream(new File(outputPath + "\\render."+IMAGE_TYPE)))) {

                ImageIO.write(maps.getRawHGT(), IMAGE_TYPE, hgtStream);
                ImageIO.write(maps.getRawOSM(), IMAGE_TYPE, osmStream);
                ImageIO.write(maps.getShaded(), IMAGE_TYPE, renderStream);
                System.out.println("Saved images");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
