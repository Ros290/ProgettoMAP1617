package server;

import data.Attribute;
import data.ContinuousAttribute;
import data.Data;
import data.DiscreteAttribute;
import data.EmptySetException;
import database.DatabaseConnectionException;
import mining.AssociationRule;
import mining.AssociationRuleArchieve;
import mining.AssociationRuleMiner;
import mining.ContinuousItem;
import mining.DiscreteItem;
import mining.FrequentPattern;
import mining.FrequentPatternMiner;
import mining.Interval;
import mining.NoPatternException;
import mining.OneLevelPatternException;

import java.net.*;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.io.*;


/**
 * Classe che gestisce una singola connessione da parte di un client
 */
class ServerOneClient extends Thread 
{
    private Socket socket;
    //private Data data;
    private List<Attribute> listAttribute;
    private AssociationRuleArchieve archive;


    /**
     * Inizializza gli attributi socket, in ed out. Avvia il thread.
     * @param s
     * @throws IOException
     */
    ServerOneClient(Socket s) throws IOException {
        socket = s;
    }

    /**
     * Si occupa della ricezione sicura di un oggetto via socket
     * @param socket la quale ricevere l'oggetto
     * @return oggetto ricevuto dal socket specificato
     * @throws ClassNotFoundException nel caso la classe ricevuta non vi è riconosciuta
	 * @throws IOException nel caso si verifichi un errore in fase di lettura
     */
	private static Object readObject(Socket socket) throws ClassNotFoundException, IOException
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
	private static void writeObject(Socket socket, Object o) throws IOException
	{
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(o);
		out.flush();
	}

    /**
     * Riscrive il metodo run della superclasse Thread al fine di gestire le
     * richieste del client
     */
    public void run()
    {
    	while (socket.isConnected())
    	{
            try 
            {
            	int choice = (int)readObject(socket);
                switch (choice)
                {
                	case 1: // LEARNING FROM DB
                		learningFromDb(socket);
                        break;
                    case 2: // STORE CLUSTER IN FILE
                        learningFromFile(socket);
                        break;
                    case 3 : //SEND ATTRIBUTES
                    	sendAttributesList(socket);
                    	break;
                    case 4 :
                    	sendRulesFromQuery(socket);
                    	break;
                    default:
                       	// Nel caso venga selezionata un'operazione non supportata, si esce
                        break;
                }
                
            } 
            catch (IOException | ClassNotFoundException e) 
            {
                System.out.println(e.getMessage());
                break;
            }
        }
    }
    
    private void sendAttributesList(Socket socket)
    {
    	try 
    	{
    		Iterator it = listAttribute.iterator();
    		//for (int i = 0; i < data.getNumberOfAttributes() ; i++)
    		while (it.hasNext())
    		{ 
    			Attribute attribute = (Attribute) it.next();
    			//if (data.getAttribute(i) instanceof DiscreteAttribute)
    			if (attribute instanceof DiscreteAttribute)
    			{
    				DiscreteAttribute da =  (DiscreteAttribute) attribute;
    				writeObject(socket, 'A');
    				writeObject(socket, da.getName());
    				writeObject(socket, da.getIndex());
    				for (int j = 0; j < da.getNumberOfDistinctValues(); j++)
    				{
    					writeObject(socket, 'V');
    					writeObject(socket, da.getValue(j));
    				}
    			}
    			else
    			{
    				ContinuousAttribute ca = (ContinuousAttribute) attribute;
    				writeObject(socket, 'A');
    				writeObject(socket, ca.getName());
    				writeObject(socket, ca.getIndex());
    				Iterator<Float> itCA =  ca.iterator();
    				if (itCA.hasNext())
    				{
    					float inf = itCA.next();
    					while (itCA.hasNext())
    					{
    						writeObject(socket, 'V');
    						float sup = itCA.next();
    						//ContinuousItem item;
    						if (itCA.hasNext())
    							//item = new ContinuousItem ((ContinuousAttribute)currentAttribute, new Interval (inf,sup));
    							writeObject(socket, inf + "," + sup);
    						else
    							writeObject(socket, inf + "," + (sup+0.01f*(sup-inf)));
    						inf = sup;

    					}
    				}
    			}
    		}
    		writeObject(socket, 'K');
		} 
    	catch (IOException e) {
			e.printStackTrace();
		}
    }
    /**
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws EmptySetException 
     * @throws NoPatternException 
     * 
     */
    private void sendRulesFromQuery (Socket socket) throws IOException
    {
    	try
    	{
    		FrequentPattern fpQuery = new FrequentPattern();
    		AssociationRule arQuery = new AssociationRule();
    		int index;
    		char type = (char)readObject(socket); 
    		while(type != 'E')
    		{
    			Attribute attribute = null;
    			index = (int)readObject(socket);
    			Iterator it = listAttribute.listIterator();
    			while (it.hasNext())
    			{
    				attribute = (Attribute) it.next();
    				if (attribute.getIndex() == index)
    					break;
    			}
    			switch (type)
    			{
    				case 'A':
    					if (attribute instanceof DiscreteAttribute)
    					{
    						DiscreteAttribute da = (DiscreteAttribute)attribute;
    						fpQuery.addItem(new DiscreteItem (da, (String)readObject(socket)));
    					}
    					else
    					{
    						ContinuousAttribute ca = (ContinuousAttribute)attribute;
    						String item = (String)readObject(socket);
    						float inf = Float.parseFloat(item.substring(0, item.indexOf(',')));
    						float sup = Float.parseFloat(item.substring(item.indexOf(',') + 1, item.length()));
    						fpQuery.addItem(new ContinuousItem (ca, new Interval(inf,sup) ));
    					}
    					break;
    					
    				case 'L':
    					if (attribute instanceof DiscreteAttribute)
    					{
    						DiscreteAttribute da = (DiscreteAttribute)attribute;
    						arQuery.addAntecedentItem(new DiscreteItem (da, (String)readObject(socket)));
    						//fpQuery.addItem(new DiscreteItem (da, (String)readObject(socket)));
    					}
    					else
    					{
    						ContinuousAttribute ca = (ContinuousAttribute)attribute;
    						String item = (String)readObject(socket);
    						float inf = Float.parseFloat(item.substring(0, item.indexOf(',')));
    						float sup = Float.parseFloat(item.substring(item.indexOf(',') + 1, item.length()));
    						arQuery.addAntecedentItem(new ContinuousItem (ca, new Interval(inf,sup)));
    						//fpQuery.addItem(new ContinuousItem (ca, new Interval(inf,sup) ));
    					}
    					break;
    					
    				case 'R':
    					if (attribute instanceof DiscreteAttribute)
    					{
    						DiscreteAttribute da = (DiscreteAttribute)attribute;
    						arQuery.addConsequentItem(new DiscreteItem (da, (String)readObject(socket)));
    						//fpQuery.addItem(new DiscreteItem (da, (String)readObject(socket)));
    					}
    					else
    					{
    						ContinuousAttribute ca = (ContinuousAttribute)attribute;
    						String item = (String)readObject(socket);
    						float inf = Float.parseFloat(item.substring(0, item.indexOf(',')));
    						float sup = Float.parseFloat(item.substring(item.indexOf(',') + 1, item.length()));
    						arQuery.addConsequentItem(new ContinuousItem (ca, new Interval(inf,sup)));
    						//fpQuery.addItem(new ContinuousItem (ca, new Interval(inf,sup) ));
    					}
    					break;
    			}
    			type = (char)readObject(socket); 
    		}
    		AssociationRuleArchieve subArchieve = archive.getSubArchieve(fpQuery,arQuery);
    		if (!subArchieve.isEmpty())
    		{
    			writeObject(socket,"OK");
    			writeObject(socket,subArchieve.toString());
    			writeObject(socket,"Ottenute le regole ricercate!");
    		}
    		else
    		{
    			writeObject(socket,"ERR");
    			writeObject(socket,"La query inserita non è compresa in alcuna regola");
    		}
    	}
    	catch (IOException | ClassNotFoundException e) 
    	{
			writeObject(socket,"ERR");
			writeObject(socket, "Rilevato errore durante l'esecuzione : " + e.getMessage());
    	}
    	
    	catch (NullPointerException e)
    	{
    		writeObject(socket,"ERR");
			writeObject(socket, "Ricavare prima le regole nel panello 'MINING'");
    	}
    }

    /**
     * Si occupa di leggere il set da file e di inviarlo al client
     * @param socket del client
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void learningFromFile(Socket socket) throws IOException, ClassNotFoundException
    {
		try
        {
	        String fileName = (String)readObject(socket);
	        archive=new AssociationRuleArchieve();
	        try
	        {
	        	//Qualora, durante il caricamento del file, venga sollevata un'eccezione, molto probabilmente esso sarà provocata dal fatto che
	        	//non vi sia alcun file con lo stesso nome di fileName.
	        	archive = AssociationRuleArchieve.carica(fileName);
	        	
	            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName + "Att.dmp"));
	            listAttribute = (LinkedList<Attribute>) in.readObject();
	            in.close();

	        	String result = archive.toString();
	        	writeObject(socket,"OK");
	        	writeObject(socket, result);
	        	writeObject(socket, "Operazione Completata");
	        }
	        catch (Exception e)
	        {
	        	writeObject(socket,"ERR");
	        	writeObject(socket, "Il file " +fileName+ " non esiste!");
	        }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * Si occupa di leggere il set dal database e di inviarlo al client
     * @param socket del client
     * @throws IOException 
     */
    private void learningFromDb(Socket socket) throws IOException
    {
    	float minSup,minConf;
    	try 
    	{
			String tableName = (String)readObject(socket);
			minSup = (float)readObject(socket);
			minConf = (float)readObject(socket);
			String fileName = (String)readObject(socket);
			Data data = new Data(tableName);
			if ((minSup>0 && minSup<1) && (minConf>0 && minConf<1))
			{
				LinkedList<FrequentPattern> outputFP = FrequentPatternMiner.frequentPatternDiscovery(data,minSup);
				archive=new AssociationRuleArchieve();
				try 
				{
					Iterator<FrequentPattern> it=outputFP.iterator();
					while(it.hasNext())
					{
						FrequentPattern FP=it.next();
						archive.put(FP);	
						LinkedList<AssociationRule> outputAR=null;
						try 
						{
							outputAR = AssociationRuleMiner.confidentAssociationRuleDiscovery(data,FP,minConf);
							Iterator<AssociationRule> itRule=outputAR.iterator();
							//genero il treeSet composto dalle regole di confidenza ricavate da FP
							TreeSet<AssociationRule> tree= new TreeSet<AssociationRule>();
							while(itRule.hasNext())
							{
								tree.add(itRule.next());
							}
							//inserisco il frequent Pattern e le sue regole di confidenza nell'archivio
							if (!tree.isEmpty())
								archive.put(FP,tree);	
						} 
						catch (OneLevelPatternException | NoPatternException e) 
						{
							// TODO Auto-generated catch block
							//System.out.println(e.getMessage());
						}		
					}
					String result = archive.toString();
					archive.salva(fileName);
					writeObject(socket,"OK");
					writeObject(socket, result);
					writeObject(socket, "Ottenute le regole richieste!");
					listAttribute = new LinkedList <Attribute>();
					for (int i = 0; i < data.getNumberOfAttributes() ; i++){
						listAttribute.add(data.getAttribute(i));
					}
					
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName + "Att.dmp"));
			        out.writeObject(listAttribute);
			        out.close();
				} 		
				catch(Exception e)
				{
					writeObject(socket,"ERR");
					writeObject(socket, e.getMessage());
				}
			}
			else
			{
				writeObject(socket,"ERR");
				writeObject(socket, "Valore di Supporto e di Confidenza devono essere > 0 e < 1");
			}
		}
    	
    	catch (IOException | ClassNotFoundException e) 
    	{
			writeObject(socket,"ERR");
			writeObject(socket, "Rilevato errore durante l'esecuzione : " + e.getMessage());
    	}
    	
    	catch (SQLException e) 
    	{
			writeObject(socket,"ERR");
			writeObject(socket, "Errore, non vi è alcuna tabella nel database associato a quello ricercato");
    	}
    	
    	catch (DatabaseConnectionException e) 
    	{
			writeObject(socket,"ERR");
			writeObject(socket, "Problemi con la comunicazione con il database");
    	}
    	
    	catch (EmptySetException e) 
    	{
			writeObject(socket,"ERR");
			writeObject(socket, "Tale tabella non contiene elementi");
    	}
    	
    }

}
