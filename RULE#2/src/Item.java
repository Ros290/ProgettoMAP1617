
public abstract class Item 
{
	private Attribute attribute;
	private Object value;
	
	/**
	 * Costruttore
	 * @param attribute attributo relativo al valore assunto dall'item
	 * @param value valore assunto dall'item
	 */
	Item (Attribute attribute, Object value)
	{
		this.attribute = attribute;
		this.value = value;
	}
	
	/**
	 * ritorna l'attributo relativo all'item
	 * @return
	 */
	Attribute getAttribute ()
	{
		return this.attribute;
	}
	
	/**
	 * Ritorna il valore relativo all'item
	 * @return
	 */
	Object getValue ()
	{
		return this.value;
	}
	
	abstract boolean checkItemCondition(Object value);
	
	public String toString()
	{
		return "<" + this.attribute.toString() + ">=<" + this.value.toString() + ">"; 
	}

}
