package ng.com.systemspecs.apigateway.service.dto.stp;

import ng.com.systemspecs.apigateway.domain.enumeration.RequestCode;

@SuppressWarnings("serial")
public class DefaultRequestPayload extends AbstractRequestPayload {

    public DefaultRequestPayload() {
        super();
    }

    public DefaultRequestPayload(RequestCode action, String bank) {
        super();

        this.bank = bank;
        this.requestCode = action;
    }
}
