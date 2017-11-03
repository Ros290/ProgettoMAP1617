package database;

/**
 * Lanciato quando non viene trovato nessun valore
 * @author Ciccariello Luciano, Palumbo Vito, Rosini Luigi
 */
class NoValueException extends Exception {

    private String message;

    NoValueException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
