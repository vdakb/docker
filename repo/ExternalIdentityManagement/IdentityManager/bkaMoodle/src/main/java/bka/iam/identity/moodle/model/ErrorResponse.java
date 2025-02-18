package bka.iam.identity.moodle.model;

public class ErrorResponse {

    private String exception;   // Example value: "invalid_parameter_exception"
    private String errorcode;   // Example value: "invalidparameter"
    private String message;     // Example value: "Invalid parameter value detected"
    private String debuginfo;   // Example value: "Username already exists: joebloggs"

    @Override
    public String toString() {
        return "Exception: " + exception + ", errorcode: " + errorcode + ", message: " + message + (debuginfo == null ? "" : (", debuginfo: " + debuginfo));
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebuginfo() {
        return debuginfo;
    }

    public void setDebuginfo(String debuginfo) {
        this.debuginfo = debuginfo;
    }
}
