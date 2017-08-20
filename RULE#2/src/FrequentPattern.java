
public class FrequentPattern 
{

	private Item fp[];
	private float support;
	
	
	FrequentPattern()
	{
		fp=new Item[0];
	}
	//aggiunge un nuovo item al pattern
	void addItem(Item item)
	{
		int length =fp.length;
		
		Item temp []=new Item[length+1];
		System.arraycopy(fp, 0, temp, 0, length);
		temp [length]=item;
		fp=temp;
	}
	
	int getPatternLength ()
	{
		return fp.length;
	}
	
	Item getItem (int index)
	{
		return fp[index];
	}
	
	float getSupport ()
	{
		return support;
	}
	
	void setSupport (float support)
	{
		this.support = support;
	}
	
	public String toString()
	{
		String value="";
		for(int i=0;i<fp.length-1;i++)
			value+=fp[i] +" AND ";
		if(fp.length>0){
			value+=fp[fp.length-1];
			value+="["+support+"]";
		}
		
		return value;
	}


}
