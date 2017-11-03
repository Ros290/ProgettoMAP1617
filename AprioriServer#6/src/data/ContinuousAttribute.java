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
	 * @param max
	 * @param min
	 */
	public ContinuousAttribute (String name, int index,  float min, float max)
	{
		super (name,index);
		this.max = max;
		this.min = min;
	}
	
	float getMin ()
	{
		return this.min;
	}
	
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
