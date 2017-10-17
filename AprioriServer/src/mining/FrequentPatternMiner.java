package mining;
import java.util.Iterator;
import java.util.LinkedList;

import utility.EmptyQueueException;
import utility.LinkList;
import utility.Queue;
import data.Attribute;
import data.ContinuousAttribute;
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
			if (currentAttribute instanceof DiscreteAttribute)
			//ATTRIBUTO DISCRETO
			{
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
			//ATTRIBUTO CONTINUO
			else
			{
				Iterator<Float> it =  ((ContinuousAttribute)currentAttribute).iterator();
				if (it.hasNext())
				{
					float inf = it.next();
					while (it.hasNext())
					{
						float sup = it.next();
						ContinuousItem item;
						if (it.hasNext())
							item = new ContinuousItem ((ContinuousAttribute)currentAttribute, new Interval (inf,sup));
						else
							item = new ContinuousItem ((ContinuousAttribute)currentAttribute, new Interval (inf,sup+0.01f*(sup-inf)));
						inf = sup;
						FrequentPattern fp = new FrequentPattern ();
						fp.addItem(item);
						fp.setSupport(FrequentPatternMiner.computeSupport(data, fp));
						if(fp.getSupport() >= minSup)
						{
							fpQueue.enqueue(fp);
							outputFP.add(fp);
						}
					}
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
			/*
			 * Dato un pattern Frequente, l'intenzione è quello di "estenderlo", associandoci una nuovo valore.
			 * Ovviamente tale valore, dovrà appartenere ad un attributo DIFFERENTE da quelli già presenti nel suddetto Pattern.
			 * Esempio:
			 * 
			 * Supponendo di avere come attributi i campi "A", "B" e "C", se fp è composto da un valore di attributo "A", 
			 * ed un altro di attributo "B", non si può "estendere" fp aggiungendoci un altro valore di attributo "A" o "B".
			 * Difatti, in questo caso, l'unico modo per estenderlo è quello di associarci un altro valore di attributo "C".
			 * 
			 * Si inizia analizzando un dato attributo, e si cicla finchè non siano stati analizzatti tutti gli attributi
			 */
			for (i=0; i<data.getNumberOfAttributes(); i++)
			{
				Iterator<Item> it_patt = fp.iterator();
				j=0;
				Attribute temp = ((Attribute)data.getAttribute(i));
				/*
				 * ricavato l'attributo da analizzare, contenuto in "da_temp" (attributo in posizione i-esima nella lista degli attributi)
				 * , adesso si verifica che tale attributo
				 * non sia già presente all'interno del pattern frequente. 
				 */
				while (it_patt.hasNext())
				{
					Attribute patt = (Attribute)((Item)it_patt.next()).getAttribute();
					/*
					 * Ricavo da ciascun valore presente nel pattern, l'attributo ad esso associato. Dopo di che
					 * si confronta con l'attributo contenuto in "da_temp". Qualora nessun attributo presente nel pattern
					 * corrisponda a "da_temp", allora si può procedere con l'estensione. Qualora, invece, vi fosse presente
					 * un attributo uguale a "da_temp", in tal caso si procede con l'attributo successivo. Qualora siano già
					 * stati controllati tutti gli altri attributi, si ricava un nuovo pattern dalla testa della coda
					 */
					if (temp.equals(patt))
						break;
					j++;
				}
				if (j==fp.getPatternLength())
				{
					Attribute attribute = (Attribute)data.getAttribute(i);
					//temp Attributo Discreto
					if ( attribute instanceof DiscreteAttribute)
					{
						/*
						 * Ottenuto l'attributo da usare per l'estensione, si procede adesso ad analizzare uno ad uno i
						 * suoi valori, estendendoli al pattern e verificando se siano validi o meno, in base al valore
						 * di supporto ricavato dal nuovo pattern. 
						 */
						for (j=0; j<((DiscreteAttribute)attribute).getNumberOfDistinctValues(); j++)
						{
							FrequentPattern newfp = refineFrequentPattern(fp,new DiscreteItem(((DiscreteAttribute)attribute),((DiscreteAttribute)attribute).getValue(j)));
							newfp.setSupport(FrequentPatternMiner.computeSupport(data, newfp));
							if(newfp.getSupport()>=minSup)
							{
								fpQueue.enqueue(newfp);
								outputFP.add(newfp);
							}
						}
					}
					//temp Attributo Continuo
					else
					{
						Iterator<Float> it =  ((ContinuousAttribute)attribute).iterator();
						if (it.hasNext())
						{
							float inf = it.next();
							while (it.hasNext())
							{
								float sup = it.next();
								ContinuousItem item;
								if (it.hasNext())
									item = new ContinuousItem ((ContinuousAttribute)attribute, new Interval (inf,sup));
								else
									item = new ContinuousItem ((ContinuousAttribute)attribute, new Interval (inf,sup+0.01f*(sup-inf)));
								inf = sup;
								FrequentPattern newfp = refineFrequentPattern(fp,item);
								newfp.setSupport(FrequentPatternMiner.computeSupport(data, newfp));
								if(newfp.getSupport()>=minSup)
								{
									fpQueue.enqueue(newfp);
									outputFP.add(newfp);
								}
							}
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
				Item item = (Item)it.next();
				Attribute attribute=(Attribute)item.getAttribute();
				Object valueInExample=data.getAttributeValue(i, attribute.getIndex());
				if(!item.checkItemCondition(valueInExample))
				{
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
	


