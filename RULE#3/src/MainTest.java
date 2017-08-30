import keyboardinput.Keyboard;
import mining.AssociationRule;
import mining.AssociationRuleMiner;
import mining.FrequentPattern;
import mining.FrequentPatternMiner;
import mining.OneLevelPatternException;
import utility.LinkList;
import utility.Puntatore;
import data.Data;
import data.EmptySetException;





public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		Data data= new Data();
		float minSup=(float)0.0,minConf=(float)0.0;
		do
		{
			System.out.println("Inserisci minsup (in [0,1])");
			minSup=Keyboard.readFloat();
		}while (minSup<0 || minSup>1);
		
		do
		{
			System.out.println("Inserisci minconf (in [0,1])");
			minConf=Keyboard.readFloat();
		}while (minConf<0 || minConf>1);
		try
		{
			LinkList outputFP=FrequentPatternMiner.frequentPatternDiscovery(data,minSup);
			
			int i=1;
			Puntatore p=outputFP.firstList();
			while(!outputFP.endList(p))
			{
				FrequentPattern FP=(FrequentPattern)outputFP.readList(p);
				System.out.println(i+":"+FP);
				
				LinkList outputAR;
					try
					{
					outputAR = AssociationRuleMiner.confidentAssociationRuleDiscovery(data,FP,minConf);
					Puntatore q=outputAR.firstList();
					int j=1;
					while(!outputAR.endList(q))
					{
						AssociationRule AR=(AssociationRule)outputAR.readList(q);
						System.out.println("--"+i+"."+j+":"+AR);
						j++;
						q=outputAR.succ(q);
					}
					}
					catch (OneLevelPatternException e)
					{
						System.out.println(e.getMessage());
					}
					
				

				p=outputFP.succ(p);
				i++;
			}
		}
		catch(EmptySetException e)
		{
			System.out.println(e.getMessage());
			
		}
		
		
	}

}
