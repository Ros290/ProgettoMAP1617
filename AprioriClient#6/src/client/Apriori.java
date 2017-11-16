package client;

import java.awt.Component;
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
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.MenuElement;
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
	private static String ADD_BUTTON_ARULE = "addAss";
	private static String SUB_BUTTON_ARULE = "subAss";
	private static String ADD_BUTTON_CRULE_LEFT = "addConfL";
	private static String SUB_BUTTON_CRULE_LEFT = "subConfL";
	private static String ADD_BUTTON_CRULE_RIGHT = "addConfR";
	private static String SUB_BUTTON_CRULE_RIGHT = "subConfR";
	Frame window;
	String rules;
	JPopupMenu menuAssociation;
	JPopupMenu menuConfident;
	
	private class Frame extends JPanel
	{
		private JFrame frame;
		private JTextField nameDataTxt, nameMinSupTxt, nameMinConfTxt, nameFileTxt, leftConfRule, rightConfRule, associationRule;
		private JPanelRulesArea cpRuleViewer, cpRuleFinder;
		private JRadioButton db, file;
		
		private class JPanelRulesArea extends JPanel
		{
			private TextArea rulesAreaTxt, msgAreaTxt;
			
			JPanelRulesArea(JPanel panel)
			{
				JPanel cpRuleViewer = new JPanel();
				cpRuleViewer.setBounds(10, 117, 617, 246);
				cpRuleViewer.setBorder(BorderFactory.createTitledBorder("Pattern and Rules"));
				panel.add(cpRuleViewer);
				cpRuleViewer.setLayout(null);
		
				rulesAreaTxt = new TextArea();
				rulesAreaTxt.setBounds(10, 22, 597, 126);
				rulesAreaTxt.setEditable(false);
				cpRuleViewer.add(rulesAreaTxt);
		
				msgAreaTxt = new TextArea();
				msgAreaTxt.setBounds(10, 154, 597, 82);
				msgAreaTxt.setEditable(false);
				cpRuleViewer.add(msgAreaTxt);
		
			}
		}
		
		/**
		 * Create the application.
		 */
		public Frame() 
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
			db.addActionListener
			(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					boolean x = window.db.isSelected();
					window.nameMinSupTxt.setEditable(x);
					window.nameMinConfTxt.setEditable(x);
					window.nameDataTxt.setEditable(x);
				}
			});
			db.setSelected(true);
			
			file = new JRadioButton("Reading from File");
			file.setBounds(6, 50, 109, 23);
			cpAprioriMining.add(file);
			file.addActionListener
			(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					boolean x = window.db.isSelected();
					window.nameMinSupTxt.setEditable(x);
					window.nameMinConfTxt.setEditable(x);
					window.nameDataTxt.setEditable(x);
				}
			});
			
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
			aprioriConstructionBt.addActionListener
			(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					Learning();
				}
			});
			
			JButton aprioriPDFBt = new JButton("Mine & Save on PDF");
			aprioriPDFBt.setBounds(300, 48, 161, 23);
			cpAprioriInput.add(aprioriPDFBt);
			aprioriPDFBt.addActionListener
			(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					Learning();
					PDFCreator();
				}
			});
			
			cpRuleViewer = new JPanelRulesArea(miningPanel);

			JLayeredPane findRulePanel = new JLayeredPane();
			aprioriTab.addTab("Find", null, findRulePanel, null);
			
			JPanel selectionRulesApriori = new JPanel();
			selectionRulesApriori.setBounds(0, 0, 637, 374);
			findRulePanel.add(selectionRulesApriori);
			selectionRulesApriori.setLayout(null);
			
			JPanel findConfRulePanel = new JPanel();
			findConfRulePanel.setLayout(null);
			findConfRulePanel.setBorder(BorderFactory.createTitledBorder("Confident Rule"));
			findConfRulePanel.setBounds(239, 11, 276, 95);
			selectionRulesApriori.add(findConfRulePanel);
			
			leftConfRule = new JTextField();
			leftConfRule.setColumns(10);
			leftConfRule.setBounds(10, 24, 103, 20);
			leftConfRule.setEditable(false);
			findConfRulePanel.add(leftConfRule);
			
			JLabel label = new JLabel("=>");
			label.setBounds(123, 27, 27, 14);
			findConfRulePanel.add(label);
			
			rightConfRule = new JTextField();
			rightConfRule.setColumns(10);
			rightConfRule.setBounds(160, 24, 103, 20);
			rightConfRule.setEditable(false);
			findConfRulePanel.add(rightConfRule);
			
			JButton addLeftConfRule = new JButton("+");
			addLeftConfRule.setBounds(10, 61, 41, 23);
			addLeftConfRule.setName(ADD_BUTTON_CRULE_LEFT);
			addLeftConfRule.addActionListener
			(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					menuConfident.show(addLeftConfRule, 0, addLeftConfRule.getBounds().height);
				}
			});
			findConfRulePanel.add(addLeftConfRule);
			
			JButton subLeftConfRule = new JButton("-");
			subLeftConfRule.setBounds(72, 61, 41, 23);
			subLeftConfRule.setName(SUB_BUTTON_CRULE_LEFT);
			findConfRulePanel.add(subLeftConfRule);
			
			JButton addRightConfRule = new JButton("+");
			addRightConfRule.setBounds(159, 61, 41, 23);
			addRightConfRule.setName(ADD_BUTTON_CRULE_RIGHT);
			addRightConfRule.addActionListener
			(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					menuConfident.show(addRightConfRule, 0, addRightConfRule.getBounds().height);
				}
			});
			findConfRulePanel.add(addRightConfRule);
			
			JButton subRightConfRule = new JButton("-");
			subRightConfRule.setBounds(222, 61, 41, 23);
			subRightConfRule.setName(SUB_BUTTON_CRULE_RIGHT);
			findConfRulePanel.add(subRightConfRule);
			
			JPanel findAssRulePanel = new JPanel();
			findAssRulePanel.setLayout(null);
			findAssRulePanel.setBorder(BorderFactory.createTitledBorder("Association Rule"));
			findAssRulePanel.setBounds(10, 11, 219, 95);
			selectionRulesApriori.add(findAssRulePanel);
			
			associationRule = new JTextField();
			associationRule.setColumns(10);
			associationRule.setBounds(10, 24, 200, 20);
			associationRule.setEditable(false);
			findAssRulePanel.add(associationRule);
			
			JButton addAssociationRule = new JButton("+");
			addAssociationRule.setBounds(10, 61, 41, 23);
			addAssociationRule.setName(ADD_BUTTON_ARULE);
			addAssociationRule.addActionListener
			(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					menuAssociation.show(addAssociationRule, 0, addAssociationRule.getBounds().height);
				}
			});
			findAssRulePanel.add(addAssociationRule);
			
			JButton subAssociationRule = new JButton("-");
			subAssociationRule.setBounds(169, 61, 41, 23);
			subAssociationRule.setName(SUB_BUTTON_ARULE);
			findAssRulePanel.add(subAssociationRule);

			cpRuleFinder = new JPanelRulesArea (selectionRulesApriori);
			
			//addMenuOnButton(addAssociationRule);
			//addMenuOnButton(subAssociationRule);
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
	
	private void getAttributesList (Socket socket) throws ClassNotFoundException, IOException
	{
		String string;
		menuAssociation = new JPopupMenu();
		menuConfident = new JPopupMenu();
		char c = (char)readObject(socket);
		while (c == 'A')
		{
			string = (String)readObject(socket);
			JMenu menuAttributeAss = new JMenu (string);
			JMenu menuAttributeConf = new JMenu (string);
			c = (char)readObject(socket);
			while (c == 'V')
			{
				string = (String)readObject(socket);
				JMenuItem menuItemAss = new JMenuItem(string);
				JMenuItem menuItemConf = new JMenuItem(string);
				//Inserire actionListener
				menuItemAss.addActionListener
				(new java.awt.event.ActionListener() 
				{
					public void actionPerformed(ActionEvent e) 
					{

						//removingItemFromMenu(menuAttribute, menuItem);
					    removingItemFromMenu(e);
					}
				});
				menuItemConf.addActionListener
				(new java.awt.event.ActionListener() 
				{
					public void actionPerformed(ActionEvent e) 
					{

						//removingItemFromMenu(menuAttribute, menuItem);
					    removingItemFromMenu(e);
					}
				});
				
				menuAttributeAss.add(menuItemAss);
				menuAttributeConf.add(menuItemConf);
				c = (char)readObject(socket);
			}
			menuAssociation.add(menuAttributeAss);
			menuConfident.add(menuAttributeConf);
		}
		/*
		for (MenuElement me :  menuAssociation.getSubElements())
		{
			if (me instanceof JMenu)
				System.out.println(((JMenu)me).getText());
			else if (me instanceof JMenuItem)
				System.out.println(((JMenuItem)me).getText());
		}
		*/
	}
	
	private void removingItemFromMenu (ActionEvent e)
	{
		JTextField textField;
	    JMenuItem selectedMenuItem = (JMenuItem) e.getSource(); 
	    JPopupMenu selectedMenuAttribute = (JPopupMenu) selectedMenuItem.getParent(); 
	    Component invoker = selectedMenuAttribute.getInvoker();
	    JPopupMenu popupMenu = (JPopupMenu) invoker.getParent();
		Component invokerMenu = popupMenu.getInvoker();
		//JButton button = (JButton) invokerMenu.getParent();
		JButton button = (JButton)invokerMenu; 
	    //JButton button = (JButton)((Component)((JPopupMenu)((Component)((JPopupMenu)((JMenuItem) e.getSource()).getParent()).getInvoker()).getParent()).getInvoker()).getParent();
	    if (button.getName().equals(ADD_BUTTON_ARULE))
	    	textField = window.associationRule;
	        else if (button.getName().equals(ADD_BUTTON_CRULE_LEFT))
	        	textField = window.leftConfRule;
	        	else if ((button.getName().equals(ADD_BUTTON_CRULE_RIGHT)))
	        		textField = window.rightConfRule;
	        		else	return;
	    //MOSTRA TUTTE LE OPZIONI DISPONIBILI NEL MENU
	    /*
		for (MenuElement popupElement :  popupMenu.getSubElements())
		{
			if (popupElement instanceof JMenu)
			{
				JMenu menuAttribute = (JMenu)popupElement;
				System.out.println((menuAttribute).getText());
				for (MenuElement menuElement : menuAttribute.getSubElements())
				{
					JPopupMenu menuItem = (JPopupMenu)menuElement;
					for (MenuElement item : menuItem.getSubElements())
						System.out.print(((JMenuItem)item).getText());
				}
				System.out.println();
			}
		}
		*/
	    
		if (!textField.getText().isEmpty())
			textField.setText(textField.getText() + " AND ");
		textField.setText(textField.getText() + "<" + ((JMenu)invoker).getText() + ">=<" + selectedMenuItem.getText() + ">");
		((JMenu)invoker).setEnabled(false);
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
				window.cpRuleViewer.rulesAreaTxt.setText(rules);
				window.cpRuleViewer.msgAreaTxt.setText((String)readObject(socket));
				writeObject(socket, 3);
				getAttributesList(socket);
			}
			else if (esito.equals("ERR"))
			{
				window.cpRuleViewer.rulesAreaTxt.setText("");
				window.cpRuleViewer.msgAreaTxt.setText((String)readObject(socket));
			}
			else
			{
				window.cpRuleViewer.rulesAreaTxt.setText("");
				window.cpRuleViewer.msgAreaTxt.setText("Messaggio non riconosciuto dal Client");
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
				com.itextpdf.text.Paragraph p = new com.itextpdf.text.Paragraph(window.cpRuleViewer.rulesAreaTxt.getText());
				document.add(p);
				document.setPageSize(two);
				document.close();
				window.cpRuleViewer.msgAreaTxt.setText(window.cpRuleViewer.msgAreaTxt.getText() + "\nFile PDF salvato con successo!");
			}
			catch (FileNotFoundException | DocumentException e) 
			{
				window.cpRuleViewer.msgAreaTxt.setText(window.cpRuleViewer.msgAreaTxt.getText() + "\nSi è verificato un errore durante la creazione del file PDF");
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
			
			window = new Frame();
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
