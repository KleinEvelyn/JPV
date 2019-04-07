package dhbw.jpv.webservice.client;

public class WebAppException extends Exception {

    private final ExceptionResponse exceptionResponse;

    public WebAppException(ExceptionResponse exceptionResponse) {
        super(exceptionResponse.message);
        this.exceptionResponse = exceptionResponse;
    }

    public ExceptionResponse getExceptionResponse() {
        return this.exceptionResponse;
    }
}