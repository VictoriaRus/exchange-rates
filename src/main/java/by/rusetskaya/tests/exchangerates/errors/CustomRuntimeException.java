package by.rusetskaya.tests.exchangerates.errors;

public class CustomRuntimeException extends RuntimeException {
    private int statusCode;
    public CustomRuntimeException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
