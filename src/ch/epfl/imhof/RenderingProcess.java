/**
 * Classe représentant le processus principal de rendu sous forme de tâche
 * retournant les cartes encapsulée dans un objet RenderData
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */

package ch.epfl.imhof;

import static ch.epfl.imhof.State.*;
import static ch.epfl.imhof.view.UserInterfaceUtils.*;

import ch.epfl.imhof.dem.DigitalElevationModel;
import ch.epfl.imhof.dem.Earth;
import ch.epfl.imhof.dem.HGTDigitalElevationModel;
import ch.epfl.imhof.dem.ReliefShader;
import ch.epfl.imhof.geometry.Point;
import ch.epfl.imhof.osm.OSMMap;
import ch.epfl.imhof.osm.OSMMapReader;
import ch.epfl.imhof.osm.OSMToGeoTransformer;
import ch.epfl.imhof.painting.Color;
import ch.epfl.imhof.painting.Java2DCanvas;
import ch.epfl.imhof.painting.Painter;
import ch.epfl.imhof.projection.CH1903Projection;
import ch.epfl.imhof.projection.Projection;
import ch.epfl.imhof.view.RenderLayoutController;
import javafx.application.Platform;
import javafx.concurrent.Task;
import org.xml.sax.SAXException;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static ch.epfl.imhof.painting.Color.convertColor;
import static ch.epfl.imhof.painting.Color.multiplyColors;
import static ch.epfl.imhof.painting.Color.rgb;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.Math.toRadians;

public class RenderingProcess extends Task<RenderData> {

    private final static double MAP_SCALE = 1 / 25_000d;
    private final static double METERS_PER_INCH = 0.0254;
    private final static Vector3 LIGHT = new Vector3(-1, 1, 1);
    private final Future<BufferedImage> threaded_rawOSMImage, threaded_rawHGTImage;
    private final int width, height;
    private final RenderLayoutController renderController;
    private ScheduledExecutorService scheduledExecutor;

    /**
     * Constructeur de la tâche
     *
     * @param args             Les arguments qui serviront à charger les fichiers pour le rendu
     * @param renderController Une référence vers le contrôleur de rendu pour mettre à jour l'interface graphique
     */
    public RenderingProcess(String[] args, RenderLayoutController renderController) {

        this.renderController = renderController;

        //Exécuteurs
        ExecutorService executor = Executors.newFixedThreadPool(2);
        scheduledExecutor = Executors.newScheduledThreadPool(1);
        scheduledExecutor.shutdown();

        Projection projection = new CH1903Projection();
        OSMToGeoTransformer transformer = new OSMToGeoTransformer(projection);

        double phiBL, phiTR;
        int tmpHeight = 0, tmpWidth = 0, dpi = 0;
        Point bl = new Point(0, 0), tr = new Point(0, 0);

        try {
            phiBL = toRadians(parseDouble(args[3]));
            phiTR = toRadians(parseDouble(args[5]));
            dpi = parseInt(args[6]);

            bl = projection.project(new PointGeo(toRadians(parseDouble(args[2])), phiBL));
            tr = projection.project(new PointGeo(toRadians(parseDouble(args[4])), phiTR));

            double r = dpi / METERS_PER_INCH;
            tmpHeight = (int) Math.round(r * MAP_SCALE * (phiTR - phiBL) * Earth.RADIUS);
            tmpWidth = (int) Math.round((tr.x() - bl.x()) / (tr.y() - bl.y()) * tmpHeight);
        } catch (Exception e) {
            displayErrorMessage("Given longitudes and/or latitudes are invalid");
            cancel();
        }

        height = tmpHeight;
        width = tmpWidth;

        //Image OSM uniquement, création de variable finales pour utilisation dans la méthode lambda
        final Point finalBl = bl;
        final Point finalTr = tr;
        final int finalDpi = dpi;

        threaded_rawOSMImage = executor.submit(() -> {
            if (isCancelled()) {
                return new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
            }
            Map rawMap = null;

            //Si l'utilisateur a indiqué un fichier sérialisé alors le programme tente de l'ouvrir
            if (args[0].endsWith(".ser.gz")) {
                try (ObjectInputStream in =
                             new ObjectInputStream(new GZIPInputStream(new BufferedInputStream(new FileInputStream(args[0]))))) {
                    rawMap = (Map) in.readObject();
                } catch (Exception e) {
                    displayErrorMessage("Operation aborted because serialized file was invalid");
                    cancel();
                }
            } else {
                try {
                    OSMMap osmMap = OSMMapReader.readOSMFile(args[0], true);
                    rawMap = transformer.transform(osmMap);

                    //Effectue la sérialisation de la map si l'utilisateur le spécifie
                    if (args[8].equals("-serialize")) {
                        try (ObjectOutputStream out =
                                     new ObjectOutputStream(new GZIPOutputStream(
                                             new FileOutputStream(args[0].substring(0, args[0].length() - 7) + ".ser.gz")))) {
                            out.writeObject(rawMap);
                        } catch (Exception e) {
                            displayWarningMessage("Could not generate or save the selialized version of the map. " +
                                    "However, rendering will continue..");
                        }
                    }
                } catch (SAXException e) {
                    displayErrorMessage("Operation aborted because the XML file of the map presented formatting errors");
                    cancel();
                } catch (IOException e) {
                    displayErrorMessage("Operation aborted because there was an error with the specified OSM file");
                    cancel();
                }
            }

            Painter swissPainter = SwissPainter.painter();
            Java2DCanvas canvas = new Java2DCanvas(finalBl, finalTr, width, height, finalDpi, Color.WHITE);
            swissPainter.drawMap(rawMap, canvas);
            return canvas.image();
        });

        //Image élévation uniquement
        threaded_rawHGTImage = executor.submit(() -> {
            if (isCancelled()) {
                return new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
            }
            try (DigitalElevationModel dem = new HGTDigitalElevationModel(new File(args[1]))) {
                ReliefShader reliefShader = new ReliefShader(projection, dem, LIGHT);
                return reliefShader.shadedRelief(finalBl, finalTr, width, height);

            } catch (IOException e) {
                displayErrorMessage("Operation aborted because there was an error with the specified HGT file");
                cancel();
            } catch (IllegalArgumentException e) {
                displayErrorMessage("Operation aborted because HGT file was invalid");
                e.printStackTrace();
                cancel();
            }
            return new BufferedImage(1,1,BufferedImage.TYPE_INT_RGB);
        });

        executor.shutdown();
    }

    /**
     * Méthode automatiquement appelée lors de l'éxecution de la tâche par le thread
     *
     * @return Les cartes encapsulées dans un objet de type RenderData
     */
    @Override
    public RenderData call() throws ExecutionException, InterruptedException {
        if (isCancelled()) {
            return null;
        }

        //Indique à l'interface graphique que le programme génère les données brutes pour le rendu
        Platform.runLater(() -> {
            renderController.updateState(RENDERING_RAWDATA);
            System.out.println("Changing state to rendering raw data");
        });

        BufferedImage rawHGTImage = threaded_rawHGTImage.get(), rawOSMImage = threaded_rawOSMImage.get();
        BufferedImage render = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        //Indique à l'interface graphique que le programme débute le rendu final
        Platform.runLater(() -> {
            renderController.updateState(RENDERING_SHADEDRELIEF);
            System.out.println("Changing state to rendering shaded relief");
        });

        int nProcessors = Runtime.getRuntime().availableProcessors(), verticalSpacing = height / (nProcessors), remainingHeight = verticalSpacing * nProcessors;
        ExecutorService executor = Executors.newFixedThreadPool(nProcessors + 1);
        ExecutorService controlExecutor = Executors.newSingleThreadExecutor();


        for (int i = 0; i < nProcessors; i++) {
            int startY = i * verticalSpacing;
            executor.submit(() -> imageSectionMultiplier(startY, startY + verticalSpacing, rawHGTImage, rawOSMImage, render));
        }

        if (remainingHeight > 0) {
            executor.submit(() -> imageSectionMultiplier(remainingHeight, height, rawHGTImage, rawOSMImage, render));
        }

        executor.shutdown();
        setProgressiveView(true);

        //L'exécuteur de contrôle attend que toutes les portions de l'image aient été dessinées pour arrêter le rafraîchissement
        //périodique de l'image ombrée
        controlExecutor.submit(() -> {
                    try {
                        executor.awaitTermination(1, TimeUnit.HOURS);
                        setProgressiveView(false);
                        Platform.runLater(() -> renderController.updateState(IDLE));
                        System.out.println("Going idle..");

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );
        controlExecutor.shutdown();
        return new RenderData(rawHGTImage, rawOSMImage, render);
    }

    /**
     * Multiplie les couleurs du relief ainsi que de la carte OSM brute pour obtenir le rendu final sur une portion de l'image.
     *
     * @param startY      La hauteur Y de l'image qui définit le début de la tranche de rendu
     * @param finishY     La hauteur Y de l'image qui définit la fin de la tranchde de rendu
     * @param rawHGTImage Une référence vers le relief brut
     * @param rawOSMImage Une référence vers la carte brute
     * @param shadedImage Une référence vers le rendu final
     */
    private void imageSectionMultiplier(int startY, int finishY, BufferedImage rawHGTImage, BufferedImage rawOSMImage, BufferedImage shadedImage) {
        for (int x = 0; x < width; x++) {
            for (int y = startY; y < finishY; y++) {
                java.awt.Color mix = convertColor(multiplyColors(rgb(rawHGTImage.getRGB(x, y)), rgb(rawOSMImage.getRGB(x, y))));
                shadedImage.setRGB(x, y, mix.getRGB());
            }
        }
    }

    /**
     * Utilisée pour voir la création du rendu final en temps réel
     *
     * @param b Un booléen qui active ou désactive le rendu en temps réels. Devrait être définit à false lorsque le rendu est terminé
     */
    private void setProgressiveView(boolean b) {
        if (b && scheduledExecutor.isTerminated()) {
            System.out.println("Going into fast refresh mode");
            scheduledExecutor = Executors.newScheduledThreadPool(1);
            scheduledExecutor.scheduleAtFixedRate(renderController::updateShadedRelief, 100, 300, TimeUnit.MILLISECONDS);
        } else if (!b) {
            System.out.println("Exiting fast refresh mode");
            renderController.updateDisplay();
            scheduledExecutor.shutdown();
        }
    }
}
