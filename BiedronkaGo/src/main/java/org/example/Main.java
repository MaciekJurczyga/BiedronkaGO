package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.IIOException;

public class Main extends Application {
    // dodanie log4j do klasy
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // metoda start służy do zaczynania aplikacji
        try {
            // Ładowanie pliku FXML

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/startingPanel.fxml"));
            Parent root = loader.load();


            // Ustawianie sceny
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setMaximized(false);
            // Ustawianie tytułu okna
            primaryStage.setTitle("BiedronkaGo");

            // Pokazanie okna
            primaryStage.show();
            // utworzenie obiektu w celu wywołania metody setCloseRequestHandler na obiekcie primaryStage
             MyController myController = new MyController();
             myController.setCloseRequestHandler(primaryStage);


        }
        catch (IIOException e){
            logger.error("Błąd podczas otwierania okna startowego", e);
        }
    }

}