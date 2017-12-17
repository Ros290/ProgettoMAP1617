package mining;

import data.ContinuousAttribute;

public class ContinuousItem extends Item
{

	/**
	 * Costruttore 
	 * @param attribute attributo da associare 
	 * @param value intervallo all'interno di cui dev'essere compreso il valore assunto dall'item
	 */
	public ContinuousItem(ContinuousAttribute attribute, Interval value) 
	{
		super(attribute, value);
	}

	boolean compareTo (Item item)
	{
		if (item instanceof ContinuousItem)
		{
			ContinuousItem ci = (ContinuousItem) item;
			Interval ciInterval =(Interval) ci.getValue();
			Interval thisInterval = (Interval) this.getValue();
			if ((ciInterval.getInf() == thisInterval.getInf())&&(ciInterval.getSup() == thisInterval.getSup()))
				return true;
		}
		return false;
	}
	
	boolean checkItemCondition(Object value) 
	{
		return ((Interval)this.getValue()).checkValueInclusion((float)value);
	}
	
	public String toString()
	{
		Interval interval = ((Interval)this.getValue());
		return this.getAttribute().getName() + "[" + interval.getInf() + "," + interval.getSup() + "]";
	}
}
