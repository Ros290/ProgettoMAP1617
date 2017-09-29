import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

import keyboardinput.Keyboard;
import mining.AssociationRule;
import mining.AssociationRuleArchieve;
import mining.AssociationRuleMiner;
import mining.FrequentPattern;
import mining.FrequentPatternMiner;
import mining.NoPatternException;
import mining.OneLevelPatternException;
import data.Data;
import data.EmptySetException;





public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Data data= new Data();
		AssociationRuleArchieve archive=new AssociationRuleArchieve();
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
		
		
		
		try{
			LinkedList<FrequentPattern> outputFP=FrequentPatternMiner.frequentPatternDiscovery(data,minSup);
			
			
			Iterator<FrequentPattern> it=outputFP.iterator();
			while(it.hasNext())
			{
				FrequentPattern FP=it.next();
				archive.put(FP);
								
				LinkedList<AssociationRule> outputAR=null;
				try 
				{
					outputAR = AssociationRuleMiner.confidentAssociationRuleDiscovery(data,FP,minConf);
					Iterator<AssociationRule> itRule=outputAR.iterator();
					//genero il treeSet composto dalle regole di confidenza ricavate da FP
					TreeSet<AssociationRule> tree= new TreeSet<AssociationRule>();
					while(itRule.hasNext())
					{
						tree.add(itRule.next());
					}
					//inserisco il frequent Pattern e le sue regole di confidenza nell'archivio
					archive.put(FP,tree);
									
				
				} catch (OneLevelPatternException | NoPatternException e) 
				{
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
				
			}
		}
		catch(EmptySetException e){
			System.out.println(e.getMessage());
		}
		System.out.print(archive);
		
		
	}

}
