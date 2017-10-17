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

	protected static Object readObject(Socket socket) throws ClassNotFoundException, IOException
	{
		Object o;
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		o = in.readObject();
		return o;
	}
	protected static void writeObject(Socket socket, Object o) throws IOException
	{
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(o);
		out.flush();
	}

	private static void Comunicazione () 
	{
		char c;
		int value;
		String string;
		try
		{
			while(String.valueOf(readObject(socket)).equals("OK"))
			{
				System.out.println(String.valueOf(readObject(socket)));
				string = Keyboard.readString();
				writeObject(socket,string);
			}	
		}
		catch (Exception e)
		{
			System.out.println("ERRORE\n");
		}
	}
	public static void main(String[] args)  {
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
