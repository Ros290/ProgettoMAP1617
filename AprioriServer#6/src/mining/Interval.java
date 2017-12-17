package mining;

import java.io.Serializable;

public class Interval implements Serializable
{
	private float inf;
	private float sup;
	
	/**
	 * Costruttore 
	 * @param inf valore minimo dell'intervallo
	 * @param sup valore massimo dell'intervallo
	 */
	public Interval (float inf, float sup)
	{
		this.inf = inf;
		this.sup = sup;
	}
	
	/**
	 * Imposta il valore minimo dell'intervallo
	 * @param inf valore minimo
	 */
	void setInf (float inf)
	{
		this.inf = inf;
	}
	
	/**
	 * Imposta il valore massimo dell'intervallo
	 * @param sup valore massimo
	 */
	void setSup (float sup)
	{
		this.sup = sup;
	}
	
	/**
	 * restituisce il valore minimo dell'intervallo
	 * @return valore minimo
	 */
	float getInf ()
	{
		return inf;
	}
	
	/**
	 * restituisce il valore massimo dell'intervallo
	 * @return valore massimo
	 */
	float getSup ()
	{
		return sup;
	}
	
	/**
	 * verifica che il valore passato come parametro, rientra all'interno dell'intevallo
	 * @param value valore da verificare 
	 * @return true se il valore è compreso nell'intervallo, false altrimenti
	 */
	boolean checkValueInclusion (float value)
	{
		if ((value<sup)&&(value>=inf))
			return true;
		else
			return false;
		
	}
	
	public String toString ()
	{
		return "[" + inf + "," + sup + "[";
	}
	
}
