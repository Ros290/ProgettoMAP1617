package data;

import java.util.Iterator;


public class ContinuousAttribute extends Attribute implements Iterable<Float>
{
	float max;
	float min;
	
	/**
	 * Costruttore 
	 * @param name nome attributo
	 * @param index indice relativo all'attributo
	 * @param max valore massimo dell'intervallo
	 * @param min valore minimo dell'intervallo
	 */
	public ContinuousAttribute (String name, int index,  float min, float max)
	{
		super (name,index);
		this.max = max;
		this.min = min;
	}
	
	/**
	 * ritorna il valore minimo impostato nell'intervallo
	 * @return valore minimo dell'intervallo
	 */
	float getMin ()
	{
		return this.min;
	}
	
	/**
	 * ritorna il valore massimo impostato nell'intervallo
	 * @return valore massimo dell'intervallo
	 * 
	 */
	float getMax ()
	{
		return this.max;
	}
	

	public Iterator<Float> iterator ()
	{
		ContinuousAttributeIterator cutPoints = new ContinuousAttributeIterator (min, max, 5);
		return cutPoints;
	}
}
