package ng.com.justjava.corebanking.service.impl;

import com.google.gson.Gson;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.kafka.producer.TransProducer;
import ng.com.justjava.corebanking.domain.User;
import ng.com.justjava.corebanking.domain.enumeration.SpecificChannel;
import ng.com.justjava.corebanking.service.ItexService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.TransactionLogService;
import ng.com.justjava.corebanking.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.justjava.corebanking.util.ItexConnectUtils;
import ng.com.justjava.corebanking.util.Utility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

import static java.lang.System.out;


@Service
@Transactional
public class ItexServiceImpl implements ItexService {

    @Value("${app.itex.channel}")
    private String channel;

    @Value("${app.itex.pin}")
    private String pin;

    @Value("${app.itex.validate-meter-url}")
    private String validateMeterUrl;

    @Value("${app.itex.search-criteria-url}")
    private String searchCriteriaUrl;

    @Value("${app.itex.cabletv-subscription-url}")
    private String cableTvSubscriptionUrl;

    @Value("${app.itex.cabletv-bouquets-url}")
    private String cabletvBouquetUrl;

    @Value("${app.itex.cabletv-validation-url}")
    private String cabletvValidationUrl;

    @Value("${app.itex.subscribe-data-url}")
    private String subscribeDataUrl;

    @Value("${app.itex.data-lookup-url}")
    private String dataLookupUrl;

    @Value("${app.itex.vtu-purchase-url}")
    private String vtuPurchaseUrl;

    @Value("${app.itex.internet-validation-url}")
    private String internetValidationUrl;

    @Value("${app.itex.internet-bundles-url}")
    private String internetBundlesUrl;

    @Value("${app.itex.internet-subscription-url}")
    private String internetSubscriptionUrl;

    @Value("${app.itex.electricity-payment-url}")
    private String electricityPaymentUrl;
    @Value("${app.charges.no-charge}")
    private Double noCharge;
    @Value("${app.constants.dfs.itex-payable-acct}")
    private String ITEX_PAYABLE_ACCT;

    @Value("${app.itex.mock-json.validate-meter}")
    private String validateMeterResponse;
    @Value("${app.itex.mock-json.purchase-electricity}")
    private String purchaseElectricityResponse;
    @Value("${app.itex.mock-json.vtu-purchase}")
    private String vtuPurchaseResponse;
    @Value("${app.itex.mock-json.data-lookup}")
    private String dataLookupResponse;
    @Value("${app.itex.mock-json.subscribe-data}")
    private String subscribeDataResponse;
    @Value("${app.itex.mock-json.validate-cable}")
    private String validateCableResponse;
    @Value("${app.itex.mock-json.cable-tv-bouquets}")
    private String bouquetResponse;
    @Value("${app.itex.mock-json.subscribe-cable-tv}")
    private String subscribeCableTvResponse;
    @Value("${app.itex.mock-json.validate-startimes}")
    private String validateStarTimesResponse;
    @Value("${app.itex.mock-json.startimes-subscription}")
    private String subscribeStarTimesResponse;
    @Value("${app.itex.mock-json.subscribe-multichoice}")
    private String subscribeMultichoiceResponse;
    @Value("${app.itex.mock-json.validate-internet}")
    private String validateInternetResponse;
    @Value("${app.itex.mock-json.internet-bundle}")
    private String getInternetBundleResponse;
    @Value("${app.itex.mock-json.subscribe-internet}")
    private String subscribeInternetResponse;

    private final ItexConnectUtils itexConnectUtils;
    private final Utility utility;
    private final WalletAccountService walletAccountService;
    private final TransProducer producer;
    private final ProfileService profileService;
    private final TransactionLogService transactionLogService;
    private final Environment environment;

    public ItexServiceImpl(ItexConnectUtils itexConnectUtils, Utility utility, WalletAccountService walletAccountService, TransProducer producer, ProfileService profileService, TransactionLogService transactionLogService, Environment environment) {
        this.itexConnectUtils = itexConnectUtils;
        this.utility = utility;
        this.walletAccountService = walletAccountService;
        this.producer = producer;
        this.profileService = profileService;
        this.transactionLogService = transactionLogService;
        this.environment = environment;
    }

    @Override
    public GenericResponseDTO authorize(String requestBody) {
        try {
            Optional<HttpHeaders> authorize = itexConnectUtils.authorize("");
            if (authorize.isPresent()) {
                return new GenericResponseDTO("00", HttpStatus.OK, "success", authorize);
            }
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed");
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred!");
        }
    }

    @Override
    public GenericResponseDTO validateMeter(MeterValidationDTO meterValidationDTO) {
        meterValidationDTO.setChannel(channel);
        int version = 1;
        if(meterValidationDTO.getService().equalsIgnoreCase("AEDC")){
            version = 2;
//            ItexSearchRequestDTO itexSearchRequestDTO = new ItexSearchRequestDTO();
//            GenericResponseDTO genericResponseDTO = itexConnectUtils.callItexApi(version, searchCriteriaUrl, itexSearchRequestDTO, HttpMethod.POST);
//            String genericResponseDTOData = (String) genericResponseDTO.getData();
//            ItexSearchResponseDTO itexSearchResponseDTO = new Gson().fromJson(genericResponseDTOData, ItexSearchResponseDTO.class);
//            ItexSearchResponseDataDTO itexSearchResponseDataDTO = itexSearchResponseDTO.getData();
//            List<ItexSearchResponseDetailsDTO> itexSearchResponseDetailsDTOS = itexSearchResponseDataDTO.getDetails();

            meterValidationDTO.setSearchCode("MY003");
        }

        if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
            return new GenericResponseDTO("00", HttpStatus.OK, "Successful", new Gson().fromJson(validateMeterResponse, ItexResponseDTO.class));
        }
        try {

            GenericResponseDTO genericResponseDTO = itexConnectUtils.callItexApi(version, validateMeterUrl, meterValidationDTO, HttpMethod.POST);

            if (!HttpStatus.OK.equals(genericResponseDTO.getStatus()) || genericResponseDTO.getData() == null) {
                return genericResponseDTO;
            }

            String genericResponseDTOData = (String) genericResponseDTO.getData();

            MeterValidationResponseDTOData data = new Gson().fromJson(genericResponseDTOData, MeterValidationResponseDTOData.class);

            return new GenericResponseDTO("00", HttpStatus.OK, "Successful", data);

//            return itexConnectUtils.callItexApi(1, validateMeterUrl, meterValidationDTO, HttpMethod.POST);
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Unable to verify meter at this time. Please try again later.", e);
        }
    }

    @Override
    public GenericResponseDTO purchaseElectricity(PurchaseElectricityDTO purchaseElectricityDTO) {
        User currentUser = utility.getCurrentUser();
        int version = 1;
        if(purchaseElectricityDTO.getService().equalsIgnoreCase("AEDC")){
            version = 2;
        }
        if (currentUser == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid current user");
        }

        String sourceAccountNumber = purchaseElectricityDTO.getSourceAccountNumber();
        GenericResponseDTO response = utility.checkSufficientFunds(purchaseElectricityDTO.getAmount(),
            purchaseElectricityDTO.getBonusAmount(), sourceAccountNumber, 50.0);
        if (!HttpStatus.OK.equals(response.getStatus())) return response;

        utility.isValidTransaction("walletToWallet", purchaseElectricityDTO.getSourceAccountNumber(), ITEX_PAYABLE_ACCT, purchaseElectricityDTO.getAmount(), purchaseElectricityDTO.getBonusAmount(), false);

        purchaseElectricityDTO.setPin(pin);
        String uniqueTransRef = utility.getUniqueTransRef();
        purchaseElectricityDTO.setClientReference(uniqueTransRef);
        purchaseElectricityDTO.setPaymentMethod("cash");
        Double amount;
        String token;
        String reference;
        try {
            GenericResponseDTO genericResponseDTO;
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                genericResponseDTO = new GenericResponseDTO("00", HttpStatus.OK, "Electricity Purchase Successful", new Gson().fromJson(purchaseElectricityResponse, PurchaseElectricityResponseData.class));
                amount = 100.00;
                token = "3538  1451  7195  0265  1802";
                reference = "ITEX-KEDCO5F3175F2DFEB501E2XDR199605";
            } else {
                genericResponseDTO = itexConnectUtils.callItexApi(version, electricityPaymentUrl, purchaseElectricityDTO, HttpMethod.POST);
                if (!HttpStatus.OK.equals(genericResponseDTO.getStatus()) || genericResponseDTO.getData() == null) {
                    return genericResponseDTO;
                }

                String genericResponseDTOData = (String) genericResponseDTO.getData();
                out.println("Purchase electricity");

                PurchaseElectricityResponseDataData purchaseElectricityResponseDataData  = new Gson().fromJson(genericResponseDTOData, PurchaseElectricityResponseDataData.class);

                String tokenList = purchaseElectricityResponseDataData.getTokenList().replace("\\", "");

                PurchaseElectricityTokenListResponseData purchaseElectricityTokenListResponseData = new Gson().fromJson(tokenList, PurchaseElectricityTokenListResponseData.class);

                PurchaseElectricityResponseData data = itexConnectUtils.toPurchaseElectricityResponseData(purchaseElectricityResponseDataData);
                data.setTokenList(purchaseElectricityTokenListResponseData);
                genericResponseDTO.setData(data);

//                PurchaseElectricityResponseData data = new Gson().fromJson(genericResponseDTOData, PurchaseElectricityResponseData.class);

                amount = data.getAmount();
                token = data.getToken();
                reference = data.getReference();
            }

            String login = currentUser.getLogin();

            String specificChannel = purchaseElectricityDTO.getService().trim().toUpperCase() + " Disco";

            Optional<FundDTO> fundDTOOptional = utility.buildFundDTO(purchaseElectricityDTO.getSourceAccountNumber(), ITEX_PAYABLE_ACCT, amount - purchaseElectricityDTO.getBonusAmount(), login, specificChannel, "walletToWallet", uniqueTransRef);

            if (fundDTOOptional.isPresent()) {

                FundDTO fundDTO = fundDTOOptional.get();
                if (purchaseElectricityDTO.getCharges() > 0.0){
                    fundDTO.setCharges(purchaseElectricityDTO.getCharges());
                }
                fundDTO.setNarration(purchaseElectricityDTO.getNarration());
                fundDTO.setRedeemBonus(purchaseElectricityDTO.isRedeemBonus());
                fundDTO.setBonusAmount(purchaseElectricityDTO.getBonusAmount());
                fundDTO.setShortComment("Token: " + token);
                fundDTO.setRrr(reference);

                out.println("Funddto {} ====> " + fundDTO);

                fundDTO = transactionLogService.save(fundDTO);

                out.println("Funddto {} ====> " + fundDTO);

                producer.send(fundDTO);

                return genericResponseDTO;

            } else {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", "Invalid source account");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred!");
        }
    }

    @Override
    public GenericResponseDTO purchaseVTU(PurchaseVTU purchaseVTU) {

        User currentUser = utility.getCurrentUser();

        if (currentUser == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid current user");
        }

        String sourceAccountNumber = purchaseVTU.getSourceAccountNumber();
        GenericResponseDTO response = utility.checkSufficientFunds(Double.valueOf(purchaseVTU.getAmount()), purchaseVTU.getBonusAmount(), sourceAccountNumber, 0.0);

        out.println("Balance check response ===> " + response);
        if (!HttpStatus.OK.equals(response.getStatus())) return response;

        utility.isValidTransaction("walletToWallet", purchaseVTU.getSourceAccountNumber(), ITEX_PAYABLE_ACCT, Double.valueOf(purchaseVTU.getAmount()), purchaseVTU.getBonusAmount(), false);

        purchaseVTU.setPaymentMethod("cash");
        purchaseVTU.setPin(pin);
        purchaseVTU.setChannel(channel);
        String uniqueTransRef = utility.getUniqueTransRef();
        purchaseVTU.setClientReference(uniqueTransRef);

        Double amount;
        String reference;
        try {
            GenericResponseDTO genericResponseDTO;
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                genericResponseDTO = new GenericResponseDTO("00", HttpStatus.OK, "VTU Purchase Successful", new Gson().fromJson(vtuPurchaseResponse, PurchaseVTUResponseData.class));
                amount = 100.00;
                reference = "ITEX-TAMSVTU5F318D8131B64FYJEUB1361261";
            } else {
                genericResponseDTO = itexConnectUtils.callItexApi(1, vtuPurchaseUrl, purchaseVTU, HttpMethod.POST);
                if (!HttpStatus.OK.equals(genericResponseDTO.getStatus()) || genericResponseDTO.getData() == null) {
                    return genericResponseDTO;
                }

//            PurchaseElectricityResponseData data = (PurchaseElectricityResponseData) genericResponseDTO.getData();
                String genericResponseDTOData = (String) genericResponseDTO.getData();
                PurchaseVTUResponseData data = new Gson().fromJson(genericResponseDTOData, PurchaseVTUResponseData.class);

//            PurchaseVTUResponseData data = new ObjectMapper().readValue(genericResponseDTOData, PurchaseVTUResponseData.class);

                amount = Double.valueOf(data.getAmount());
                reference = data.getReference();
            }


            String login = currentUser.getLogin();

            String specificChannel = purchaseVTU.getService().trim().toUpperCase();

            Optional<FundDTO> fundDTOOptional = utility.buildFundDTO(purchaseVTU.getSourceAccountNumber(), ITEX_PAYABLE_ACCT, amount - purchaseVTU.getBonusAmount(), login, specificChannel, "walletToWallet", uniqueTransRef);

            if (fundDTOOptional.isPresent()) {
                FundDTO fundDTO = fundDTOOptional.get();
                fundDTO.setCharges(purchaseVTU.getCharges());
                fundDTO.setNarration(purchaseVTU.getNarration());
                fundDTO.setRedeemBonus(purchaseVTU.isRedeemBonus());
                fundDTO.setBonusAmount(purchaseVTU.getBonusAmount());
                fundDTO.setRrr(reference);

                out.println("Funddto {} ====> " + fundDTO);
                fundDTO = transactionLogService.save(fundDTO);

                out.println("Funddto {} ====> " + fundDTO);

                producer.send(fundDTO);

                return genericResponseDTO;

            } else {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", "Invalid source account");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred!", e.getStackTrace());
        }
    }

    @Override
    public GenericResponseDTO dataLookup(DataLookupDTO dataLookupDTO) {

        dataLookupDTO.setChannel(channel);
        try {
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                return new GenericResponseDTO("00", HttpStatus.OK, "Successful", new Gson().fromJson(dataLookupResponse, LookUpDataResponseData.class));
            }

            GenericResponseDTO genericResponseDTO = itexConnectUtils.callItexApi(1, dataLookupUrl, dataLookupDTO, HttpMethod.POST);
            if (!HttpStatus.OK.equals(genericResponseDTO.getStatus()) || genericResponseDTO.getData() == null) {
                return genericResponseDTO;
            }

            String genericResponseDTOData = (String) genericResponseDTO.getData();

            LookUpDataResponseData data = new Gson().fromJson(genericResponseDTOData, LookUpDataResponseData.class);

            return new GenericResponseDTO("00", HttpStatus.OK, "Successful", data);

        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred!");
        }
    }

    @Override
    public GenericResponseDTO subscribeData(SubscribeDataDTO subscribeDataDTO) {

        User currentUser = utility.getCurrentUser();

        if (currentUser == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid current user");
        }

        String sourceAccountNumber = subscribeDataDTO.getSourceAccountNumber();
        GenericResponseDTO response = utility.checkSufficientFunds(subscribeDataDTO.getAmount(), subscribeDataDTO.getBonusAmount(), sourceAccountNumber, 0.0);
        if (!HttpStatus.OK.equals(response.getStatus())) return response;

        utility.isValidTransaction("walletToWallet", subscribeDataDTO.getSourceAccountNumber(), ITEX_PAYABLE_ACCT, subscribeDataDTO.getAmount(), subscribeDataDTO.getBonusAmount(), false);

        subscribeDataDTO.setPin(pin);
        subscribeDataDTO.setPaymentMethod("cash");
        String uniqueTransRef = utility.getUniqueTransRef();
        subscribeDataDTO.setClientReference(uniqueTransRef);

        Double amount;
        String reference;
        try {
            GenericResponseDTO genericResponseDTO;
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                genericResponseDTO = new GenericResponseDTO("00", HttpStatus.OK, "Subscribe Data Successful", new Gson().fromJson(subscribeDataResponse, SubscribeDataResponse.class));
                amount = 100.00;
                reference = "ITEX-TAMSVTU5F319192E71BAUSE0SGFX55179";
            } else {
                genericResponseDTO = itexConnectUtils.callItexApi(1, subscribeDataUrl, subscribeDataDTO, HttpMethod.POST);
                if (!HttpStatus.OK.equals(genericResponseDTO.getStatus()) || genericResponseDTO.getData() == null) {
                    return genericResponseDTO;
                }

//            PurchaseElectricityResponseData data = (PurchaseElectricityResponseData) genericResponseDTO.getData();
                String genericResponseDTOData = (String) genericResponseDTO.getData();

                SubscribeDataResponse data = new Gson().fromJson(genericResponseDTOData, SubscribeDataResponse.class);

//            SubscribeDataResponse data = new ObjectMapper().readValue(genericResponseDTOData, SubscribeDataResponse.class);

                amount = Double.valueOf(data.getAmount());
                reference = data.getReference();
            }


String login = currentUser.getLogin();

            String specificChannel = subscribeDataDTO.getService().trim().toUpperCase();

            Optional<FundDTO> fundDTOOptional = utility.buildFundDTO(subscribeDataDTO.getSourceAccountNumber(), ITEX_PAYABLE_ACCT, amount - subscribeDataDTO.getBonusAmount(), login, specificChannel, "walletToWallet", uniqueTransRef);

            if (fundDTOOptional.isPresent()) {
                FundDTO fundDTO = fundDTOOptional.get();
                fundDTO.setCharges(subscribeDataDTO.getCharges());
                fundDTO.setNarration(subscribeDataDTO.getNarration());
                fundDTO.setRedeemBonus(subscribeDataDTO.isRedeemBonus());
                fundDTO.setBonusAmount(subscribeDataDTO.getBonusAmount());
                fundDTO.setRrr(reference);

                out.println("Funddto {} ====> " + fundDTO);

                fundDTO = transactionLogService.save(fundDTO);

                out.println("Funddto {} ====> " + fundDTO);

                producer.send(fundDTO);

                return genericResponseDTO;

            } else {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", "Invalid source account");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occured!", e.getStackTrace());
        }

    }

    @Override
    public GenericResponseDTO validateCableTv(ValidateCableTvDTO validateCableTvDTO) {
        validateCableTvDTO.setChannel(channel);
        validateCableTvDTO.setService("multichoice");
        try {
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                return new GenericResponseDTO("00", HttpStatus.OK, "Validate Cable Successful", new Gson().fromJson(validateCableResponse, ValidateCableTvResponseData.class));
            } else {

                GenericResponseDTO genericResponseDTO = itexConnectUtils.callItexApi(1, cabletvValidationUrl, validateCableTvDTO, HttpMethod.POST);

                if (!HttpStatus.OK.equals(genericResponseDTO.getStatus()) || genericResponseDTO.getData() == null) {
                    return genericResponseDTO;
                }

                String genericResponseDTOData = (String) genericResponseDTO.getData();

                ValidateMultichoiceResponseDataDTO data = new Gson().fromJson(genericResponseDTOData, ValidateMultichoiceResponseDataDTO.class);

                return new GenericResponseDTO("00", HttpStatus.OK, "Successful", data);

//                return itexConnectUtils.callItexApi(1, cabletvValidationUrl, validateCableTvDTO, HttpMethod.POST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occured", e.getStackTrace());
        }
    }

    @Override
    public GenericResponseDTO validateMultichoice(ValidateCableTvDTO validateCableTvDTO) {

        validateCableTvDTO.setChannel(channel);
        validateCableTvDTO.setService("multichoice");
        try {
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                return new GenericResponseDTO("00", HttpStatus.OK, "Successful", new Gson().fromJson(validateCableResponse, ValidateCableTvResponseData.class));
            } else {

                GenericResponseDTO genericResponseDTO = itexConnectUtils.callItexApi(1, cabletvValidationUrl, validateCableTvDTO, HttpMethod.POST);

                if (!HttpStatus.OK.equals(genericResponseDTO.getStatus()) || genericResponseDTO.getData() == null) {
                    return genericResponseDTO;
                }

                String genericResponseDTOData = (String) genericResponseDTO.getData();

                ValidateMultichoiceResponseDataDTO data = new Gson().fromJson(genericResponseDTOData, ValidateMultichoiceResponseDataDTO.class);

                return new GenericResponseDTO("00", HttpStatus.OK, "Successful", data);

//                return itexConnectUtils.callItexApi(1, cabletvValidationUrl, validateCableTvDTO, HttpMethod.POST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred!", e.getLocalizedMessage());
        }
    }

    @Override
    public GenericResponseDTO getItexBillers() {
        AccountTypeDto prepaid = new AccountTypeDto("prepaid");
        AccountTypeDto postpaid = new AccountTypeDto("postpaid");
        ArrayList<AccountTypeDto> accountTypeDtos = new ArrayList<>();
        accountTypeDtos.add(prepaid);
        accountTypeDtos.add(postpaid);
        ItexBiller aedc = new ItexBiller("Abuja Electricity Distribution Company (AEDC)", "aedc", accountTypeDtos);
        ItexBiller eedc = new ItexBiller("Enugu Electricity Distribution Company (EEDC)", "eedc", accountTypeDtos);
        ItexBiller ekedc = new ItexBiller("Eko Electricity Distribution Company (EKEDC)", "ekedc", accountTypeDtos);
        ItexBiller ibedc = new ItexBiller("Ibadan Electricity Distribution Company (IBEDC)", "ibedc", accountTypeDtos);
        ItexBiller ikedc = new ItexBiller("Ikeja Electricity Distribution Company (IKEDC)", "ikedc", accountTypeDtos);
        ItexBiller phedc = new ItexBiller("Port Harcourt Electricity Distribution Company (PHEDC)", "phedc", accountTypeDtos);
        ItexBiller kedco = new ItexBiller("Kano Electricity Distribution Company(KEDCO)", "kedco",
            accountTypeDtos);

        ArrayList<ItexBiller> discos = new ArrayList<>();
        discos.add(aedc);
        discos.add(eedc);
        discos.add(ekedc);
        discos.add(ibedc);
        discos.add(ikedc);
        discos.add(phedc);
        discos.add(kedco);
        ItexBillersDto electricity = new ItexBillersDto("Electricity", discos);

        ItexBiller mtnVtu = new ItexBiller("MTN", "mtnvtu", null);
        ItexBiller nineMobileVtu = new ItexBiller("9 Mobile", "9mobilevtu", null);
        ItexBiller gloVtu = new ItexBiller("GLO", "glovtu", null);
        ItexBiller airtelVtu = new ItexBiller("Airtel", "airtelvtu", null);


        ArrayList<ItexBiller> telcos = new ArrayList<>();
        telcos.add(mtnVtu);
        telcos.add(gloVtu);
        telcos.add(nineMobileVtu);
        telcos.add(airtelVtu);

        ItexBillersDto airtime = new ItexBillersDto("Airtime", telcos);
        ItexBillersDto data = new ItexBillersDto("Data", telcos);


        AccountTypeDto dstvType = new AccountTypeDto("DSTV");
        ArrayList<AccountTypeDto> dstvTypes = new ArrayList<>();
        dstvTypes.add(dstvType);
        AccountTypeDto gotvType = new AccountTypeDto("GOTV");
        ArrayList<AccountTypeDto> gotvTypes = new ArrayList<>();
        gotvTypes.add(gotvType);
        ItexBiller dstv = new ItexBiller("DSTV", "multichoice", dstvTypes);
        ItexBiller gotv = new ItexBiller("GOTV", "multichoice", gotvTypes);

        AccountTypeDto startTimesType = new AccountTypeDto("Startimes");
        ArrayList<AccountTypeDto> startTimesTypes = new ArrayList<>();
        startTimesTypes.add(startTimesType);
        ItexBiller startTimes = new ItexBiller("Startimes", "startimes", startTimesTypes);


        ArrayList<ItexBiller> cableTvList = new ArrayList<>();
        cableTvList.add(dstv);
        cableTvList.add(gotv);
        cableTvList.add(startTimes);

        ItexBillersDto cableTv = new ItexBillersDto("CableTv", cableTvList);

        AccountTypeDto smileType = new AccountTypeDto("account");
        ArrayList<AccountTypeDto> smiles = new ArrayList<>();
        smiles.add(smileType);
        ItexBiller smile = new ItexBiller("Smile", "smile", smiles);

        ArrayList<ItexBiller> internetList = new ArrayList<>();
        internetList.add(smile);

        ItexBillersDto internet = new ItexBillersDto("Internet", internetList);

        ArrayList<ItexBillersDto> itexBillersDtos = new ArrayList<>();
        itexBillersDtos.add(airtime);
        itexBillersDtos.add(data);
        itexBillersDtos.add(electricity);
        itexBillersDtos.add(cableTv);
        itexBillersDtos.add(internet);

        return new GenericResponseDTO("00", HttpStatus.OK, "success", itexBillersDtos);
    }

    @Override
    public GenericResponseDTO requestBouquets(BouquetRequestDTO bouquetRequestDTO) {
        try {
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                return new GenericResponseDTO("00", HttpStatus.OK, "Successful", new Gson().fromJson(bouquetResponse, ItexResponseDTO.class).getData());
            } else {

                GenericResponseDTO genericResponseDTO = itexConnectUtils.callItexApi(1, cabletvBouquetUrl, bouquetRequestDTO, HttpMethod.POST);
                if (!HttpStatus.OK.equals(genericResponseDTO.getStatus()) || genericResponseDTO.getData() == null) {
                    return genericResponseDTO;
                }

                String genericResponseDTOData = (String) genericResponseDTO.getData();

                MultiChoiceBouquetsResponseDTO data = new Gson().fromJson(genericResponseDTOData, MultiChoiceBouquetsResponseDTO.class);

                return new GenericResponseDTO("00", HttpStatus.OK, "Successful", data);

//                return itexConnectUtils.callItexApi(1, cabletvBouquetUrl, bouquetRequestDTO, HttpMethod.POST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred!");
        }
    }

    @Override
    public GenericResponseDTO retrieveMultiChoiceBouquets(BouquetRequestDTO bouquetRequestDTO) {
        bouquetRequestDTO.setService("multichoice");
        try {
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                return new GenericResponseDTO("00", HttpStatus.OK, "Successful", new Gson().fromJson(bouquetResponse, ItexResponseDTO.class).getData());
            } else {

                GenericResponseDTO genericResponseDTO = itexConnectUtils.callItexApi(1, cabletvBouquetUrl, bouquetRequestDTO, HttpMethod.POST);
                if (!HttpStatus.OK.equals(genericResponseDTO.getStatus()) || genericResponseDTO.getData() == null) {
                    return genericResponseDTO;
                }

                String genericResponseDTOData = (String) genericResponseDTO.getData();

                MultiChoiceBouquetsResponseDTO data = new Gson().fromJson(genericResponseDTOData, MultiChoiceBouquetsResponseDTO.class);

                return new GenericResponseDTO("00", HttpStatus.OK, "Successful", data);

//                return itexConnectUtils.callItexApi(1, cabletvBouquetUrl, bouquetRequestDTO, HttpMethod.POST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred!");
        }
    }

    @Override
    public GenericResponseDTO subscribeCableTv(SubscribeCableTVDTO subscribeCableTVDTO) {

        User currentUser = utility.getCurrentUser();

        if (currentUser == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid current user");
        }

        String sourceAccountNumber = subscribeCableTVDTO.getSourceAccountNumber();
        GenericResponseDTO response = utility.checkSufficientFunds(subscribeCableTVDTO.getAmount(),
            subscribeCableTVDTO.getBonusAmount(), sourceAccountNumber, 50.0);
        if (!HttpStatus.OK.equals(response.getStatus())) return response;

        utility.isValidTransaction("walletToWallet", subscribeCableTVDTO.getSourceAccountNumber(), ITEX_PAYABLE_ACCT, subscribeCableTVDTO.getAmount(), subscribeCableTVDTO.getBonusAmount(), false);

        subscribeCableTVDTO.setPin(pin);
        subscribeCableTVDTO.setPaymentMethod("cash");
        subscribeCableTVDTO.setService("multichoice");
        String uniqueTransRef = utility.getUniqueTransRef();
        subscribeCableTVDTO.setClientReference(uniqueTransRef);
        Double amount;
        String reference;
        try {
            GenericResponseDTO genericResponseDTO;
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                genericResponseDTO = new GenericResponseDTO("00", HttpStatus.OK, "Cable TV Subscription Successful", new Gson().fromJson(subscribeCableTvResponse, ItexResponseDTO.class).getData());
                amount = 100.00;
                reference = "ITEX-MULTICHOICE5F31AA451E404QYPD6VKO56712|387851860";
            } else {
                genericResponseDTO = itexConnectUtils.callItexApi(1, cableTvSubscriptionUrl, subscribeCableTVDTO, HttpMethod.POST);
                if (!HttpStatus.OK.equals(genericResponseDTO.getStatus()) || genericResponseDTO.getData() == null) {
                    return genericResponseDTO;
                }

//            PurchaseElectricityResponseData data = (PurchaseElectricityResponseData) genericResponseDTO.getData();
                String genericResponseDTOData = (String) genericResponseDTO.getData();
//            PurchaseCableTVResponseData data = new ObjectMapper().readValue(genericResponseDTOData, PurchaseCableTVResponseData.class);
                PurchaseCableTVResponseData data = new Gson().fromJson(genericResponseDTOData, PurchaseCableTVResponseData.class);

                amount = Double.valueOf(data.getAmount());
                reference = data.getReference();
            }


            String login = currentUser.getLogin();

            Optional<FundDTO> fundDTOOptional = utility.buildFundDTO(subscribeCableTVDTO.getSourceAccountNumber(), ITEX_PAYABLE_ACCT, amount - subscribeCableTVDTO.getBonusAmount(), login, SpecificChannel.PAY_CABLE_TV_ITEX.getName(), "walletToWallet", uniqueTransRef);

            if (fundDTOOptional.isPresent()) {
                FundDTO fundDTO = fundDTOOptional.get();
                if (subscribeCableTVDTO.getCharges() > 0.0){
                    fundDTO.setCharges(subscribeCableTVDTO.getCharges());
                }
                fundDTO.setNarration(subscribeCableTVDTO.getNarration());
                fundDTO.setRedeemBonus(subscribeCableTVDTO.isRedeemBonus());
                fundDTO.setBonusAmount(subscribeCableTVDTO.getBonusAmount());
                fundDTO.setRrr(reference);

                out.println("Funddto {} ====> " + fundDTO);

                fundDTO = transactionLogService.save(fundDTO);

                out.println("Funddto {} ====> " + fundDTO);

                producer.send(fundDTO);

                return genericResponseDTO;

            } else {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", "Invalid source account");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred!");
        }

    }

    @Override
    public GenericResponseDTO validateStartimes(ValidateCableTvDTO validateCableTvDTO) {
        validateCableTvDTO.setService("startimes");
        validateCableTvDTO.setChannel(channel);
        validateCableTvDTO.setType("default");

        try {
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                return new GenericResponseDTO("00", HttpStatus.OK, "Validate StarTimes Successful", new Gson().fromJson(validateStarTimesResponse, ItexResponseDTO.class).getData());
            } else {

                GenericResponseDTO genericResponseDTO = itexConnectUtils.callItexApi(1, cabletvValidationUrl, validateCableTvDTO, HttpMethod.POST);

                if (!HttpStatus.OK.equals(genericResponseDTO.getStatus()) || genericResponseDTO.getData() == null) {
                    return genericResponseDTO;
                }

                String genericResponseDTOData = (String) genericResponseDTO.getData();

                StartTimesValidationResponseDTO data = new Gson().fromJson(genericResponseDTOData, StartTimesValidationResponseDTO.class);

                return new GenericResponseDTO("00", HttpStatus.OK, "Successful", data);

//                return itexConnectUtils.callItexApi(1, cabletvValidationUrl, validateCableTvDTO, HttpMethod.POST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred!");
        }
    }

    @Override
    public GenericResponseDTO subscribeStartimes(SubscribeStartimesDTO subscribeStartimesDTO) {

        User currentUser = utility.getCurrentUser();

        if (currentUser == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid current user");
        }

        String sourceAccountNumber = subscribeStartimesDTO.getSourceAccountNumber();
        GenericResponseDTO response = utility.checkSufficientFunds(Double.valueOf(subscribeStartimesDTO.getAmount()),
            subscribeStartimesDTO.getBonusAmount(), sourceAccountNumber, 50.0);
        if (!HttpStatus.OK.equals(response.getStatus()) || response.getData() == null) return response;

        utility.isValidTransaction("walletToWallet", subscribeStartimesDTO.getSourceAccountNumber(), ITEX_PAYABLE_ACCT, subscribeStartimesDTO.getAmount(), subscribeStartimesDTO.getBonusAmount(), false);

        subscribeStartimesDTO.setPaymentMethod("cash");
        subscribeStartimesDTO.setPin(pin);
        subscribeStartimesDTO.setService("startimes");
        String uniqueTransRef = utility.getUniqueTransRef();
        subscribeStartimesDTO.setClientReference(uniqueTransRef);

        Double amount;
        String reference;
        try {
            GenericResponseDTO genericResponseDTO;
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                genericResponseDTO = new GenericResponseDTO("00", HttpStatus.OK, "StarTimes Subscription Successful", new Gson().fromJson(subscribeStarTimesResponse, ItexResponseDTO.class).getData());
                amount = 100.00;
                reference = "ITEX-STARTIMES5F31AD02582F0X5JO93BH31484";
            } else {
                genericResponseDTO = itexConnectUtils.callItexApi(1, cableTvSubscriptionUrl, subscribeStartimesDTO, HttpMethod.POST);
                if (!HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                    return genericResponseDTO;
                }

//            PurchaseElectricityResponseData data = (PurchaseElectricityResponseData) genericResponseDTO.getData();
                String genericResponseDTOData = (String) genericResponseDTO.getData();
//            SubscribeStartimesResponseData data = new ObjectMapper().readValue(genericResponseDTOData, SubscribeStartimesResponseData.class);
                SubscribeStartimesResponseData data = new Gson().fromJson(genericResponseDTOData, SubscribeStartimesResponseData.class);

                amount = Double.valueOf(data.getAmount());
                reference = data.getReference();
            }


            String login = currentUser.getLogin();

            Optional<FundDTO> fundDTOOptional = utility.buildFundDTO(subscribeStartimesDTO.getSourceAccountNumber(), ITEX_PAYABLE_ACCT, amount - subscribeStartimesDTO.getBonusAmount(), login, SpecificChannel.STARTIMES_TV.getName(), "walletToWallet", uniqueTransRef);

            if (fundDTOOptional.isPresent()) {
                FundDTO fundDTO = fundDTOOptional.get();
                if (subscribeStartimesDTO.getCharges() > 0.0){
                    fundDTO.setCharges(subscribeStartimesDTO.getCharges());
                }
                fundDTO.setNarration(subscribeStartimesDTO.getNarration());
                fundDTO.setRedeemBonus(subscribeStartimesDTO.isRedeemBonus());
                fundDTO.setBonusAmount(subscribeStartimesDTO.getBonusAmount());
                fundDTO.setRrr(reference);

                out.println("Funddto {} ====> " + fundDTO);

                fundDTO = transactionLogService.save(fundDTO);

                out.println("Funddto {} ====> " + fundDTO);

                producer.send(fundDTO);

            }

            return genericResponseDTO;

        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred!");
        }

    }

    @Override
    public GenericResponseDTO subscribeMultiChoice(MultiChoiceSubscriptionRequestDTO multiChoiceSubscriptionRequestDTO) {

        User currentUser = utility.getCurrentUser();

        if (currentUser == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid current user");
        }

        if (utility.checkStringIsNotValid(multiChoiceSubscriptionRequestDTO.getTotalAmount())) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid amount");
        }

        String sourceAccountNumber = multiChoiceSubscriptionRequestDTO.getSourceAccountNumber();
        GenericResponseDTO response =
            utility.checkSufficientFunds(Double.valueOf(multiChoiceSubscriptionRequestDTO.getTotalAmount()),
                multiChoiceSubscriptionRequestDTO.getBonusAmount(), sourceAccountNumber, 50.0);
        if (!HttpStatus.OK.equals(response.getStatus()) || response.getData() == null) return response;

        utility.isValidTransaction("walletToWallet", multiChoiceSubscriptionRequestDTO.getSourceAccountNumber(), ITEX_PAYABLE_ACCT, Double.valueOf(multiChoiceSubscriptionRequestDTO.getTotalAmount()), multiChoiceSubscriptionRequestDTO.getBonusAmount(), false);

        String uniqueTransRef = utility.getUniqueTransRef();

        multiChoiceSubscriptionRequestDTO.setPaymentMethod("cash");
        multiChoiceSubscriptionRequestDTO.setPin(pin);
        multiChoiceSubscriptionRequestDTO.setChannel(channel);
        multiChoiceSubscriptionRequestDTO.setService("multichoice");
        multiChoiceSubscriptionRequestDTO.setClientReference(uniqueTransRef);

        Double amount;
        String reference;
        try {
            GenericResponseDTO genericResponseDTO;
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                genericResponseDTO = new GenericResponseDTO("00", HttpStatus.OK, "MultiChoice Subscription Successful", new Gson().fromJson(subscribeMultichoiceResponse, MultiChoiceSubscriptionResponseDataDTO.class));
                out.println("Response data for multichoice staging===> " + genericResponseDTO.getData());
                amount = 100.00;
                reference = "ITEX-MULTICHOICE5F31AA451E404QYPD6VKO56712|387851860";
            } else {
                genericResponseDTO = itexConnectUtils.callItexApi(1, cableTvSubscriptionUrl, multiChoiceSubscriptionRequestDTO, HttpMethod.POST);
                if (!HttpStatus.OK.equals(genericResponseDTO.getStatus())) {
                    return genericResponseDTO;
                }
                String genericResponseDTOData = (String) genericResponseDTO.getData();
                out.println("Response data for multichoice ===> " + genericResponseDTOData);
                MultiChoiceSubscriptionResponseDataDTO data =
                    new Gson().fromJson(genericResponseDTOData, MultiChoiceSubscriptionResponseDataDTO.class);
                amount = Double.valueOf(data.getAmount());
                reference = data.getReference();
            }

            String login = currentUser.getLogin();

            Optional<FundDTO> fundDTOOptional = utility.buildFundDTO(
                multiChoiceSubscriptionRequestDTO.getSourceAccountNumber(),
                ITEX_PAYABLE_ACCT,
                amount - multiChoiceSubscriptionRequestDTO.getBonusAmount(),
                login, SpecificChannel.STARTIMES_TV.getName(),
                "walletToWallet",
                uniqueTransRef);

            if (fundDTOOptional.isPresent()) {
                FundDTO fundDTO = fundDTOOptional.get();
                if (multiChoiceSubscriptionRequestDTO.getCharges() > 0.0){
                    fundDTO.setCharges(multiChoiceSubscriptionRequestDTO.getCharges());
                }
                fundDTO.setNarration(multiChoiceSubscriptionRequestDTO.getNarration());
                fundDTO.setRedeemBonus(multiChoiceSubscriptionRequestDTO.isRedeemBonus());
                fundDTO.setBonusAmount(multiChoiceSubscriptionRequestDTO.getBonusAmount());
                fundDTO.setRrr(reference);

                out.println("Funddto {} ====> " + fundDTO);

                fundDTO = transactionLogService.save(fundDTO);
                out.println("Funddto {} ====> " + fundDTO);

                producer.send(fundDTO);
            }
            return genericResponseDTO;
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occured!");
        }
    }

    @Override
    public GenericResponseDTO validateInternet(InternetValidationDTO internetValidationDTO) {

        internetValidationDTO.setChannel(channel);
        internetValidationDTO.setService("smile");
        internetValidationDTO.setType("account");
        try {
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                return new GenericResponseDTO("00", HttpStatus.OK, "Validate Internet Successful", new Gson().fromJson(validateInternetResponse, ItexResponseDTO.class).getData());
            } else {

                GenericResponseDTO genericResponseDTO = itexConnectUtils.callItexApi(1, internetValidationUrl, internetValidationDTO, HttpMethod.POST);

                if (!HttpStatus.OK.equals(genericResponseDTO.getStatus()) || genericResponseDTO.getData() == null) {
                    return genericResponseDTO;
                }

                String genericResponseDTOData = (String) genericResponseDTO.getData();

                InternetValidationResponseDTO data = new Gson().fromJson(genericResponseDTOData, InternetValidationResponseDTO.class);

                return new GenericResponseDTO("00", HttpStatus.OK, "Successful", data);

//                return itexConnectUtils.callItexApi(1, internetValidationUrl, internetValidationDTO, HttpMethod.POST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred!");
        }
    }

    @Override
    public GenericResponseDTO getInternetBundles(InternetValidationDTO internetValidationDTO) {

        internetValidationDTO.setType("account");
        internetValidationDTO.setChannel(channel);
        internetValidationDTO.setService("smile");
        try {
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                return new GenericResponseDTO("00", HttpStatus.OK, "Internet Bundle Successful", new Gson().fromJson(getInternetBundleResponse, ItexResponseDTO.class).getData());
            } else {

                GenericResponseDTO genericResponseDTO = itexConnectUtils.callItexApi(1, internetBundlesUrl, internetValidationDTO, HttpMethod.POST);

                if (!HttpStatus.OK.equals(genericResponseDTO.getStatus()) || genericResponseDTO.getData() == null) {
                    return genericResponseDTO;
                }

                String genericResponseDTOData = (String) genericResponseDTO.getData();

                InternetValidationBundleResponseDTO data = new Gson().fromJson(genericResponseDTOData, InternetValidationBundleResponseDTO.class);

                return new GenericResponseDTO("00", HttpStatus.OK, "Successful", data);

//                return itexConnectUtils.callItexApi(1, internetBundlesUrl, internetValidationDTO, HttpMethod.POST);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred!");
        }
    }

    @Override
    public GenericResponseDTO subscribeInternet(InternetSubscriptionDTO internetSubscriptionDTO) {

        User currentUser = utility.getCurrentUser();

        if (currentUser == null) {
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid current user");
        }

        String sourceAccountNumber = internetSubscriptionDTO.getSourceAccountNumber();
        GenericResponseDTO response =
            utility.checkSufficientFunds(Double.valueOf(internetSubscriptionDTO.getAmount()),
                internetSubscriptionDTO.getBonusAmount(), sourceAccountNumber, 50.0);
        if (!HttpStatus.OK.equals(response.getStatus())) return response;

        utility.isValidTransaction("walletTowallet", internetSubscriptionDTO.getSourceAccountNumber(), ITEX_PAYABLE_ACCT, Double.valueOf(internetSubscriptionDTO.getAmount()), internetSubscriptionDTO.getBonusAmount(), false);

        internetSubscriptionDTO.setService("smile");
        internetSubscriptionDTO.setType("subscription");
        internetSubscriptionDTO.setPaymentMethod("cash");
        internetSubscriptionDTO.setPin(pin);
        String uniqueTransRef = utility.getUniqueTransRef();
        internetSubscriptionDTO.setClientReference(uniqueTransRef);

        Double amount;
        String reference;
        try {
            GenericResponseDTO genericResponseDTO;
            if (environment.acceptsProfiles(Profiles.of("staging", "dev"))) {
                genericResponseDTO = new GenericResponseDTO("00", HttpStatus.OK, "Internet Subscription Successful", new Gson().fromJson(subscribeInternetResponse, SubscribeInternetResponseData.class));
                amount = 100.00;
                reference = "ITEX-SMILE5F31B793027A5PV4BS38172436";
            } else {
                genericResponseDTO = itexConnectUtils.callItexApi(1, internetSubscriptionUrl, internetSubscriptionDTO, HttpMethod.POST);
                if (!HttpStatus.OK.equals(genericResponseDTO.getStatus()) || genericResponseDTO.getData() == null) {
                    return genericResponseDTO;
                }

                String genericResponseDTOData = (String) genericResponseDTO.getData();

                out.println("genericResponseDTOData ===> " + genericResponseDTOData);

                SubscribeInternetResponseData data = new Gson().fromJson(genericResponseDTOData, SubscribeInternetResponseData.class);

                amount = Double.valueOf(data.getAmount());
                reference = data.getReference();
            }


            String login = currentUser.getLogin();

            Optional<FundDTO> fundDTOOptional = utility.buildFundDTO(sourceAccountNumber, ITEX_PAYABLE_ACCT, amount - internetSubscriptionDTO.getBonusAmount(), login, SpecificChannel.PAY_INTERNET_ITEX.getName(), "walletToWallet", uniqueTransRef);

            if (fundDTOOptional.isPresent()) {
                FundDTO fundDTO = fundDTOOptional.get();
                if (internetSubscriptionDTO.getCharges() > 0.0){
                    fundDTO.setCharges(internetSubscriptionDTO.getCharges());
                }
                fundDTO.setNarration(internetSubscriptionDTO.getNarration());
                fundDTO.setRedeemBonus(internetSubscriptionDTO.isRedeemBonus());
                fundDTO.setBonusAmount(internetSubscriptionDTO.getBonusAmount());
                fundDTO.setRrr(reference);

                out.println("Funddto {} ====> " + fundDTO);

                fundDTO = transactionLogService.save(fundDTO);

                out.println("Funddto {} ====> " + fundDTO);

                producer.send(fundDTO);
            }

            return genericResponseDTO;

        } catch (Exception e) {
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "An error occurred!");
        }

    }

}
