package mining;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class AssociationRuleArchieve 
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
		if (archive.containsKey(fp))
			return archive.get(fp);
		else
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
}
