package data;

/**
 * Eccezione evocata quando la collezione di dati non contiene valori / esempi
 * @author Windows 7
 *
 */
public class EmptySetException extends Exception
{
    private String message;

    public EmptySetException(String message) 
    {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
