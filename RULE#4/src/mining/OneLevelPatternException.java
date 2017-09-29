package mining;

/**
 * 
 * @author Windows 7
 *Eccezzione evocata quando si cerca di generare una regola d'associazione da un pattern di lunghezza 1
 */
public class OneLevelPatternException extends Exception
{
    private String message;

    public OneLevelPatternException(String message) 
    {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
