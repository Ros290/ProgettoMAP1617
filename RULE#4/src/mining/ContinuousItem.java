package mining;

import data.Attribute;
import data.ContinuousAttribute;

public class ContinuousItem extends Item
{

	ContinuousItem(ContinuousAttribute attribute, Interval value) 
	{
		super(attribute, value);
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
