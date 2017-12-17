package data;

import java.util.Iterator;

public class ContinuousAttributeIterator implements Iterator<Float> {

	private float min;
	private float max;
	private int j=0;
	private int numValues;
	
	/**
	 * Costruttore 
	 * @param min valore minimo dell'intervallo
	 * @param max valore massimo dell'intervallo
	 * @param numValues numero di sotto-intervalli (sempre compresi tra [min,max] ) 
	 */
	ContinuousAttributeIterator(float min,float max,int numValues){
		this.min=min;
		this.max=max;
		this.numValues=numValues;
	}
	
	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return (j<=numValues);
	}

	@Override
	public Float next() {
		// TODO Auto-generated method stub
		j++;
		return min+((max-min)/numValues)*(j-1);
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}

}
