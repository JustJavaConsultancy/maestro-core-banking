package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.IbilePaymentDTO;
import ng.com.justjava.corebanking.service.dto.PaymentNotificationResponseDTO;
import ng.com.justjava.corebanking.service.dto.ReferenceVerificationResponseDTO;
import ng.com.justjava.corebanking.domain.IbilePaymentDetails;

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
