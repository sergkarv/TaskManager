package taskManager.dao;

/**
 * Created by Администратор on 25.03.16.
 */
public class EmptyParamException extends Exception {
    public EmptyParamException() {
        super("Empty collection parameters!");
    }

    public EmptyParamException(String message) {
        super(message);
    }

    public EmptyParamException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyParamException(Throwable cause) {
        super(cause);
    }

    public EmptyParamException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
