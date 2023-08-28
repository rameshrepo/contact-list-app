package contactassigment.contactlistapp.util;

public class ContactListException extends Exception {
    private Error error;
    public ContactListException(String message, Throwable cause, Error code) {
        super(message, cause);
        this.error = code;
    }

    public int getErrorCode() {
        return this.error.getCode();
    }
}
