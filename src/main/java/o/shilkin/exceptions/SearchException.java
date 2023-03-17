package o.shilkin.exceptions;

/**
 * Класс исключения для Библиотеки поиска.
 */
public class SearchException extends Exception {
    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }

}