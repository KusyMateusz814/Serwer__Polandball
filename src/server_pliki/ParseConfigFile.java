package server_pliki;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Klasa odpowiedzialna za parsowanie pliku serwera z podstawowymi parametrami aplikacji
 */
public class ParseConfigFile {

    /**
     * Sciezka do pliku konfiguracyjnego
     **/
    public static final String Config = "src\\server_pliki\\config.xml";

    /**
     * Wysokosc ramki
     **/

    public static String Boardheight;

    /**
     * Szerokosc ramki
     **/

    public static String Boardwidth;

    /**
     * Wysokość okna głównego - menu
     */

    public static String MainFrameheight;

    /**
     * Szerokość okna głównego - menu
     */

    public static String MainFramewidth;

    /**
     * Rozmiar okna Highscores, ramka jest kwadratowa
     */

    public static String HighscoresFrameSize;

    /**
     * Zmienna zawierajaca ilosc sekund, po ktorym wybuchnie bomba
     */
    public static String TimeToExplosion;
    /**
     * Szybkosc gracza
     */
    public static String SpeedPlayer;

    /**
     * Bufor, gdzie beda przetrzymywane dane konfiguracyjne
     */

    public static String[] configbufor;
    /**
     * Metoda parsujaca plik config.xml
     */

    public static void ParseConfig(){

        try {
            File file = new File(Config);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            configbufor = new String[7];
            Boardheight = doc.getElementsByTagName("Boardheight").item(0).getTextContent();
            configbufor[0] = Boardheight;
            Boardwidth = doc.getElementsByTagName("Boardwidth").item(0).getTextContent();
            configbufor[1] = Boardwidth;
            MainFrameheight = doc.getElementsByTagName("MainFrameheight").item(0).getTextContent();
            configbufor[2] = MainFrameheight;
            MainFramewidth = doc.getElementsByTagName("MainFramewidth").item(0).getTextContent();
            configbufor[3] = MainFramewidth;
            HighscoresFrameSize = doc.getElementsByTagName("HighscoresFrameSize").item(0).getTextContent();
            configbufor[4] = HighscoresFrameSize;
            SpeedPlayer = doc.getElementsByTagName("SpeedPlayer").item(0).getTextContent();
            configbufor[5] = SpeedPlayer;
            TimeToExplosion = doc.getElementsByTagName("TimeToExplosion").item(0).getTextContent();
            configbufor[6] = TimeToExplosion;
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
