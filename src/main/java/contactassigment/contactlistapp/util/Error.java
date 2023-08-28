package contactassigment.contactlistapp.util;

public enum Error {
    TOO_MANY_ROWS(1, "Too many records fetched");

    private final int code;
    private final String description;

    private Error(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "Code: " + code + "Error Description: " + description;
    }
}
