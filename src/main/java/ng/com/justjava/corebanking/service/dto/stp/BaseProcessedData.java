package ng.com.justjava.corebanking.service.dto.stp;

import java.io.Serializable;

public class BaseProcessedData implements Serializable {

    //@NotNull
    private String processCode;

    //@NotNull
    private String processMessage;

    public String getProcessCode() {
        return processCode;
    }

    public void setProcessCode(String processCode) {
        this.processCode = processCode;
    }

    public String getProcessMessage() {
        return processMessage;
    }

    public void setProcessMessage(String processMessage) {
        this.processMessage = processMessage;
    }
}

