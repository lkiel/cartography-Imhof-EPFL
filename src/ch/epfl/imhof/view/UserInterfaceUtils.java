package ch.epfl.imhof.view;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;

public final class UserInterfaceUtils {

    private UserInterfaceUtils(){}

    /**
     * Méthode affichant un message d'erreur
     * @param message Une description de l'erreur
     */
    public static void displayErrorMessage(String message){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle("Error");
            alert.showAndWait();
        });
    }

    /**
     * Méthode affichant un message d'avertissement
     * @param message Une description de l'avertissement
     */
    public static void displayWarningMessage(String message){
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle("Warning");
            alert.showAndWait();
        });
    }

    /**
     * Méthode affichant un message d'information
     * @param message
     */
    public static void displayInfoMessage(String message){
        Platform.runLater(() ->{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
            alert.setHeaderText(null);
            alert.setTitle("Info");
            alert.showAndWait();
        });
    }
}
