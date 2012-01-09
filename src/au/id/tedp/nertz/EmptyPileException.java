package au.id.tedp.nertz;

import java.lang.Exception;
import java.lang.Throwable;

public class EmptyPileException extends Exception {
    public EmptyPileException(String detailMessage) {
        super(detailMessage);
    }

    public EmptyPileException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
