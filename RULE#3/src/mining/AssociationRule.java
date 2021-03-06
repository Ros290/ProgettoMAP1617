package mining;


public class AssociationRule implements Comparable<AssociationRule>
{
	private Item antecedent [] = new Item [0];
	private Item consequent [] = new Item [0];
	float support;
	private float confidence;
	
	/**
	 * Costruttore della classe 
	 * @param support valore di supporto
	 */
	public AssociationRule (float support)
	{
		this.support = support;
	}
	
	/**
	 * @return valore di supporto
	 */
	public float getSupport()
	{
		return this.support;
	}
	
	/**
	 * 
	 * @return valore di confidenza
	 */
	public float getConfidence()
	{
		return this.confidence;
	}
	
	/**
	 * 
	 * @return numero di item nella parte "antecedente"
	 */
	public int getAntecedentLenght ()
	{
		return antecedent.length;
	}
	
	/**
	 * 
	 * @return numero di item nella parte "consequente"
	 */
	public int getConsequentLenght()
	{
		return this.consequent.length;
	}
	
	/**
	 * Aggiunge un item nella parte antecedente 
	 * @param item item da aggiungere 
	 */
	public void addAntecedentItem (Item item)
	{
		//copio gli antecedenti in una variabile temporanea, 
		Item temp [] = antecedent.clone();
		//modifico la dimensione dell'antecedente, estendendolo alla propria dimensione + 1 (dato che dovr� essere inserito item)
		antecedent = new Item [temp.length+1];
		int i = -1;
		//infine, inserisco i valori contenuti nella variabile temporanea nell'antecedente. Dopo di che, in ultima posizione inserisco item
		for (Item it : temp)
		{
			i++;
			antecedent[i] = it;
		}
		int l = antecedent.length;
		i++;
		antecedent[i] = item;
	}
	
	/**
	 * Aggiunge un item nella parte consequente
	 * @param item item da aggiungere
	 */
	public void addConsequentItem (Item item)
	{
		//stessa procedura di "addAntecedentItem"
		Item temp [] = consequent.clone();
		consequent= new Item [temp.length+1];
		int i = -1;
		for (Item it : temp)
		{
			i++;
			consequent[i] = it;
		}
		i++;
		consequent[i] = item;
	}
	
	/**
	 * Ricava un item dalla parte consequente
	 * @param index indice relativo alla posizione dell'item desiderato
	 * @return item in posizione index 
	 */
	public Item getConsequentItem (int index)
	{
		return consequent[index];
	}
	
	/**
	 * Ricava un item dalla parte antecedente
	 * @param index indice relativo alla posizione dell'item desiderato
	 * @return item in posizione index 
	 */
	public Item getAntecedentItem (int index)
	{
		return antecedent[index];
	}
	
	public void setConfidence (float confedence)
	{
		this.confidence = confedence;
	}
	
	public int compareTo(AssociationRule AR)
	{
		return (this.confidence != AR.getConfidence()) == true ? 1 : -1;
	}
	
	public String toString()
	{
		String value = "";
		for(int i=0 ; i<antecedent.length-1 ; i++)
			value += antecedent[i].toString() + " AND ";
		if(antecedent.length>0)
		{
			value += antecedent[antecedent.length-1];
			if (consequent.length>0)
			{
				value += " ==> ";
				for (int i=0 ; i<consequent.length-1; i++)
					value += consequent[i].toString() + " AND ";
				value += consequent[consequent.length-1];
			}
			value += "(" + support + "," + confidence + ")"; 
		}
		return value;
	}
	
}
