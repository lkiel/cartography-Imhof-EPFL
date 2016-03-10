package ch.epfl.imhof.view;

import static ch.epfl.imhof.view.UserInterfaceUtils.*;
import ch.epfl.imhof.Main;
import ch.epfl.imhof.RenderData;
import ch.epfl.imhof.State;
import javafx.beans.binding.Bindings;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Classe représentant le contrôleur pour l'interface de rendu
 *
 * @author Clément Nussbaumer   (250261)
 * @author Leandro Kieliger     (246263)
 */
public class RenderLayoutController {

    //Référence à l'application de base
    private Main mainApp;
    private final FileChooser fileChooser;
    private final DirectoryChooser directoryChooser;
    private final static double ZOOM_IN_SCALE = 0.95;
    private final static double ZOOM_OUT_SCALE = 1.05;

    //Variable liées au layout
    @FXML
    private TextField osmPathField;
    @FXML
    private TextField hgtPathField;
    @FXML
    private TextField bottomLeftLONField;
    @FXML
    private TextField bottomLeftLATField;
    @FXML
    private TextField topRightLONField;
    @FXML
    private TextField topRightLATField;
    @FXML
    private TextField outputPathField;
    @FXML
    private Label sliderValueLabel;
    @FXML
    private Label stateValueLabel;
    @FXML
    private CheckBox serializeBox;
    @FXML
    private Slider resolutionSlider;
    @FXML
    private Button selectOSMPathButton;
    @FXML
    private Button selectHGTPathButton;
    @FXML
    private Button selectOutputPathButton;
    @FXML
    private Button startRenderingButton;
    @FXML
    private ImageView shadedImage;
    @FXML
    private ImageView rawOSMImage;
    @FXML
    private ImageView rawHGTImage;
    @FXML
    private ImageView warningIcon;
    @FXML
    private AnchorPane rawHGTImagePane;
    @FXML
    private AnchorPane rawOSMImagePane;
    @FXML
    private AnchorPane shadedImagePane;
    @FXML
    private Accordion accordion;
    @FXML
    private Tooltip warningTooltip;

    public RenderLayoutController() {
        File currentDirFile = new File(System.getProperty("user.dir"));
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(currentDirFile);
        fileChooser.setTitle("Select resources");
        directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(currentDirFile);
        directoryChooser.setTitle("Select output path");
    }

    /**
     * Initialise le contrôleur, cette methode est automatiquement appelée lorsque le fichier .fxml
     * a terminé de charger
     */
    @FXML
    private void initialize() {

        //Lie la valeur du slider au texte du label juste au dessus
        sliderValueLabel.textProperty().bind(Bindings.format("%1.0f",resolutionSlider.valueProperty()));

        //Définit quel onglet de l'accordéon est ouvert par défaut
        accordion.setExpandedPane(accordion.getPanes().get(2));

        //Définit les largeurs des zones de rendu d'image
        shadedImage.fitWidthProperty().bind(shadedImagePane.widthProperty());
        shadedImage.fitHeightProperty().bind(shadedImagePane.heightProperty());
        rawHGTImage.fitWidthProperty().bind(rawHGTImagePane.widthProperty());
        rawHGTImage.fitHeightProperty().bind(rawHGTImagePane.heightProperty());
        rawOSMImage.fitWidthProperty().bind(rawOSMImagePane.widthProperty());
        rawOSMImage.fitHeightProperty().bind(rawOSMImagePane.heightProperty());

        //Définit les actions à adopter en cas de scroll
        shadedImage.setOnScroll(this::handleScrollingZoom);

    }

    /**
     * Appelée par l'application principale pour qu'elle puisse donner au contrôleur
     * une référence à elle-meme
     *
     * @param mainApp
     */
    public void setMainApp(Main mainApp) {this.mainApp = mainApp;
    }

    /**
     * Méthode appelée par les processus pour renseigner le contrôleur sur l'état du programme
     */
    @FXML
    public void updateState(State newState){
        switch(newState){
            case IDLE:
                startRenderingButton.setText("Start rendering");
                startRenderingButton.setDisable(false);
                stateValueLabel.setText("Program is idle");
                break;

            case RENDERING_RAWDATA:
                startRenderingButton.setDisable(true);
                startRenderingButton.setText("Processing, please wait..");
                stateValueLabel.setText("Program is generating raw data, this may take a while..");
                break;

            case RENDERING_SHADEDRELIEF:
                startRenderingButton.setDisable(true);
                startRenderingButton.setText("Processing, please wait..");
                stateValueLabel.setText("Program is computing shaded relief");
                break;

            default:
        }
    }

    /**
     *
     * @return Un String contenant le chemin pour l'output des images
     */
    public String getOutputPath(){
        return outputPathField.getText();
    }

    /**
     * Assigne les cartes générées aux différents conteneurs de l'interface graphique
     * et le viewport (zoom) par défaut
     */
    public void updateDisplay(){
        RenderData maps = mainApp.getRenderedData();
        Image shaded = maps.getFXShaded();
        shadedImage.setImage(shaded);
        rawHGTImage.setImage(maps.getFXRawHGT());
        rawOSMImage.setImage(maps.getFXRawOSM());
        shadedImage.setViewport(new Rectangle2D(0, 0, shaded.getWidth(), shaded.getHeight()));
    }

    public void updateShadedRelief(){
        shadedImage.setImage(mainApp.getRenderedData().getFXShaded());
    }

    /**
     * Est appelée lorsque l'utilisateur clique sur le bouton de sélection du fichier OSM
     */
    @FXML
    private void handleOSMSelect(){
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("OSM or binaries", "*.osm.gz", "*.ser.gz"));
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if(file != null) {
            osmPathField.setText(file.getPath());
            fileChooser.getExtensionFilters().clear();
        }
    }

    /**
     * Est appelée lorsque l'utilisateur clique sur le bouton de sélection du fichier HGT
     */
    @FXML
    private void handleHGTSelect() {
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("1'' HGT files", "*.hgt"));
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
        if (file != null) {
            hgtPathField.setText(file.getPath());
            fileChooser.getExtensionFilters().clear();

            //Pré-remplit les champs de latitude et longitude
            String fileName = file.getName();
            int longSW = ((fileName.charAt(0) == 'N') ? 1 : -1) * Integer.parseInt(fileName.substring(4, 7));  //Longitude
            int latSW = ((fileName.charAt(0) == 'N') ? 1 : -1) * Integer.parseInt(fileName.substring(1, 3));  //Latitude

            bottomLeftLATField.setText(latSW+0.45+"");
            topRightLATField.setText(latSW+0.55+"");
            bottomLeftLONField.setText(longSW+0.4+"");
            topRightLONField.setText(longSW+0.6+"");
        }
    }

    /**
     * Est appelée lorsque l'utilisateur clique sur le bouton de sélection du dossier d'output
     */
    @FXML
    private void handleOutputSelect(){
        File file = directoryChooser.showDialog(mainApp.getPrimaryStage());
        if (file != null) {
            outputPathField.setText(file.getPath());
        }
    }

    /**
     * Collecte les informations entrées et lance le rendu
     */
    @FXML
    private void handleRenderButton(){
        String[] args = new String[9];
        args[0] = osmPathField.getText();
        args[1] = hgtPathField.getText();
        args[2] = bottomLeftLONField.getText();
        args[3] = bottomLeftLATField.getText();
        args[4] = topRightLONField.getText();
        args[5] = topRightLATField.getText();
        args[6] = ""+resolutionSlider.getValue();
        args[6] = args[6].substring(0, args[6].length()-2);
        args[7] = outputPathField.getText()+"\\ImhofRendering.png";
        args[8] = (serializeBox.isSelected()) ? "-serialize": "";

        try {
            mainApp.render(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gère le zoom avec scroll de la molette
     */
    //TODO: Empêcher de pouvoir trop dézoomer

    @FXML
    private void handleScrollingZoom(ScrollEvent event){

        Rectangle2D viewPort = shadedImage.getViewport();
        if(viewPort != null) {

            double scaleValue = (event.getDeltaY() > 0) ? ZOOM_IN_SCALE : ZOOM_OUT_SCALE;
            /*
            * Calcule le ratio entre le nombre de pixels de l'image par rapport à la présentation qui en
            * est faite dans la GUI
            */
            double ratioPaneImage = viewPort.getWidth() / shadedImage.getBoundsInLocal().getWidth();

            double newHeight = viewPort.getHeight() * scaleValue;
            double newWidth = viewPort.getWidth() * scaleValue;

            //Calcule les nouvelles valeurs du viewport pour donner l'effet de zoom centré autour de la souris
            double trueX = (event.getX() * ratioPaneImage) + viewPort.getMinX();
            double newMinX = trueX - ((trueX - viewPort.getMinX()) * scaleValue);
            double trueY = (event.getY() * ratioPaneImage) + viewPort.getMinY();
            double newMinY = trueY - ((trueY - viewPort.getMinY()) * scaleValue);

            Rectangle2D newViewPort = new Rectangle2D(newMinX, newMinY, newWidth, newHeight);

            //Applique le nouveau viewport à chaque image
            shadedImage.setViewport(newViewPort);
            rawHGTImage.setViewport(newViewPort);
            rawOSMImage.setViewport(newViewPort);
        }
    }

    /**
     * Gère la vérification de la résolution avec apparition d'un message d'avertissement si cette dernière
     * est trop élevée
     */
    @FXML
    private void handleResolutionValue(){
        boolean highRes = resolutionSlider.getValue()>400;
        warningIcon.setVisible(highRes);
        warningTooltip.setOpacity((highRes)? 1: 0);
    }

}
