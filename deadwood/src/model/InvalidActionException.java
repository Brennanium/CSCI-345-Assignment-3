package model;

public class InvalidActionException extends Exception{
    private String reason;

    /**
     * 
     * Constructor
     * @param message
     */
    public InvalidActionException(String message) {
        super(message);
        reason = message;
    }

    /**
     * 
     * @return String
     */
    public String getReason(){
        return reason;
    }
}
