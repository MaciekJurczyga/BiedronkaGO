package org.example;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SearchController  {
    // dodanie log4j do klasy SearchController
    private static final Logger logger = LogManager.getLogger(SearchController.class);

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> resultListView;

    @FXML // metoda do pobierania wpisanego ciągu znaków/znaku przez użytkownika
    private void handleSearchButtonClick() {
        String query = searchField.getText();
        searchShops(query);
    }

    @FXML
    private void searchShops(String query) {
        // stworzenie listy, która będzie zawierac sklepy
        // ktróch adresy będa pokrywac się z znakami wpisanymi przez użytkownika
        ObservableList<String> searchResults = FXCollections.observableArrayList();
        // przeszukanie sklepów przez adresy, i dodanie do searchResults tych które zawierają odpowiedni ciąg znaków
        for (int i = 0; i < MyController.size; i++) {
            if (MyController.addresses2[i].toLowerCase().contains(query.toLowerCase())) {
                searchResults.add("Biedronka " + (i + 1) + " - " + MyController.addresses2[i]);
            }
        }
        // dodanie do resultListView znalezionych sklepów
        resultListView.setItems(searchResults);
        // logowanie informacji o wpisaniu do ObserbavleList
        logger.info("Wpisano do ObservableList ulice, które zawierają podany przez użytkownika ciąg liter");

        //obsługa zdarzenia kliknięcia w wybrany sklep
        resultListView.setOnMouseClicked(event -> {
            String selectedShop = resultListView.getSelectionModel().getSelectedItem();
            if (selectedShop != null) {
                int selectedShopIndex = Integer.parseInt(selectedShop.split(" ")[1]) - 1;
                openShopInfoWindow(selectedShopIndex);
                // wywołanie metody do otwierania okna ShopInfoWindow
                // jako argument podajemu indeks wybranego sklepu, aby wpisac odpowiednie dane
            }
        });
    }


    // metoda do okna ShopInfoWindow
    private void openShopInfoWindow(int selectedShopIndex) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ShopInfoWindow.fxml"));
            Parent root = fxmlLoader.load();
            // odwołanie się do klasy kontrollera, w celu wpisaniu w oknie danych sklepu odpowiadającemu selectedShopIndex
            ShopInfoWindowController shopInfoController = fxmlLoader.getController();
            shopInfoController.setShopInfo(selectedShopIndex);
            // ustawienie sceny
            Stage stage = new Stage();
            stage.setTitle("Informacje o Biedronce na ulicy: " + MyController.addresses2[selectedShopIndex].split(",")[0]);
            stage.setScene(new Scene(root));
            stage.show();
            stage.setResizable(false);
            stage.setMaximized(false);
        } catch (Exception e) {
            // obsługa błędy podczas otwierania okna
            logger.error("Błąd podczas otwierania okna ShopInfoWindow");
        }
    }
}

