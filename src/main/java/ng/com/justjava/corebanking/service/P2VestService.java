package ng.com.justjava.corebanking.service;


import ng.com.justjava.corebanking.service.dto.*;
import ng.com.systemspecs.apigateway.service.dto.*;

public interface P2VestService {

    GenericResponseDTO validateOffer(P2VestValidateOfferRequestDTO p2VestValidateOfferRequestDTO);

    GenericResponseDTO offer(P2VestOfferRequestDTO p2VestOfferRequestDTO);

    GenericResponseDTO createBorrower(P2VestCreateBorrowerRequestDTO p2VestCreateBorrowerRequestDTO);

    GenericResponseDTO createLoan(P2VestCreateLoanDTO p2VestCreateLoanDTO);

}
