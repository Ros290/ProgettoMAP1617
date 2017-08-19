
class ContinuousAttribute extends Attribute
{
	float max;
	float min;
	
	ContinuousAttribute (String name, int index, float max, float min)
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
}
