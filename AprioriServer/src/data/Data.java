package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import database.DatabaseConnectionException;
import database.DbAccess;
import database.TableData;
import database.TableData.TupleData;


public class Data 
{
	
	private List<TupleData> data;
	private int numberOfExamples;
	private List<Attribute> attributeSet = new LinkedList <Attribute>();
	
	
	public Data(String table) throws SQLException, DatabaseConnectionException
	{
        this.data = new ArrayList<TupleData>();
        DbAccess db = new DbAccess();
        TableData td = new TableData(db);
        //db.closeConnection();
        db.initConnection();
        this.data = td.getTransazioni(table);

        this.numberOfExamples = this.data.size();
        attributeSet = new LinkedList<>();	 
		 
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
        return data.get(exampleIndex).get(attributeIndex);
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
				string += data.get(i).get(j).toString() + ",";
			}
			string += "\n";
		}
		return string;
	}

}