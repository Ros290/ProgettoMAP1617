package data;

public abstract class Attribute 
{
	String name;
	int index;
	
	/**
	 * Costruttore 
	 * @param name nome attributo
	 * @param index indice che si vuole assegnare all'attributo
	 */
	Attribute (String name, int index)
	{
		this.name = name;
		this.index = index;
	}
	
	/**
	 * 
	 * @return nome dell'attributo
	 */
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * 
	 * @return valore dell'indice relativo all'attributo
	 */
	public int getIndex()
	{
		return this.index;
	}
	
	public String toString()
	{
		return this.name;
	}
}
