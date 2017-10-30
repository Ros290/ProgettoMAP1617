package mining;

import java.io.Serializable;

public class Interval implements Serializable
{
	private float inf;
	private float sup;
	
	Interval (float inf, float sup)
	{
		this.inf = inf;
		this.sup = sup;
	}
	
	void setInf (float inf)
	{
		this.inf = inf;
	}
	
	void setSup (float sup)
	{
		this.sup = sup;
	}
	
	float getInf ()
	{
		return inf;
	}
	
	float getSup ()
	{
		return sup;
	}
	
	boolean checkValueInclusion (float value)
	{
		if ((value<sup)&&(value>=inf))
			return true;
		else
			return false;
		
	}
	
	public String toString ()
	{
		return "[" + inf + "," + sup + "[";
	}
	
}
