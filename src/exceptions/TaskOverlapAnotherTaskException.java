package exceptions;

public class TaskOverlapAnotherTaskException extends RuntimeException {

    public TaskOverlapAnotherTaskException(String message) {
        super(message);
    }
}
