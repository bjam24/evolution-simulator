package agh.ics.oop;
import javafx.application.Application;

public class World {
    public static void main(String[] arg) {
        /*
           OBSLUGA SYMULACJI
           - przy ustawianu parametrów symulacji priorytet ma wybrana gotowa konfiguracja, jesli uzytkownik preferuje
             ustawienie własnych parametrow powinien wyczyścić GUI za pomoca przycisku a nastepnie wprowadzic dane
           - gotowa konfiguracja ma ustawiony status zapisywania statystyk do pliku (tak lub nie)
           - parametry symulacji sa numeryczne, w przypadku ChoiceBox górna opcja ma wartość 0, a dolna opcja wartość 1
           - po wstrzymaniu symulacji można oznaczyć zwierzę do śledzenia, powoduje to oznaczenie go w czasie pauzy na
             niebiesko oraz uniemozliwienie zaznaczenia innych zwierzat (kolko). Wybrane zwierze odznacza się kilkajac na
             nie ponownie. Mozna wtedy wybrac inne zwierze
           - ponowne wstrzymanie symulacji powoduje, ze pola statystyk zwierzecia sa null
           - statystyki sa zapisywane do utworzonego pliku CSV w resources
        */

        Application.launch(App.class);
    }
}
