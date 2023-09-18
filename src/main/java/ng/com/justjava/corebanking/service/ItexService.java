package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.BouquetRequestDTO;
import ng.com.justjava.corebanking.service.dto.DataLookupDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.InternetSubscriptionDTO;
import ng.com.justjava.corebanking.service.dto.InternetValidationDTO;
import ng.com.justjava.corebanking.service.dto.MeterValidationDTO;
import ng.com.justjava.corebanking.service.dto.MultiChoiceSubscriptionRequestDTO;
import ng.com.justjava.corebanking.service.dto.PurchaseElectricityDTO;
import ng.com.justjava.corebanking.service.dto.PurchaseVTU;
import ng.com.justjava.corebanking.service.dto.SubscribeCableTVDTO;
import ng.com.justjava.corebanking.service.dto.SubscribeDataDTO;
import ng.com.justjava.corebanking.service.dto.SubscribeStartimesDTO;
import ng.com.justjava.corebanking.service.dto.ValidateCableTvDTO;

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
