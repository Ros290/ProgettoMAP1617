package client;


import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import keyboardinput.Keyboard;



public class MainTest extends JApplet
{
	
	
	private static String DEFAULT_HOST = "localhost";
	private static int DEFAULT_PORT = 8080;
	private static Socket socket = null;
	Frame window;
	
	private class Frame extends JPanel
	{
		private JFrame frame;
		private JTextField nameDataTxt, minSupTxt, minConfTxt, nameFileTxt;
		private TextArea msgAreaTxt, rulesAreaTxt;
		private JRadioButton db, file;
		
		/**
		 * Create the application.
		 */
		public Frame(java.awt.event.ActionListener aLearn, java.awt.event.ActionListener aChange) 
		{
			frame = new JFrame();
			frame.setBounds(100, 100, 621, 440);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(null);
			
			JPanel cpParameterSetting = new JPanel();
			cpParameterSetting.setBounds(10, 11, 585, 132);
			cpParameterSetting.setBorder(BorderFactory.createTitledBorder("Apriori"));
			frame.getContentPane().add(cpParameterSetting);
			cpParameterSetting.setLayout(null);
			
			JPanel cpAprioriMining = new JPanel();
			cpAprioriMining.setBounds(10, 23, 160, 81);
			cpAprioriMining.setBorder(BorderFactory.createTitledBorder("Selecting Data Source"));
			cpParameterSetting.add(cpAprioriMining);
			cpAprioriMining.setLayout(null);
			
			db = new JRadioButton("Learning rules from db");
			db.setBounds(6, 25, 148, 23);
			db.addActionListener(aChange);
			db.setSelected(true);
			cpAprioriMining.add(db);
			
			file = new JRadioButton("Reading rules from file");
			file.setBounds(6, 51, 148, 23);
			file.addActionListener(aChange);
			cpAprioriMining.add(file);
			
			ButtonGroup group = new ButtonGroup();
			group.add(db);
			group.add(file);
			
			JPanel cpAprioriInput = new JPanel();
			cpAprioriInput.setBounds(251, 23, 283, 81);
			cpAprioriInput.setBorder(BorderFactory.createTitledBorder("Input Parameters"));
			cpParameterSetting.add(cpAprioriInput);
			cpAprioriInput.setLayout(null);
			
			JLabel data = new JLabel("Data");
			data.setBounds(10, 27, 46, 14);
			cpAprioriInput.add(data);
			
			nameDataTxt = new JTextField();
			nameDataTxt.setBounds(39, 24, 86, 20);
			cpAprioriInput.add(nameDataTxt);
			nameDataTxt.setColumns(10);
			
			JLabel minSup = new JLabel("MinSup");
			minSup.setBounds(150, 27, 46, 14);
			cpAprioriInput.add(minSup);
			
			minSupTxt = new JTextField();
			minSupTxt.setBounds(206, 24, 39, 20);
			cpAprioriInput.add(minSupTxt);
			minSupTxt.setColumns(3);
			
			JLabel minConf = new JLabel("MinConf");
			minConf.setBounds(150, 56, 46, 14);
			cpAprioriInput.add(minConf);
			
			minConfTxt = new JTextField();
			minConfTxt.setColumns(3);
			minConfTxt.setBounds(206, 53, 39, 20);
			cpAprioriInput.add(minConfTxt);
			
			JLabel nameFile = new JLabel("Save");
			nameFile.setBounds(10, 52, 46, 14);
			cpAprioriInput.add(nameFile);
			
			nameFileTxt = new JTextField();
			nameFileTxt.setBounds(39, 52, 86, 20);
			cpAprioriInput.add(nameFileTxt);
			nameFileTxt.setColumns(10);
			
			JPanel cpRuleViewer = new JPanel();
			cpRuleViewer.setBounds(10, 195, 585, 196);
			cpRuleViewer.setBorder(BorderFactory.createTitledBorder("Pattern and Rules"));
			frame.getContentPane().add(cpRuleViewer);
			cpRuleViewer.setLayout(null);
			
			rulesAreaTxt = new TextArea();
			rulesAreaTxt.setBounds(10, 25, 565, 96);
			rulesAreaTxt.setEditable(false);
			cpRuleViewer.add(rulesAreaTxt);
			
			msgAreaTxt = new TextArea();
			msgAreaTxt.setBounds(10, 127, 565, 59);
			msgAreaTxt.setEditable(false);
			cpRuleViewer.add(msgAreaTxt);
			
			JPanel cpMiningCommand = new JPanel();
			cpMiningCommand.setBounds(10, 151, 585, 33);
			frame.getContentPane().add(cpMiningCommand);
			cpMiningCommand.setLayout(null);
			
			JButton aprioriConstructionBt = new JButton("Apriori");
			aprioriConstructionBt.setBounds(248, 0, 89, 23);
			aprioriConstructionBt.addActionListener(aLearn);
			cpMiningCommand.add(aprioriConstructionBt);
		}

	}

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

	private void Learning () 
	{
		try 
		{
			if (window.db.isSelected())
			{	
				writeObject (socket, 1);
				writeObject (socket, window.nameDataTxt.getText());
				writeObject(socket, Float.parseFloat(window.minSupTxt.getText()));
				writeObject (socket, Float.parseFloat(window.minConfTxt.getText()));
			}
			else
				writeObject (socket, 2);
			if (window.nameFileTxt.getText().isEmpty())
				writeObject (socket, "map");
			else
				writeObject (socket, window.nameFileTxt.getText());
			if (((String)readObject(socket)).equals("OK"))
			{
				window.rulesAreaTxt.setText((String)readObject(socket));
				window.msgAreaTxt.setText((String)readObject(socket));
			}
			else if (((String)readObject(socket)).equals("ERR"))
			{
				window.rulesAreaTxt.setText("");
				window.msgAreaTxt.setText((String)readObject(socket));
			}
			else
			{
				window.rulesAreaTxt.setText("");
				window.msgAreaTxt.setText("Messaggio non riconosciuto dal Client");
			}

		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	//public static void main(String[] args)  
	public void init()
	{
		String strHost = DEFAULT_HOST;
		int port = DEFAULT_PORT;


		try 
		{
			
			window = new Frame(
					new java.awt.event.ActionListener() 
					{
						public void actionPerformed(ActionEvent e) 
						{
							Learning();
						}
					},
					new java.awt.event.ActionListener() 
					{
						public void actionPerformed(ActionEvent e) 
						{
							boolean x = window.db.isSelected();
							window.minSupTxt.setEditable(x);
							window.minConfTxt.setEditable(x);
							window.nameDataTxt.setEditable(x);
						}
					});
			window.frame.setVisible(true);
			//getContentPane().setLayout(new GridLayout(1, 1));
			//getContentPane().add(window);
			
			InetAddress addr = InetAddress.getByName(strHost); // ottiene l'indirizzo dell'host specificato
			System.out.println("Connecting to " + addr + "...");
			socket = new Socket(addr, port); // prova a connetters
			System.out.println("Success! Connected to " + socket);
			//socket.close();
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Unable to connect to " + strHost + ":" + port + ".\n" + e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
			this.destroy();
			System.exit(0);
		}
		

	}
}
