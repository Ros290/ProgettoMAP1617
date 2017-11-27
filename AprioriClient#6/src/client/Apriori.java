package client;

import java.awt.Component;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import java.util.LinkedList;
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
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
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
	JPopupMenu menuAssociation, menuConfident;
	JPopupMenu queryAssociation, queryLeftConfident, queryRightConfident;
	
	private class Frame extends JPanel
	{
		private JFrame frame;
		private JTextField nameDataTxt, nameMinSupTxt, nameMinConfTxt, nameFileTxt, leftConfRule, rightConfRule, associationRule;
		private JPanelRulesArea cpRuleViewer, cpRuleFinder;
		private JRadioButton db, file;
		
		/*
		 * Rapressenta i due JText presenti su ciascun panel
		 */
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
			/*
			 * Ogni volta che viene selezionato il pallino, viene modificata la visibilit�
			 * dei campi qui sotto riportati
			 * 
			 */
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
			subLeftConfRule.addActionListener
			(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					queryLeftConfident.show(subLeftConfRule, 0, subLeftConfRule.getBounds().height);
				}
			});
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
			subRightConfRule.addActionListener
			(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					queryRightConfident.show(subRightConfRule, 0, subRightConfRule.getBounds().height);
				}
			});
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
			subAssociationRule.addActionListener
			(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					queryAssociation.show(subAssociationRule, 0, subAssociationRule.getBounds().height);
				}
			});
			findAssRulePanel.add(subAssociationRule);
			
			JButton findPdf = new JButton("FIND & PDF");
			findPdf.setBounds(525, 65, 102, 30);
			selectionRulesApriori.add(findPdf);
			
			JButton find = new JButton("FIND");
			find.setBounds(525, 24, 102, 30);
			find.addActionListener
			(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					getRulesFromQuery();
				}
			});
			selectionRulesApriori.add(find);

			cpRuleFinder = new JPanelRulesArea (selectionRulesApriori);
			
			//addMenuOnButton(addAssociationRule);
			//addMenuOnButton(subAssociationRule);
		}

	}

    /**
     * Si occupa della ricezione sicura di un oggetto via socket
     * @param socket la quale ricevere l'oggetto
     * @return oggetto ricevuto dal socket specificato
     * @throws ClassNotFoundException nel caso la classe ricevuta non vi � riconosciuta
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
     * @param socket la quale ricever� l'oggetto
	 * @param o oggetto da inviare
	 * @throws IOException nel caso si verifichi un errore in fase di scrittura
	 */
	protected static void writeObject(Socket socket, Object o) throws IOException
	{
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(o);
		out.flush();
	}

	/**
	 *Ricava gli attributi e gli item associati ad essi dal server nel seguente formato:
	 * 		
	 * 		- 'A', carattere che indica che la prossima stringa indica il nome di un attributo -> 'nome Attributo'
	 * 		- 'V', carattere che indica che la prossima stringa indica un valore riferito all'attributo
	 * 			passato precendemente -> 'nome Valore'
	 * 
	 *Oltre questo, istanzia i men� e inserisce i valori ricavati dal server su 'menuAssociation' e 'menuConfident'
	 * 
	 *ATTENZIONE: finch� il client non effettua alcuna ricerca di mining al server, non potr� ottenere gli attributi-valori
	 *da ricercare. Inoltre, ad ogni nuova ricerca di mining effettuata, viene invocata questa funzione, riportando
	 *i nuovi attributi-valori ottenuti dalla nuova ricerca
	 */
	private void getAttributesList (Socket socket) throws ClassNotFoundException, IOException
	{
		String string;
		int index;
		menuAssociation = new JPopupMenu();
		menuConfident = new JPopupMenu();
		queryAssociation = new JPopupMenu();
		queryLeftConfident = new JPopupMenu();
		queryRightConfident = new JPopupMenu();
		char c = (char)readObject(socket);
		while (c == 'A')
		{
			string = (String)readObject(socket);
			index = (int)readObject(socket);
			/*
			 * definisco il campo quale sar� associato al nome dell'attributo.
			 * tale campo, conterr� al suo interno i campi relativi ai valori assumibili da tale attributo.
			 * Potr� essere selezionato in caso si debba modificare la query, rimuovendo un determinato attributo (pulsante -)
			 */
			JMenu menuAttributeAss = new JMenu (string);
			menuAttributeAss.setName(String.valueOf(index));
			menuAttributeAss.addMouseListener
			(new MouseListener()
			{

				@Override
				public void mouseClicked(MouseEvent arg0) 
				{
					// ricavo il popupMenu che contiene l'elemento del menu selezionato
					JPopupMenu popupMenu = (JPopupMenu) ((JMenu)arg0.getSource()).getParent();
					Component invokerMenu = popupMenu.getInvoker();
					// dal popupMenu, ricavo il bottone da cui � stato invocato
					JButton button = (JButton)invokerMenu;
					if (button.getName().startsWith("sub"))
						removingItemFromQuery(arg0);
					javax.swing.MenuSelectionManager.defaultManager().clearSelectedPath();
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			JMenu menuAttributeConf = new JMenu (string);
			menuAttributeConf.setName(String.valueOf(index));
			menuAttributeConf.addMouseListener
			(new MouseListener()
			{

				@Override
				public void mouseClicked(MouseEvent arg0) 
				{
					// ricavo il popupMenu che contiene l'elemento del menu selezionato
					JPopupMenu popupMenu = (JPopupMenu) ((JMenu)arg0.getSource()).getParent();
					Component invokerMenu = popupMenu.getInvoker();
					// dal popupMenu, ricavo il bottone da cui � stato invocato
					JButton button = (JButton)invokerMenu;
					if (button.getName().startsWith("sub"))
						removingItemFromQuery(arg0);
					javax.swing.MenuSelectionManager.defaultManager().clearSelectedPath();
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			c = (char)readObject(socket);
			while (c == 'V')
			{
				string = (String)readObject(socket);
				/*
				 * definisco il campo quale sar� associato il valore.
				 * tale valore potr� essere aggiunto alla query tramite il pulsante +
				 */
				JMenuItem menuItemAss = new JMenuItem(string);
				menuItemAss.setName("N");
				menuItemAss.addActionListener				
				(new java.awt.event.ActionListener() 
				{
					public void actionPerformed(ActionEvent e) 
					{
						JButton button = (JButton)((Component)((JPopupMenu)((Component)((JPopupMenu)((JMenuItem) e.getSource()).getParent()).getInvoker()).getParent()).getInvoker());
						if (button.getName().startsWith("add"))
							addingItemToQuery(e);	
					}
				});
				
				JMenuItem menuItemConf = new JMenuItem(string);
				menuItemConf.setName("N");
				menuItemConf.addActionListener
				(new java.awt.event.ActionListener() 
				{
					public void actionPerformed(ActionEvent e) 
					{
						JButton button = (JButton)((Component)((JPopupMenu)((Component)((JPopupMenu)((JMenuItem) e.getSource()).getParent()).getInvoker()).getParent()).getInvoker());
						if (button.getName().startsWith("add"))
							addingItemToQuery(e);
					}
				});
				//aggiunge il valore al campo attributo
				menuAttributeAss.add(menuItemAss);
				menuAttributeConf.add(menuItemConf);
				c = (char)readObject(socket);
			}
			//infine, aggiunge il campo attributo ad entrambi i menu
			menuAssociation.add(menuAttributeAss);
			menuConfident.add(menuAttributeConf);
		}
	}

	/**
	 * In base al valore selezionato, lo aggiunge alla query e modifica i men� riferiti alla barra di ricerca su cui si opera
	 * @param e
	 */
	private void addingItemToQuery (ActionEvent e)
	{
		JTextField textField;
		JPopupMenu menu;
	    JMenuItem selectedMenuItem = (JMenuItem) e.getSource(); 
	    JPopupMenu selectedMenuAttribute = (JPopupMenu) selectedMenuItem.getParent(); 
	    Component invoker = selectedMenuAttribute.getInvoker();
	    JPopupMenu popupMenu = (JPopupMenu) invoker.getParent();
		Component invokerMenu = popupMenu.getInvoker();
		//ricavo il bottone da cui � stato aperto il menu
		JButton button = (JButton)invokerMenu; 
	    /*
	     * Definisco la barra associata al bottone, e il menu su cui sar� spostato il campo selezionato.
	     * 
	     * Difatti, lo stesso campo JMenu non pu� risiedere su differenti men�, perci� se viene aggiunto
	     * nel menu Y, il menu X che lo conteneva adesso non vi avr� pi� accesso
	     * 
	     * Quindi il nuovo men� su cui andr� ad aggiungere tale campo, sar� quello riferito al bottone -
	     * mostrando il menu che indica gli attributi presenti nella query 
	     */
		if (button.getName().equals(ADD_BUTTON_ARULE))
	    {
	    	textField = window.associationRule;
	    	menu = queryAssociation;
	    }
	        else if (button.getName().equals(ADD_BUTTON_CRULE_LEFT))
	        {
	        	textField = window.leftConfRule;
	        	menu = queryLeftConfident;
	        }
	        	else if ((button.getName().equals(ADD_BUTTON_CRULE_RIGHT)))
	        	{
	        		textField = window.rightConfRule;
	        		menu = queryRightConfident;
	        	}
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
		/*
		 * Inserisco l'attributo e il valore dell'attributo selezionato nella query
		 */
		if (!textField.getText().isEmpty())
			textField.setText(textField.getText() + " AND ");
		textField.setText(textField.getText() + "<" + ((JMenu)invoker).getText() + ">=<" + selectedMenuItem.getText() + ">");
		selectedMenuItem.setName("Y");
		
		/*
		 * dal campo dell'attributo, tolgo la visibilit� a tutti i valori ad esso associati, ed infine lo aggiungo al
		 * menu associato al bottone - della barra. 
		 * 
		 * Questo perch�, appena si aprir� il menu nel bottone -, si vedranno solamente i nomi degli attributi presenti
		 * nella query, ma non i valori da essi assunti.
		 */
		JMenu menuAttribute = (JMenu)invoker;
		for (MenuElement menuElement : (menuAttribute).getSubElements())
			for (MenuElement menuItem : ((JPopupMenu)menuElement).getSubElements())
				((JMenuItem)menuItem).setVisible(false);
		menu.add(menuAttribute);
	}
	
	/**
	 * Contrariamente alla funzione addingItemToQuery, rimuove un determinato campo dalla query e modifica
	 * i men� associati alla barra di ricerca su cui si opera
	 * @param e
	 */
	private void removingItemFromQuery (MouseEvent e)
	{
		JTextField textField;
		JPopupMenu menu;
		JPopupMenu popupMenu = (JPopupMenu) ((JMenu)e.getSource()).getParent();
		Component invokerMenu = popupMenu.getInvoker();
		JButton button = (JButton)invokerMenu;
	    /*
	     * Definisco la barra associata al bottone, e il menu su cui sar� spostato il campo selezionato.
	     * 
	     * (vedi commento presente all'interno della funzione 'addingItemToQuery'
	     * 
	     * Quindi il nuovo men� su cui andr� ad aggiungere tale campo, sar� quello riferito al bottone +
	     * mostrando il menu che indica i valori degli attributi da poter aggiungere nella query
	     */
	    if (button.getName().equals(SUB_BUTTON_ARULE))
	    {
	    	textField = window.associationRule;
	    	menu = menuAssociation;
	    }
	        else if (button.getName().equals(SUB_BUTTON_CRULE_LEFT))
	        {
	        	textField = window.leftConfRule;
	        	menu = menuConfident;
	        }
	        	else if ((button.getName().equals(SUB_BUTTON_CRULE_RIGHT)))
	        	{
	        		textField = window.rightConfRule;
	        		menu = menuConfident;
	        	}
	        		else	return;
	    
	    /*
	     * ricavo la stringa nella query che � associata all'attributo selezionato nel menu, e lo rimuovo
	     */
	    String string = textField.getText();
	    String attribute = "<" + ((JMenu)e.getSource()).getText() + ">=<";
	    int startStrAttribute = string.indexOf(attribute);
	    int endStrAttribute = string.indexOf('>', startStrAttribute + attribute.length()) + 1;
	    String regularExpression = string.substring(startStrAttribute, endStrAttribute);
	    if ((startStrAttribute != 0) || (endStrAttribute != string.length()))
	    {
	    	if (endStrAttribute == string.length())
	    		regularExpression = " AND " + regularExpression;
	    	else
	    		regularExpression += " AND ";
	    }
	    textField.setText(string.replaceFirst(regularExpression, ""));
	    
		/*
		 * dal campo dell'attributo, rimetto la visibilit� a tutti i valori ad esso associati, ed infine lo aggiungo al
		 * menu associato al bottone + della barra. 
		 */
		for (MenuElement menuElement : ((JMenu)e.getSource()).getSubElements())
		{
			for (MenuElement menuItem : ((JPopupMenu)menuElement).getSubElements())
			{
				((JMenuItem)menuItem).setVisible(true);
				((JMenuItem)menuItem).setName("N");
			}
		}
		menu.add((JMenu)e.getSource());
	}
	
	private void getRulesFromQuery ()
	{
		/*
		MenuElement attributeMenuAss [] = queryAssociation.getSubElements();
		MenuElement attributeMenuConfL [] = queryLeftConfident.getSubElements();
		MenuElement attributeMenuConfR [] = queryRightConfident.getSubElements();
		*/
		try
		{
			writeObject(socket,4);
			sendQuery(queryAssociation, 'A');
			sendQuery(queryLeftConfident, 'L');
			sendQuery(queryRightConfident, 'R');
			writeObject(socket,'E');
			if (((String)readObject(socket)).equals("OK"))
				window.cpRuleFinder.rulesAreaTxt.setText((String)readObject(socket));
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	private void sendQuery (JPopupMenu menu, char type) throws IOException, ClassNotFoundException
	{
		
		MenuElement attributeMenu [] = menu.getSubElements();
		if (attributeMenu.length != 0)
		{
			for (MenuElement elementMenu : attributeMenu)
			{
				JMenu attribute = (JMenu)elementMenu;
				MenuElement subPopupMenu [] = attribute.getSubElements();
				MenuElement elementAttributeMenu[] = subPopupMenu[0].getSubElements();
				for (MenuElement itemMenu : elementAttributeMenu)
				{
					JMenuItem item = (JMenuItem)itemMenu;
					if (item.getName().equals("Y"))
					{
						writeObject(socket, type);
						writeObject(socket,Integer.parseInt(attribute.getName()));
						writeObject(socket,item.getText());
					} 
				}
			}
		}
		/*
		else
		{
			writeObject(socket, type);
			writeObject(socket, "NULL");
		}
		*/
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
				window.cpRuleViewer.msgAreaTxt.setText(window.cpRuleViewer.msgAreaTxt.getText() + "\nSi � verificato un errore durante la creazione del file PDF");
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
