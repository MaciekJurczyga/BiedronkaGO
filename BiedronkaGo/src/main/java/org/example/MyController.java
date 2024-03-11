package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Double.parseDouble;

public class MyController implements Initializable  {
    private static final Logger logger = LogManager.getLogger(MyController.class);
    public static int size = 0;
    public static Boolean[] ifOpen;
    public void SetSize(int numberOfShopes){
        size = numberOfShopes;
        ifOpen = new Boolean[size];

    }
    public MyController() {
    }
    public static String[] urlOfShops2 = new String[size];
    public static String[] addresses2 = new String[size];
    public static String[] cities2 = new String[size];
    public static String[] workingHours2 = new String[size];
    public static String[] GPSes2 = new String[size];



    @FXML
    public GridPane gridPane;

    @FXML
    private Button startingButton;

    @FXML
    private Button gpsButton;

    @FXML
    private TextField stopnieN;
    @FXML
    private TextField minutyN;
    @FXML
    private TextField sekundyN;
    @FXML
    private TextField stopnieE;
    @FXML
    private TextField minutyE;
    @FXML
    private TextField sekundyE;
    @FXML
    private Label closestShop;
    @FXML
    private Button confirmData;
    @FXML
    private Button findShopButton;



    @Override // przesłonienie metody o tej samej nazwie z interfejsu Initializable
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // inicjalizacja wszystkich przycisków występujących w interfejsie
        // obłusga każdego przycisku
        if (startingButton != null) {
            // analogicznie do każdego przycisku wywołanie na nim metody slużącej do jego obsługi
            //this::handleStartingButtonClick operator lambda slużący do wywołania metody na tym konkretnym obiekcie
            startingButton.setOnAction(this::handleStartingButtonClick);

        } else {
            fillGridPane();
        }
        if (gpsButton != null) {
            // analogicznie jak wyżej
            gpsButton.setOnAction(this::handleGPSButtonClick);
        }
        if (confirmData != null){
            // analogicznie jak wyżej
            confirmData.setOnAction(this::handleConfirmDataButtonClick);
        }
        if(findShopButton != null){
            // analogicznie jak wyżej
            findShopButton.setOnAction(this::handleFindShopButtonClick);
        }
    }

    private void handleStartingButtonClick(ActionEvent event) {
        Stage mainStage;
        try {
            // obsługa przycisku startingButton, otworzenie okna GridPane6 i ustawienie jego parametrów
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GridPane6.fxml"));

            Parent root = loader.load();
            MyController gridPaneController = loader.getController();
            gridPaneController.setGridPane(gridPane);

            mainStage = new Stage();
            mainStage.setTitle("BiedronkaGo - główny widok");

            mainStage.setScene(new Scene(root));
            mainStage.show();
            mainStage.setMaximized(false);
            mainStage.setResizable(false);

            // Zamykanie okna StartingPannel
            ((Stage) startingButton.getScene().getWindow()).close();
            logger.info("Użytkownik otworzył okno główne i automatycznie zamnkął okno startowe");
            setCloseRequestHandler(mainStage);


        } catch (Exception e) {
            logger.error("Błąd podczas otwierania nowego okna", e);
            // Obsłuż wyjątki, np. nieudane wczytywanie pliku FXML
        }

    }
    private void handleGPSButtonClick(ActionEvent event) {
        Stage gpsStage;
        try {
            // metoda obsługi GPSButton, służy do otwierania okna GPSPanel3
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/GPSPanel3.fxml"));
            Parent root = loader.load();
            // ustawianie parametrów sceny
            gpsStage = new Stage();
            gpsStage.setTitle("BiedronkaGo - GPS");
            gpsStage.setScene(new Scene(root));
            gpsStage.show();
            gpsStage.setMaximized(false);
            gpsStage.setResizable(false);
            logger.info("Użytkownik kliknął przycisk oraz otworzył okno do szukania najbliższego sklepu.");


        } catch (Exception e) {
            logger.error("Błąd podczas otwierania nowego okna GPS", e);
        }
    }
    private void handleConfirmDataButtonClick(ActionEvent event){
        //obługa przycisku do potwierdazania koordynatów
        logger.info("użytkownik kliknął przycisk potwierdzający wpisanie swoich koordynatów");
        calcuclateClosestShop caluclate = new calcuclateClosestShop();
        if(getUserLoc()) {
            logger.info("Użytkownik wprowadził porpawne dane GPS");
            // wywołanie metody klasy calculateClosetShop w celu znalezienia najbliższego sklepu
            int index = caluclate.shortestPath();
            // wpisanie do labelu najbliższego znalezionego sklpeu
            closestShop.setText("Najbliższa biedronka znajduje się na ulicy: " + addresses2[index]);
        }
        else{
            logger.warn("Użytkownik wprowadził niepoprawne dane GPS");
        }
    }
    private void handleFindShopButtonClick(ActionEvent event){
        // obsługa przycisku FindShopButton, otwieranie okna SearchPane
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/SearchPane.fxml"));
            Parent root = loader.load();
            //ustawianie parametrów sceny
            Stage searchStage;
            searchStage = new Stage();
            searchStage.setTitle("BiedronkaGo - przeszukiwanie sklepów");
            searchStage.setScene(new Scene(root));
            searchStage.show();
            searchStage.setMaximized(false);
            searchStage.setResizable(false);
            logger.info("Otwarto okno do przeszukiwania sklepów");

        }
        catch(Exception e){
            logger.error("Błąd poczas otwierania okna SearchPane", e);
        }

    }
    public void setCloseRequestHandler(Stage stage) {
        // obsługa zamknięcia aplikacji
        stage.setOnCloseRequest(this::handleApplicationClose);
    }

    private void handleApplicationClose(WindowEvent event) {
        // Logowanie informacji o zamknięciu aplikacji
        logger.info("Użytkownik zamknął aplikacje używając domyślnego przyicsku");
    }
    public void fillGridPane() {
        if (gridPane == null) {
            return;
        }
        // wpisanie danych do siatki znajdującej się w scene GridPane6
        String[] titles = {"Sklep", "Miasto", "Adres", "Godziny otwarcia", "GPS", "Otwarte"};
        // wpisanie tytułów kolumn
        for (int i = 0; i < titles.length; i++) {
            Label label = new Label(titles[i]);
            label.setStyle("-fx-font-size: 30; -fx-font-weight: bold; -fx-scale-shape: false;");
            label.setFont(new Font("System", 30));


            gridPane.add(label, i, 0);
            GridPane.setHalignment(label, HPos.CENTER);
        }
        //wpisanie do odpowiednich komórek danych oraz wyśrodkowanie ich
        for (int i = 0; i < size; i++) {
            Label label = new Label((i+1)+". "+"Biedronka" );
            gridPane.add(label, 0, i + 1);
            label.setAlignment(Pos.CENTER);
            GridPane.setHalignment(label, HPos.CENTER);
            GridPane.setValignment(label, VPos.CENTER);
        }
        logger.info("Wpisano informacje o sklepie do siatki");
        // Dodajemy dane z pozostałych tablic do odpowiednich kolumn

        for (int i = 0; i < size; i++) {
            Label labelAddress = new Label(addresses2[i]);
            gridPane.add(labelAddress, 2, i + 1);
            labelAddress.setAlignment(Pos.CENTER);
            GridPane.setHalignment(labelAddress, HPos.CENTER);
            GridPane.setValignment(labelAddress, VPos.CENTER);

            Label labelCity = new Label(cities2[i]);
            gridPane.add(labelCity, 1, i + 1);
            labelCity.setAlignment(Pos.CENTER);
            GridPane.setHalignment(labelCity, HPos.CENTER);
            GridPane.setValignment(labelCity, VPos.CENTER);

            Label labelWorkingHours = new Label(workingHours2[i]);
            gridPane.add(labelWorkingHours, 3, i + 1);
            labelWorkingHours.setAlignment(Pos.CENTER);
            GridPane.setHalignment(labelWorkingHours, HPos.CENTER);
            GridPane.setValignment(labelWorkingHours, VPos.CENTER);


            Label labelGPS = new Label(GPSes2[i]);
            gridPane.add(labelGPS, 4, i + 1);
            labelGPS.setAlignment(Pos.CENTER);
            GridPane.setHalignment(labelGPS, HPos.CENTER);
            GridPane.setValignment(labelGPS, VPos.CENTER);
            ImageView imageView = new ImageView();
            // sprawdzenie czy sklep jest otwarty i wklejenie odpowiedniego obrazu
            if (ifOpen[i]) {
                Image trueImage = new Image("/TAK4_2.png");
                imageView.setImage(trueImage);
            } else {
                // Obraz dla false
                Image falseImage = new Image("/NIE4_2.png");
                imageView.setImage(falseImage);
            }
            // wklejenie odpowiedniego obrazu
            gridPane.add(imageView, 5, i + 1);
            GridPane.setHalignment(imageView, HPos.CENTER);
            GridPane.setValignment(imageView, VPos.CENTER);

        }

    }
    public void setGridPane(GridPane gridPane) {
        this.gridPane = gridPane;
    }
    public void SetIfOpen(Boolean[] ifOpen1){
        ifOpen = ifOpen1;
    }

    public void SetArrays(String[] urlOfShops, String[] addresses, String[] cities, String[] workingHours, String[] GPSes) {
        // przesłanie danych z klasy Scrapp do klasy MyController
        urlOfShops2 = urlOfShops;
        addresses2 = addresses;
        cities2 = cities;
        workingHours2 = workingHours;
        GPSes2 = GPSes;

    }
    public boolean getUserLoc(){
        // pobieranie danych wpisnaych przez użytkownika
        String stopnieNorth = stopnieN.getText();
        String minutyNorth = minutyN.getText();
        String sekundyNorth = sekundyN.getText();

        String stopnieEast = stopnieE.getText();
        String minutyEast = minutyE.getText();
        String sekundyEast = sekundyE.getText();
        calcuclateClosestShop calc = new calcuclateClosestShop();
        if (isNumeric(stopnieNorth) || isNumeric(minutyNorth) || isNumeric(sekundyNorth) ||
                isNumeric(stopnieEast) || isNumeric(minutyEast) || isNumeric(sekundyEast) || (!isValidCoordinate(stopnieNorth, minutyNorth, sekundyNorth)) || !isValidCoordinate(stopnieEast, minutyEast, sekundyEast)) {
            // wyskakuje ostrzeżenie jesli wpisane dane są złe
            // wpisanie do labela informacji o niepoprawnych danych
            // zabieg w takim celu, ze jesli użytkownik wpisuje kilka lokalizacji, to może cały czas widniec stary, znaleziony sklep
            closestShop.setText("Niepoprawne dane. Pamiętaj, żeby liczbach zmiennoprzecinkowych używać '.' a nie ','");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Niepoprawne dane");
            alert.setContentText("Wprowadź poprawne liczby w pola tekstowe.");
            alert.showAndWait();
            return false;
        }


        else {
            //przesłanie poprawnych danych do klasy cacluateClosestShop
            calc.setter(stopnieNorth, minutyNorth, sekundyNorth, stopnieEast, minutyEast, sekundyEast, GPSes2);
        }
        return true;
    }


    // metoda do sprawdzania czy wpisane koordynaty gps są poprawnie wpisanymi liczbami
    private boolean isNumeric(String str) {
        try {
            parseDouble(str);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
    private boolean isValidCoordinate(String degrees, String minutes, String seconds) {
        try {
            double d = Double.parseDouble(degrees);
            double m = Double.parseDouble(minutes);
            double s = Double.parseDouble(seconds);

            if (d < -180 || d > 180 || m < 0 || m > 60 || s < 0 || s > 60) {
                return false;
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }
}



