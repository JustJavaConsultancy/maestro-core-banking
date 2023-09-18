package ng.com.systemspecs.apigateway.client.webservices;

public class SoapClientInterceptor
//    implements ClientInterceptor
{

    /*@Override
    public boolean handleRequest(org.springframework.ws.context.MessageContext messageContext) throws WebServiceClientException {
        return false;
    }

    @Override
    public boolean handleResponse(org.springframework.ws.context.MessageContext messageContext) throws WebServiceClientException {
        return false;
    }

    @Override
    public boolean handleFault(org.springframework.ws.context.MessageContext messageContext) throws WebServiceClientException {

        WebServiceMessage message = messageContext.getResponse();
        SaajSoapMessage saajSoapMessage = (SaajSoapMessage)message;
        SOAPMessage soapMessage = saajSoapMessage.getSaajMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        try {
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPBody soapBody = soapEnvelope.getBody();
            SOAPFault soapFault = soapBody.getFault();
            throw new RuntimeException(String.format("Error occurred while invoking web service - %s ",soapFault.getFaultString()));
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void afterCompletion(org.springframework.ws.context.MessageContext messageContext, Exception e) throws WebServiceClientException {

    }*/
}
