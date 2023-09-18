package ng.com.justjava.corebanking.service.dto.stp;

import java.io.Serializable;
import java.util.List;

public class AccountStatementResponse implements Serializable {

    private String responseCode;
    private String responseMessage;
    List<StatementLine> statementLines;

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public List<StatementLine> getStatementLines() {
        return statementLines;
    }

    public void setStatementLines(List<StatementLine> statementLines) {
        this.statementLines = statementLines;
    }

    @Override
    public String toString() {
        return "AccountStatementResponse{" +
            "responseCode='" + responseCode + '\'' +
            ", responseMessage='" + responseMessage + '\'' +
            ", statementLines=" + statementLines +
            '}';
    }
}
