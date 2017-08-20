

 public class AssociationRuleMiner {
		
	
	public static LinkList confidentAssociationRuleDiscovery(Data data,FrequentPattern fp,float minConf)	{
//		TO DO
		return outputAR;
	}


private static AssociationRule confidentAssociationRuleDiscovery(Data data,FrequentPattern fp,float minConf, int iCut){
	AssociationRule AR=new AssociationRule(fp.getSupport());
	
	//to generate the antecedent of the association rule
	for(int j=0;j<iCut;j++){
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
static float  computeConfidence(Data data, AssociationRule AR){
	// TO DO
	
}

}