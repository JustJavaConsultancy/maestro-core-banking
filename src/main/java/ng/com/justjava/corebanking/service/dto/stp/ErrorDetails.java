package ng.com.justjava.corebanking.service.dto.stp;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.io.Serializable;
import java.util.Map;


@SuppressWarnings("serial")
public class ErrorDetails implements Serializable {

    protected long id;
    protected String cause;
    protected String details;// stackTrace;
    protected String exception; // exception class
    protected long timestamp;
    protected long sessionId;

    public ErrorDetails() {
        super();

        this.timestamp = System.currentTimeMillis();
    }

    public ErrorDetails(Exception ex) {
        this();

        Throwable rootCause = ExceptionUtils.getRootCause(ex);
        if (rootCause != null) {
            this.cause = rootCause.getMessage();
            this.exception = rootCause.getClass().getName();
            this.details = ExceptionUtils.getStackTrace(rootCause);
        } else {
            this.cause = ex.getMessage();
            this.exception = ex.getClass().getName();
            this.details = ExceptionUtils.getStackTrace(ex);
        }
    }

    public ErrorDetails(Map<String, Object> errorStatus) {
        this();

        if (errorStatus.containsKey("error")) {
            Object error = errorStatus.get("error");
            if (error instanceof String) {
                String status = String.valueOf(errorStatus.get("status"));
                if (StringUtils.isNotEmpty(status)) {
                    status = status.replace(".0", "");
                }
                this.exception = status + ": " + error;
                this.cause = errorStatus.get("message") + " (path: " + errorStatus.get("path") + ")";
                this.details = StringUtils.EMPTY;
            } else {
                Map<String, Object> errorMap = (Map<String, Object>) error;
                this.exception = String.valueOf(errorMap.get("exception"));
                this.cause = String.valueOf(errorMap.get("cause"));
                this.details = String.valueOf(errorMap.get("details"));
            }
        } else {
            this.exception = String.valueOf(errorStatus.get("exception"));
            this.cause = String.valueOf(errorStatus.get("cause"));
            this.details = String.valueOf(errorStatus.get("details"));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSessionId() {
        return sessionId;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
