package mining;
import data.DiscreteAttribute;


public class DiscreteItem extends Item
{
	
	/**
	 * Costruttore 
	 * @param attribute attributo relativo all'item
	 * @param value valore assunto dall'item
	 */
	DiscreteItem (DiscreteAttribute attribute, String value)
	{
		super (attribute,value);
	}
	
	/**
	 * Verifica che il valore passato corrisponda al valore assunto dall'item
	 */
	boolean checkItemCondition (Object value)
	{
		String value1 = ((String)this.getValue()).toLowerCase();
		String value2 = ((String)value).toLowerCase();
		if (value1.compareTo(value2) == 0 )
			return true;
		else return false;
	}
	
}
