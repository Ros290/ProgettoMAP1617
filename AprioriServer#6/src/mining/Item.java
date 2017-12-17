package mining;
import java.io.Serializable;

import data.Attribute;


public abstract class Item implements Serializable
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
	
	
	/**
	 * Verifica che il valore passato corrisponda al valore assunto dall'item
	 * @param value valore da confrontare
	 * @return true se corrispondono, altrimenti false
	 */
	abstract boolean checkItemCondition(Object value);
	
	/**
	 * Verifica se l'item passato come argomento corrisponde, sia come tipo e sia come valore, a quello in analisi
	 * @param item item da confrontare
	 * @return true se entrambi appartengono alla stessa sottoclasse, false altrimenti
	 */
	abstract boolean compareTo (Item item);
	
	public String toString()
	{
		return "<" + this.attribute.toString() + ">=<" + this.value.toString() + ">"; 
	}

}
