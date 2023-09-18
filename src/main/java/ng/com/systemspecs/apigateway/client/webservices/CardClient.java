package ng.com.systemspecs.apigateway.client.webservices;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class CardClient
//    extends WebServiceGatewaySupport
{

    private static final Logger log = LoggerFactory.getLogger(CardClient.class);
    @Value("${app.uri.webservices.base-uri}")
    private String webserviceUri;

    @Value("${app.uri.webservices.create-virtual-card-action-uri}")
    private String virtualCardActionCallback;

    @Value("${app.uri.webservices.create-debit-card-action-uri}")
    private String debitCardActionCallback;

    @Value("${app.uri.webservices.cancel-card-action-uri}")
    private String cancelCardActionCallback;

    @Value("${app.uri.webservices.activate-card-action-uri}")
    private String activateCardActionCallback;

    @Value("${app.uri.webservices.load-card-action-uri}")
    private String loadCardActionCallback;

    @Value("${app.uri.webservices.fetch-transactions-action-uri}")
    private String fetchTransactionsActionCallback;

    @Value("${app.uri.webservices.view-card-balance-action-uri}")
    private String viewCardBalanceActionCallback;

    @Value("${app.uri.webservices.get-value-with-pan-action-uri}")
    private String getValueWithPanActionCallback;

    //Virtual card creation method

   /* public CreateVirtualCardResponse createVirtualCard(CreateVirtualCard virtualCard) {
        log.info("Virtual card request == " + virtualCard);
        return (CreateVirtualCardResponse) getWebServiceTemplate()
            .marshalSendAndReceive(
                webserviceUri,
                virtualCard,
                new SoapActionCallback(virtualCardActionCallback));
    }

    public CreateDebitCardResponse createDebitCard(CreateDebitCard debitCard) {
        log.info("Debit card request == " + debitCard);
        return (CreateDebitCardResponse) getWebServiceTemplate()
            .marshalSendAndReceive(
                webserviceUri,
                debitCard,
                new SoapActionCallback(debitCardActionCallback));
    }

    public CancelCardResponse cancelCard(CancelCard cancelCard) {
        log.info("Virtual card request == " + cancelCard);
        return (CancelCardResponse) getWebServiceTemplate()
            .marshalSendAndReceive(
                webserviceUri,
                cancelCard,
                new SoapActionCallback(cancelCardActionCallback));
    }

    public ActivateCardResponse activateCard(ActivateCard activateCard) {
        log.info("Virtual card request == " + activateCard);
        return (ActivateCardResponse) getWebServiceTemplate()
            .marshalSendAndReceive(
                webserviceUri,
                activateCard,
                new SoapActionCallback(activateCardActionCallback));
    }

    public LoadCardResponse loadCard(LoadCard loadCard) {
        log.info("Virtual card request == " + loadCard);
        return (LoadCardResponse) getWebServiceTemplate()
            .marshalSendAndReceive(
                webserviceUri,
                loadCard,
                new SoapActionCallback(loadCardActionCallback));
    }

    public FetchTransactionsResponse fetchTransactions(FetchTransactions fetchTransactions) {
        log.info("Virtual card request == " + fetchTransactions);
        return (FetchTransactionsResponse) getWebServiceTemplate()
            .marshalSendAndReceive(
                webserviceUri,
                fetchTransactions,
                new SoapActionCallback(fetchTransactionsActionCallback));
    }

    public ViewCardBalanceResponse viewCardBalance(ViewCardBalance viewCardBalance) {
        log.info("Virtual card request == " + viewCardBalance);
        return (ViewCardBalanceResponse) getWebServiceTemplate()
            .marshalSendAndReceive(
                webserviceUri,
                viewCardBalance,
                new SoapActionCallback(viewCardBalanceActionCallback));
    }

    public GetValueWithPanResponse getValueWithPan(GetValueWithPan getValueWithPan) {
        log.info("Virtual card request == " + getValueWithPan);
        return (GetValueWithPanResponse) getWebServiceTemplate()
            .marshalSendAndReceive(
                webserviceUri,
                getValueWithPan,
                new SoapActionCallback(getValueWithPanActionCallback));
    }*/
}
