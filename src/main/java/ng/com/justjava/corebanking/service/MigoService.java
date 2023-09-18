package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.*;
import ng.com.systemspecs.apigateway.service.dto.*;

import java.util.HashMap;

public interface MigoService {

    GenericResponseDTO getLoan(HashMap<String, String> queryParams);

    GenericResponseDTO getLoanRealTimeOffers(MigoLoanRealTimeOffersRequestDTO migoLoanRealTimeOffersRequestDTO);

    GenericResponseDTO selectOffer(MigoSelectOfferRequestDTO migoSelectOfferRequestDTO);

    GenericResponseDTO getAccount(HashMap<String, String> queryParams);

    GenericResponseDTO getBankList();

    GenericResponseDTO submitApplication(MigoSubmitApplicationRequestDTO migoSubmitApplicationRequestDTO);

    GenericResponseDTO termsAndConditions();

    GenericResponseDTO applicationCheckout(MigoSubmitApplicationRequestDTO migoSubmitApplicationRequestDTO);

    GenericResponseDTO getActiveLoan(HashMap<String, String> queryParams);

    GenericResponseDTO postPayment(MigoPostPaymentsRequestDTO migoPostPaymentsRequestDTO);

}
