package mining;
import java.util.Iterator;
import java.util.LinkedList;

import utility.EmptyQueueException;
import utility.LinkList;
import utility.Queue;
import data.Attribute;
import data.Data;
import data.DiscreteAttribute;
import data.EmptySetException;



public class FrequentPatternMiner 
{

	/**
	 * Ricava tutti i pattern frequenti all'interno della collezioni. 
	 * I pattern frequenti ricavati avranno supporto pari o maggiore al valore indicato nei parametri
	 * @param data collezione di dati da cui ricavare i pattern
	 * @param minSup valore "minimo" di supporto per i pattern frequenti ricavati
	 * @return Pattern frequenti
	 * @exception EmptySetException
	 */
	public static LinkedList<FrequentPattern> frequentPatternDiscovery(Data data,float minSup) throws EmptySetException
	{
		if (data.getNumberOfExamples() == 0)
			throw new EmptySetException("Errore, impossibile ricavare i pattern frequenti. La collezione di dati risulta vuota.\n");
		Queue<FrequentPattern> fpQueue=new Queue<FrequentPattern>();		
		LinkedList<FrequentPattern>outputFP=new LinkedList<FrequentPattern>();
		for(int i=0;i<data.getNumberOfAttributes();i++)
		{
			Attribute currentAttribute=data.getAttribute(i);
			for(int j=0;j<((DiscreteAttribute)currentAttribute).getNumberOfDistinctValues();j++)
			{
				DiscreteItem item=new DiscreteItem( 
						(DiscreteAttribute)currentAttribute, 
						((DiscreteAttribute)currentAttribute).getValue(j));
				FrequentPattern fp=new FrequentPattern();
				fp.addItem(item);
				fp.setSupport(FrequentPatternMiner.computeSupport(data, fp));
				if(fp.getSupport()>=minSup)
				{ // 1-FP CANDIDATE
					fpQueue.enqueue(fp);
					//System.out.println(fp);
					outputFP.add(fp);
				}
				
			}
			
		}
		outputFP.addAll(expandFrequentPatterns(data,minSup,fpQueue,outputFP));
		return outputFP;
	}
	
	/**
	 * è un'"estensione" della funzione frequentPatternDiscovery, con la sola differenza che opera su pattern 
	 * di lunghezza >1 . Per il resto, lo scopo della funzione è il medesimo di frequentPatternDiscovery
	 * @param data collezione di dati da cui ricavare i pattern
	 * @param minSup valore "minimo" di supporto per i pattern frequenti ricavati
	 * @param fpQueue coda dei pattern frequenti di lunghezza 1
	 * @param outputFP Lista dei pattern frequenti
	 * @return Lista completa dei pattern frequenti ricavati
	 * @throws EmptySetException 
	 */
	private static   LinkedList<FrequentPattern> expandFrequentPatterns(Data data, float minSup, 	Queue<FrequentPattern> fpQueue,LinkedList<FrequentPattern> outputFP) throws EmptySetException
	{			
		try
		{
		while ((!fpQueue.isEmpty())&&(((FrequentPattern) fpQueue.first()).getPatternLength()<=3))
		{

			FrequentPattern fp = (FrequentPattern) fpQueue.first();
			
			fpQueue.dequeue();
			int i,j;
			int k = fp.getPatternLength();
			for (i=0; i<data.getNumberOfAttributes(); i++)
			{
				Iterator<Item> it = fp.iterator();
				j=0;
				while (it.hasNext())
				{
					
					DiscreteAttribute da_temp = ((DiscreteAttribute)data.getAttribute(i));
					//DiscreteAttribute da_patt = (DiscreteAttribute)((DiscreteItem)fp.getNext(j)).getAttribute();
					DiscreteAttribute da_patt = (DiscreteAttribute)((DiscreteItem)it.next()).getAttribute();
					if (da_temp.equals(da_patt))
						break;
					j++;
				}
				if (j==fp.getPatternLength())
				{
					DiscreteAttribute da = ((DiscreteAttribute)data.getAttribute(i));
					for (j=0; j<da.getNumberOfDistinctValues(); j++)
					{
						FrequentPattern newfp = refineFrequentPattern(fp,new DiscreteItem(da,da.getValue(j)));
						newfp.setSupport(FrequentPatternMiner.computeSupport(data, newfp));
						if(newfp.getSupport()>=minSup)
						{ // 1-FP CANDIDATE
							fpQueue.enqueue(newfp);
							//System.out.println(fp);
							outputFP.add(newfp);
						}
					}
				}
			}
			
		}
		}
		catch(EmptyQueueException e)
		{
			System.out.println(e.getMessage());
		}
		return outputFP;
	}
	
	/**
	 * Tramite la collezione di dati, ricava il valore di supporto relativo al pattern
	 * @param data collezione di dati
	 * @param FP pattern 
	 * @return il valore di supporto relativo al pattern
	 */
	private static float computeSupport(Data data,FrequentPattern FP)
	{
		int suppCount=0;
		// indice esempio
		for(int i=0;i<data.getNumberOfExamples();i++)
		{
			//indice item
			boolean isSupporting=true;
			Iterator<Item> it = FP.iterator();
			while (it.hasNext())
			{
				//DiscreteItem
				DiscreteItem item = (DiscreteItem)it.next();
				DiscreteAttribute attribute=(DiscreteAttribute)item.getAttribute();
				//Extract the example value
				Object valueInExample=data.getAttributeValue(i, attribute.getIndex());
				if(!item.checkItemCondition(valueInExample)){
					isSupporting=false;
					break; //the ith example does not satisfy fp
				}
				
			}
			if(isSupporting)
				suppCount++;
		}
		return ((float)suppCount)/(data.getNumberOfExamples());
		
	}
	
	/**
	 * Mantenendo invariando lo stato del pattern passato come argomento, la funzione restitutisce un nuovo
	 * pattern, contenente i valori del pattern in argomento, in aggiunta all'item passato come argomento
	 * @param FP pattern da cui verrano copiati i suoi item al nuovo pattern
	 * @param item item da aggiungere al nuovo pattern
	 * @return pattern con gli item di FP più l'item nel parametro
	 */
	private static FrequentPattern refineFrequentPattern(FrequentPattern FP, Item item)
	{
		FrequentPattern newfp = new FrequentPattern();
		Iterator<Item> it = FP.iterator();
		while (it.hasNext())
			newfp.addItem(it.next());
		newfp.addItem(item);
		return newfp;
	}

}
	


