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
    private String fileName;


    /**
     * Inizializza gli attributi socket, in ed out. Avvia il thread.
     * @param s
     * @throws IOException
     */
    ServerOneClient(Socket s) throws IOException {
        socket = s;
        fileName = "map";
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
            	char c[];
            	//Fintanto che dal client non arriva un CHIARO comando (y/n), il server manderà la stessa richiesta, fintanto non otterrà il comando corretto
            	//o finchè la connessione rimane attiva
            	do
            	{
            		writeObject(socket,"OK");
            		writeObject(socket, "Carica archivio/Crea archivio? (y/n)");
            		c = String.valueOf(readObject(socket)).toCharArray();
            	}while ((c[0] != 'y')&&(c[0] != 'n'));

                switch (c[0])
                {
                	case 'n': // LEARNING FROM DB
                		learningFromDb(socket);
                        break;
                    case 'y': // STORE CLUSTER IN FILE
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
			writeObject(socket,"OK");
			writeObject(socket, "Nome file di restore:");
	        this.fileName = (String)readObject(socket);
	        AssociationRuleArchieve archive=new AssociationRuleArchieve();
	        try
	        {
	        	//Qualora, durante il caricamento del file, venga sollevata un'eccezione, molto probabilmente esso sarà provocata dal fatto che
	        	//non vi sia alcun file con lo stesso nome di fileName. Ciò significa che, l'unica operazione che si può svolgere, è quella
	        	//di assegnare il nomeFile ad un nuovo file, quale conterrà il risultato della nuova ricerca da parte del client
	        	archive = AssociationRuleArchieve.carica(this.fileName);
	        }
	        catch (Exception e)
	        {
	        	learningFromDb(socket);
	        	return;
	        }
    		String result = archive.toString();
    		writeObject(socket,"END");
            writeObject(socket, result);
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
     */
    private boolean learningFromDb(Socket socket)
    {
    	float minSup,minConf;
    	Object o;
    	try 
    	{
    		writeObject(socket,"OK");
    		writeObject(socket, "Digita il nome della tabella da processare");
			o = readObject(socket);
			String tableName = (String)o;
			Data data = new Data(tableName);
			do
			{
				writeObject(socket,"OK");
				writeObject(socket, "Inserisci minsup (in [0,1])");
				minSup = Float.valueOf(String.valueOf(readObject(socket)));
			}while (minSup<0 || minSup>1);
			do
			{
				writeObject(socket,"OK");
				writeObject(socket, "Inserisci minconf (in [0,1])");
				minConf = Float.valueOf(String.valueOf(readObject(socket)));
			}while (minConf<0 || minConf>1);
			LinkedList<FrequentPattern> outputFP = FrequentPatternMiner.frequentPatternDiscovery(data,minSup);
			AssociationRuleArchieve archive=new AssociationRuleArchieve();
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
                writeObject(socket,"END");
                writeObject(socket, result);
			} 		
            catch(Exception e)
            {
               	writeObject(socket,"END");
              	writeObject(socket, e.getMessage());
			}
		}
    	catch (Exception e) 
    	{
            e.printStackTrace();
            System.out.println(e.getMessage());
    	} 
    	finally
    	{
    		try 
    		{
				socket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return true;
    }

}
