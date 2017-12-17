package client;

import java.awt.Component;
import java.awt.Font;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
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
import java.util.Map;
//import java.util.List;
import java.awt.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
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





























import client.Apriori.Frame.JPanelRulesArea;

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
	JPopupMenu queryAssociation = new JPopupMenu();
	JPopupMenu queryLeftConfident = new JPopupMenu(); 
	JPopupMenu queryRightConfident = new JPopupMenu();
	
	class Frame extends JPanel
	{
		private JFrame frame;
		private JTextField nameDataTxt, nameMinSupTxt, nameMinConfTxt, nameFileTxt;
		private List associationRule, leftConfRule, rightConfRule;
		private JPanelRulesArea cpRuleViewer, cpRuleFinder;
		private JRadioButton db, file;
		
		/*
		 * Rapressenta i due JText presenti su ciascun panel
		 */
		class JPanelRulesArea extends JPanel
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
		 * permette ad un JLabel di essere cliccato, in modo tale da mostrare un determinato messaggio.
		 * Utile qualora si voglia descrivere un determinato campo
		 * @param label il campo che deve contenere questa funzionalità
		 * @param text titolo da impostare nel messaggio
		 * @param descriptionText messaggio per esteso da mostrare
		 */
		public void setLabelDescription (JLabel label, String text, String descriptionText)
		{
			label.setText("<html><u>"+text+"</u></html>");
			label.addMouseListener(
					new MouseListener(){
						
						@Override
						public void mouseClicked(MouseEvent arg0) {
							// TODO Auto-generated method stub
							JOptionPane.showMessageDialog(null, descriptionText,"Description "+ text, JOptionPane.INFORMATION_MESSAGE);
							
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
			cpAprioriMining.setBounds(10, 11, 179, 95);
			miningPanel.add(cpAprioriMining);
			cpAprioriMining.setBorder(BorderFactory.createTitledBorder("Selecting Data Source"));
			cpAprioriMining.setLayout(null);
			
			db = new JRadioButton("Learning from db");
			db.setBounds(6, 24, 150, 23);
			cpAprioriMining.add(db);
			/*
			 * Ogni volta che viene selezionato il pallino, viene modificata la visibilità
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
			file.setBounds(6, 50, 150, 23);
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
			cpAprioriInput.setBounds(199, 11, 428, 95);
			cpAprioriInput.setBorder(BorderFactory.createTitledBorder("Input Parameters"));
			miningPanel.add(cpAprioriInput);
			cpAprioriInput.setLayout(null);
			
			JLabel data = new JLabel();
			setLabelDescription(data, "Data", "Riportare il nome della tabella quale contiene i valori su cui si desidera effettuare l'operazione di Mining");
			data.setBounds(10, 24, 46, 14);
			cpAprioriInput.add(data);
			
			nameDataTxt = new JTextField();
			nameDataTxt.setBounds(58, 21, 62, 20);
			cpAprioriInput.add(nameDataTxt);
			nameDataTxt.setColumns(10);
			
			JLabel minSup = new JLabel();
			minSup.setBounds(130, 24, 56, 14);
			setLabelDescription (minSup, "Min Sup", "Riportare il valore minimo di supporto per ricercare le regole d'associazione");
			cpAprioriInput.add(minSup);
			
			nameMinSupTxt = new JTextField();
			nameMinSupTxt.setColumns(10);
			nameMinSupTxt.setBounds(187, 21, 62, 20);
			cpAprioriInput.add(nameMinSupTxt);
			
			JLabel nameFile = new JLabel("");
			nameFile.setBounds(10, 52, 46, 14);
			setLabelDescription (nameFile, "Save", "Riportare il nome del file su cui si desidera salvare / caricare le regole ricavate (facoltativo)");
			cpAprioriInput.add(nameFile);
			
			nameFileTxt = new JTextField();
			nameFileTxt.setColumns(10);
			nameFileTxt.setBounds(58, 49, 62, 20);
			cpAprioriInput.add(nameFileTxt);
			
			JLabel minConf = new JLabel();
			minConf.setBounds(130, 52, 56, 14);
			setLabelDescription (minConf, "Min Conf", "Riportare il valore minimo di confidenza per ricercare le regole di confidenza associate alle regole di associazione");
			cpAprioriInput.add(minConf);
			
			nameMinConfTxt = new JTextField();
			nameMinConfTxt.setColumns(10);
			nameMinConfTxt.setBounds(187, 49, 62, 20);
			cpAprioriInput.add(nameMinConfTxt);
			
			JButton aprioriConstructionBt = new JButton("Mine");
			aprioriConstructionBt.setBounds(259, 20, 161, 23);
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
			aprioriPDFBt.setBounds(259, 48, 161, 23);
			cpAprioriInput.add(aprioriPDFBt);
			aprioriPDFBt.addActionListener
			(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					Learning();
					if (!cpRuleViewer.rulesAreaTxt.getText().isEmpty())
						PDFCreator(cpRuleViewer);
					else
						cpRuleViewer.msgAreaTxt.setText(cpRuleViewer.msgAreaTxt.getText() + "\nImpossibile creare il file PDF");
				}
			});
			
			cpRuleViewer = new JPanelRulesArea(miningPanel);

			JLayeredPane findRulePanel = new JLayeredPane();
			aprioriTab.addTab("Find", null, findRulePanel, null);
			
			JPanel selectionRulesApriori = new JPanel();
			selectionRulesApriori.setBounds(0, 0, 637, 374);
			findRulePanel.add(selectionRulesApriori);
			selectionRulesApriori.setLayout(null);
			
			//CONFIDENT RULE QUERY PANEL
			
			JPanel findConfRulePanel = new JPanel();
			findConfRulePanel.setLayout(null);
			findConfRulePanel.setBorder(BorderFactory.createTitledBorder("Confident Rule"));
			findConfRulePanel.setBounds(212, 11, 315, 95);
			selectionRulesApriori.add(findConfRulePanel);
			
			leftConfRule = new List();
			leftConfRule.setBounds(10, 22, 135, 42);
			findConfRulePanel.add(leftConfRule);
			
			JLabel label = new JLabel("=>");
			label.setBounds(142, 40, 16, 14);
			findConfRulePanel.add(label);
			
			rightConfRule = new List();
			rightConfRule.setBounds(159, 22, 146, 42);
			findConfRulePanel.add(rightConfRule);
			
			JButton addLeftConfRule = new JButton("+");
			addLeftConfRule.setBounds(10, 70, 41, 16);
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
			subLeftConfRule.setBounds(101, 70, 41, 16);
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
			addRightConfRule.setBounds(158, 70, 41, 16);
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
			subRightConfRule.setBounds(264, 70, 41, 16);
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
			
			
			//ASSOCIATION RULE QUERY PANEL
			
			JPanel findAssRulePanel = new JPanel();
			findAssRulePanel.setLayout(null);
			findAssRulePanel.setBorder(BorderFactory.createTitledBorder("Association Rule"));
			findAssRulePanel.setBounds(10, 11, 192, 95);
			selectionRulesApriori.add(findAssRulePanel);

			associationRule = new List();
			associationRule.setBounds(10, 22, 172, 40);
			findAssRulePanel.add(associationRule);
			
			JButton addAssociationRule = new JButton("+");
			addAssociationRule.setBounds(10, 68, 41, 16);
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
			subAssociationRule.setBounds(141, 68, 41, 16);
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
			findPdf.setBounds(537, 65, 90, 30);
			findPdf.addActionListener
			(new java.awt.event.ActionListener() 
			{
				public void actionPerformed(ActionEvent e) 
				{
					getRulesFromQuery();
					if (!cpRuleFinder.rulesAreaTxt.getText().isEmpty())
						PDFCreator(cpRuleFinder);
					else
						cpRuleFinder.msgAreaTxt.setText(cpRuleFinder.msgAreaTxt.getText() + "\nImpossibile creare il file PDF");
				}
			});
			selectionRulesApriori.add(findPdf);
			
			JButton find = new JButton("FIND");
			find.setBounds(537, 24, 90, 30);
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


	/**
	 *Istanzia i menù e inserisce i valori ricavati dal server su 'menuAssociation' e 'menuConfident'
	 * 
	 *ATTENZIONE: finchè il client non effettua alcuna ricerca di mining al server, non potrà ottenere gli attributi-valori
	 *da ricercare. Inoltre, ad ogni nuova ricerca di mining effettuata, viene invocata questa funzione, riportando
	 *i nuovi attributi-valori ottenuti dalla nuova ricerca
	 */
	@SuppressWarnings("deprecation")
	private void getAttributesList (Socket socket) throws ClassNotFoundException, IOException
	{
		String string;
		int index;
		menuAssociation = new JPopupMenu ();
		menuConfident = new JPopupMenu ();
		queryAssociation = new JPopupMenu ();
		queryLeftConfident = new JPopupMenu ();
		queryRightConfident = new JPopupMenu ();

		char c = (char)readObject(socket);
		/*
		 *Ricava gli attributi e gli item associati ad essi dal server nel seguente formato:
		 * 		
		 * 		- 'A', carattere che indica che la prossima stringa indica il nome di un attributo -> 'nome Attributo'
		 * 		- 'V', carattere che indica che la prossima stringa indica un valore riferito all'attributo
		 * 			passato precendemente -> 'nome Valore'
		 */
		while (c == 'A')
		{
			string = (String)readObject(socket);
			index = (int)readObject(socket);
			/*
			 * definisco il campo quale sarà associato al nome dell'attributo.
			 * tale campo, conterrà al suo interno i campi relativi ai valori assumibili da tale attributo.
			 * 
			 * ES: attributo Marca Macchina , i valori assumibili possono essere "Peugot", "Fiat", ecc 
			 * 
			 * Serve affinchè l'utente possa, in seguito, effettuare delle ricerce dettagliate in merito alle regole ricavate (vedi pannello Query)
			 * 
			 * Ci sono due menu quali, almeno inizialmente, conterranno gli stessi elementi, ovvero il menù relativo alle regole d'associazione, e quello
			 * relativo alle regole di confidenza
			 */
			
			//MENU REGOLE D'ASSOCIAZIONE
			JMenu menuAttributeAss = new JMenu (string);
			menuAttributeAss.setName(String.valueOf(index));
			/*
			 * il campo relativo al nome dell'attributo potrà essere selezionato quando si effettuerà
			 * l'operazione di modifica della query di ricerca delle regole (vedi pannello Query).
			 * Per la precisione, quando verrà cliccato nel menù che comparirà dopo che si è premuto il pulsante ' - '.
			 * Così facendo, si rimuoverà l'attributo sia dalla query e sia dal relativo menu
			 */
			menuAttributeAss.addMouseListener
			(new MouseListener()
			{

				@Override
				public void mouseClicked(MouseEvent arg0) 
				{
					// ricavo il popupMenu che contiene l'elemento del menu selezionato
					JPopupMenu popupMenu = (JPopupMenu) ((JMenu)arg0.getSource()).getParent();
					Component invokerMenu = popupMenu.getInvoker();
					// dal popupMenu, ricavo il bottone da cui è stato invocato
					JButton button = (JButton)invokerMenu;
					//controllo che si operi sul bottone ' - ', in tal caso procedo a rimuovere l'elemento selezionato dal menu a tendina
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
			
			//MENU REGOLE DI CONFIDENZA
			
			/*
			 * Grosso modo, è identico a quello delle regole di Associazione
			 */
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
					// dal popupMenu, ricavo il bottone da cui è stato invocato
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
				 * definisco il campo quale sarà associato il valore.
				 * tale valore potrà essere aggiunto alla query tramite il pulsante +
				 */
				JMenuItem menuItemAss = new JMenuItem(string);
				//Il motivo per cui si imposta tale nome sarà più chiaro nelle funzioni 'addingItemToQuery' e 'removingItemFromQuery'
				menuItemAss.setName("N");
				//il valore dell'attributo potrà essere selezionato per poterlo inserire nella Query
				menuItemAss.addActionListener				
				(new java.awt.event.ActionListener() 
				{
					public void actionPerformed(ActionEvent e) 
					{
						//ricavo il bottone quale ha generato l'evento, assicurandomi che si tratti del pulsante ' + ', dopo di che provvedo ad aggiungerlo alla query
						JButton button = (JButton)((Component)((JPopupMenu)((Component)((JPopupMenu)((JMenuItem) e.getSource()).getParent()).getInvoker()).getParent()).getInvoker());
						if (button.getName().startsWith("add"))
							addingItemToQuery(e);	
					}
				});
				
				//come prima
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
				//aggiungo il valore al campo attributo
				menuAttributeAss.add(menuItemAss);
				menuAttributeConf.add(menuItemConf);
				
				c = (char)readObject(socket);
			}
			//infine, aggiungo il campo attributo ad entrambi i menu
			menuAssociation.add(menuAttributeAss);
			menuConfident.add(menuAttributeConf);
		}
	}

	/**
	 * In base al valore selezionato, lo aggiunge alla query e modifica i menù riferiti alla barra di ricerca su cui si opera
	 * @param e
	 */
	private void addingItemToQuery (ActionEvent e)
	{
		List textField;
		JPopupMenu menu;
	    JMenuItem selectedMenuItem = (JMenuItem) e.getSource(); 
	    JPopupMenu selectedMenuAttribute = (JPopupMenu) selectedMenuItem.getParent(); 
	    Component invoker = selectedMenuAttribute.getInvoker();
	    JPopupMenu popupMenu = (JPopupMenu) invoker.getParent();
		Component invokerMenu = popupMenu.getInvoker();
		//ricavo il bottone da cui è stato aperto il menu
		JButton button = (JButton)invokerMenu; 
	    /*
	     * Definisco la barra associata al bottone, e il menu su cui sarà spostato il campo selezionato.
	     * 
	     * Difatti, lo stesso campo JMenu non può risiedere su differenti menù, perciò se viene aggiunto
	     * nel menu Y, il menu X che lo conteneva adesso non vi avrà più accesso
	     * 
	     * Quindi il nuovo menù su cui andrò ad aggiungere tale campo, sarà quello riferito al bottone -
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
		
		JMenu menuAttribute = (JMenu)invoker;
		textField.add("<" + menuAttribute.getText() + ">=<" + selectedMenuItem.getText() + ">");
		selectedMenuItem.setName("Y");		
		
		/*
		 * dal campo dell'attributo, tolgo la visibilità a tutti i valori ad esso associati, ed infine lo aggiungo al
		 * menu associato al bottone - della barra. 
		 * 
		 * Questo perchè, appena si aprirà il menu nel bottone -, si vedranno solamente i nomi degli attributi presenti
		 * nella query, ma non i valori da essi assumibili.
		 */
		for (MenuElement menuElement : (menuAttribute).getSubElements())
			for (MenuElement menuItem : ((JPopupMenu)menuElement).getSubElements())
				((JMenuItem)menuItem).setVisible(false);
		menu.add(menuAttribute);
	}
	
	/**
	 * Contrariamente alla funzione addingItemToQuery, rimuove un determinato campo dalla query e modifica
	 * i menù associati alla barra di ricerca su cui si opera
	 * @param e
	 */
	private void removingItemFromQuery (MouseEvent e)
	{
		List textField;
		JPopupMenu menu;
		JPopupMenu popupMenu = (JPopupMenu) ((JMenu)e.getSource()).getParent();
		Component invokerMenu = popupMenu.getInvoker();
		JButton button = (JButton)invokerMenu;
		
	    /*
	     * Definisco la barra associata al bottone, e il menu su cui sarà spostato il campo selezionato.
	     * 
	     * (vedi commento presente all'interno della funzione 'addingItemToQuery'
	     * 
	     * Quindi il nuovo menù su cui andrò ad aggiungere tale campo, sarà quello riferito al bottone +
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
		 * dal campo dell'attributo, rimetto la visibilità a tutti i valori ad esso associati, ed infine lo aggiungo al
		 * menu associato al bottone + della barra. 
		 */
	    textField.removeAll();
	    
		for (MenuElement menuElement : ((JMenu)e.getSource()).getSubElements())
		{
			for (MenuElement menuItem : ((JPopupMenu)menuElement).getSubElements())
			{
				((JMenuItem)menuItem).setVisible(true);
				((JMenuItem)menuItem).setName("N");
			}
		}
		menu.add((JMenu)e.getSource());
		
		for (MenuElement menuElement : popupMenu.getSubElements())
		{
			JMenu attribute = (JMenu)menuElement;
			MenuElement subPopupMenu [] = attribute.getSubElements();
			MenuElement elementAttributeMenu[] = subPopupMenu[0].getSubElements();
			for (MenuElement itemMenu : elementAttributeMenu)
			{
				JMenuItem item = (JMenuItem) itemMenu;
				if (item.getName().equals("Y"))
				{
					textField.add("<" + attribute.getText() + ">=<" + item.getText() + ">");
					break;
				}
			}
		}
	}
	
	/**
	 * Effettua la richiesta al server di ottenere le regole in base ai valori impostati nella sua ricerca
	 */
	private void getRulesFromQuery ()
	{
		try
		{
			writeObject(socket,4);
			sendQuery(queryAssociation, 'A');
			sendQuery(queryLeftConfident, 'L');
			sendQuery(queryRightConfident, 'R');
			writeObject(socket,'E');
			String esito = (String)readObject(socket); 
			if (esito.equals("OK"))
			{
				window.cpRuleFinder.rulesAreaTxt.setText((String)readObject(socket));
				window.cpRuleFinder.msgAreaTxt.setText((String)readObject(socket));
			}
			else if (esito.equals("ERR"))
			{
				window.cpRuleFinder.rulesAreaTxt.setText("");
				window.cpRuleFinder.msgAreaTxt.setText((String)readObject(socket));
			}
			else
			{
				window.cpRuleFinder.rulesAreaTxt.setText("");
				window.cpRuleFinder.msgAreaTxt.setText("Messaggio non riconosciuto dal Client");
			}
		}
		catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * provvede ad analizzare, nel menu passato come parametro, quali sono stati le regole selezionate
	 * in base anche al tipo di regole (regole di supporto o di confidenza (antecedenti o consequenti).
	 * Dopo di che, provvede a mandare la richiesta al server.
	 * @param menu menu da cui ricavare le regole selezionate
	 * @param type descrive il tipo di regole quali devono essere inviate al server
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void sendQuery (JPopupMenu menu, char type) throws IOException, ClassNotFoundException
	{
		//ricava tutti gli elementi presenti nel menu
		MenuElement attributeMenu [] = menu.getSubElements();
		//qualora non siano stati selezionate regole , semplicemente non manda la richiesta e termina la funzione. altrimenti continua
		if (attributeMenu.length != 0)
		{
			for (MenuElement elementMenu : attributeMenu)
			{
				JMenu attribute = (JMenu)elementMenu;
				MenuElement subPopupMenu [] = attribute.getSubElements();
				//riporto in un vettore tutti i valori assumibili dall'attributo in analisi
				MenuElement elementAttributeMenu[] = subPopupMenu[0].getSubElements();
				for (MenuElement itemMenu : elementAttributeMenu)
				{
					//per ogni valore, verifico se esso sia stato selezionato (ovvero se ha nome = Y) o meno
					JMenuItem item = (JMenuItem)itemMenu;
					if (item.getName().equals("Y"))
					{
						//trovato il valore selezionato dell'attributo, provvedo a mandare la richiesta al server
						//indicando il tipo della regola, l'indice associato all'attributo ed, infine, il valore ricercato
						writeObject(socket, type);
						writeObject(socket,Integer.parseInt(attribute.getName()));
						writeObject(socket,item.getText());
					} 
				}
			}
		}
	}

	/**
	 * provvede a mandare al server la richiesta di ottenere le regole, in base ai valori impostati
	 * dopo di che, ricevuta la risposta, provvede a caricare i dati sulla schermata  
	 */
	private void Learning () 
	{
		try 
		{
			//in base alla richiesta, provvederà inanzitutto a mandare un numero intero al server, quale indicherà il tipo di operazione richiesto.
			// se 1 -> indica che vuole acquisire le regole dal database
			// se 2 -> indica che vuole acquisire le regole da un file 
			if (window.db.isSelected())
			{	
				if ((!window.nameDataTxt.getText().isEmpty())&&(!window.nameMinSupTxt.getText().isEmpty())&&(!window.nameMinConfTxt.getText().isEmpty())){
					writeObject (socket, 1);
					writeObject (socket, window.nameDataTxt.getText());
					writeObject(socket, Float.parseFloat(window.nameMinSupTxt.getText()));
					writeObject (socket, Float.parseFloat(window.nameMinConfTxt.getText()));
				}
				else {
					window.cpRuleViewer.rulesAreaTxt.setText("");
					window.cpRuleViewer.msgAreaTxt.setText("Assicurarsi di aver compilato i campi Data / min Sup / min Conf");
					return;
				}
			}
			else
				writeObject (socket, 2);
			//per semplicità, qualora il client non abbia specificato un file su cui immettere le regole, il server provvederà comunque
			//a salvarli in un file rinominato "map". ovviamente tale file verrà sovrascritto ad ogni nuova ricerca effettuata.
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
	
	/**
	 * provvedere a salvare le regole ottenute dal server in un file pdf (quale sarà depositato nel dispositivo del client attualmente in uso)
	 * richiedendo, inoltre, dove depositarlo e il nome da affidargli
	 * @param jPanel il pannello da cui prendere le regole che poi saranno riportate nel file pdf 
	 */
	public void PDFCreator (JPanelRulesArea jPanel) 
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
				com.itextpdf.text.Paragraph p = new com.itextpdf.text.Paragraph(jPanel.rulesAreaTxt.getText());
				document.add(p);
				document.setPageSize(two);
				document.close();
				jPanel.msgAreaTxt.setText(jPanel.msgAreaTxt.getText() + "\nFile PDF salvato con successo!");
			}
			catch (FileNotFoundException | DocumentException e) 
			{
				jPanel.msgAreaTxt.setText(jPanel.msgAreaTxt.getText() + "\nSi è verificato un errore durante la creazione del file PDF");
			}
		}
	}
	
	public void init()
	{
		String strHost = DEFAULT_HOST;
		int port = DEFAULT_PORT;
		try 
		{
			window = new Frame();
			window.frame.setVisible(true);
			InetAddress addr = InetAddress.getByName(strHost); // ottiene l'indirizzo dell'host specificato
			System.out.println("Connecting to " + addr + "...");
			socket = new Socket(addr, port); // prova a connetters
			System.out.println("Success! Connected to " + socket);
		} 
		catch (IOException e) 
		{
			JOptionPane.showMessageDialog(null, "Unable to connect to " + strHost + ":" + port + ".\n" + e.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
			this.destroy();
			System.exit(0);
		}
	}
}
