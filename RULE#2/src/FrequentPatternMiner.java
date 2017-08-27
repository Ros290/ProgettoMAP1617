

public class FrequentPatternMiner 
{

	static LinkList frequentPatternDiscovery(Data data,float minSup)
	{
		Queue fpQueue=new Queue();		
		LinkList outputFP=new LinkList();
		for(int i=0;i<data.getNumberOfAttributes();i++)
		{
			Attribute currentAttribute=data.getAttribute(i);
			for(int j=0;j<((DiscreteAttribute)currentAttribute).getNumberOfDistinctValues();j++)
			{
				DiscreteItem item=new DiscreteItem( 
						(DiscreteAttribute)currentAttribute, 
						((DiscreteAttribute)currentAttribute).getValue(j));
				FrequentPattern fp=new FrequentPattern();
				fp.addItem(item);
				fp.setSupport(FrequentPatternMiner.computeSupport(data, fp));
				if(fp.getSupport()>=minSup)
				{ // 1-FP CANDIDATE
					fpQueue.enqueue(fp);
					//System.out.println(fp);
					outputFP.add(fp);
				}
				
			}
			
		}
		outputFP=expandFrequentPatterns(data,minSup,fpQueue,outputFP);
		return outputFP;
	}
	
	private static   LinkList expandFrequentPatterns(Data data, float minSup, 	Queue fpQueue,LinkList outputFP)
	{
		while ((!fpQueue.isEmpty())&&(((FrequentPattern) fpQueue.first()).getPatternLength()<=3))
		{
			FrequentPattern fp = (FrequentPattern) fpQueue.first();
			fpQueue.dequeue();
			int i,j;
			int k = fp.getPatternLength();
			for (i=0; i<data.getNumberOfAttributes(); i++)
			{
				for (j=0; j<k; j++)
				{
					DiscreteAttribute da_temp = ((DiscreteAttribute)data.getAttribute(i));
					DiscreteAttribute da_patt = (DiscreteAttribute)((DiscreteItem)fp.getItem(j)).getAttribute();
					if (da_temp.equals(da_patt))
						break;
				}
				if (j==k)
				{
					DiscreteAttribute da = ((DiscreteAttribute)data.getAttribute(i));
					for (j=0; j<da.getNumberOfDistinctValues(); j++)
					{
						FrequentPattern newfp = refineFrequentPattern(fp,new DiscreteItem(da,da.getValue(j)));
						newfp.setSupport(FrequentPatternMiner.computeSupport(data, newfp));
						if(newfp.getSupport()>=minSup)
						{ // 1-FP CANDIDATE
							fpQueue.enqueue(newfp);
							//System.out.println(fp);
							outputFP.add(newfp);
						}
					}
				}
			}
		}
		return outputFP;
	}
	
	// Aggiorna il supporto
	static float computeSupport(Data data,FrequentPattern FP)
	{
		int suppCount=0;
		// indice esempio
		for(int i=0;i<data.getNumberOfExamples();i++)
		{
			//indice item
			boolean isSupporting=true;
			for(int j=0;j<FP.getPatternLength();j++)
			{
				//DiscreteItem
				DiscreteItem item=(DiscreteItem)FP.getItem(j);
				DiscreteAttribute attribute=(DiscreteAttribute)item.getAttribute();
				//Extract the example value
				Object valueInExample=data.getAttributeValue(i, attribute.getIndex());
				if(!item.checkItemCondition(valueInExample)){
					isSupporting=false;
					break; //the ith example does not satisfy fp
				}
				
			}
			if(isSupporting)
				suppCount++;
		}
		return ((float)suppCount)/(data.getNumberOfExamples());
		
	}
	
	static FrequentPattern refineFrequentPattern(FrequentPattern FP, Item item)
	{
		FrequentPattern newfp = new FrequentPattern();
		for (int i=0; i<FP.getPatternLength(); i++)
			newfp.addItem(FP.getItem(i));
		newfp.addItem(item);
		return newfp;
	}

}
	


