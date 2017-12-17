package mining;

import java.io.Serializable;
import java.util.Iterator;


public class AssociationRule implements Comparable<AssociationRule>, Serializable
{
	private Item antecedent [] = new Item [0];
	private Item consequent [] = new Item [0];
	float support;
	private float confidence;
	
	
	/**
	 * Costruttore
	 */
	public AssociationRule ()
	{
		
	}
	
	/**
	 * Costruttore della classe 
	 * @param support valore di supporto
	 */
	public AssociationRule (float support)
	{
		this.support = support;
	}
	
	/**
	 * ritorna il valore di supporto impostato per la regola di associazione
	 * @return valore di supporto
	 */
	public float getSupport()
	{
		return this.support;
	}
	
	/**
	 * ritorna il valore di confidenza impostato per la regola di associazione
	 * @return valore di confidenza
	 */
	public float getConfidence()
	{
		return this.confidence;
	}
	
	/**
	 * ritorna il numero di regole presenti nella parte antecedente della regola di associazione
	 * @return numero di item nella parte "antecedente"
	 */
	public int getAntecedentLenght ()
	{
		return antecedent.length;
	}
	
	/**
	 * ritorna il numero di regole presenti nella parte consequente della regola di associazione
	 * @return numero di item nella parte "consequente"
	 */
	public int getConsequentLenght()
	{
		return this.consequent.length;
	}
	
	/**
	 * Aggiunge un item nella parte antecedente 
	 * @param item item da aggiungere 
	 */
	public void addAntecedentItem (Item item)
	{
		//copio gli antecedenti in una variabile temporanea, 
		Item temp [] = antecedent.clone();
		//modifico la dimensione dell'antecedente, estendendolo alla propria dimensione + 1 (dato che dovrà essere inserito item)
		antecedent = new Item [temp.length+1];
		int i = -1;
		//infine, inserisco i valori contenuti nella variabile temporanea nell'antecedente. Dopo di che, in ultima posizione inserisco item
		for (Item it : temp)
		{
			i++;
			antecedent[i] = it;
		}
		int l = antecedent.length;
		i++;
		antecedent[i] = item;
	}
	
	/**
	 * Aggiunge un item nella parte consequente
	 * @param item item da aggiungere
	 */
	public void addConsequentItem (Item item)
	{
		//stessa procedura di "addAntecedentItem"
		Item temp [] = consequent.clone();
		consequent= new Item [temp.length+1];
		int i = -1;
		for (Item it : temp)
		{
			i++;
			consequent[i] = it;
		}
		i++;
		consequent[i] = item;
	}
	
	/**
	 * Ricava un item dalla parte consequente
	 * @param index indice relativo alla posizione dell'item desiderato
	 * @return item in posizione index 
	 */
	public Item getConsequentItem (int index)
	{
		return consequent[index];
	}
	
	/**
	 * Ricava un item dalla parte antecedente
	 * @param index indice relativo alla posizione dell'item desiderato
	 * @return item in posizione index 
	 */
	public Item getAntecedentItem (int index)
	{
		return antecedent[index];
	}
	
	/**
	 * imposta il valore di confidenza della regola di associazione 
	 * @param confedence valore di confidenza
	 */
	public void setConfidence (float confedence)
	{
		this.confidence = confedence;
	}
	
	public int compareTo(AssociationRule AR)
	{
		return (this.confidence != AR.getConfidence()) == true ? 1 : -1;
	}
	
	/**
	 * Verifica che gli item presenti nella regola d'associazione passato come parametro ('AR') siano contenuti regola d'associazione in analisi.
	 * Si può effettuare la ricerca per la parte "antecedente" o "consequente"
	 * @param AR regola d'associazione da verificare
	 * @param side indica quale "parte" delle regole di associazione bisogna verificare. 
	 * Se 'L' -> antecedente;
	 * Se 'R' -> consequente;
	 * @return true se gli item di AR sono contenuti nella regola d'associazione, false altrimenti
	 */
	boolean isContained (AssociationRule AR,char side)
	{
		//verifica inanzitutto che il numero degli item presenti in AR siano AL MASSIMO pari a quelli presenti nella regola d'associazione in analisi
		//qualora così non fosse, vorrà dire che, inevitabilmente, ci sarà almeno un item di AR che non sarà contenuto
		if ((this.antecedent.length < AR.antecedent.length)||(this.consequent.length < AR.consequent.length))
			return false;
		Item [] arPattern;
		Item [] arThis;
		switch (side)
		{
			case 'L':
				arPattern = AR.antecedent;
				arThis = this.antecedent;
				break;
			
			case 'R':
				arPattern = AR.consequent;
				arThis = this.consequent;
				break;
			
			default:
				return false;
		}
		boolean flag = true;
		for (Item arPatternItem : arPattern)
		{
			flag = false;
			for (Item arThisItem : arThis)
			{
				if (arThisItem.compareTo(arPatternItem))
				{
					flag = true;
					break;
				}
			}
			if (flag == false)
				break;
		}
		return flag;
	}
	
	public String toString()
	{
		String value = "";
		for(int i=0 ; i<antecedent.length-1 ; i++)
			value += antecedent[i].toString() + " AND ";
		if(antecedent.length>0)
		{
			value += antecedent[antecedent.length-1];
			if (consequent.length>0)
			{
				value += " ==> ";
				for (int i=0 ; i<consequent.length-1; i++)
					value += consequent[i].toString() + " AND ";
				value += consequent[consequent.length-1];
			}
			value += "(" + support + "," + confidence + ")"; 
		}
		return value;
	}
	
}
