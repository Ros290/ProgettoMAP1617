package mining;
import data.DiscreteAttribute;


public class DiscreteItem extends Item
{
	
	/**
	 * Costruttore 
	 * @param attribute attributo relativo all'item
	 * @param value valore assunto dall'item
	 */
	public DiscreteItem (DiscreteAttribute attribute, String value)
	{
		super (attribute,value);
	}

	boolean checkItemCondition (Object value)
	{
		String value1 = ((String)this.getValue()).toLowerCase();
		String value2 = ((String)value).toLowerCase();
		if (value1.compareTo(value2) == 0 )
			return true;
		else return false;
	}
	

	boolean compareTo (Item item)
	{
		if (item instanceof DiscreteItem)
		{
			if (this.getValue().equals(item.getValue()))
				return true;
		}
		return false;
	}
	
}
