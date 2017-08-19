
class DiscreteAttribute extends Attribute
{
	String values [];
	
	DiscreteAttribute (String name, int index, String values[])
	{
		super (name, index);
		this.values = values;
	}
	
	int getNumberOfDistinctValues ()
	{
		return this.values.length;
	}
	
	String getValue (int index)
	{
		return this.values[index];
	}
}
