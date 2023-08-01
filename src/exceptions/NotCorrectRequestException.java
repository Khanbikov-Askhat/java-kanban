package exceptions;

public class NotCorrectRequestException extends RuntimeException {
    public NotCorrectRequestException(String message) {
        super(message);
    }
}
