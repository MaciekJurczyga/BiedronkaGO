package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ShopInfoWindowController {


    @FXML
    private Label gpsLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label openingHoursLabel;

    @FXML
    private Label nameOfPromo;

    @FXML
    private Label priceBefore;

    @FXML
    private Label priceAfter;

    @FXML
    private VBox vbox;

    @FXML
    private VBox vbox2;
    // stowrzenie tablic na dodatkowe dane o promocji
    public static String[] nameOfPromo2 = new String[MyController.size];
    public static String[] priceBefore2 = new String[MyController.size];
    public static String[] priceAfter2 = new String[MyController.size];


    public void setShopInfo(int selectedShopIndex) {
        // wpisanie danych do Labeli odpowiadającego sklepu opisanego przez selectedShopIndex
        gpsLabel.setText(" " + MyController.GPSes2[selectedShopIndex]);
        addressLabel.setText(" " +MyController.addresses2[selectedShopIndex].split(",")[0]);
        cityLabel.setText(" " + MyController.cities2[selectedShopIndex]);
        openingHoursLabel.setText(" " +MyController.workingHours2[selectedShopIndex]);
        nameOfPromo.setText(" " +nameOfPromo2[selectedShopIndex]);
        priceBefore.setText(" " +priceBefore2[selectedShopIndex].split(":")[1]);
        priceAfter.setText(" " +priceAfter2[selectedShopIndex].split(":")[1]);

        //Ustawienie rozmiaru czczionki, stylu, oraz koloru tła dla wpisanych informacji
        gpsLabel.setStyle("-fx-font-size: 15; -fx-font-style: italic;");
        addressLabel.setStyle("-fx-font-size: 15; -fx-font-style: italic;");
        cityLabel.setStyle("-fx-font-size: 15; -fx-font-style: italic;-fx-background-color: #e5e1e1;");
        openingHoursLabel.setStyle("-fx-font-size: 15; -fx-font-style: italic;-fx-background-color: #e5e1e1;");
        nameOfPromo.setStyle("-fx-font-size: 15; -fx-font-style: italic;-fx-background-color: #e5e1e1;");
        priceBefore.setStyle("-fx-font-size: 15; -fx-font-style: italic;");
        priceAfter.setStyle("-fx-font-size: 15; -fx-font-style: italic;-fx-background-color: #e5e1e1;");
        vbox.setStyle("-fx-border-width: 3 3 0 0; -fx-border-color: black;");
        vbox2.setStyle("-fx-border-width: 3 0 0 0; -fx-border-color: black;");
    }


    public void setPromotions(String[] name, String[] priceBef, String[] priceAft){
        // pobieranie danych z klasy Scrapp
        nameOfPromo2 = name;
        priceBefore2 = priceBef;
        priceAfter2 = priceAft;
    }
}
