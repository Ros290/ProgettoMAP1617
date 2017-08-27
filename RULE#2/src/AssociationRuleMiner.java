

 public class AssociationRuleMiner {
		
	
	public static LinkList confidentAssociationRuleDiscovery(Data data,FrequentPattern fp,float minConf)	
	{
		LinkList outputAR = new LinkList();
		for (int i=0; i < fp.getPatternLength(); i++)
		{
			AssociationRule AR = confidentAssociationRuleDiscovery(data,fp,minConf,i);
			if (AR.getConfidence() >= minConf)
				outputAR.add(AR);
		}
		return outputAR;
	}


	private static AssociationRule confidentAssociationRuleDiscovery(Data data,FrequentPattern fp,float minConf, int iCut)
	{
		AssociationRule AR=new AssociationRule(fp.getSupport());
	
		//to generate the antecedent of the association rule
		for(int j=0;j<iCut;j++)
		{
			AR.addAntecedentItem(fp.getItem(j));		
		}
		//to generate the consequent of the association rule
		for(int j=iCut;j<fp.getPatternLength();j++){
			AR.addConsequentItem(fp.getItem(j));
		}	
		AR.setConfidence(AssociationRuleMiner.computeConfidence(data,AR));
		return AR;
}

//Aggiorna il supporto
static float  computeConfidence(Data data, AssociationRule AR)
{
	int antCount = 0;
	int assCount = 0;
	// indice esempio
	for(int i=0;i<data.getNumberOfExamples();i++)
	{
		//indice item
		boolean isSupporting=true;
		for(int j=0;j<AR.getAntecedentLenght();j++)
		{
			//DiscreteItem
			DiscreteItem item=(DiscreteItem)AR.getAntecedentItem(j);
			DiscreteAttribute attribute=(DiscreteAttribute)item.getAttribute();
			//Extract the example value
			Object valueInExample=data.getAttributeValue(i, attribute.getIndex());
			if(!item.checkItemCondition(valueInExample))
			{
				isSupporting=false;
				break; //the ith example does not satisfy fp
			}
			
		}
		if(isSupporting)
			antCount++;
		assCount = antCount;
		for(int j=0;j<AR.getConsequentLenght();j++)
		{
			//DiscreteItem
			DiscreteItem item=(DiscreteItem)AR.getConsequentItem(j);
			DiscreteAttribute attribute=(DiscreteAttribute)item.getAttribute();
			//Extract the example value
			Object valueInExample=data.getAttributeValue(i, attribute.getIndex());
			if(!item.checkItemCondition(valueInExample))
			{
				isSupporting=false;
				break; //the ith example does not satisfy fp
			}
			
		}
		if(isSupporting)
			assCount++;
	}
	return ((float)assCount)/(antCount);	
}

}