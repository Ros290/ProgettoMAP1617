
public class FrequentPattern 
{

	private Item fp[];
	private float support;
	
	/**
	 * Costruttore
	 */
	FrequentPattern()
	{
		fp=new Item[0];
	}

	/**
	 * Aggiunge un nuovo item al pattern
	 * @param item item da aggiungere
	 */
	void addItem(Item item)
	{
		int length =fp.length;
		
		Item temp []=new Item[length+1];
		System.arraycopy(fp, 0, temp, 0, length);
		temp [length]=item;
		fp=temp;
	}
	
	/**
	 * 
	 * @return ritorna il numero di item compresi nel pattern
	 */
	int getPatternLength ()
	{
		return fp.length;
	}
	
	/**
	 * 
	 * @param index posizione dell'item all'interno del pattern
	 * @return l'item in posizione index
	 */
	Item getItem (int index)
	{
		return fp[index];
	}
	
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
		String value="";
		for(int i=0;i<fp.length-1;i++)
			value+=fp[i] +" AND ";
		if(fp.length>0){
			value+=fp[fp.length-1];
			value+="["+support+"]";
		}
		
		return value;
	}


}
