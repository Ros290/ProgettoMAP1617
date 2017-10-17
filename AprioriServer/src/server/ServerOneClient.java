package server;

import data.Data;
import data.EmptySetException;
import mining.AssociationRule;
import mining.AssociationRuleArchieve;
import mining.AssociationRuleMiner;
import mining.FrequentPattern;
import mining.FrequentPatternMiner;
import mining.NoPatternException;
import mining.OneLevelPatternException;

import java.net.*;
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
            	char c[];
            	do
            	{
            		writeObject(socket,"OK");
            		writeObject(socket, "Carica archivio/Crea archivio? (y/n)");
            		c = String.valueOf(readObject(socket)).toCharArray();
            	}while ((c[0] != 'y')&&(c[0] != 'n'));

                switch (c[0])
                {
                	case 'n': // LEARNING FROM DB
                		learningFromDb(socket, fileName);
                        break;
                    case 'y': // STORE CLUSTER IN FILE
                        learningFromFile(socket);
                        break;
                    default:
                       	// Nel caso venga selezionata un'operazione non supportata, si esce
                        System.out.println("Operation " + c[0] + " from " + socket + " not supported.\nThe connection will be closed.");
                        socket.close();
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
	        String fileName = (String)readObject(socket);
        }
        catch (Exception e)
        {
        	
        }
		AssociationRuleArchieve archive=new AssociationRuleArchieve();
	    try
	    {
	    	archive = AssociationRuleArchieve.carica(fileName);
	    }
	    catch (Exception e)
	    {
	    	learningFromDb(socket,fileName);
	    }
        
    	try
        {
    		String result = archive.toString();
    		writeObject(socket,"OK");
            writeObject(socket, result);
        }
        catch (Exception e)
        {
            	
        }
    }
    
    /**
     * Si occupa di leggere il set dal database e di inviarlo al client
     * @param socket del client
     */
    private boolean learningFromDb(Socket socket, String fileName)
    {
    	float minSup,minConf;
    	Object o;
    	try {
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
                	            writeObject(socket,"OK");
                	            writeObject(socket, result);
							} 		
                            catch(Exception e)
                			{
                            	writeObject(socket,"OK");
                            	writeObject(socket, e.getMessage());
							}

		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
			return false;
		} catch (IOException e) {
			System.out.println("I/O Error: " + e.getMessage());
			return false;
		} catch (EmptySetException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
