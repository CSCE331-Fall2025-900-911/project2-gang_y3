

package boba.shop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The main entry point for the JavaFX application.
 * This class handles loading the initial FXML scene, setting the primary stage, and provides static methods for switching scenes.
 * 
 * @author Sudeep Sirivella and Sonith Guduru
 */
public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    /**
     * The main entry point for all JavaFX applications.
     * This method is called after the init method has returned, and sets up the primary stage with the initial scene.
     *
     * @param stage The primary stage for this application, onto which the application scene can be set.
     * @throws IOException If the FXML file "pos-main.fxml" cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        scene = new Scene(loadFXML("pos-main"), 1000, 700);
        stage.setTitle("Boba Shop POS System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Sets the root of the current scene to the given FXML file.
     *
     * @param fxml The name of the FXML file (without the .fxml extension) to load as the new root.
     * @throws IOException If the FXML file cannot be loaded.
     */
    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Creates and sets a new Scene on the primary stage with a specific width and height. This is used for changing the window size when switching scenes.
     *
     * @param fxml The name of the FXML file (without the .fxml extension).
     * @param width The desired width for the new scene.
     * @param height The desired height for the new scene.
     * @throws IOException If the FXML file cannot be loaded.
     */
    static void setRoot(String fxml, int width, int height) throws IOException {
        scene = new Scene(loadFXML(fxml), width, height);
        primaryStage.setScene(scene);
    }

    /**
     * Loads an FXML file from the resources directory.
     *
     * @param fxml The name of the FXML file (without the .fxml extension).
     * @return The loaded Parent root node of the FXML.
     * @throws IOException If the FXML file cannot be loaded.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * The main method, used to launch the JavaFX application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch();
    }

}
