package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.service.dto.*;

/**
 * Service Interface for managing UUSD Integration
 */
public interface CoralPayService {
    CgatePaymentNotificationResponseDTO processPaymentNotification(CgatePaymentNotificationRequestDTO payload);


    CgateDetailsResponseDTO getPaymentDetails(CgateDetailsRequestDTO request);

//Not working
    String invokeReference(String payload);


    String invokeExp(String payload);

    String coralPayInvokeReference(String payload);

    String coralPayPaymentNotification(String payload);

    String statusQuery(String payload);

    String refund(String payload);

    String convertToCoralPayInvokeReferencePayload(InvokeReferenceRequestDTO invokeReferenceRequestDTO);

    InvokeReferenceResponseDTO convertBackCoralPayInvokeReferenceResponse(String response);

    InvokeReferenceResponseDTO callInvokeReference(InvokeReferenceRequestDTO invokeReferenceRequestDTO);


    String getTransactionReference(String walletNumber, Double amount);
}
