package phoenixit.education.exceptions;

import org.aspectj.bridge.Message;

public class UniqueKeyException extends Exception {
    public UniqueKeyException(String errorMessage) {
        super(errorMessage);
    }
}
