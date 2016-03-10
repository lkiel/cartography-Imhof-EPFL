package ch.epfl.imhof;

import ch.epfl.imhof.view.BaseLayoutController;
import ch.epfl.imhof.view.RenderLayoutController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.concurrent.*;
import java.io.*;

/**
 * Programme principal version améliorée GUI
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public final class Main extends Application {

    private Stage primaryStage;
    private BorderPane baseLayout;
    private BaseLayoutController baseLayoutController;
    private RenderLayoutController renderLayoutController;
    private RenderData maps;

    public static void main(String[] args){

        // Lancement de la GUI
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Projet Imhof");
        initRootLayout();
        showRenderView();
        maps = null;

        primaryStage.setOnCloseRequest(e ->{
                Platform.exit();
                System.exit(0);
        });
    }

    /**
     * Initialise le layout de base pour l'application
     *
     */
    public void initRootLayout() {
        try {
            // charge le layout de base à partir du fichier FXML
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/BaseLayout.fxml"));
            baseLayout = loader.load();
            baseLayoutController = loader.getController();
            baseLayoutController.setMainApp(this);

            // Rends la scene visible
            Scene scene = new Scene(baseLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Affiche la boîte de rendu dans le layout de base
     */
    public void showRenderView() {
        try {
            // charge le layout de rendu
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RenderLayout.fxml"));
            AnchorPane configView = loader.load();

            // définit le layout de rendu comme étant au centre du layout de base
            baseLayout.setCenter(configView);
            renderLayoutController = loader.getController();
            renderLayoutController.setMainApp(this);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Les points sont spécifiés dans le système WGS 84 et la projection
     * utilisée est projection suisse CH1903. La résolution doit être un nombre entier.
     *
     * @param args - Accepte les 9 arguments suivants: <br/>
     *             1) le nom (chemin) d'un fichier OSM compressé avec gzip, ou d'une {@link Map} sérializée <br/>
     *             2) le nom (chemin) d'un fichier HGT couvrant la zone à dessiner <br/>
     *             3) la longitude du point bas-gauche de la carte en degrés <br/>
     *             4) la latitude du point bas-gauche de la carte en degrés <br/>
     *             5) la longitude du point haut-droite de la carte en degrés <br/>
     *             6) la latitude du point haut-droite de la carte en degrés <br/>
     *             7) la résolution de l'image à dessiner en points par pouce <br/>
     *             8) le nom (chemin) du fichier PNG à générer <br/>
     *             9) la commande -serialize pour convertir le fichier OSM lu en fichier binaire optimisé
     *             pour le programme. le fichier binaire est sauvegardé à côté du fichier OSM
     */
    public void render(String[] args){

        ExecutorService executor = Executors.newSingleThreadExecutor();
        RenderingProcess renderingProcess = new RenderingProcess(args, renderLayoutController);

        renderingProcess.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, t -> {
            maps = renderingProcess.getValue();
            renderLayoutController.updateDisplay();
            System.out.println("Rendering worker has suceeded");
        });
        renderingProcess.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, t -> {
            renderLayoutController.updateState(State.IDLE);
            System.out.println("Rendering worker has been cancelled");
        });

        executor.submit(renderingProcess);
        executor.shutdown();
    }

    /**
     * Utilisée par les contrôleurs pour communiquer entre eux
     *
     * @return Le chemin d'output pour les images générées
     */
    public String getOutputPath(){
        return renderLayoutController.getOutputPath();
    }

    /**
     * Retourne la scène principale
     */
    public Stage getPrimaryStage(){
        return primaryStage;
    }

    /**
     *
     * @return Les données du rendu ou null si aucun rendu n'a été effectué
     */
    public RenderData getRenderedData(){
        return maps;
    }
}
