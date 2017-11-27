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
	
	public AssociationRuleArchieve()
	{
		archive = new LinkedHashMap<FrequentPattern, TreeSet<AssociationRule>>();
	}
	
	public void put(FrequentPattern fp)
	{
		archive.put(fp, null);
	}
	
	public void put(FrequentPattern fp, TreeSet<AssociationRule> rule)
	{

		archive.put(fp,rule);
	}
	
	public TreeSet<AssociationRule> getRules (FrequentPattern fp) throws NoPatternException
	{
		//if ((archive.containsKey(fp))&&(!(archive.get(fp)).isEmpty()))
		if(archive.containsKey(fp))
			if(archive.get(fp) != null)
				return archive.get(fp);
		//else
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
    
    public boolean isEmpty ()
    {
    	return archive.isEmpty();
    }
    
    public AssociationRuleArchieve getSubArchieve (FrequentPattern fp, AssociationRule ar) 
    {
    	AssociationRuleArchieve newArchieve = new AssociationRuleArchieve();
    	boolean changed;
    	Set<FrequentPattern> fpSet = this.archive.keySet();
    	Iterator it = fpSet.iterator();
    	FrequentPattern fpArchieve = new FrequentPattern();
    	while (it.hasNext())
    	{
    		fpArchieve = (FrequentPattern)it.next(); 
    		if ((fpArchieve).isContained(fp))
    		{
    			TreeSet<AssociationRule> rulesContained = new TreeSet<AssociationRule>();
    			try
    			{
    				TreeSet<AssociationRule> rulesPattern = this.getRules(fpArchieve);
    				//rulesContained = rulesPattern;
    			
    				Iterator itPattern = rulesPattern.iterator();
    				while (itPattern.hasNext())
    				{
    					AssociationRule arPattern = (AssociationRule)itPattern.next();
    						if ((arPattern.isContained(ar, 'L'))&&(arPattern.isContained(ar, 'R')))
    							rulesContained.add(arPattern);
    				}
    			}
    			catch (NoPatternException e)
    			{
    			}
    			finally
    			{
    				if (rulesContained.isEmpty())
    					rulesContained = null;
    				newArchieve.put(fpArchieve, rulesContained);
    			}
    		}
    	}
    	return newArchieve;
    }
}
