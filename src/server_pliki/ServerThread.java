package server_pliki;

import java.io.*;
import java.net.Socket;

/**
 * watek serwera, tworzony dla kazdego klienta
 */

public class ServerThread implements Runnable {

    /**
     * gniazdo
     */

    private Socket socket;

    /**
     * konstruktor watku serwera
     * @param socket
     */

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    /**
     * funckja oblsugujaca zdarzenia pomiedzy klientem i serwerem
     */

    @Override
    public void run() {
        try {
            //dane wejsciowe od klienta sa w postaci bitowej, trzeba je przekonwertowac na tekst
            InputStream inputStream = socket.getInputStream();//strumien wejsciowy na gniezdzie, dane od klienta
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputStream));//czytanie żądań klienta
            OutputStream outputstream = socket.getOutputStream();//dane wyjsciowe od serwera
            PrintWriter printwriter = new PrintWriter(outputstream, true);//przypisanie do strumienia wyjsciowego tego,
                                                                                    //co odpowie serwer
            String clientmesssage = bufferedreader.readLine();//przypisanie zadania klienta do zmiennej typu string
            if (clientmesssage != null) {//sprawdzanie czy klient wyslal wiadomosc
                System.out.println("ZADANIE OD KLIENTA: " + clientmesssage);//wypisanie zadania klienta w konsoli
                String servermessage = ServerAction.ServerResponse(clientmesssage);//obsluga zadania klienta przez klase
                                                                                   // ServerAction
                if (servermessage == "CONNECTION_REJECTED") {//w przypadku zadania natychmiastowego zerwania polaczenia
                    socket.close();                       //zamkniecie gniazda
                }
                else {
                    printwriter.println(servermessage);//utworzenie wiadomosci
                    printwriter.flush();//flush() wymusza "wypchniecie" z bufora wszystkich danych
                    System.out.println("ODPOWIEDZ SERWERA: " + ServerAction.serverflag_ + servermessage);//wypisanie odpowiedzi serwera w konsoli
                    /*if (servermessage == "LOGOUT") {//zamkniecie gniazda w przypadku wylogowania
                        socket.close();
                    }*/
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

