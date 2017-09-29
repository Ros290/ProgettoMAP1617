package data;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class Data 
{
	
	 private Object data [][];
	 private int numberOfExamples;
	 private List<Attribute> attributeSet = new LinkedList <Attribute>();
	
	
	public Data()
	{
		
		//data
		
		data = new Object [14][5];

        data[0][0] = "Sunny";
        data[0][1] = new Float (30.3);
        data[0][2] = "High";
        data[0][3] = "Weak";
        data[0][4] = "No";
        data[1][0] = "Sunny";
        data[1][1] = new Float (30.3);
        data[1][2] = "High";
        data[1][3] = "Strong";
        data[1][4] = "No";
        data[2][0] = "Overcast";
        data[2][1] = new Float (30);
        data[2][2] = "High";
        data[2][3] = "Weak";
        data[2][4] = "Yes";
        data[3][0] = "Rain";
        data[3][1] = new Float (13);
        data[3][2] = "High";
        data[3][3] = "Weak";
        data[3][4] = "Yes";
        data[4][0] = "Rain";
        data[4][1] = new Float (0);
        data[4][2] = "Normal";
        data[4][3] = "Weak";
        data[4][4] = "Yes";
        data[5][0] = "Rain";
        data[5][1] = new Float (0);
        data[5][2] = "Normal";
        data[5][3] = "Strong";
        data[5][4] = "No";
        data[6][0] = "Overcast";
        data[6][1] = new Float (0.1);
        data[6][2] = "Normal";
        data[6][3] = "Strong";
        data[6][4] = "Yes";
        data[7][0] = "Sunny";
        data[7][1] = new Float (13);
        data[7][2] = "High";
        data[7][3] = "Weak";
        data[7][4] = "No";
        data[8][0] = "Sunny";
        data[8][1] = new Float (0.1);
        data[8][2] = "Normal";
        data[8][3] = "Weak";
        data[8][4] = "Yes";
        data[9][0] = "Rain";
        data[9][1] = new Float (12);
        data[9][2] = "Normal";
        data[9][3] = "Weak";
        data[9][4] = "Yes";
        data[10][0] = "Sunny";
        data[10][1] = new Float (12.5);
        data[10][2] = "Normal";
        data[10][3] = "Strong";
        data[10][4] = "Yes";
        data[11][0] = "Overcast";
        data[11][1] = new Float (12.5);
        data[11][2] = "High";
        data[11][3] = "Strong";
        data[11][4] = "Yes";
        data[12][0] = "Overcast";
        data[12][1] = new Float (29.21);
        data[12][2] = "Normal";
        data[12][3] = "Weak";
        data[12][4] = "Yes";
        data[13][0] = "Rain";
        data[13][1] = new Float (12.5);
        data[13][2] = "High";
        data[13][3] = "Strong";
        data[13][4] = "No";
        
		 numberOfExamples=14;		 
		 
		 String outLookValues[] = {"Sunny", "Overcast", "Rain"};
		 attributeSet.add(new DiscreteAttribute("Outlook", 0, outLookValues));
		 
		 attributeSet.add(new ContinuousAttribute("Temperature", 1, 0, (float) 30.3));
		 
		 String humidityValues[] = {"High", "Normal"};
		 attributeSet.add(new DiscreteAttribute("Humidity", 2, humidityValues));
		 
		 String windValues[] = {"Weak", "Strong"};
		 attributeSet.add(new DiscreteAttribute("Wind", 3, windValues));
		 
		 String playTennisValues[] = {"No", "Yes"};
		 attributeSet.add(new DiscreteAttribute("PlayTennis", 4, playTennisValues));

	}
	
	/**
	 * 
	 * @return Ritorna il numero di esempi riportati nella collezione 
	 */
	public int getNumberOfExamples()
	{
		return this.numberOfExamples;
	}
	
	/**
	 * 
	 * @return Ritorna il numero di attributi che compongono gli esempi nella collezione
	 */
	public int getNumberOfAttributes()
	{
		return this.attributeSet.size();
	}
	
	
	/**
	 * 
	 * @param exampleIndex posizione dell'esempio
	 * @param attributeIndex indice relativo all'attributo
	 * @return il valore del relativo attributo riportato nel relativo esempio
	 */
	public Object getAttributeValue(int exampleIndex, int attributeIndex)
	{
		return this.data[exampleIndex][attributeIndex];
	}
	
	/**
	 * 
	 * @param index indice relativo all'attributo desiderato
	 * @return l'attributo richiesto
	 * @exception EmptySetException
	 */
	public Attribute getAttribute(int index) throws EmptySetException
	{
		Iterator it = attributeSet.listIterator();
		while (it.hasNext())
		{
			Attribute da = (Attribute) it.next();
			if (da.getIndex() == index)
				return da;
		}
		throw new EmptySetException ("Nessun attributo con indice "+index+"\n");
	}
	
	
	public String toString()
	{
		String string = "";
		for (int i = 0; i < this.numberOfExamples; i++)
		{
			string += (i+1) +":";
			for (int j = 0; j < this.attributeSet.size(); j++)
			{
				string += data[i][j].toString() + ",";
			}
			string += "\n";
		}
		return string;
	}

}