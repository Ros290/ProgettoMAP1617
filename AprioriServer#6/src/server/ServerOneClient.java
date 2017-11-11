package server;

import data.Data;
import data.EmptySetException;
import database.DatabaseConnectionException;
import mining.AssociationRule;
import mining.AssociationRuleArchieve;
import mining.AssociationRuleMiner;
import mining.FrequentPattern;
import mining.FrequentPatternMiner;
import mining.NoPatternException;
import mining.OneLevelPatternException;

import java.net.*;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;
import java.io.*;


/**
 * Classe che gestisce una singola connessione da parte di un client
 */
class ServerOneClient extends Thread {
    private Socket socket;
    private Data data;
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
                    default:
                       	// Nel caso venga selezionata un'operazione non supportata, si esce
                        break;
                }
                
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e.getMessage());
                break;
            }
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
			data = new Data(tableName);
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
				} 		
				catch(Exception e)
				{
					writeObject(socket,"END");
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
