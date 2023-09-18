package ng.com.systemspecs.apigateway.service.dto.stp;

import ng.com.systemspecs.apigateway.domain.enumeration.RequestCode;
import ng.com.systemspecs.apigateway.domain.enumeration.ResponseCode;

@SuppressWarnings("serial")
public class DefaultResponsePayload extends AbstractResponsePayload {

    public DefaultResponsePayload() {
    }

    public DefaultResponsePayload(RequestCode requestCode) {
        super(requestCode);
    }

    public static DefaultResponsePayload getDefaultStatus(DefaultRequestPayload requestPayload) {
        DefaultResponsePayload payload = new DefaultResponsePayload();

        if (requestPayload != null) {
            payload = new DefaultResponsePayload(requestPayload.getRequestCode());
            payload.setBank(requestPayload.getBank());
        }

        payload.setResponseCode(ResponseCode.SUCCESS.getCode());
        payload.setResponseMessage(ResponseCode.SUCCESS.name());
        payload.setTimestamp(System.currentTimeMillis());

        return payload;
    }

    public static DefaultResponsePayload getDefaultStatus(RequestCode action, String bankId) {
        return getDefaultStatus(new DefaultRequestPayload(action, bankId));
    }

}
