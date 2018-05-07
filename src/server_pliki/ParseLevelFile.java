package server_pliki;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;
/**
 * Klasa odpowiedzialna za parsowanie pliku serwera z parametrami konkretnego poziomu
 */
public class ParseLevelFile {

    /**
     * Ilosc kolumn
     **/

    public static String Amountofcolumns;

    /**
     *Ilosc wierszy
     **/

    public static String Amountoflines;

    /**
     * Szybkosc potworow
     **/

    public static String Monsterspeed;

    /**
     * Ilosc zyc na poczatku rozgrywki
     **/

    public static String Amountoflifes;


    /**
     * Ilosc zwykłych bomb
     */

    public static String Amountofordinarybombs;

    /**
     * ilosc bomb zdalnych
     */

    public static String Amountofremotebombs;

    /**
     * ilosc skrzydel husarskich
     */

    public static String Amountofhusarswings;

    /**
     * ilosc lasrów
     */

    public static String Amountoflasers;

    /**
     * ilosc bomb zdalnych
     */

    public static String Amountofkeys;

    /**
     * String przechowujcy informacje o wszystkich polach danego poziomu.
     * W kolejnych elementach tablicy zawarte sa kolejne linie planszy
     */

    public static String buforrow[];

    /**
     * Bufor zawierajacy dane konfiguracyjne
     */

    public static String levelbufor[];
    /**
     * Metoda tworzaca sciezke do poziomu
     * @param level
     * @return path - sciezka do konkretnego levela
     */

    private static String create_path_to_level(int level){
        String first_part="src\\server_pliki\\Level";
        String second_part=Integer.toString(level);
        String third_part=".xml";

        String path=first_part+second_part+third_part;

        //System.out.println(path);
        return path;
    }

    /**
     * Metoda wczytyjaca level
     * @param level
     */
    public static void ParseLevelFile(int level){
        try {
            //sciezka do konkretne levela
            String path_to_level=create_path_to_level(level);
            File file = new File(path_to_level);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            levelbufor = new String[9];

            Amountofcolumns = doc.getElementsByTagName("Amountofcolumns").item(0).getTextContent();
            levelbufor[0] = Amountofcolumns;
            Amountoflines = doc.getElementsByTagName("Amountoflines").item(0).getTextContent();
            levelbufor[1] = Amountoflines;
            Monsterspeed = doc.getElementsByTagName("Monsterspeed").item(0).getTextContent();
            levelbufor[2] = Monsterspeed;
            Amountoflifes = doc.getElementsByTagName("Amountoflifes").item(0).getTextContent();
            levelbufor[3] = Amountoflifes;
            Amountofordinarybombs = doc.getElementsByTagName("Amountofordinarybombs").item(0).getTextContent();
            levelbufor[4] = Amountofordinarybombs;
            Amountofremotebombs = doc.getElementsByTagName("Amountofremotebombs").item(0).getTextContent();
            levelbufor[5] = Amountofremotebombs;
            Amountofhusarswings = doc.getElementsByTagName("Amountofhusarswings").item(0).getTextContent();
            levelbufor[6] = Amountofhusarswings;
            Amountoflasers = doc.getElementsByTagName("Amountoflasers").item(0).getTextContent();
            levelbufor[7] = Amountoflasers;
            Amountofkeys = doc.getElementsByTagName("Amountofkeys").item(0).getTextContent();
            levelbufor[8] = Amountofkeys;

            //konwersja stringa - liczby wierszy
            int rowiteration = Integer.valueOf(Amountoflines);
            //pakowanie znakow do bufora
            buforrow = new String[rowiteration];
            for (int i = 0; i < rowiteration; i++) {
                buforrow[i] = doc.getElementsByTagName("row").item(i).getTextContent();
            }
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
