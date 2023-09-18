package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.service.dto.BouquetRequestDTO;
import ng.com.systemspecs.apigateway.service.dto.DataLookupDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.InternetSubscriptionDTO;
import ng.com.systemspecs.apigateway.service.dto.InternetValidationDTO;
import ng.com.systemspecs.apigateway.service.dto.MeterValidationDTO;
import ng.com.systemspecs.apigateway.service.dto.MultiChoiceSubscriptionRequestDTO;
import ng.com.systemspecs.apigateway.service.dto.PurchaseElectricityDTO;
import ng.com.systemspecs.apigateway.service.dto.PurchaseVTU;
import ng.com.systemspecs.apigateway.service.dto.SubscribeCableTVDTO;
import ng.com.systemspecs.apigateway.service.dto.SubscribeDataDTO;
import ng.com.systemspecs.apigateway.service.dto.SubscribeStartimesDTO;
import ng.com.systemspecs.apigateway.service.dto.ValidateCableTvDTO;

public interface ItexService {

    GenericResponseDTO authorize(String requestBody);

    GenericResponseDTO validateMeter(MeterValidationDTO meterValidationDTO);

    GenericResponseDTO purchaseElectricity(PurchaseElectricityDTO purchaseElectricityDTO);

    GenericResponseDTO purchaseVTU(PurchaseVTU purchaseVTU);

    GenericResponseDTO dataLookup(DataLookupDTO dataLookupDTO);

    GenericResponseDTO subscribeData(SubscribeDataDTO subscribeDataDTO);

    GenericResponseDTO validateCableTv(ValidateCableTvDTO validateCableTvDTO);

    GenericResponseDTO requestBouquets(BouquetRequestDTO bouquetRequestDTO);

    GenericResponseDTO retrieveMultiChoiceBouquets(BouquetRequestDTO bouquetRequestDTO);

    GenericResponseDTO subscribeCableTv(SubscribeCableTVDTO subscribeCableTVDTO);

    GenericResponseDTO validateStartimes(ValidateCableTvDTO validateCableTvDTO);

    GenericResponseDTO subscribeStartimes(SubscribeStartimesDTO subscribeStartimesDTO);

    GenericResponseDTO subscribeMultiChoice(MultiChoiceSubscriptionRequestDTO multiChoiceSubscriptionRequestDTO);

    GenericResponseDTO validateInternet(InternetValidationDTO internetValidationDTO);

    GenericResponseDTO getInternetBundles(InternetValidationDTO internetValidationDTO);

    GenericResponseDTO subscribeInternet(InternetSubscriptionDTO internetSubscriptionDTO);

    GenericResponseDTO validateMultichoice(ValidateCableTvDTO validateCableTvDTO);

    GenericResponseDTO getItexBillers();

}
