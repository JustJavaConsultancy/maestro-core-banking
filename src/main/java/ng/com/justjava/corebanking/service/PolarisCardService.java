package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.*;
import ng.com.systemspecs.apigateway.service.dto.*;

import java.util.List;

public interface PolarisCardService {

    GenericResponseDTO requestCard(PolarisCardRequestDTO polarisCardRequestDTO, String phoneNumber, String scheme);

    List<PolarisCard> getVendorCards(String scheme, String phoneNumber);

    GenericResponseDTO activateCard(PolarisCardOperationsDTO polarisCardOperationsDTO);

    GenericResponseDTO resetCardPin(PolarisCardOperationsDTO polarisCardOperationsDTO);

    GenericResponseDTO changeCardPin(PolarisCardOperationsDTO polarisCardOperationsDTO);

    String getAccessToken();

    GenericResponseDTO getVendorCardProfile(String scheme);

    GenericResponseDTO getPolarisVendorCardProfile(String scheme);

    PolarisCardProfileDTO getCardProfile(String scheme, String cardType, String cardName);

    GenericResponseDTO addAccountsToVendor(List<String> accounts);

    GenericResponseDTO getBranches();

    GenericResponseDTO openCollectionAccount(String phoneNumber, String scheme);
}
