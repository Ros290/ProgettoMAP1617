package utility;

/**
 * Eccezione evocata quando si tenta di leggere la testa della coda, quando quest'ultima invece risulta essere vuota
 * @author Windows 7
 *
 */
public class EmptyQueueException extends Exception
{
    private String message;

    public EmptyQueueException(String message) 
    {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
