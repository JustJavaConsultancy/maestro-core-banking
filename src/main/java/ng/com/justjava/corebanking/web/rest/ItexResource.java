package ng.com.justjava.corebanking.web.rest;

import ng.com.justjava.corebanking.domain.Biller;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.repository.UserRepository;
import ng.com.justjava.corebanking.service.ItexService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.util.Utility;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * REST controller for managing {@link Biller}.
 */
@RestController
@RequestMapping("/api")
public class ItexResource {

    private final Logger log = LoggerFactory.getLogger(ItexResource.class);

    private final ItexService itexService;
    private final UserRepository userRepository;
    private final Utility utility;
    private final ProfileService profileService;
    private final PasswordEncoder passwordEncoder;
    private User theUser;

    public ItexResource(ItexService itexService, UserRepository userRepository, Utility utility, ProfileService profileService, PasswordEncoder passwordEncoder) {
        this.itexService = itexService;
        this.userRepository = userRepository;
        this.utility = utility;
        this.profileService = profileService;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/itex/authorize")
    public ResponseEntity<GenericResponseDTO> authorize(String requestBody) {
        log.debug("REST request to call Itex authorize method : {}", requestBody);

        GenericResponseDTO authorize = itexService.authorize(requestBody);
        return new ResponseEntity<>(authorize, authorize.getStatus());
    }

    @PostMapping("/itex/validate/meter")
    public ResponseEntity<GenericResponseDTO>
    validateMeter(@RequestBody MeterValidationDTO meterValidationDTO) {
        log.debug("REST request to validate meter from Itex : {}", meterValidationDTO);

        GenericResponseDTO validateMeter = itexService.validateMeter(meterValidationDTO);
        return new ResponseEntity<>(validateMeter, validateMeter.getStatus());
    }

    @PostMapping("/itex/validate/cabletv")
    public ResponseEntity<GenericResponseDTO> validateCableTv(@RequestBody ValidateCableTvDTO validateCableTvDTO) {
        log.debug("REST request to validate cabletv from Itex : {}", validateCableTvDTO);

        GenericResponseDTO validateCableTv = itexService.validateCableTv(validateCableTvDTO);
        return new ResponseEntity<>(validateCableTv, validateCableTv.getStatus());
    }

    @PostMapping("/itex/validate/startimes")
    public ResponseEntity<GenericResponseDTO> validateStartimes(@RequestBody ValidateCableTvDTO validateCableTvDTO) {
        log.debug("REST request to validate startimes from Itex : {}", validateCableTvDTO);

        GenericResponseDTO validateCableTv = itexService.validateStartimes(validateCableTvDTO);
        return new ResponseEntity<>(validateCableTv, validateCableTv.getStatus());
    }


    @PostMapping("/itex/validate/multichoice")
    public ResponseEntity<GenericResponseDTO> validateMultichoice(@RequestBody ValidateCableTvDTO validateCableTvDTO) {
        log.debug("REST request to validate multichoice from Itex : {}", validateCableTvDTO);

        GenericResponseDTO validateCableTv = itexService.validateMultichoice(validateCableTvDTO);

        return new ResponseEntity<>(validateCableTv, validateCableTv.getStatus());
    }


    @PostMapping("/itex/purchase/electricity")
    public ResponseEntity<GenericResponseDTO> purchaseElectricity(@RequestBody PurchaseElectricityDTO purchaseElectricityDTO, HttpSession session) {

        log.debug("REST request to purchase Electrcity from Itex : {}", purchaseElectricityDTO);

        ResponseEntity<GenericResponseDTO> responseEntity = utility.checkTransactionPin(session, purchaseElectricityDTO.getTransactionPin());
        if (responseEntity.getStatusCode().isError()) {
            return responseEntity;
        }

        GenericResponseDTO purchaseElectricity = itexService.purchaseElectricity(purchaseElectricityDTO);
        return new ResponseEntity<>(purchaseElectricity, purchaseElectricity.getStatus());
    }

    @PostMapping("/itex/purchase/vtu")
    public ResponseEntity<GenericResponseDTO> purchaseVTU(@RequestBody PurchaseVTU purchaseVTU, HttpSession session) {

        log.debug("REST request to purchase VTU from Itex : {}", purchaseVTU);

        ResponseEntity<GenericResponseDTO> responseEntity = utility.checkTransactionPin(session, purchaseVTU.getTransactionPin());
        if (responseEntity.getStatusCode().isError()) {
            return responseEntity;
        }

        GenericResponseDTO genericResponseDTO = itexService.purchaseVTU(purchaseVTU);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @PostMapping("/itex/lookup/data")
    public ResponseEntity<GenericResponseDTO> dataLookUp(@RequestBody DataLookupDTO dataLookupDTO) {
        log.debug("REST request to lookup data from Itex : {}", dataLookupDTO);

        GenericResponseDTO dataLookup = itexService.dataLookup(dataLookupDTO);
        return new ResponseEntity<>(dataLookup, dataLookup.getStatus());
    }

    @PostMapping("/itex/subscribe/data")
    public ResponseEntity<GenericResponseDTO> subscribeData(@RequestBody SubscribeDataDTO subscribeDataDTO, HttpSession session) {

        log.debug("REST request to subscribe data from Itex : {}", subscribeDataDTO);

        ResponseEntity<GenericResponseDTO> responseEntity = utility.checkTransactionPin(session, subscribeDataDTO.getTransactionPin());
        if (responseEntity.getStatusCode().isError()) {
            return responseEntity;
        }

        GenericResponseDTO purchaseData = itexService.subscribeData(subscribeDataDTO);
        return new ResponseEntity<>(purchaseData, purchaseData.getStatus());
    }

    @PostMapping("/itex/cabletv/bouquets")
    public ResponseEntity<GenericResponseDTO> retrieveBouquets(@RequestBody BouquetRequestDTO bouquetRequestDTO) {

        log.debug("REST request to retrieve bouquets from Itex : {}", bouquetRequestDTO);

        GenericResponseDTO requestBouquests = itexService.requestBouquets(bouquetRequestDTO);
        return new ResponseEntity<>(requestBouquests, requestBouquests.getStatus());
    }

    @PostMapping("/itex/multichoice/bouquets")
    public ResponseEntity<GenericResponseDTO> retrieveMultiChoiceBouquets(@RequestBody BouquetRequestDTO bouquetRequestDTO) {

        log.debug("REST request to retrieve multichoice bouquets from Itex : {}", bouquetRequestDTO);

        GenericResponseDTO requestBouquests = itexService.retrieveMultiChoiceBouquets(bouquetRequestDTO);

        return new ResponseEntity<>(requestBouquests, requestBouquests.getStatus());
    }

    @PostMapping("/itex/subscribe/cabletv")
    public ResponseEntity<GenericResponseDTO> subscribeCableTv(@RequestBody SubscribeCableTVDTO subscribeCableTVDTO, HttpSession session) {

        log.debug("REST request to subscribe cable tv from Itex : {}", subscribeCableTVDTO);

        ResponseEntity<GenericResponseDTO> responseEntity = utility.checkTransactionPin(session, subscribeCableTVDTO.getTransactionPin());
        if (responseEntity.getStatusCode().isError()) {
            return responseEntity;
        }

        GenericResponseDTO subscribeCableTv = itexService.subscribeCableTv(subscribeCableTVDTO);
        return new ResponseEntity<>(subscribeCableTv, subscribeCableTv.getStatus());
    }

    @PostMapping("/itex/subscribe/startimes")
    public ResponseEntity<GenericResponseDTO> subscribeStartimes(@RequestBody SubscribeStartimesDTO subscribeStartimesDTO, HttpSession session) {

        log.debug("REST request to subscribe startimes from Itex : {}", subscribeStartimesDTO);

        ResponseEntity<GenericResponseDTO> responseEntity = utility.checkTransactionPin(session, subscribeStartimesDTO.getTransactionPin());
        if (responseEntity.getStatusCode().isError()) {
            return responseEntity;
        }

        GenericResponseDTO subscribeStartimes = itexService.subscribeStartimes(subscribeStartimesDTO);
        return new ResponseEntity<>(subscribeStartimes, subscribeStartimes.getStatus());
    }

    @PostMapping("/itex/subscribe/multichoice")
    public ResponseEntity<GenericResponseDTO> subscribeMultiChoice(@RequestBody MultiChoiceSubscriptionRequestDTO subscribeStartimesDTO, HttpSession session) {

        log.debug("REST request to subscribe multichoice from Itex : {}", subscribeStartimesDTO);

        ResponseEntity<GenericResponseDTO> responseEntity = utility.checkTransactionPin(session, subscribeStartimesDTO.getTransactionPin());
        if (responseEntity.getStatusCode().isError()) {
            return responseEntity;
        }

        GenericResponseDTO subscribeStartimes = itexService.subscribeMultiChoice(subscribeStartimesDTO);
        return new ResponseEntity<>(subscribeStartimes, subscribeStartimes.getStatus());
    }

    @PostMapping("/itex/internet/validation")
    public ResponseEntity<GenericResponseDTO> validateInternet(@RequestBody InternetValidationDTO internetValidationDTO) {

        log.debug("REST request to validate internet from Itex : {}", internetValidationDTO);

        GenericResponseDTO internetValidation = itexService.validateInternet(internetValidationDTO);
        return new ResponseEntity<>(internetValidation, internetValidation.getStatus());
    }

    @PostMapping("/itex/internet/bundles")
    public ResponseEntity<GenericResponseDTO> getInternetBundles(@RequestBody InternetValidationDTO internetValidationDTO) {

        log.debug("REST request to validate internet from Itex : {}", internetValidationDTO);

        GenericResponseDTO getInternetBundles = itexService.getInternetBundles(internetValidationDTO);
        return new ResponseEntity<>(getInternetBundles, getInternetBundles.getStatus());
    }

    @PostMapping("/itex/internet/subscribe")
    public ResponseEntity<GenericResponseDTO> subscribeInternet(
        @RequestBody InternetSubscriptionDTO internetSubscriptionDTO, HttpSession session) {

        log.debug("REST request to subscribe internet from Itex : {}", internetSubscriptionDTO);

        ResponseEntity<GenericResponseDTO> responseEntity =
            utility.checkTransactionPin(session, internetSubscriptionDTO.getTransactionPin());
        if (responseEntity.getStatusCode().isError()) {
            return responseEntity;
        }

        GenericResponseDTO subscribeInternet = itexService.subscribeInternet(internetSubscriptionDTO);
        return new ResponseEntity<>(subscribeInternet, subscribeInternet.getStatus());
    }

    @GetMapping("/itex/billers")
    public ResponseEntity<GenericResponseDTO> getItexBillers() {

        log.debug("REST request to get Billers available on Itex : ");

        GenericResponseDTO responseDTO = itexService.getItexBillers();
        return new ResponseEntity<>(responseDTO, responseDTO.getStatus());
    }
}
