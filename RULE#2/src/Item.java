
public abstract class Item 
{
	private Attribute attribute;
	private Object value;
	
	Item (Attribute attribute, Object value)
	{
		this.attribute = attribute;
		this.value = value;
	}
	
	Attribute getAttribute ()
	{
		return this.attribute;
	}
	
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
