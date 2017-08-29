
public class AssociationRule 
{
	private Item antecedent [] = new Item [0];
	private Item consequent [] = new Item [0];
	float support;
	private float confidence;
	
	/**
	 * Costruttore della classe 
	 * @param support valore di supporto
	 */
	AssociationRule (float support)
	{
		this.support = support;
	}
	
	/**
	 * @return valore di supporto
	 */
	float getSupport()
	{
		return this.support;
	}
	
	/**
	 * 
	 * @return valore di confidenza
	 */
	float getConfidence()
	{
		return this.confidence;
	}
	
	/**
	 * 
	 * @return numero di item nella parte "antecedente"
	 */
	int getAntecedentLenght ()
	{
		return antecedent.length;
	}
	
	/**
	 * 
	 * @return numero di item nella parte "consequente"
	 */
	int getConsequentLenght()
	{
		return this.consequent.length;
	}
	
	/**
	 * Aggiunge un item nella parte antecedente 
	 * @param item item da aggiungere 
	 */
	void addAntecedentItem (Item item)
	{
		Item temp [] = antecedent.clone();
		antecedent = new Item [temp.length+1];
		int i = -1;
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
	void addConsequentItem (Item item)
	{
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
	Item getConsequentItem (int index)
	{
		return consequent[index];
	}
	
	/**
	 * Ricava un item dalla parte antecedente
	 * @param index indice relativo alla posizione dell'item desiderato
	 * @return item in posizione index 
	 */
	Item getAntecedentItem (int index)
	{
		return antecedent[index];
	}
	
	void setConfidence (float confedence)
	{
		this.confidence = confedence;
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
