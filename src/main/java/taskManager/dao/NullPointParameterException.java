package taskManager.dao;

/**
 * Created by Администратор on 25.03.16.
 */
public class NullPointParameterException extends Exception {
    public NullPointParameterException() {
        super("Null Point on neded parameter!");
    }

    public NullPointParameterException(String message) {
        super(message);
    }

    public NullPointParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullPointParameterException(Throwable cause) {
        super(cause);
    }

    public NullPointParameterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
