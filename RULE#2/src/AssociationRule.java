
public class AssociationRule 
{
	private Item antecedent [] = new Item [0];
	private Item consequent [] = new Item [0];
	float support;
	private float confidence;
	
	AssociationRule (float support)
	{
		this.support = support;
	}
	
	float getSupport()
	{
		return this.support;
	}
	
	float getConfidence()
	{
		return this.confidence;
	}
	
	int getAntecedentLenght ()
	{
		return antecedent.length;
	}
	
	int getConsequentLenght()
	{
		return this.consequent.length;
	}
	
	void addAntecedentItem (Item item)
	{
		Item temp [] = antecedent.clone();
		antecedent= new Item [temp.length+1];
		int i = -1;
		for (Item it : temp)
		{
			antecedent[i++] = it;
		}
		antecedent[i++] = item;
	}
	
	
	void addConsequentItem (Item item)
	{
		Item temp [] = consequent.clone();
		consequent= new Item [temp.length+1];
		int i = -1;
		for (Item it : temp)
			consequent[i++] = it;
		consequent[i++] = item;
	}
	
	Item getConsequentItem (int index)
	{
		return consequent[index];
	}
	
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
