
public class Data 
{
	
	 private Object data [][];
	 private int numberOfExamples;
	 private Attribute attributeSet[];
	
	
	Data()
	{
		
		//data
		
		data = new Object [14][5];

        data[0][0] = "Sunny";
        data[0][1] = "Hot";
        data[0][2] = "High";
        data[0][3] = "Weak";
        data[0][4] = "No";
        data[1][0] = "Sunny";
        data[1][1] = "Hot";
        data[1][2] = "High";
        data[1][3] = "Strong";
        data[1][4] = "No";
        data[2][0] = "Overcast";
        data[2][1] = "Hot";
        data[2][2] = "High";
        data[2][3] = "Weak";
        data[2][4] = "Yes";
        data[3][0] = "Rain";
        data[3][1] = "Mild";
        data[3][2] = "High";
        data[3][3] = "Weak";
        data[3][4] = "Yes";
        data[4][0] = "Rain";
        data[4][1] = "Cool";
        data[4][2] = "Normal";
        data[4][3] = "Weak";
        data[4][4] = "Yes";
        data[5][0] = "Rain";
        data[5][1] = "Cool";
        data[5][2] = "Normal";
        data[5][3] = "Strong";
        data[5][4] = "No";
        data[6][0] = "Overcast";
        data[6][1] = "Cool";
        data[6][2] = "Normal";
        data[6][3] = "Strong";
        data[6][4] = "Yes";
        data[7][0] = "Sunny";
        data[7][1] = "Mild";
        data[7][2] = "High";
        data[7][3] = "Weak";
        data[7][4] = "No";
        data[8][0] = "Sunny";
        data[8][1] = "Cool";
        data[8][2] = "Normal";
        data[8][3] = "Weak";
        data[8][4] = "Yes";
        data[9][0] = "Rain";
        data[9][1] = "Mild";
        data[9][2] = "High";
        data[9][3] = "Weak";
        data[9][4] = "No";
        data[10][0] = "Sunny";
        data[10][1] = "Mild";
        data[10][2] = "Normal";
        data[10][3] = "Strong";
        data[10][4] = "Yes";
        data[11][0] = "Overcast";
        data[11][1] = "Mild";
        data[11][2] = "High";
        data[11][3] = "Strong";
        data[11][4] = "Yes";
        data[12][0] = "Overcast";
        data[12][1] = "Hot";
        data[12][2] = "Normal";
        data[12][3] = "Weak";
        data[12][4] = "Yes";
        data[13][0] = "Rain";
        data[13][1] = "Mild";
        data[13][2] = "High";
        data[13][3] = "Strong";
        data[13][4] = "No";
        
         
		// numberOfExamples
		
		 numberOfExamples=14;		 
		 
		
		//explanatory Set
		
		attributeSet = new Attribute[5];
		
		String outLookValues[] = {"Sunny", "Overcast", "Rain"};
		attributeSet[0] = new DiscreteAttribute("Outlook", 0, outLookValues);
		
		String temperatureValues[] = {"Hot", "Mid", "Cold"};
		attributeSet[1] = new DiscreteAttribute("Temperature", 1, temperatureValues);
		
		String humidityValues[] = {"Hight", "Normal"};
		attributeSet[2] = new DiscreteAttribute("Humidity", 2, humidityValues);
		
		String windValues[] = {"Weak", "Strong"};
		attributeSet[3] = new DiscreteAttribute("Wind", 3, windValues);
		
		String playTennisValues[] = {"No", "Yes"};
		attributeSet[4] = new DiscreteAttribute("PlayTennis", 4, playTennisValues);
	}
	
	public int getNumberOfExamples()
	{
		return this.numberOfExamples;
	}
	
	public int getNumberOfAttributes()
	{
		return this.attributeSet.length;
	}
	
	
	
	public Object getAttributeValue(int exampleIndex, int attributeIndex)
	{
		return this.data[exampleIndex][attributeIndex];
	}
	
	public Attribute getAttribute(int index)
	{
		return this.attributeSet[index];
	}
	
	
	public String toString()
	{
		String string = "";
		for (int i = 0; i < this.numberOfExamples; i++)
		{
			string += (i+1) +":";
			for (int j = 0; j < this.attributeSet.length; j++)
			{
				string += data[i][j].toString() + ",";
			}
			string += "\n";
		}
		return string;
	}


	
	public static void main(String args[])
	{
		Data trainingSet=new Data();
		System.out.println(trainingSet);
	}

}