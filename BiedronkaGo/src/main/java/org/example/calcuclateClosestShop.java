package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class calcuclateClosestShop {
    private static final Logger logger = LogManager.getLogger(calcuclateClosestShop.class);
    public static String stopnieNorth1;
    public static String minutyNorth1;
    public static String sekundyNorth1;

    public static String stopnieEast1;
    public static String minutyEast1;
    public static String sekundyEast1;

    public static String[] GPSes;
    public static double minDistance = Double.POSITIVE_INFINITY;
    public static int index = 0;


    // metoda do pobierania danych z klasy MyController
    public void setter(String stopnieNorth, String minutyNorth, String sekundyNorth, String stopnieEast, String minutyEast, String sekundyEast, String[] gpses) {
        stopnieNorth1 = stopnieNorth;
        minutyNorth1 = minutyNorth;
        sekundyNorth1 = sekundyNorth;
        stopnieEast1 = stopnieEast;
        minutyEast1 = minutyEast;
        sekundyEast1 = sekundyEast;
        GPSes = gpses;
    }


    //metoda do zamiana stopni na liczby dziesiętne
    private static double degreesToDecimal(String degrees, String minutes, String seconds) {
        double d = Double.parseDouble(degrees);
        double m = Double.parseDouble(minutes);
        double s = Double.parseDouble(seconds);

        return d + (m / 60.0) + (s / 3600.0);
    }



    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Średnica Ziemi w kilometrach
        // różnica w dystancie między dwoma punktami
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        // korzystamy ze wzoru na obliczenie odległości między dwoma punktami
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Odległość w kilometrach
    }



    public int shortestPath() {
        double userLat = degreesToDecimal(stopnieNorth1, minutyNorth1, sekundyNorth1);
        double userLon = degreesToDecimal(stopnieEast1, minutyEast1, sekundyEast1);
        for(int i=0; i<GPSes.length; i++){
            if(GPSes[i].equals("Brak danych o lokalizacji")){
                continue;
            }
            String[] shopCoordsArray = GPSes[i].split("[^\\d.]+"); //separatorem są wszystkie znaki które nie są liczbami

            double shopLat = degreesToDecimal(shopCoordsArray[0], shopCoordsArray[1], shopCoordsArray[2]);
            double shopLon = degreesToDecimal(shopCoordsArray[3], shopCoordsArray[4], shopCoordsArray[5]);
            double distance = calculateDistance(userLat, userLon, shopLat, shopLon);

            if(distance < minDistance){
                minDistance = distance;
                index = i;
            }
        }
        logger.info("znaleziono najbliższy sklep na podstawie danych GPS wpisanych przez użytkownika");
        minDistance = Double.POSITIVE_INFINITY;
        return index; // zwracamy indeks w celu wpisania odpowiedniej biedronki do siatki GPSPane5
    }
}