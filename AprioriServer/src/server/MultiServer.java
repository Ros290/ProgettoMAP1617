package server;

import java.net.*;
import java.io.IOException;

/**
 * Classe avviabile che gestisce le varie connessioni in ingresso, smistandole
 * in thread separati.
 * @author Ciccariello Luciano, Palumbo Vito, Rosini Luigi
 */
public class MultiServer {
    private static int PORT = 8080;
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        int port = 8080;
        if (args.length > 1)
            {   try {
                 port = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                // nessun bisogno di gestire questa eccezione
                  }
            }
               new MultiServer(port).run();
    }

    /**
     * Initializza la porta ed invoca Run
     * @param port porta la quale il server ascolterà connessioni in ingresso
     */
    public MultiServer(int port)
    {
        try
        {
            InetAddress addr = InetAddress.getByName(null);
            System.out.println("Current machine address: " + addr.toString());
            serverSocket = new ServerSocket(port);
        }
        catch (java.net.UnknownHostException e)
        {
            System.out.println(e.getMessage());
        }
        catch (java.io.IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Istanzia un oggetto ServerSocket che pone in attesa di richiesta
     * connessioni da parte del client. Ad ogni nuova richiesta di
     * connessione verrà istanziato un oggetto ServerOneClient
     */
    private void run()
    {
        while (true)
        {
            try
            {
                System.out.println("Waiting for an incoming connection...");
                Socket s = serverSocket.accept();
                System.out.println("Client connected!" + s.toString());
                new ServerOneClient(s).start();
            }
            catch (UnknownHostException e)
            {
                System.out.println(e.getMessage());
            }
            catch (IOException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
}
