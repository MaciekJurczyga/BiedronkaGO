package org.example;

import javafx.application.Application;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.helper.ValidationException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


public class Scrapp  {
    // dodanie log4j do klasy scrapp
    private static final Logger logger = LogManager.getLogger(Scrapp.class);
    public static boolean isConnceted = true;
    public static void main(String[] args) {

        String[] urls = {
                "https://www.promoceny.pl/sklepy/szukaj/biedronka/malopolskie/krakow/",
                "https://www.promoceny.pl/sklepy/szukaj/biedronka/malopolskie/krakow/p/2/",
                "https://www.promoceny.pl/sklepy/szukaj/biedronka/malopolskie/krakow/p/3/"
        };

        // Zmienna do zapisu ilości sklepów

        int numberOfShops = 0;

        // Pierwszę otwarcie strony w celu zliczenia ilości wszystkich sklepów, obsługujemy brak plików url i błąd podczas ich pobierania

        for (String url : urls) {
            try {
                Document document = Jsoup.connect(url).timeout(6000).get();
                Elements body = document.select("tbody");
                numberOfShops += body.select("tr").size();
            } catch (ValidationException ve) {
                // Obsługa wyjątku ValidationException - czy dane są poprawne, niekompletne, niedozowolne znaki, przerkaczają dany zakres itp
                logger.error("ValidationException while connecting to {}: {}", url, ve.getMessage());
                logger.fatal("Fatal error podczas łączenia się do {}: {}", url, ve.getMessage());
                isConnceted = false;
            } catch (IOException e) {
                // Pozostała obsługa IOException
                logger.warn("Warning: Pojawiło się IOException dla  {}: {}", url, e.getMessage());
                isConnceted = false;
            }

        }

        // Tworzymy dynamiczne tablice przechowujące wszystkie porządane przez nas informacje

        String[] urlOfShops = new String[numberOfShops];
        String[] addresses = new String[numberOfShops];
        String[] cities = new String[numberOfShops];
        String[] workingHours = new String[numberOfShops];
        String[] GPSes = new String[numberOfShops];
        String[] nameOfPromos = new String[numberOfShops];
        String[] beforePrices = new String[numberOfShops];
        String[] afterPrices = new String[numberOfShops];
        int shopIndex = 0;

        // Pobieranie porządanych danych i uzupełnianie talbic

        for (String url : urls) {
            try {
                Document document = Jsoup.connect(url).timeout(6000).get();
                Elements trElements = document.select("tbody tr");

                for (Element trElement : trElements) {
                    try {

                        // Pobieranie  URL sklepu w celu pobrania dokładniejszych danych
                        // Kazdy sklep ma osobną stronę do ich wyświetlenia, i stąd też są pobierane

                        String urlOfShop = trElement.select("td a").attr("href");
                        if (urlOfShop.isEmpty()) {
                            urlOfShops[shopIndex] = null;
                        } else {
                            urlOfShops[shopIndex] = "https://www.promoceny.pl" + urlOfShop;
                            logger.debug("Udało się zdobyć URL Biedronki numer {}", (shopIndex + 1));
                        }
                    } catch (Exception e) {
                        logger.error("Błąd podczas pobierania url", e);
                        logger.fatal("Fatal error occurred while retrieving URL for Biedronka numer {}: {}", (shopIndex + 1), e.getMessage());
                    }

                    try {

                        // Pobieranie adresu/ulicy

                        String address = trElement.select("td").get(1).text();
                        addresses[shopIndex] = address;
                        logger.debug("Udało się pobrać informacje o adresie dla biedornki numer {}", (shopIndex + 1));
                    } catch (Exception e) {
                        logger.error("Błąd podczas pobierania adresu", e);
                    }

                    try {

                        // Pobieranie Godzin pracy Sklepu

                        String workHours = trElement.select("td").get(4).text();
                        workingHours[shopIndex] = workHours;
                        logger.debug("Udało się pobrać informacje o godzinach otwarcia dla biedornki numer {}", (shopIndex + 1));
                    } catch (Exception e) {
                        // Obsługa błędu - logowanie informacji o błędzie
                        logger.error("Błąd podczas pobierania godzin pracy", e);
                    }

                    try {

                        // Pobieranie nazwy Miasta

                        String city = trElement.select("td").get(2).text();
                        cities[shopIndex] = city;
                        logger.debug("Udało się pobrać informacje o nazwie miasta dla biedornki numer {}", (shopIndex + 1));
                    } catch (Exception e) {
                        // Obsługa błędu - logowanie informacji o błędzie
                        logger.error("Błąd podczas pobierania Miasta", e);
                    }
                    shopIndex++;
                }
            // to samo co wyżej
            } catch (ValidationException ve) {
                logger.error("ValidationException while connecting to {}: {}", url, ve.getMessage());
            } catch (IOException e) {
                logger.error("Błąd podczas pobierania dokumentu HTML22.", e);
            }
        }

        shopIndex = 0;

        // Pobieranie GPS z poszczególnych stron URL

        for (String urlShop : urlOfShops) {
            try {
                Document document = Jsoup.connect(urlShop).timeout(6000).get();
                int numberOfTr = document.select("tr").size();
                if (numberOfTr < 5) {
                    GPSes[shopIndex] = "Brak danych o lokalizacji";
                } else {
                    try {

                        // Pobieranie adresu GPS ze strony

                        Element trElements = document.select("tbody tr").get(4);
                        String GPS = trElements.select("td").text();
                        if (GPS.isEmpty()) {
                            logger.warn("Błąd: Brak danych GPS dla sklepu o indeksie " + (shopIndex+1));
                            GPSes[shopIndex] = "Brak danych o lokalizacji";
                        } else {
                            GPSes[shopIndex] = GPS;
                            logger.debug("Udało się pobrac informacje dotyczące GPS dla biedronki numer " + (shopIndex + 1));
                        }
                    } catch (Exception e) {
                        logger.error("Błąd podczas pobierania danych GPS.", e);
                        GPSes[shopIndex] = "Brak danych o lokalizacji";
                    }

                }
                try{
                    Element ulElements = document.select("ul").get(4);
                    String nameOfPromo = ulElements.select("li a").attr("title");
                    nameOfPromos[shopIndex] = nameOfPromo;
                    logger.info("Udało się pobrac nazwe promocji dla biedronki " +(shopIndex+1));
                } catch (Exception e) {
                    logger.error("Błąd podczas pobierania nazwy promocji", e);
                }
                try {
                    Element ulElements = document.select("ul").get(4);

                    // Obsługa NullPointerException dla starej ceny
                    Element beforePriceElement = ulElements.select(".product-price-old").first();
                    if (beforePriceElement != null) {
                        String priceTextBefore = beforePriceElement.text();
                        beforePrices[shopIndex] = priceTextBefore;
                        logger.info("Udało się pobrac cenę przed promocją dla bierdonki " + (shopIndex+1));
                    } else {
                        beforePrices[shopIndex] = "Brak danych o cenie przed promocją";
                        logger.warn("Nie znaleziono elementu '.product-price-old' dla starej ceny w sklepie {}", shopIndex);
                    }
                } catch (Exception e) {
                    logger.error("Błąd podczas pobierania starej ceny.", e);
                }

                try {
                    Element ulElements = document.select("ul").get(4);

                    // Obsługa NullPointerException dla nowej ceny
                    Element afterPriceElement = ulElements.select(".product-price").first();
                    if (afterPriceElement != null) {
                        String priceTextAfter = afterPriceElement.text();
                        afterPrices[shopIndex] = priceTextAfter;
                        logger.info("Udało się pobrac informacje o cenie po promocji dla biedronki " +(shopIndex+1));
                    } else {
                        afterPrices[shopIndex] = "Brak danych o cenie po promocji";
                        logger.warn("Nie znaleziono elementu '.product-price' dla nowej ceny w sklepie {}", shopIndex);
                    }
                } catch (Exception e) {
                    logger.error("Błąd podczas pobierania nowej ceny.", e);
                }
                shopIndex++;

            } catch (ValidationException ve) {
                logger.error("ValidationException while connecting to {}: {}", urlShop, ve.getMessage());
                GPSes[shopIndex] = "Błąd pobierania dokumentu HTML dla danych GPS";
                shopIndex++;
            } catch (IOException e) {
                logger.error("Błąd podczas pobierania dokumentu HTML dla danych GPS.", e);
                GPSes[shopIndex] = "Błąd pobierania dokumentu HTML dla danych GPS";
                shopIndex++;
            }
        }

        // Wpisywanie informacji pobranych ze strony do Pliku app.log

        for (int i = 0; i < shopIndex; i++) {
            logger.info("Biedronka numer {}", (i + 1));
            logger.info("Adres: {}", addresses[i] != null ? addresses[i] : "Brak danych o adresie");
            logger.info("Miasto: {}", cities[i] != null ? cities[i] : "Brak danych o Mieście");
            logger.info("Godziny otwarcia: {}", workingHours[i] != null ? workingHours[i] : "Brak danych o godzinach pracy");
            logger.info("GPS: {}", GPSes[i] != null ? GPSes[i] : "Brak danych o GPS");
            logger.info("URL sklepu: {}", urlOfShops[i] != null ? urlOfShops[i] : "Brak danych o URL");
            logger.info("Nazwa produktu: {}", nameOfPromos[i] != null ? nameOfPromos[i]: "Brak danych o nazwie");
            logger.info("{}", beforePrices[i] != null ? beforePrices[i]: "Brak danych o starej cenie");
            logger.info("{}", afterPrices[i] != null ? afterPrices[i]: "Brak danych o nowej cenie");
            logger.info("------------------------------------------");
        }


        MyController myController = new MyController();
        myController.SetSize(numberOfShops); // przesłanie rozmiaru tablicy do klasy MyController
        myController.SetArrays( urlOfShops, addresses,  cities,  workingHours,  GPSes); // przesłanie tablic z danymi do klasy MyController
        ShopInfoWindowController setter = new ShopInfoWindowController();
        CheckIfOpen checker = new CheckIfOpen(); // przesłanie odpowiednich danych do klasy CheckIfOpen i wywołanie alogrytmu
        checker.setSize(numberOfShops);
        checker.getInfo(workingHours);
        checker.IfOpen();
        setter.setPromotions(nameOfPromos, beforePrices, afterPrices); // przesłanie dodatkowych danych do klasy MyController
        if(isConnceted) {
            Application.launch(Main.class, args); // uruchomienie interfejsu graficznego stworoznego w javafx, main temu bo zawiera metode start
        }

    }


}