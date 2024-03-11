package org.example;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CheckIfOpen {
    public static int shopIndex = 0;
    public static int size;
    public static Boolean[] ifOpenBoll;
    public static String[] workingHours3;
    public void setSize(int numberOfShops){
        size = numberOfShops;
        ifOpenBoll = new Boolean[size];
        workingHours3 = new String[size];
    }



    public void getInfo(String [] workingHours){
        workingHours3 = workingHours;
    }


    public void IfOpen() {

        String[][] Hours = new String[size][3]; // 3 opcje godzin otwarcia

        for (int i = 0; i < size; i++) {
            // podzielenie całych godzin otwarcia na 3 części
            String[] days = workingHours3[i].split(", ");

            for (String day : days) {
                //podzielenie każdej części na dni i godziny
                String[] dayAndHours = day.split(": ");

                //wpisanie do tablic dni i godzin
                String dayOfWeek = dayAndHours[0];
                String hours = dayAndHours[1];

                // wpisanie do tablicy hours w dobrym miejscu, w zależności od dni tygodnia
                switch (dayOfWeek) {
                    case "Poniedziałek-Piątek":
                        Hours[i][0] = hours;
                        break;
                    case "Sobota":
                        Hours[i][1] = hours;
                        break;
                    case "Niedziela":
                        Hours[i][2] = hours;
                        break;

                }
            }
            String currentDayOfWeek1 = LocalDate.now().getDayOfWeek().toString();
            // wywołanie algorytmu sprawdzającego dla odpowiedniego, dzisiejszego dnia tygodnia
            if(currentDayOfWeek1.equals("SATURDAY")){
                checkCurrentTime(Hours[i][1]);
            }
            else if(currentDayOfWeek1.equals("SUNDAY")){
                checkCurrentTime(Hours[i][2]);
            }
            else{
                checkCurrentTime(Hours[i][0]);
            }

        }
    }


    private static void checkCurrentTime(String hours) {

        if (hours != null) {
                // algorytm sprawdza czy systemowa godzina jest po godzinie otwarcia i przed zamknięcia
                // uzupełnianie tablicy ifOpenBoll odpowiednio wartościami true i false
                String[] hoursRange = hours.split(" - ");
                LocalTime startTime = LocalTime.parse(hoursRange[0], DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime endTime = LocalTime.parse(hoursRange[1], DateTimeFormatter.ofPattern("HH:mm"));
                if(endTime.toString().equals("00:00")){
                    endTime = LocalTime.MAX;
                    // jeśli godzina zamknięcia to 00:00 algorytm popełniał błedy, więc wtedy ustawiamy
                    // endTime na najpóźnijeszą godzine systemową.
                }
                LocalTime currentTime = LocalTime.now();

                if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)) {
                    ifOpenBoll[shopIndex] = true;
                    shopIndex++;
                } else {
                    ifOpenBoll[shopIndex] = false;
                    shopIndex++;
                }

                MyController set = new MyController();
                set.SetIfOpen(ifOpenBoll);
                // przesłanie danych do klasy MyController

        }
    }
}
