package exceptions;

import java.io.IOException;

public class ManagerServerSaveException extends RuntimeException {

    public ManagerServerSaveException(String message) {
        super(message);
    }
}
