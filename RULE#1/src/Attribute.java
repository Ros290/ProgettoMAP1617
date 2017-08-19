
abstract class Attribute 
{
	String name;
	int index;
	
	Attribute (String name, int index)
	{
		this.name = name;
		this.index = index;
	}
	
	String getName()
	{
		return this.name;
	}
	
	int getIndex()
	{
		return this.index;
	}
	
	public String toString()
	{
		return this.name;
	}
}
