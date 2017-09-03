package mining;
import java.util.Iterator;
import java.util.LinkedList;

import utility.LinkList;
import data.Data;
import data.DiscreteAttribute;



 public class AssociationRuleMiner {
		
	/** In base ad un pattern frequente, analizzando una collezione di dati, genera le regole d'associazione
	 * ad esso associate, confrontandole con il valore confidente desiderato
	 * @param data collezione di dati su cui operare
	 * @param fp pattern frequente da cui generare le regole d'associazione
	 * @param minConf valore confidente, per definire quali regole d'associazione possano essere valide
	 * @return lista di regole d'associazione relative al pattern passato come argomento
	 * @throws OneLevelPatternException 
	 * @throws NoPatternException 
	 */
	public static LinkedList<AssociationRule> confidentAssociationRuleDiscovery(Data data,FrequentPattern fp,float minConf) throws OneLevelPatternException, NoPatternException	
	{
		if (fp.getPatternLength()==1)
			throw new OneLevelPatternException("Il pattern '"+fp.toString()+"' ha lunghezza 1\n");
		LinkedList<AssociationRule> outputAR = new LinkedList<AssociationRule>();
		for (int i=1; i < fp.getPatternLength(); i++)
		{
			AssociationRule AR = confidentAssociationRuleDiscovery(data,fp,minConf,i);
			if (AR.getConfidence() >= minConf)
				outputAR.add(AR);
		}
		if (outputAR.isEmpty())
			throw new NoPatternException("Il pattern '"+fp.toString()+"' non ha generato regole confidenti");
		return outputAR;
	}

	/**
	 * A partire dal pattern frequente, genera una regola d'associazione, suddividendo i valori "antecedenti" e
	 * quelli "consequenti" in base al valore di iCut, ricavando la confidenza della suddetta.
	 * @param data collezione di dati su cui operare
	 * @param fp pattern frequente da cui generare le regole d'associazione
	 * @param minConf valore confidente, per definire quali regole d'associazione possano essere valide
	 * @param iCut indica a quale punto del pattern termina la parte "antecedente" e comincia quella "consequente
	 * @return regola d'associazione generata
	 * @throws OneLevelPatternException 
	 */
	private static AssociationRule confidentAssociationRuleDiscovery(Data data,FrequentPattern fp,float minConf, int iCut)
	{
		
		AssociationRule AR=new AssociationRule(fp.getSupport());
	
		//to generate the antecedent of the association rule
		Iterator<Item> it= fp.iterator();
		for(int j=0;j<iCut;j++)
			AR.addAntecedentItem(it.next());		
		//to generate the consequent of the association rule
		while(it.hasNext())
			AR.addConsequentItem(it.next());
		AR.setConfidence(AssociationRuleMiner.computeConfidence(data,AR));
		return AR;
}

/**
 * Valuta la confidenza della regola d'associazione in base alla collezione di dati
 * @param data collezione di dati su cui operare
 * @param AR regola d'associazione da cui generare la relativa confidenza
 * @return confidenza della regola d'associazione passata come parametro 
 */
private static float  computeConfidence(Data data, AssociationRule AR)
{
	int antCount = 0;
	int assCount = 0;
	// indice esempio
	for(int i=0;i<data.getNumberOfExamples();i++)
	{
		//indice item
		boolean isSupporting=true;
		for(int j=0;j<AR.getAntecedentLenght();j++)
		{
			//DiscreteItem
			DiscreteItem item=(DiscreteItem)AR.getAntecedentItem(j);
			DiscreteAttribute attribute=(DiscreteAttribute)item.getAttribute();
			//Extract the example value
			Object valueInExample=data.getAttributeValue(i, attribute.getIndex());
			if(!item.checkItemCondition(valueInExample))
			{
				isSupporting=false;
				break; //the ith example does not satisfy fp
			}
			
		}
		if(isSupporting)
			antCount++;
		assCount = antCount;
		for(int j=0;j<AR.getConsequentLenght();j++)
		{
			//DiscreteItem
			DiscreteItem item=(DiscreteItem)AR.getConsequentItem(j);
			DiscreteAttribute attribute=(DiscreteAttribute)item.getAttribute();
			//Extract the example value
			Object valueInExample=data.getAttributeValue(i, attribute.getIndex());
			if(!item.checkItemCondition(valueInExample))
			{
				isSupporting=false;
				break; //the ith example does not satisfy fp
			}
			
		}
		if(isSupporting)
			assCount++;
	}
	return ((float)assCount)/(antCount);	
}

}