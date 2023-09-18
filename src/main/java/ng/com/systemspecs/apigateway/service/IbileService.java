package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.IbilePaymentDetails;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.IbilePaymentDTO;
import ng.com.systemspecs.apigateway.service.dto.PaymentNotificationResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.ReferenceVerificationResponseDTO;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public interface IbileService {

    ReferenceVerificationResponseDTO referenceVerification();

    PaymentNotificationResponseDTO paymentVerification();

    GenericResponseDTO getPaymentDetails(String billType, String billReferenceNo, HttpSession session);

    GenericResponseDTO pay(List<IbilePaymentDTO> ibilePaymentDTOs);

    GenericResponseDTO generateAgentReceipt(String externalRef);

    GenericResponseDTO getWebguids(String pId);

    GenericResponseDTO getReceiptsByRef(String refId);

    Optional<IbilePaymentDetails> findByReferenceNumber(String referenceNumber);


}
