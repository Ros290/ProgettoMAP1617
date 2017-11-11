package client;



import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;









import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfWriter;



public class Apriori extends JApplet
{
	
	
	private static String DEFAULT_HOST = "localhost";
	private static int DEFAULT_PORT = 8080;
	private static Socket socket = null;
	Frame window;
	String rules;
	
	private class Frame extends JPanel
	{
		private JFrame frame;
		private JTextField nameDataTxt, nameMinSupTxt, nameMinConfTxt, nameFileTxt;
		private TextArea msgAreaTxt, rulesAreaTxt;
		private JRadioButton db, file;
		
		/**
		 * Create the application.
		 */
		public Frame(java.awt.event.ActionListener aLearn, java.awt.event.ActionListener aSaveOnPDF, java.awt.event.ActionListener aChange) 
		{
			frame = new JFrame();
			frame.setBounds(100, 100, 668, 440);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().setLayout(null);
			
			JTabbedPane aprioriTab = new JTabbedPane(JTabbedPane.TOP);
			aprioriTab.setBounds(0, 0, 642, 402);
			frame.getContentPane().add(aprioriTab);
			
			JPanel miningPanel = new JPanel();
			aprioriTab.addTab("Mining", null, miningPanel, null);
			miningPanel.setLayout(null);
			
			JPanel cpAprioriMining = new JPanel();
			cpAprioriMining.setBounds(10, 11, 136, 95);
			miningPanel.add(cpAprioriMining);
			cpAprioriMining.setBorder(BorderFactory.createTitledBorder("Selecting Data Source"));
			cpAprioriMining.setLayout(null);
			
			db = new JRadioButton("Learning from db");
			db.setBounds(6, 24, 109, 23);
			cpAprioriMining.add(db);
			db.addActionListener(aChange);
			db.setSelected(true);
			
			file = new JRadioButton("Reading from File");
			file.setBounds(6, 50, 109, 23);
			cpAprioriMining.add(file);
			file.addActionListener(aChange);
			
			ButtonGroup group = new ButtonGroup();
			group.add(db);
			group.add(file);
			
			JPanel cpAprioriInput = new JPanel();
			cpAprioriInput.setBounds(156, 11, 471, 95);
			cpAprioriInput.setBorder(BorderFactory.createTitledBorder("Input Parameters"));
			miningPanel.add(cpAprioriInput);
			cpAprioriInput.setLayout(null);
			
			JLabel data = new JLabel("Data");
			data.setBounds(10, 24, 46, 14);
			cpAprioriInput.add(data);
			
			nameDataTxt = new JTextField();
			nameDataTxt.setBounds(52, 21, 86, 20);
			cpAprioriInput.add(nameDataTxt);
			nameDataTxt.setColumns(10);
			
			JLabel minSup = new JLabel("Min Sup");
			minSup.setBounds(148, 21, 56, 14);
			cpAprioriInput.add(minSup);
			
			nameMinSupTxt = new JTextField();
			nameMinSupTxt.setColumns(10);
			nameMinSupTxt.setBounds(214, 18, 76, 20);
			cpAprioriInput.add(nameMinSupTxt);
			
			JLabel nameFile = new JLabel("Save");
			nameFile.setBounds(10, 52, 46, 14);
			cpAprioriInput.add(nameFile);
			
			nameFileTxt = new JTextField();
			nameFileTxt.setColumns(10);
			nameFileTxt.setBounds(52, 49, 86, 20);
			cpAprioriInput.add(nameFileTxt);
			
			JLabel minConf = new JLabel("Min Conf");
			minConf.setBounds(148, 52, 56, 14);
			cpAprioriInput.add(minConf);
			
			nameMinConfTxt = new JTextField();
			nameMinConfTxt.setColumns(10);
			nameMinConfTxt.setBounds(214, 49, 76, 20);
			cpAprioriInput.add(nameMinConfTxt);
			
			JButton aprioriConstructionBt = new JButton("Mine");
			aprioriConstructionBt.setBounds(300, 15, 161, 23);
			cpAprioriInput.add(aprioriConstructionBt);
			aprioriConstructionBt.addActionListener(aLearn);
			
			JButton aprioriPDFBt = new JButton("Mine & Save on PDF");
			aprioriPDFBt.setBounds(300, 48, 161, 23);
			cpAprioriInput.add(aprioriPDFBt);
			aprioriPDFBt.addActionListener(aSaveOnPDF);
			
			JPanel cpRuleViewer = new JPanel();
			cpRuleViewer.setBounds(10, 117, 617, 246);
			cpRuleViewer.setBorder(BorderFactory.createTitledBorder("Pattern and Rules"));
			miningPanel.add(cpRuleViewer);
			cpRuleViewer.setLayout(null);
			
			rulesAreaTxt = new TextArea();
			rulesAreaTxt.setBounds(10, 22, 597, 126);
			rulesAreaTxt.setEditable(false);
			cpRuleViewer.add(rulesAreaTxt);
			
			msgAreaTxt = new TextArea();
			msgAreaTxt.setBounds(10, 154, 597, 82);
			msgAreaTxt.setEditable(false);
			cpRuleViewer.add(msgAreaTxt);
			
			JLayeredPane layeredPane = new JLayeredPane();
			aprioriTab.addTab("New tab", null, layeredPane, null);
			
			JPanel selectionRulesApriori = new JPanel();
			selectionRulesApriori.setBounds(0, 0, 637, 374);
			layeredPane.add(selectionRulesApriori);
			
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
				writeObject(socket, Float.parseFloat(window.nameMinSupTxt.getText()));
				writeObject (socket, Float.parseFloat(window.nameMinConfTxt.getText()));
			}
			else
				writeObject (socket, 2);
			if (window.nameFileTxt.getText().isEmpty())
				writeObject (socket, "map");
			else
				writeObject (socket, window.nameFileTxt.getText());
			String esito = (String)readObject(socket);
			if (esito.equals("OK"))
			{
				rules = (String)readObject(socket);
				window.rulesAreaTxt.setText(rules);
				window.msgAreaTxt.setText((String)readObject(socket));
			}
			else if (esito.equals("ERR"))
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
		} 
		catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		

	}
	
	public void PDFCreator () 
	{
		JFileChooser request = new JFileChooser();
		request.setFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
		if (request.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			String dest = request.getSelectedFile().getAbsolutePath();
			if (!request.getSelectedFile().getAbsolutePath().endsWith(".pdf"))
				dest += ".pdf";
		    File file = new File(dest);
			file.getParentFile().mkdirs();
			com.itextpdf.text.Document document = new com.itextpdf.text.Document();
			try 
			{
				PdfWriter.getInstance(document, new FileOutputStream(dest)); 
				com.itextpdf.text.Rectangle two = new com.itextpdf.text.Rectangle(700,400);
				document.open();
				com.itextpdf.text.Paragraph p = new com.itextpdf.text.Paragraph(window.rulesAreaTxt.getText());
				document.add(p);
				document.setPageSize(two);
				document.close();
				window.msgAreaTxt.setText(window.msgAreaTxt.getText() + "\nFile PDF salvato con successo!");
			}
			catch (FileNotFoundException | DocumentException e) 
			{
				window.msgAreaTxt.setText(window.msgAreaTxt.getText() + "\nSi è verificato un errore durante la creazione del file PDF");
			}
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
							Learning();
							PDFCreator();
						}
					},
					new java.awt.event.ActionListener() 
					{
						public void actionPerformed(ActionEvent e) 
						{
							boolean x = window.db.isSelected();
							window.nameMinSupTxt.setEditable(x);
							window.nameMinConfTxt.setEditable(x);
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
