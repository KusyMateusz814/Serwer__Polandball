package server_pliki;

import java.io.BufferedReader;
import java.io.FileReader;
import static server_pliki.ParseConfigFile.*;
import static server_pliki.ParseLevelFile.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * klasa obslugujaca zlecenia od klienta otrzymane przez serwer
 */
public class ServerAction {

    /**
     * Napis, zalezny od odpowiedzi serwera
     */

    public static String serverflag_;

    public static String bufor[] = new String[2];


    /**
     * funkcja oblsugujaca zadania od klienta
     *
     * @param command zadanie klienta
     * @return odpowiedz serwera
     */
    public static String ServerResponse(String command) {

        String servercommand = command;//przypisanie otrzymanego zadanie do zmiennej
        String servermessage;//wiadomosc zwracana przez metode

        //obsluga zadan na podstawie otrzymanej wiadomosci od klienta
        switch (servercommand) {
            /*case "LOGIN":
                servermessage=login();//wywolanie metody zwracajacaj potwierdzenie o zalogowaniu
                break;*/
            case "GET_HIGHSCORES":
                servermessage = GetHighscores();//wywolanie metody GetHighscores, pobranie listy najlepszych wynikow
                serverflag_ = "HIGHSCORES: ";
                break;
            case "GET_CONFIGFILE":
                servermessage = GetConfigfile();//wywolanie metody ladujacej sparsowane zmienne do napisu-budowanie wiadomosci
                serverflag_ = "CONFIGFILE: ";
                break;
            default:
                try {
                    //robienie na chama troche, ale dziala, switch musi miec const przypadek a levele rozne bedziemy mieli
                    WhichLevel(command);//wywolanie metody odczytujacej dane ktorego levela trzeba wyslac
                    StringBuilder stringbuilder = new StringBuilder();
                    stringbuilder.append("GET_LEVEL: " + bufor[1]);//tworzenie Stringa, zeby porownac z wiadomoscia
                    String makecommandlevel = stringbuilder.toString();//konwersja na String
                    System.out.println(makecommandlevel);
                    if (servercommand.equals(makecommandlevel)==true) {//sprawdzenie odebrane zadanie dotyczy levela
                        servermessage = GetLevelConfig(WhichLevel(command));//wywolanie metody tworzacej dane poziomu do wyslania
                        serverflag_= "LEVEL: " +bufor[1]+": ";
                    } else {
                        throw new Exception("Nieznana komenda");//rzucenie wyjatku, jak nie wiadomo jak komenda
                    }
                }
                catch (Exception e){
                    System.out.println(e);
                    servermessage = "INVALID_COMMAND";//w przypadku nieznanego zadania zostanie wyswietlona informacja o nieznaje komendzie
                    serverflag_= "GET_LEVEL: " +bufor[1];
                }
                break;
        }
        return servermessage;//zwrocenie wiadomosci serwera przez metode ServerResponse
    }

    /*
     * funkcja obslugujaca logowanie klientow
     * @return odpowiedz serwera
     */
   /* private static String login(){
        String servermessage;//wiadomsoc serwera
        if(serveron) {//sprawdzenie czy serwer moze przyjac klienta
            servermessage="LOGGED_IN "+clientid+"\n";//w przypadku zaakceptoawnia klienta serwer wysyla odpowiedz
            //LOGGED_IN i informacje o przydzielonym id klienta
            clientid++;//zwiekszenie id kolejnych zalogowanych klientow
        }
        else{
            servermessage="CONNECTION_REJECTED";//odrzucenie polaczenia, w przypadku wylaczenia mozliwosci przyjmowania
        }                                       //klientow
        return servermessage;//zwrocenie wiadomosci serwera przez metode login
    }*/


    /**
     * funkcja wczytujaca liste najlepszych wynikow
     *
     * @return zwraca wiadomosc (tekst) zawierajaca liste najlepszych wynikow
     */

    private static String GetHighscores() {
        //plik tekstowy zawierajacy liste najlepszych wynikow jest pakowany do stringa, aby mozna bylo go wyslac jako
        //wiadomosc tekstowa
        StringBuilder stringbuilder = new StringBuilder();
        String currentline;
        try (
                //wczytywanie kolejnych linijek pliku i dodawanie ich do zmiennej stringbuilder
                //co do pliku tekstowego
                //znak " / " oddziela kazdy wynik rogrywki i nick od reszty
                //znak " _ " oddziela dany wynik od gracza, ktory go uzyskal
                BufferedReader buildreader = new BufferedReader(new FileReader("src\\server_pliki\\highscores.txt"))) {
            while ((currentline = buildreader.readLine()) != null) {
                stringbuilder.append(currentline);
            }
        } catch (Exception e) {
            System.err.println("Nastapil niespodziewany blad");
            System.err.println(e);
        }
        //dodanie znaku konca wiadomosci
        stringbuilder.append("\n");
        return stringbuilder.toString();//zbudowany ciag konertowany na string i zwracany przez metode
    }

    /**
     * Funkcja budujaca napis ze zmiennych sparsowanych w klasie ParseConfigfile
     *
     * @return wiadomosc (tekst) zawierajaca podstawowe parametry aplikacji
     */
    private static String GetConfigfile() {
        //wywolanie metody z klasy parsujacej plik
        ParseConfigFile.ParseConfig();
        //zmienna, na ktorej bedziemy budowac wiadomsosc tekstowa
        StringBuilder stringbuilder = new StringBuilder();
        //dodanie do bufora napisow wszystkich sparsowanych zmiennych
        for (int i = 0; i < configbufor.length; i++) {
            stringbuilder.append(configbufor[i] + " ");//kazda liczbe(napis) oddziela spacja
        }
        //usuwamy ostatni " ", zeby latwiej sie obrabialo
        stringbuilder.delete(stringbuilder.length() - 1, stringbuilder.length());
        //dodanie znaku konca wiadomosci
        stringbuilder.append("\n");
        return stringbuilder.toString();//zbudowany ciag konertowany na string i zwracany przez metode
    }

    /**
     * Funkcja budujaca napis ze zmiennych sparsowanych w klasie ParseLevelFile
     *
     * @return wiadomosc (tekst) zawierajaca podstawowe parametry danego poziomu
     */

    private static String GetLevelConfig(int number_of_level) {
        try {//jak metoda WhichLevel sie dobrze wykonala to nastepuje tworzenie wiadomosci
            if(number_of_level !=-1) {
                //wywolanie metody z klasy parsujacej plik
                ParseLevelFile.ParseLevelFile(number_of_level);///------------>ZMIENIC<--------------
                //zmienna, na ktorej bedziemy budowac wiadomsosc tekstowa
                StringBuilder stringbuilder = new StringBuilder();
                //dodanie do bufora napisow wszystkich sparsowanych zmiennych
                for (int i = 0; i < levelbufor.length; i++) {
                    stringbuilder.append(levelbufor[i] + " ");//kazda liczbe(napis) oddziela spacja
                }
                //usuwamy ostatni " ", zeby latwiej sie obrabialo
                stringbuilder.delete(stringbuilder.length() - 1, stringbuilder.length());
                //dodanie na poczatku znak "&", bo ta wiadomosc bedzie sklejana z inna
                stringbuilder.append("&");
                for (int i = 0; i < buforrow.length; i++) {
                    stringbuilder.append(buforrow[i] + "%");//kazda liczbe(napis) oddziela "%
                }
                //usuwamy ostatni "%", zeby latwiej sie obrabialo
                stringbuilder.delete(stringbuilder.length() - 1, stringbuilder.length());
                //dodanie znaku konca wiadomosci
                stringbuilder.append("\n");

                return stringbuilder.toString();//zbudowany ciag konertowany na string i zwracany przez metode
            }
        }catch (Exception e){
            System.out.println(e + "Blad metody GetLevelConfig");
        }
        return "Blad jednej z funkcji serwera";
    }

    /**
     * Metoda zwracajaca numer poziomu
     * @param command zadanie klienta
     * @return numera poziomu
     */
    private static int WhichLevel(String command)
    {
        try {
            //bufor do rozdzielenia znakow zadania
            bufor = command.split(" ");
            //System.out.println(bufor[0]);
            //System.out.println(bufor[1]);
            //wyluskanie i konwersja numeru levela
            int numer_poziomu = Integer.valueOf(bufor[1]);
            return numer_poziomu;
        }catch(Exception e){
            System.out.println(e + "Blad metody WhichLevel");
        }
        return -1;//jak to nie -1, to zostanie zlapane
    }
}

