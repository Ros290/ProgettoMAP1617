package mining;

/**
 * 
 * @author Windows 7
 *Eccezzione evocata quando non viene ggenerata alcuna regola confidente da parte di un pattern frequente
 */
public class NoPatternException extends Exception
{
    private String message;

    public NoPatternException(String message) 
    {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
