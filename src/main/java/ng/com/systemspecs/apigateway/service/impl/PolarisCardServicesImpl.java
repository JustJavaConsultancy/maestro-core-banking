package ng.com.systemspecs.apigateway.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import ng.com.systemspecs.apigateway.config.AsyncConfiguration;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.systemspecs.apigateway.domain.enumeration.Gender;
import ng.com.systemspecs.apigateway.repository.AddressRepository;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.systemspecs.apigateway.service.exception.GenericException;
import ng.com.systemspecs.apigateway.service.mapper.CardRequestMapper;
import ng.com.systemspecs.apigateway.service.mapper.PolarisCardProfileMapper;
import ng.com.systemspecs.apigateway.service.mapper.UserCardsMapper;
import ng.com.systemspecs.apigateway.util.PolarisUtils;
import ng.com.systemspecs.apigateway.util.Utility;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;

import static java.lang.System.out;

@Service
@Transactional
public class PolarisCardServicesImpl implements PolarisCardService {

    private final Logger log = LoggerFactory.getLogger(PolarisCardServicesImpl.class);
    private final WalletAccountService walletAccountService;
    private final PolarisVulteService polarisVulteService;
    private final PolarisCardProfileService polarisCardProfileService;
    private final PolarisUtils polarisUtils;
    private final CardRequestService cardRequestService;
    private final UserCardsService userCardsService;
    private final UserCardsMapper userCardsMapper;
    private final CardRequestMapper cardRequestMapper;
    private final PolarisCardProfileMapper polarisCardProfileMapper;
    private final ProfileService profileService;
    private final SchemeService schemeService;
    private final AsyncConfiguration asyncConfiguration;
    private final Utility utility;
    private final AddressRepository addressRepository;
    @Value("${app.polaris-card.mode}")
    private boolean CARD_REQUEST_MODE;
    @Value("${app.polaris-card.base-url}")
    private String BASE_URL;
    @Value("${app.polaris-card.token-url}")
    private String TOKEN_URL;
    @Value("${app.polaris-card.oauth}")
    private String AUTH;
    @Value("${app.polaris-card.base-url}")
    private String GET_CARDS;
    @Value("${app.polaris-card.activate-card}")
    private String ACTIVATE_CARD;
    @Value("${app.polaris-card.request-card}")
    private String REQUEST_CARD;
    @Value("${app.polaris-card.pin-reset}")
    private String RESET_PIN;
    @Value("${app.polaris-card.pin-change}")
    private String CHANGE_PIN;
    @Value("${app.polaris-card.charges-acct}")
    private String CARD_ACCT_DEBIT;

    @Value("${app.polaris-card.card-payable-wallet}")
    private String CARD_PAYABLE_WALLET;
    @Value("${app.polaris-card.vendor-card-profile}")
    private String VENDOR_CARD_PROFILE;
    @Value("${app.polaris-card.get-branches}")
    private String GET_BRANCHES;
    @Value("${app.polaris-card.add-acct-vendor}")
    private String ADD_ACCT_TO_VENDOR;
    @Value("${app.cardrequest-url}")
    private String cardrequestUrl;

    @Value("${app.polaris-card.open-collection-account}")
    private String CREATE_COLLECTION_ACCOUNT;

    public PolarisCardServicesImpl(WalletAccountService walletAccountService, PolarisVulteService polarisVulteService, PolarisCardProfileService polarisCardProfileService, PolarisUtils polarisUtils, CardRequestService cardRequestService, UserCardsService userCardsService, UserCardsMapper userCardsMapper, CardRequestMapper cardRequestMapper, PolarisCardProfileMapper polarisCardProfileMapper, ProfileService profileService, SchemeService schemeService, AsyncConfiguration asyncConfiguration, Utility utility, AddressRepository addressRepository) {
        this.walletAccountService = walletAccountService;
        this.polarisVulteService = polarisVulteService;
        this.polarisCardProfileService = polarisCardProfileService;
        this.polarisUtils = polarisUtils;
        this.cardRequestService = cardRequestService;
        this.userCardsService = userCardsService;
        this.userCardsMapper = userCardsMapper;
        this.cardRequestMapper = cardRequestMapper;
        this.polarisCardProfileMapper = polarisCardProfileMapper;
        this.profileService = profileService;
        this.schemeService = schemeService;
        this.asyncConfiguration = asyncConfiguration;
        this.utility = utility;
        this.addressRepository = addressRepository;
    }

    @Override
    public GenericResponseDTO requestCard(PolarisCardRequestDTO polarisCardRequestDTO, String phoneNumber, String scheme) {

        Scheme schemeFromSchemeId = schemeService.findBySchemeID(scheme);
        if (schemeFromSchemeId == null) {
            throw new GenericException("Invalid Scheme Id");
        }
        PolarisCardProfileDTO cardProfileDTO = getCardProfile(scheme, "VerveCard", "VERVE");
        if (cardProfileDTO == null) {
            throw new GenericException("Cannot find any card profile for this scheme");
        }

        String uniqueRef = utility.getPolarisCardRequestUniqueRef();

        PolarisCollectionAccountRequestDTO request = new PolarisCollectionAccountRequestDTO();
        request.setUniqueIdentifier(uniqueRef);

        Profile profile = profileService.findByPhoneNumber(utility.formatPhoneNumber(phoneNumber));

        if (profile == null) {
            throw new GenericException("Could not retrieve user profile information");
        }

        User user = profile.getUser();
        if (user == null) {
            throw new GenericException("Could not retrieve user information");
        }

        if (profile.getGender() == null) {
            request.setTitle("Mr.");
            request.setGender(Gender.MALE.getName());
        }

        if (profile.getGender().equals(Gender.MALE)) {
            request.setTitle("Mr.");
            request.setGender(Gender.MALE.getName());
        } else if (profile.getGender().equals(Gender.FEMALE)) {
            request.setTitle("Mrs.");
            request.setGender(Gender.FEMALE.getName());
        }
        request.setState("Lagos");
        request.setAddressLine1("Ikeja");
        request.setAddressLine2("Ikeja");
        request.setCity("Ikeja");

        List<Address> addresses = addressRepository.findAllByAddressOwner(profile);
        if (!addresses.isEmpty()) {
            Address address = addresses.get(0);
            if (utility.checkStringIsValid(address.getState(), address.getAddress(),
                address.getCity(), address.getLocalGovt())) {
                request.setState(address.getState());
                request.setAddressLine1(address.getAddress());
                request.setAddressLine2(address.getLocalGovt());
                request.setCity(address.getCity());
            }
        }
        request.setEmail(user.getEmail());
        request.setFirstName(user.getFirstName());
        request.setLastName(user.getLastName());
        request.setDateOfBirth(profile.getDateOfBirth().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        request.setMaritalStatus("Single");
        request.setCountry("Nigeria");
        request.setMobileNumber(phoneNumber);

        GenericResponseDTO polarisResponse = new GenericResponseDTO();
        Optional<UserCards> card = userCardsService.findOneBySchemeAndOwner_PhoneNumber(scheme, phoneNumber);
        Optional<WalletAccount> walletAccount = utility.getPrimaryWalletByPhoneNumberAndScheme(phoneNumber, scheme);
        if (card.isPresent()) {
            return new GenericResponseDTO("99", org.springframework.http.HttpStatus.EXPECTATION_FAILED, "User already has a card or pending card request for this account/scheme!", polarisResponse);
        }
        if (!walletAccount.isPresent()) {
            throw new GenericException("Could not retrieve user wallet account");
        }

        try {
            polarisResponse = openCollectionAccount(phoneNumber, scheme);
//            polarisResponse = polarisVulteService.openCollectionAccount(request);
            log.debug("Polaris Open Account Response on Card Request {}", polarisResponse);

            Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
            if (asyncExecutor != null) {
                asyncExecutor.execute(() -> {
                    polarisUtils.updatePolarisKYCLevel(phoneNumber);
                });
            }
        } catch (Exception ex) {
            log.debug("CARD REQUEST ACCOUNT OPENING  ERROR {} ", ex.getMessage());
            return new GenericResponseDTO("99", org.springframework.http.HttpStatus.EXPECTATION_FAILED, "CARD REQUEST ACCOUNT OPENING  ERROR", polarisResponse);
        }
        if (polarisResponse == null || polarisResponse.getStatus() != org.springframework.http.HttpStatus.OK) {
            return new GenericResponseDTO("99", org.springframework.http.HttpStatus.EXPECTATION_FAILED, "CARD REQUEST FAILED", polarisResponse);
        }
        if (polarisResponse.getData() == null) {
            return new GenericResponseDTO("99", org.springframework.http.HttpStatus.EXPECTATION_FAILED, "CARD REQUEST ERROR -  Account Null", polarisResponse);
        }
        log.debug("Provider response::>  {}", polarisResponse.getData());
        PolarisCollectionAccountResponseDTO polData = (PolarisCollectionAccountResponseDTO) polarisResponse.getData();
        String accountNumber = polData.getAccountNumber();
        String customerId = polData.getCustomerID();
        Optional<ProfileDTO> owner = profileService.findOneByPhoneNumber(phoneNumber);

        List<String> accts = new ArrayList<>();
        accts.add(accountNumber);
        GenericResponseDTO addAcctToVendorResponse = addAccountsToVendor(accts);
        log.debug("Response (AddAccttoVendor) {}", addAcctToVendorResponse);
        if (!addAcctToVendorResponse.getCode().equalsIgnoreCase("00")) {
            return new GenericResponseDTO("99", org.springframework.http.HttpStatus.EXPECTATION_FAILED, "Polaris Error Adding Account to Vendor", addAcctToVendorResponse.getData());
        }
        FundDTO fundDTO = polarisCardRequestDTO.getFund();
        fundDTO.setChannel("Card Request");
        fundDTO.setAccountNumber(CARD_PAYABLE_WALLET);
        PaymentResponseDTO paymentResponseDTO = walletAccountService.sendMoney(fundDTO);
        log.debug("Card Request Payment Response {}", paymentResponseDTO);
        if (paymentResponseDTO.getError()) {
            return new GenericResponseDTO("99", "Payment Error in requesting for Card - " + paymentResponseDTO.getMessage(), paymentResponseDTO);
        } else {
            String cardImage = "";
            if (polarisCardRequestDTO.getCardtype().equalsIgnoreCase("MULTIFUNCTIONAL")) {
                cardImage = cardrequestUrl + accountNumber + ".jpg";
                String encodedString = polarisCardRequestDTO.getImage();
                if (encodedString != null && !encodedString.isEmpty()) {
                    boolean saved = utility.saveImage(encodedString, cardImage);
                }
                boolean sent = sendCardRequestToAdmin(polarisCardRequestDTO, request.getFirstName(), request.getLastName(), accountNumber, customerId, owner.get(), walletAccount.get().getAccountNumber(), uniqueRef);
                if (sent) {
                    return new GenericResponseDTO("00", org.springframework.http.HttpStatus.OK, "Card Request sent to Admin for Processing", paymentResponseDTO);
                } else {
                    return new GenericResponseDTO("99", org.springframework.http.HttpStatus.EXPECTATION_FAILED, "Failed", null);
                }
            } else {
                String token = getAccessToken();
                Unirest.setTimeouts(0, 0);
                try {
                    PolarisCardReqDTO polarisCardReqDTO = new PolarisCardReqDTO();
                    polarisCardReqDTO.setCardName(polarisCardReqDTO.getCardName());
                    polarisCardReqDTO.setAccountToLinked(accountNumber);
                    polarisCardReqDTO.setBranchCode(polarisCardRequestDTO.getDeliveryBranchCode());
                    polarisCardReqDTO.setDeliveryOption("PICK UP");
                    polarisCardReqDTO.setAccountToDebit(CARD_ACCT_DEBIT);
                    polarisCardReqDTO.setCardSchemeId(cardProfileDTO.getCardId());
                    HttpResponse<String> response = Unirest.post(BASE_URL + REQUEST_CARD)
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json")
                        .body(new Gson().toJson(polarisCardReqDTO))
                        .asString();
                    out.println("CARD REQUEST RESPONSE::==>>>  "+response.getStatus());
                    out.println("CARD REQUEST RESPONSE::==>>>  "+response.getBody());
                    JSONObject resp = new JSONObject(response.getBody());
                    if (response.getStatus() == HttpStatus.SC_OK) {
                        boolean sent = sendCardRequestToAdmin(polarisCardRequestDTO, request.getFirstName(), request.getLastName(), accountNumber, customerId, owner.get(), walletAccount.get().getAccountNumber(), uniqueRef);
                        return new GenericResponseDTO("00", org.springframework.http.HttpStatus.OK, resp.getString("message"), paymentResponseDTO);
                    } else {
                        return new GenericResponseDTO("99", org.springframework.http.HttpStatus.EXPECTATION_FAILED, resp.getString("message"), null);
                    }
                } catch (Exception e) {
                    out.println("Card request stack trace ========> ");
                    e.printStackTrace();
                    return new GenericResponseDTO("99", org.springframework.http.HttpStatus.EXPECTATION_FAILED, "CARD REQUEST FAILED", null);
                }
            }
        }
    }

    @Override
    public List<PolarisCard> getVendorCards(String scheme, String phoneNumber) {
        List<PolarisCard> cards = new ArrayList<>();
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        Optional<UserCards> card = userCardsService.findOneBySchemeAndOwner_PhoneNumber(scheme, phoneNumber);
        Optional<ProfileDTO> owner = profileService.findOneByPhoneNumber(phoneNumber);
        if (!owner.isPresent()) {
            throw new GenericException("Could not retrieve user profile from phone number");
        }
        if (!card.isPresent()) {
            CardRequest cardRequest = cardRequestService.findActiveRequest(owner.get(), scheme);
            out.println("Card Request Response ===> " + cardRequest);
            if (cardRequest != null) {
                String token = getAccessToken();
                Unirest.setTimeouts(0, 0);
                try {
                    HttpResponse<String> response = Unirest.get(BASE_URL + GET_CARDS + "?accountNumber=" + cardRequest.getCardNuban())
                        .header("Authorization", "Bearer " + token)
                        .header("Content-Type", "application/json")
                        .asString();
                    JSONArray jsonArray = new JSONArray(response.getBody());
                    out.println("Response Array ====> " + jsonArray);
                    Type cardListType = new TypeToken<ArrayList<PolarisCard>>() {
                    }.getType();
                    cards = new Gson().fromJson(String.valueOf(jsonArray), cardListType);
                    if (cards != null) {
                        if (!cards.isEmpty()) {
                            for (PolarisCard p : cards) {
                                UserCardsDTO userCardsDTO = new UserCardsDTO();
                                userCardsDTO.setCardName(p.getName());
                                userCardsDTO.setAccountNumber(p.getAccountNumber());
                                userCardsDTO.setCardType(p.getCardType());
                                userCardsDTO.setProvider("POLARIS");
                                userCardsDTO.setScheme(scheme);
                                userCardsDTO.setBin(p.getBin());
                                userCardsDTO.setLast4(p.getLast4());
                                userCardsDTO.setExpiryDate(p.getExpiryDate());
                                userCardsDTO.setStatus(p.getStatus().toUpperCase());
                                userCardsDTO.setPan(p.getPan());
                                userCardsDTO.setUniqueidentifier(cardRequest.getUniqueIdentifier());
                                userCardsDTO.setOwner(owner.get());
                                userCardsService.save(userCardsDTO);
                                if (p.getAccountNumber().equalsIgnoreCase(cardRequest.getCardNuban())) {
                                    CardRequestDTO cardRequestDTO = cardRequestMapper.toDto(cardRequest);
                                    cardRequestDTO.setStatus("APPROVED");
                                    cardRequestService.save(cardRequestDTO);
                                }
                            }
                        }
                    }
                    return cards;
                } catch (Exception ex) {
                    log.debug("GetVendorCards Error Exception");
                    log.debug(ex.getMessage());
                    return null;
                }
            } else {
                out.println("Return Null!!--");
                return null;
            }
        } else {
            List<UserCardsDTO> userCards = userCardsService.findAllBySchemeAndOwner_PhoneNumber(scheme, phoneNumber);
            for (UserCardsDTO u : userCards) {
                PolarisCard polarisCard = buildPolarisCardFromUserCard(u);
                cards.add(polarisCard);
            }
            return cards;
        }
    }

    @Override
    public GenericResponseDTO activateCard(PolarisCardOperationsDTO polarisCardOperationsDTO) {
        if (CARD_REQUEST_MODE) {
            return new GenericResponseDTO("00", org.springframework.http.HttpStatus.OK, "Card Activated Successfully", null);
        }
        String token = getAccessToken();
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.post(BASE_URL + ACTIVATE_CARD + "")
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(new Gson().toJson(polarisCardOperationsDTO))
                .asString();
            if (response.getStatus() == HttpStatus.SC_OK) {
                Optional<UserCards> userCards = userCardsService.findOneByAccountNumber(polarisCardOperationsDTO.getAccountNumber());
                if (userCards.isPresent()) {
                    UserCards u = userCards.get();
                    u.setStatus("ACTIVE");
                    userCardsService.save(userCardsMapper.toDto(u));
                }

                return new GenericResponseDTO("00", org.springframework.http.HttpStatus.OK, "success", response.getBody());
            }
            return new GenericResponseDTO("99", org.springframework.http.HttpStatus.BAD_REQUEST, "Failed", response.getBody());
        } catch (Exception ex) {
            log.debug("ActivateCard Error Exception");
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public GenericResponseDTO changeCardPin(PolarisCardOperationsDTO polarisCardOperationsDTO) {
        if (CARD_REQUEST_MODE) {
            return new GenericResponseDTO("00", org.springframework.http.HttpStatus.OK, "Card Pin Changed Successfully", null);
        }
        String token = getAccessToken();
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.post(BASE_URL + CHANGE_PIN + "")
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(new Gson().toJson(polarisCardOperationsDTO))
                .asString();
            if (response.getStatus() == HttpStatus.SC_OK) {
                return new GenericResponseDTO("00", org.springframework.http.HttpStatus.OK, "success", response.getBody());
            }
            return new GenericResponseDTO("99", org.springframework.http.HttpStatus.BAD_REQUEST, "Failed", response.getBody());
        } catch (Exception ex) {
            log.debug("ChangeCardPin Error Exception");
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public String getAccessToken() {
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.post(TOKEN_URL + AUTH)
                .field("client_id", "systemspecs")
                .field("client_secret", "systemspecssecret890@!!")
                .field("grant_type", "client_credentials")
                .asString();
            JSONObject json = new JSONObject(response.getBody());
            log.debug("Polaris OAuth Response {}", json);
            String accessToken = json.getString("access_token");
            return accessToken;
        } catch (Exception ex) {
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public GenericResponseDTO getVendorCardProfile(String scheme) {
        String token = getAccessToken();
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.get(BASE_URL + VENDOR_CARD_PROFILE + "?schemeCode=" + scheme)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .asString();
            if (response.getStatus() == HttpStatus.SC_OK) {
                JSONObject obj = new JSONObject(response.getBody());
                JSONArray arr = obj.getJSONArray("result");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jObj = arr.getJSONObject(i);
                    PolarisCardProfile polarisCardProfile = new PolarisCardProfile();
                    polarisCardProfile.setCardId(jObj.getString("id"));
                    polarisCardProfile.setCardFee(jObj.getDouble("cardFee"));
                    polarisCardProfile.setCardType(jObj.getString("cardType"));
                    polarisCardProfile.setScheme(scheme);
                    polarisCardProfileService.save(polarisCardProfile);
                }
                return new GenericResponseDTO("00", org.springframework.http.HttpStatus.OK, "success", response.getBody());
            }
            return new GenericResponseDTO("99", org.springframework.http.HttpStatus.BAD_REQUEST, "Failed", response.getBody());
        } catch (Exception ex) {
            log.debug("GetVendorCardProfile Error Exception");
            log.debug(ex.getMessage());
            return null;
        }
    }

    public PolarisCardProfileDTO getVendorCardProfile(String scheme, String cardType, String cardName) {
        PolarisCardProfileDTO cardProfileDTO = null;
        String token = getAccessToken();
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.get(BASE_URL + VENDOR_CARD_PROFILE + "?schemeCode=" + scheme)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .asString();
            out.println("Card Profiles 1 ===>>  "+response.getBody());
            if (response.getStatus() == HttpStatus.SC_OK) {
                JSONObject obj = new JSONObject(response.getBody());
                JSONArray arr = obj.getJSONArray("result");
                out.println("Card Profiles 2 ===>>  "+arr.toString());
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jObj = arr.getJSONObject(i);
                    out.println("Card Profiles 3 ===>>  "+jObj.toString());
                    out.println("Card Profiles 3 ===>>  "+jObj.getString("cardType"));
                    out.println("Card Profiles 3 ===>>  "+jObj.getString("name"));
                    out.println("Card Profiles 3 ===>>  "+cardType);
                    out.println("Card Profiles 3 ===>>  "+cardName);

                    if (cardType.equalsIgnoreCase(jObj.getString("cardType")) &&
                        cardName.equalsIgnoreCase(jObj.getString("name"))) {
                        out.println("Card Profile found!");
                        PolarisCardProfile polarisCardProfile = new PolarisCardProfile();
                        polarisCardProfile.setCardId(String.valueOf(jObj.get("id")));
                        polarisCardProfile.setCardFee(Double.valueOf(String.valueOf(jObj.get("cardFee"))));
                        polarisCardProfile.setCardName(jObj.getString("name"));
                        polarisCardProfile.setCardType(jObj.getString("cardType"));
                        polarisCardProfile.setScheme(scheme);
                        polarisCardProfileService.save(polarisCardProfile);
                        cardProfileDTO = polarisCardProfileMapper.toDto(polarisCardProfile);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            log.debug("GetVendorCardProfile Error Exception");
            log.debug(ex.getMessage());
            return null;
        }
        return cardProfileDTO;
    }

    @Override
    public GenericResponseDTO getPolarisVendorCardProfile(String scheme) {
        String token = getAccessToken();
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.get(BASE_URL + VENDOR_CARD_PROFILE)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .asString();
            if (response.getStatus() == HttpStatus.SC_OK) {
                return new GenericResponseDTO("00", org.springframework.http.HttpStatus.OK, "success", response.getBody());
            }
            return new GenericResponseDTO("99", org.springframework.http.HttpStatus.BAD_REQUEST, "Failed", response.getBody());
        } catch (Exception ex) {
            log.debug("GetVendorCardProfile Error Exception");
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public PolarisCardProfileDTO getCardProfile(String scheme, String cardType, String cardName) {
        Optional<PolarisCardProfile> cardProfile = polarisCardProfileService.findOneBySchemeAndCardTypeAndCardName(scheme, cardType, cardName);
        if (cardProfile.isPresent()) {
            return polarisCardProfileMapper.toDto(cardProfile.get());
        } else {
            return getVendorCardProfile(scheme, cardType, cardName);
        }
    }

    @Override
    public GenericResponseDTO addAccountsToVendor(List<String> accounts) {
        String token = getAccessToken();
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.post(BASE_URL + ADD_ACCT_TO_VENDOR)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(new Gson().toJson(accounts))
                .asString();
            if (response.getStatus() == HttpStatus.SC_OK) {
                return new GenericResponseDTO("00", org.springframework.http.HttpStatus.OK, "success", response.getBody());
            }
            return new GenericResponseDTO("99", org.springframework.http.HttpStatus.BAD_REQUEST, "Failed", response.getBody());
        } catch (Exception ex) {
            log.debug("AddAccountsToVendor Error Exception");
            log.debug(ex.getMessage());
            return null;
        }
    }

    public GenericResponseDTO getBranches() {
        String token = getAccessToken();
        List<PolarisBranchDTO> branches = new ArrayList<>();
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.get(BASE_URL + GET_BRANCHES + "")
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .asString();
            out.println("Polaris Branches response ===>  " + response.getBody());
            if (response.getStatus() == HttpStatus.SC_OK) {
                JSONObject j = new JSONObject(response.getBody());
                if (j.get("result") != null) {
                    JSONArray jsonArray = j.getJSONArray("result");
                    Type cardListType = new TypeToken<ArrayList<PolarisBranchDTO>>() {
                    }.getType();
                    branches = new Gson().fromJson(String.valueOf(jsonArray), cardListType);
                }
                return new GenericResponseDTO("00", org.springframework.http.HttpStatus.OK, "success", branches);
            }
            return new GenericResponseDTO("99", org.springframework.http.HttpStatus.BAD_REQUEST, "Failed", response.getBody());
        } catch (Exception ex) {
            log.debug("GET_BRANCHES Error Exception");
            log.debug(ex.getMessage());
            return null;
        }
    }

    @Override
    public GenericResponseDTO resetCardPin(PolarisCardOperationsDTO polarisCardOperationsDTO) {
        if (CARD_REQUEST_MODE) {
            return new GenericResponseDTO("00", org.springframework.http.HttpStatus.OK, "Card Pin Reset Successfully", null);
        }
        String token = getAccessToken();
        Unirest.setTimeouts(0, 0);
        try {
            HttpResponse<String> response = Unirest.post(BASE_URL + RESET_PIN + "")
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(new Gson().toJson(polarisCardOperationsDTO))
                .asString();
            if (response.getStatus() == HttpStatus.SC_OK) {
                return new GenericResponseDTO("00", org.springframework.http.HttpStatus.OK, "success", response.getBody());
            }
            return new GenericResponseDTO("99", org.springframework.http.HttpStatus.BAD_REQUEST, "Failed", response.getBody());
        } catch (Exception ex) {
            log.debug("ResetCardPin Error Exception");
            log.debug(ex.getMessage());
            return null;
        }
    }


    public PolarisCard buildPolarisCardFromUserCard(UserCardsDTO userCardsDTO) {
        PolarisCard polarisCard = new PolarisCard(userCardsDTO.getCardName(), userCardsDTO.getAccountNumber(), userCardsDTO.getBin(), userCardsDTO.getLast4(), userCardsDTO.getExpiryDate(), userCardsDTO.getStatus(), "", userCardsDTO.getCardType(), userCardsDTO.getPan());
        return polarisCard;
    }

    public boolean sendCardRequestToAdmin(PolarisCardRequestDTO polarisCardRequestDTO, String firstName, String lastName, String accountNumber, String customerId, ProfileDTO owner, String walletAcctNumber, String uniqueRef) {
        boolean sent = false;

        try {
            Map<String, String> emails = utility.getEmailMap();
            String sb = "<h2> Card Request Details: </h2>" +
                "<br/>" +
                "This is to notify that a card has been requested. Find below the details of the request:" +
                "<br/>" +
                "<h2>BASIC DETAILS</h2>" +
                "<br/>" +
                "Card Type ~ " + polarisCardRequestDTO.getCardtype() +
                "<br/>" +
                "First Name ~ " + firstName +
                "<br/>" +
                "Last Name ~ " + lastName +
                "<br/>" +
                "Customer ID ~ " + customerId +
                "<br/>" +
                "Image ID ~ " + accountNumber + ".jpg" +
                "<br/>" +
                "Account Number ~ " + accountNumber +
                "<br/>" +
                "Scheme ~ " + polarisCardRequestDTO.getScheme() +
                "<br/>" +
                "<h2>OTHER DETAILS</h2>" +
                "<br/>" +
                "Matric No ~ " + polarisCardRequestDTO.getMatricno() +
                "<br/>" +
                "Department ~ " + polarisCardRequestDTO.getDepartment() +
                "<br/>" +
                "Faculty ~ " + polarisCardRequestDTO.getFaculty() +
                "<br/>" +
                "<br/>";
            utility.sendEmail(emails, "CUSTOMER CARD REQUEST", sb);

            CardRequestDTO cardRequestDTO = new CardRequestDTO();
            cardRequestDTO.setFirstName(firstName);
            cardRequestDTO.setSurname(lastName);
            cardRequestDTO.setCardtype(polarisCardRequestDTO.getCardtype());
            cardRequestDTO.setCustomerid(customerId);
            cardRequestDTO.setDepartment(polarisCardRequestDTO.getDepartment());
            cardRequestDTO.setFaculty(polarisCardRequestDTO.getFaculty());
            cardRequestDTO.setStatus("NEW");
            cardRequestDTO.setImage(polarisCardRequestDTO.getImage());
            cardRequestDTO.setMatricno(polarisCardRequestDTO.getMatricno());
            cardRequestDTO.setScheme(polarisCardRequestDTO.getScheme());
            cardRequestDTO.setCardNuban(accountNumber);
            cardRequestDTO.setWalletId(walletAcctNumber);
            cardRequestDTO.setOwner(owner);
            cardRequestDTO.setUniqueIdentifier(uniqueRef);
            cardRequestService.save(cardRequestDTO);
            sent = true;
        } catch (Exception ex) {
            log.debug(ex.getMessage());
        }
        return sent;
    }

    @Override
    public GenericResponseDTO openCollectionAccount(String phoneNumber, String scheme) {
        String uniqueRef = utility.getPolarisCardRequestUniqueRef();
        PolarisCollectionAccountRequestDTO request = new PolarisCollectionAccountRequestDTO();
        request.setUniqueIdentifier(uniqueRef);
        String phone = phoneNumber;
        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        Profile profile = profileService.findByPhoneNumber(phoneNumber);
        log.debug("Returned profile {} ", profile);
        if (profile == null) {
            throw new GenericException("Could not retrieve user profile information");
        }

        User user = profile.getUser();
        if (user == null) {
            throw new GenericException("Could not retrieve user information");
        }

        if (profile.getGender() == null) {
            request.setTitle("Mr.");
            request.setGender(Gender.MALE.getName());
        }

        if (profile.getGender().equals(Gender.MALE)) {
            request.setTitle("Mr");
            request.setGender(Gender.MALE.getName());
        } else if (profile.getGender().equals(Gender.FEMALE)) {
            request.setTitle("Mrs");
            request.setGender(Gender.FEMALE.getName());
        }
        request.setState("LG");
        request.setAddressLine1("Ikeja");
        request.setAddressLine2("Ikeja");
        request.setCity("LAGOS");

        List<Address> addresses = addressRepository.findAllByAddressOwner(profile);
        if (!addresses.isEmpty()) {
            Address address = addresses.get(0);
            if (utility.checkStringIsValid(address.getState(), address.getAddress(),
                address.getCity(), address.getLocalGovt())) {
                request.setState(address.getState());
                request.setAddressLine1(address.getAddress());
                request.setAddressLine2(address.getLocalGovt());
                request.setCity(address.getCity());
            }
        }
        request.setEmail(user.getEmail());
        request.setFirstName(user.getFirstName());
        request.setLastName(user.getLastName());
        request.setMiddleName(user.getFirstName());
        out.println("Profile DOB ===> " + profile.getDateOfBirth());
        request.setDateOfBirth(profile.getDateOfBirth().toString());
        request.setMaritalStatus("U");
        request.setCountry("NGN");
        request.setMobileNumber(phone);
        request.setBVN(profile.getBvn());

        try {
            out.println("ACCOUNT OPENING REQUEST OBJ ==>  " + new Gson().toJson(request));
            String token = getAccessToken();
            String requestString = new Gson().toJson(request);
            JSONObject reQ = new JSONObject(requestString);
            out.println("REQUEST URL  ==>>>  " + CREATE_COLLECTION_ACCOUNT);
            Unirest.setTimeouts(0, 0);
            HttpResponse<String> response = Unirest.post(CREATE_COLLECTION_ACCOUNT)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .body(new Gson().toJson(request))
                .asString();
            log.debug("Polaris Open Account Response on Card Request {}", response.getBody());
            out.println("Polaris Open Account Response ===>>>   " + response.getBody());
            if (response.getStatus() != HttpStatus.SC_OK) {
                return new GenericResponseDTO("99", org.springframework.http.HttpStatus.EXPECTATION_FAILED, "Account Creation Failed", response.getBody());
            }
            JSONObject result = new JSONObject(response.getBody());
            PolarisCollectionAccountResponseDTO polarisCollectionAccountResponseDTO = new Gson().fromJson(result.get("result").toString(), PolarisCollectionAccountResponseDTO.class);
            log.debug("Provider response::>  {}", polarisCollectionAccountResponseDTO);
            if (polarisCollectionAccountResponseDTO == null) {
                return new GenericResponseDTO("99", org.springframework.http.HttpStatus.EXPECTATION_FAILED, result.getString("message"), response.getBody());
            } else
                return new GenericResponseDTO("00", org.springframework.http.HttpStatus.OK, "Polaris Account Opened! ", polarisCollectionAccountResponseDTO);
        } catch (Exception ex) {
            log.debug("ACCOUNT OPENING  ERROR {} ", ex.getMessage());
            return new GenericResponseDTO("99", org.springframework.http.HttpStatus.EXPECTATION_FAILED, "ACCOUNT OPENING  ERROR", null);
        }
    }
}
