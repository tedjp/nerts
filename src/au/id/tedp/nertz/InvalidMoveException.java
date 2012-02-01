package au.id.tedp.nertz;

import java.lang.Exception;

public class InvalidMoveException extends Exception {
    public InvalidMoveException(String detailMessage) {
        super(detailMessage);
    }
}
