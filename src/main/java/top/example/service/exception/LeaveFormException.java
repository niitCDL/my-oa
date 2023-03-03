package top.example.service.exception;


public class LeaveFormException extends RuntimeException{
    public LeaveFormException() {

    }

    public LeaveFormException(String message) {
        super(message);
    }
}
