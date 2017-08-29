
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
		return this.getValue().equals(value);
	}
	
}
