package data;


public class DiscreteAttribute extends Attribute
{
	String values [];
	
	/**
	 * Costruttore 
	 * @param name nome dell'attributo
	 * @param index valore dell'indice riferito all'attriuto
	 * @param values valori che può assumere l'attributo
	 */
	public DiscreteAttribute (String name, int index, String values[])
	{
		super (name, index);
		this.values = values;
	}
	
	/**
	 * 
	 * @return numero di valori che può assumere l'attributo
	 */
	public int getNumberOfDistinctValues ()
	{
		return this.values.length;
	}
	
	/**
	 * 
	 * @param index posizione relativo al valore assumibile dall'attributo
	 * @return valore in posizione index
	 */
	public String getValue (int index)
	{
		return this.values[index];
	}
}
