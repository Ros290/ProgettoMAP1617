package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import database.TableSchema.Column;


public class TableData 
{
	DbAccess db;
	public class TupleData
	{
		public List<Object> tuple=new ArrayList<Object>();
		public String toString()
		{
			String value="";
			Iterator<Object> it= tuple.iterator();
			while(it.hasNext())
				value+= (it.next().toString() +" ");
			
			return value;
		}
	    /**
	     * Ottiene un oggetto specifico dal campione corrente
	     * @param i indice dell'oggetto da ritornare
	     * @return oggetto specificato dal campione
	     */
	    public Object get(int i) {
	        return tuple.get(i);
	    }
	    
	    /**
	     * Compara due campioni
	     * @return 0 se i due campioni sono uguali
	     */
	    public int compareTo(TupleData ex) {

	        int i = 0;
	        for (Object o : ex.tuple) {
	            if (!o.equals(this.tuple.get(i)))
	                return ((Comparable) o).compareTo(this.tuple.get(i));
	            i++;
	        }
	        return 0;
	    }
	}
	
    /**
     * Inizializzazione
     * @param db specifica un databsae
     */
    public TableData(DbAccess db) {
        this.db = db;
    }
	
    /**
     * Restituisce una lista di Tuple (o esempi) presenti nella tabella specificata come parametro
     * @param table nome della tabella da cui ricavare gli esempi
     * @return lista di tuple (o esempi)
     * @throws SQLException
     */
	public List<TupleData> getTransazioni(String table) throws SQLException{
		LinkedList<TupleData> transSet = new LinkedList<TupleData>();
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		
		
		String query="select ";
		
		for(int i=0;i<tSchema.getNumberOfAttributes();i++){
			Column c=tSchema.getColumn(i);
			if(i>0)
				query+=",";
			query += c.getColumnName();
		}
		if(tSchema.getNumberOfAttributes()==0)
			throw new SQLException();
		query += (" FROM "+table);
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		while (rs.next()) {
			TupleData currentTuple=new TupleData();
			for(int i=0;i<tSchema.getNumberOfAttributes();i++)
				if(tSchema.getColumn(i).isNumber())
					currentTuple.tuple.add(rs.getFloat(i+1));
				else
					currentTuple.tuple.add(rs.getString(i+1));
			transSet.add(currentTuple);
		}
		rs.close();
		statement.close();

		
		
		return transSet;

	}

	
	public  Object getAggregateColumnValue(String table,Column column,QUERY_TYPE aggregate) throws SQLException,NoValueException{
		Statement statement;
		TableSchema tSchema=new TableSchema(db,table);
		Object value=null;
		String aggregateOp="";
		
		String query="select ";
		if(aggregate==QUERY_TYPE.MAX)
			aggregateOp+="max";
		else
			aggregateOp+="min";
		query+=aggregateOp+"("+column.getColumnName()+ ") FROM "+table;
		
		
		statement = db.getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		if (rs.next()) {
				if(column.isNumber())
					value=rs.getFloat(1);
				else
					value=rs.getString(1);
			
		}
		rs.close();
		statement.close();
		if(value==null)
			throw new NoValueException("No " + aggregateOp+ " on "+ column.getColumnName());
			
		return value;

	}
	
	 /**
     * Ottiene un insieme di oggetti rappresentanti le colonne
     * @param table tabella la quale estrarre le informazioni
     * @param column colonna da specificare
     * @return insieme di oggetti
     * @throws SQLException se la connessione col database fallisce
     */
    public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
        Set<Object> valueSet = new TreeSet<Object>();
        Statement statement;
        TableSchema tSchema = new TableSchema(db, table);


        String query = "select distinct ";

        query += column.getColumnName();

        query += (" FROM " + table);

        query += (" ORDER BY " + column.getColumnName());


        statement = db.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            if (column.isNumber())
                valueSet.add(rs.getDouble(1));
            else
                valueSet.add(rs.getString(1));

        }
        rs.close();
        statement.close();

        return valueSet;

    }
    

}
