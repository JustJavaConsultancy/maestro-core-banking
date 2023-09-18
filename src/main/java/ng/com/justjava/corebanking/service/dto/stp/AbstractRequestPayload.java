package ng.com.justjava.corebanking.service.dto.stp;


import ng.com.justjava.corebanking.domain.enumeration.RequestCode;

import java.io.Serializable;


@SuppressWarnings("serial")
abstract class AbstractRequestPayload implements Serializable {

    protected RequestCode requestCode;

    protected String controlRequestId;

    protected String bank;

    protected String comments;

    protected String requestHash;


    public AbstractRequestPayload() {
        super();
    }


    public RequestCode getRequestCode() {
        return requestCode;
    }


    public AbstractRequestPayload setRequestCode(RequestCode requestCode) {
        this.requestCode = requestCode;
        return this;
    }


    public String getControlRequestId() {
        return controlRequestId;
    }


    public AbstractRequestPayload setControlRequestId(String controlRequestId) {
        this.controlRequestId = controlRequestId;
        return this;
    }


    public String getBank() {
        return bank;
    }


    public AbstractRequestPayload setBank(String bank) {
        this.bank = bank;
        return this;
    }


    public String getComments() {
        return comments;
    }


    public AbstractRequestPayload setComments(String comments) {
        this.comments = comments;
        return this;
    }


    public String getRequestHash() {
        return requestHash;
    }


    public AbstractRequestPayload setRequestHash(String requestHash) {
        this.requestHash = requestHash;
        return this;
    }
}
