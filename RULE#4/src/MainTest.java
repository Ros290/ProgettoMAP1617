import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import keyboardinput.Keyboard;
import mining.AssociationRule;
import mining.AssociationRuleArchieve;
import mining.AssociationRuleMiner;
import mining.FrequentPattern;
import mining.FrequentPatternMiner;
import mining.NoPatternException;
import mining.OneLevelPatternException;
import data.Data;
import data.EmptySetException;





public class MainTest 
{

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Data data= new Data();
		AssociationRuleArchieve archive=new AssociationRuleArchieve();
		float minSup=(float)0.0,minConf=(float)0.0;
		String nomeFile = "";
		char choice;
		do
		{
			System.out.println("Salvare / Caricare le regole d'associazione? (y/n)");
			choice = Keyboard.readChar();
		}while (choice != 'n' && choice != 'y');
		//CARICARE / SALVARE
		if (choice == 'y')
		{
			try
			{
				/*
				 * Si effettua un'unica richiesta , difatti non chiede quale operazione debba svolgere
				 * nello specifico, ma chiede solo di inserire il nome del file.
				 */
				System.out.println("Inserire nome del file da caricare (qualora sia presente tale file) o da salvare (altrimenti) ");
				/*
				 * Quale che sia l'opzione da eseguire, viene mantenuto in memoria il nome del file.
				 */
				nomeFile += Keyboard.readString();
				/*
				 * Se il file è presente, allora non viene sollevata alcuna eccezione e si procede
				 * normalmente al passo successivo, caricando i valori nel file nella variabile
				 * "archive". Se, invece, il file non è presente, verrà sollevata un'eccezione quale,
				 * non farà nulla. Ma in questo caso, la variabile "archive" resterà vuota.
				 */
				archive = AssociationRuleArchieve.carica(nomeFile);
			}
			catch (Exception e)
			{
				//System.out.println(e.getMessage());
			}
		}
		/*
		 * Qualora l'utente non abbia fatto richiesta di salvare/caricare, si procede normalmente.
		 * Altrimenti, se ha "caricato" un file, vorrà dire allora che la variabile "archive" non sarà
		 * vuota, pertanto provvede unicamente a stampare i valori in esso contenuti
		 */
		if (archive.isEmpty())
		{
			do
			{
				System.out.println("Inserisci minsup (in [0,1])");
				minSup=Keyboard.readFloat();
			}while (minSup<0 || minSup>1);
			
			do
			{
				System.out.println("Inserisci minconf (in [0,1])");
				minConf=Keyboard.readFloat();
			}while (minConf<0 || minConf>1);
			
			
			try{
				LinkedList<FrequentPattern> outputFP=FrequentPatternMiner.frequentPatternDiscovery(data,minSup);
				
				
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
				/*
				 * Come mostrato precedentemente, qualora l'utente avesse richiesto di "salvare/caricare"
				 * in tal caso, giunti a questo punto, si può solo "salvare" i risultati ottenuti in
				 * un file. Infatti, qualora avesse fatto tale richiesta, si verifica se l'utente
				 * ha indicato o meno il nome del file dove memorizzarli.
				 */
				if (!nomeFile.equals(""))
					archive.salva(nomeFile);
			}
			catch(Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
		/*
		 * Quale che sia stata la scelta dell'utente, alla fine il programma termina l'esecuzione
		 * stampando i risultati inseriti all'interno della variabile "archive"
		 */
		System.out.print(archive);
		
		
	}

}
