



public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Data data= new Data();
		
		LinkList outputFP=FrequentPatternMiner.frequentPatternDiscovery(data,(float)0.2);
		
		int i=1;
		Puntatore p=outputFP.firstList();
		while(!outputFP.endList(p)){
			FrequentPattern FP=(FrequentPattern)outputFP.readList(p);
			System.out.println(i+":"+FP);
			LinkList outputAR=AssociationRuleMiner.confidentAssociationRuleDiscovery(data,FP,(float)0.3);
			Puntatore q=outputAR.firstList();
			int j=1;
			while(!outputAR.endList(q)){
				AssociationRule AR=(AssociationRule)outputAR.readList(q);
					System.out.println("--"+i+"."+j+":"+AR+"\n");
				j++;
				q=outputAR.succ(q);
			}
			p=outputFP.succ(p);
			i++;
		}
		
		
	}

}
