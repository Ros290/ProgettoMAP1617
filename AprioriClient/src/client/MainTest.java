package client;


import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.JOptionPane;

import keyboardinput.Keyboard;



public class MainTest 
{
	private static String DEFAULT_HOST = "localhost";
	private static int DEFAULT_PORT = 8080;

	private static Socket socket = null;

    /**
     * Si occupa della ricezione sicura di un oggetto via socket
     * @param socket la quale ricevere l'oggetto
     * @return oggetto ricevuto dal socket specificato
     * @throws ClassNotFoundException nel caso la classe ricevuta non vi è riconosciuta
	 * @throws IOException nel caso si verifichi un errore in fase di lettura
     */
	protected static Object readObject(Socket socket) throws ClassNotFoundException, IOException
	{
		Object o;
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		o = in.readObject();
		return o;
	}
	
	/**
     * Si occupa dell'invio sicuro di un oggetto via socket
     * @param socket la quale riceverà l'oggetto
	 * @param o oggetto da inviare
	 * @throws IOException nel caso si verifichi un errore in fase di scrittura
	 */
	protected static void writeObject(Socket socket, Object o) throws IOException
	{
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(o);
		out.flush();
	}

	private static void Comunicazione () 
	{
		String string, result;
		try
		{
			/*
			 * I messaggi tra client - server sono così strutturati:
			 * 
			 * LATO SERVER
			 * 
			 * primo messaggio : "OK" (in tal caso, indica che la comunicazione è ancora in attivo) / "END" (indica, invece, che la comunicazione è terminata)
			 * secondo messaggio : contiene una stringa, solitamente indica una richiesta, oppure il risultato di un'operazione
			 * 
			 * LATO CLIENT
			 * al contrario, il client provvederà unicamente a mandare una stringa. Solitamente sarà associato, in qualche modo, alla richiesta posta dal server
			 */
			result = String.valueOf(readObject(socket));
			//Se il primo messaggio del server è "OK", vuol dire che sta richiendendo dati al client
			while(result.equals("OK"))
			{
				//mostra la richiesta da porre al client
				System.out.println(String.valueOf(readObject(socket)));
				string = Keyboard.readString();
				//dopo di che spedisce i dati inseriti al server
				writeObject(socket,string);
				//infine legge il primo messaggio che il server ha mandato al client
				result = String.valueOf(readObject(socket));
			}
			//se il primo messaggio è "END", vuol dire allora che, per qualche motivo, il server ha chiuso la connessione con il client
			//in questo caso particolare, solitamente motiva la scelta nel secondo messaggio
			if ( result.equals("END"))
				System.out.println(String.valueOf(readObject(socket)) + "\nConnessione Terminata");
			else
				System.out.println("\nConnessione Terminata");
		}
		catch (Exception e)
		{
			System.out.println("Connessione Interrotta\n");
		}
	}
	
	public static void main(String[] args)  
	{
		String strHost = DEFAULT_HOST;
		int port = DEFAULT_PORT;


		try {
			InetAddress addr = InetAddress.getByName(strHost); // ottiene l'indirizzo dell'host specificato
			System.out.println("Connecting to " + addr + "...");
			socket = new Socket(addr, port); // prova a connetters
			System.out.println("Success! Connected to " + socket);
			Comunicazione();
			socket.close();
		} catch (IOException e) {
			System.out.println("Unable to connect to " + strHost + ":" + port + ".\n" + e.getMessage());
			System.exit(0);
		}
		

	}
}
