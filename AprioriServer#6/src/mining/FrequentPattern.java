package mining;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


public class FrequentPattern implements Comparable, Serializable
{

	//private Item fp[];
	private List <Item> fp ;
	private float support;
	
	/**
	 * Costruttore
	 */
	FrequentPattern()
	{
		fp = new LinkedList<Item>();
	}

	/**
	 * Aggiunge un nuovo item al pattern
	 * @param item item da aggiungere
	 */
	void addItem(Item item)
	{
		fp.add(item);
	}
	
	/**
	 * 
	 * @return ritorna il numero di item compresi nel pattern
	 */
	int getPatternLength ()
	{
		return fp.size();
	}
	
	public Iterator<Item> iterator()
	{
		return fp.iterator();
	}
	/**
	 * 
	 * @param index posizione dell'item all'interno del pattern
	 * @return l'item in posizione index
	 */
	/*
	Item getNext (int index)
	{
		return (Item)it.next();
		//return fp[index];
	}
	*/
	
	/**
	 * 
	 * @return ritorna il valore di supporto relativo al pattern
	 */
	float getSupport ()
	{
		return support;
	}
	
	/**
	 * 
	 * @param support valore di support da assegnare al pattern
	 */
	void setSupport (float support)
	{
		this.support = support;
	}
	
	public String toString()
	{
		Iterator<Item> it = fp.iterator();
		String value="";
		for(int i=0;i<fp.size()-1;i++)
			value+=it.next().toString() +" AND ";
		if(fp.size()>0)
		{
			value+=it.next().toString();
			value+="["+support+"]";
		}
		
		return value;
	}

	public int compareTo(Object fp) 
	{
		Iterator it = this.fp.iterator();
		Iterator itfp = ((FrequentPattern)fp).iterator();
		int flag = -1;
		while ((it.hasNext())&&(itfp.hasNext()))
		{
			if (!it.next().equals(itfp.next()))
			{
				flag=1;
				break;
			}
		}
		return flag;
	}


}
