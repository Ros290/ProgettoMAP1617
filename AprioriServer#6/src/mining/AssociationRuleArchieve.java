package mining;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class AssociationRuleArchieve implements Serializable
{
	HashMap <FrequentPattern, TreeSet<AssociationRule>> archive;
	
	/**
	 * Costruttore
	 */
	public AssociationRuleArchieve()
	{
		archive = new LinkedHashMap<FrequentPattern, TreeSet<AssociationRule>>();
	}
	
	/**
	 * aggiunge il frequent pattern all'archivio. Esso non sarà associato ad alcuna regola di associazione
	 * @param fp Frequent Pattern da riportare all'interno dell'archivio
	 */
	public void put(FrequentPattern fp)
	{
		archive.put(fp, null);
	}
	
	/**
	 * aggiunge il frequent pattern all'archivio, insieme alle regole di associazione ad essa associati
	 * @param fp Frequent Pattern da riportare all'interno dell'archivio
	 * @param rule regole di associazione quali si riferiscono a fp
	 */
	public void put(FrequentPattern fp, TreeSet<AssociationRule> rule)
	{

		archive.put(fp,rule);
	}
	
	/**
	 * Ritorna le regole di associazione relative al dato Frequent Pattern passato come parametro.
	 * Qualora nell'archivio non vi sia il pattern richiesto o qualora non vi siano regole di associazione, viene lanciata l'eccezione
	 * @param fp Frequent Pattern da ricercare per ottenere le regole di associazone richieste
	 * @return regole di associazione relative a fp
	 * @throws NoPatternException
	 */
	public TreeSet<AssociationRule> getRules (FrequentPattern fp) throws NoPatternException
	{
		if(archive.containsKey(fp))
			if(archive.get(fp) != null)
				return archive.get(fp);
			throw new NoPatternException("Il pattern '"+fp.toString()+"' non ha generato regole confidenti");
	}
	
	public String toString ()
	{
		String value="";
		int i=0;
		Iterator it = archive.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry pair = (Map.Entry)it.next();
			FrequentPattern fp = (FrequentPattern) pair.getKey();
			value += i+":"+fp.toString()+"\n[";
			TreeSet<AssociationRule> ar =(TreeSet<AssociationRule>)pair.getValue();
			if (ar!=null)
			{
				Iterator itree = ar.iterator();
				while (itree.hasNext())
					value += ((AssociationRule)itree.next()).toString()+"\n";
			}
			value += "]\n";
			i++;
		}
		return value;
	}
	
	/**
	 * Salva il contenuto dell'archivio di Regole d'associazione in un file .dmp
	 * @param fileName nome del file riferito al dato archivio
	 * @throws IOException
	 */
    public void salva(String fileName) throws IOException 
    {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileName + ".dmp"));
        out.writeObject(this);
        out.close();
    }
    
    /**
     * Carica il contenuto di un file ad un archivio di Regole d'associazione
     * @param fileName nome del file riferito al dato archivio
     * @return regole d'associazione contenuti nel file
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static AssociationRuleArchieve carica (String fileName) throws IOException, ClassNotFoundException 
    {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName + ".dmp"));
        AssociationRuleArchieve temp = (AssociationRuleArchieve) in.readObject();
        in.close();
        return temp;
    }
    
    /**
     * verifica se l'archivio è vuoto o meno
     * @return true se vuoto, false altrimenti
     */
    public boolean isEmpty ()
    {
    	return archive.isEmpty();
    }
    
    /**
     * Partendo dal Frequent Pattern ('fp') e dalle regole di associazione relative ad essa ('ar'), si ricercano, all'interno dell'archivio in analisi:
     * 
     * 		- Altri Frequent Pattern quali contengono 'fp';
     * 		- Da questi Frequent Pattern, si ricavano le regole di associazione quali contengono 'ar'.
     * 
     * I pattern/regole di associazione quali li contengono, vengono salvati in un altro archivio (quale sarebbe nient'altro che un sotto-archivio), che viene restituito come risultato della funzione
     * @param fp Frequent Pattern da ricercare all'interno dell'archivio in analisi
     * @param ar regole di associazione relative a fp da ricercare all'interno dell'archivio in analisi
     * @return un archivio quali contiene le regole che contengono 'fp' e 'ar'.
     */
    public AssociationRuleArchieve getSubArchieve (FrequentPattern fp, AssociationRule ar) 
    {
    	AssociationRuleArchieve newArchieve = new AssociationRuleArchieve();
    	boolean changed;
    	//ricavo il set di Frequent Pattern dall'archivio in analisi. Questo perchè, in buona sostanza, fungono da "chiave"
    	Set<FrequentPattern> fpSet = this.archive.keySet();
    	Iterator it = fpSet.iterator();
    	FrequentPattern fpArchieve = new FrequentPattern();
    	while (it.hasNext())
    	{
    		fpArchieve = (FrequentPattern)it.next();
    		//verifico inanzitutto se fp è contenuto nel frequent pattern in analisi ( 'fpArchieve').
    		//qualora così non fosse, si passerebbe direttamente ad analizzare il successivo frequent pattern ricavato dall'archivio in analisi
    		//questo perchè, per definizione, un Frequent Pattern può anche non avere regole di associazione, ma non può essere l'inverso.
    		//pertanto, se non esiste un frequent pattern quale contenga 'fp', non ha senso ricercare le regole d'associazione .
    		if ((fpArchieve).isContained(fp))
    		{
    			//conterrà le regole d'associazione ricercate
    			TreeSet<AssociationRule> rulesContained = new TreeSet<AssociationRule>();
    			try
    			{
    				//verificato che fp è contenuto nel frequent pattern, possiamo adesso analizzare le, eventuali , regole d'associazione relative al frequent pattern analizzato
    				TreeSet<AssociationRule> rulesPattern = this.getRules(fpArchieve);
    				Iterator itPattern = rulesPattern.iterator();
    				while (itPattern.hasNext())
    				{
    					AssociationRule arPattern = (AssociationRule)itPattern.next();
    					//verifico che sia la parte antecedente che quella consequente di 'ar' siano contenute nell' 'arPattern'
    					//in caso lo siano, la regola d'associazione in analisi viene salvata
    						if ((arPattern.isContained(ar, 'L'))&&(arPattern.isContained(ar, 'R')))
    							rulesContained.add(arPattern);
    				}
    				//se effettivamente sono state trovate regole d'associazione contenute, provvede ad inserirli nel sotto-archivio assieme al frequent pattern
    				//qualora, invece, non siano state trovate (o se non ci sono affatto regole di associazione relative al frequentPattern in analisi), viene lanciata l'eccezione
    				//provvedendo comunque ad inserire SOLO il frequent pattern, in questo caso
    				if (!rulesContained.isEmpty())
    					newArchieve.put(fpArchieve, rulesContained);
    			}
    			catch (NoPatternException e)
    			{
    				newArchieve.put(fpArchieve, null);
    			}
    		}
    	}
    	return newArchieve;
    }
}
