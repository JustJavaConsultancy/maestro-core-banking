package ng.com.systemspecs.apigateway.util;

import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.User;
import ng.com.systemspecs.apigateway.domain.WalletAccount;
import ng.com.systemspecs.apigateway.service.AddressService;
import ng.com.systemspecs.apigateway.service.CashConnectService;
import ng.com.systemspecs.apigateway.service.HealthCheckService;
import ng.com.systemspecs.apigateway.service.ProfileService;
import ng.com.systemspecs.apigateway.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.dto.StatusCheckDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@EnableAsync
public class ServiceAlertScheduler {

    private final Logger log = LoggerFactory.getLogger(ServiceAlertScheduler.class);
    private final HealthCheckService healthCheckService;
    private final Utility utility;
    private final CashConnectService cashConnectService;
    private final WalletAccountService walletAccountService;
    private final ProfileService profileService;
    private final AddressService addressService;

    public ServiceAlertScheduler(HealthCheckService healthCheckService, Utility utility, CashConnectService cashConnectService, WalletAccountService walletAccountService, ProfileService profileService, AddressService addressService) {
        this.healthCheckService = healthCheckService;
        this.utility = utility;
        this.cashConnectService = cashConnectService;
        this.walletAccountService = walletAccountService;
        this.profileService = profileService;
        this.addressService = addressService;
    }

    //    @Scheduled(cron = "0 0 * * * *") //Every hour of every day
    public void checkExternalSystemHealthStatus() {

        Map<String, String> emails = utility.getNotificationEmailMap();

        try {

            List<StatusCheckDTO> statusCheckDTOS = healthCheckService.checkExternalServicesStatus();
            for (StatusCheckDTO statusCheckDTO : statusCheckDTOS) {
                if (statusCheckDTO != null && statusCheckDTO.getStatus().equalsIgnoreCase("DOWN")) {

                    String service = statusCheckDTO.getService();
                    String description = statusCheckDTO.getDescription();
                    String message = statusCheckDTO.getMessage();

                    if (utility.checkStringIsValid(service, description, message)) {

                        for (Map.Entry<String, String> entry : emails.entrySet()) {

                            String msg = "<p>Dear " + entry.getKey() + "," + "</p>" +
                                "<br/>" +
                                "<br/>" +
                                "<br/>" +
                                "<p>This is to notify you that the service referenced below has failed.</p>" +
                                "<br/>" +
                                "<br/>" +
                                "<br/>" +
                                "<p><b><u>SYSTEM MONITORING NOTIFICATION : " + service + "</u></b></p>" +
                                "<br/>" +
                                "<br/>" +
                                "<br/>" + "<p>Service Type : " + service + "</p>" +
                                "<br/>" + "<p>Service Provider : IPG" + "</p>" +
                                "<br/>" + "<p>Application Programming Interface (API) : " + service + "API " + "</p>" +
                                "<br/>" + "<p>Service Description : " + description + "</p>" +
                                "<br/>" + "<p>Reporting Error:  " + message + "</p>" +
                                "<br/>" + "<p>Responsibility : Lara Olowolagba - Lead, Transaction Monitoring (+234 814 137 6819)" + "</p>" +
                                "<br/>" +
                                "<br/>" +
                                "<br/>" +
                                "<p><i><b>NOTICE!! </b></i> - Please investigate and ensure resolution as soon as possible</p>";

                            //utility.sendEmail(entry.getValue(), "SYSTEM  MONITORING NOTIFICATION", msg);

                            Thread.sleep(5000);

                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*//    @Scheduled(cron = "0 15 0/1 * * *") //Every hour 15mins of every day
    @Scheduled(cron = "0 52 * * * *") //Every hour of everyday
    public void getCashConnectRefNo() {

        String phoneNumber = "+2348035133291";

        Optional<WalletAccount> primaryWallet = utility.getPrimaryWalletByPhoneNumber(phoneNumber);

        out.println("Primary wallet for TrackingRef  ==> " + primaryWallet);

        if (primaryWallet.isPresent()) {

            WalletAccount walletAccount = primaryWallet.get();
            out.println("WalletAccount for TrackingRef  ==> " + walletAccount);

            Profile profile = profileService.findByPhoneNumber(phoneNumber);

            if (profile != null) {
                out.println("Account Owner Profile ==> " + profile);

                if (utility.checkStringIsNotValid(walletAccount.getTrackingRef())) {

                    out.println("BVN is valid ==> " + profile.getBvn());

                    CashConnectAccountRequestDTO requestDTO = new CashConnectAccountRequestDTO();
                    requestDTO.setAccountType("individualSavings");
                    String address = null;
                    String state = null;

                        if (StringUtils.isEmpty(profile.getAddress())) {
                            address = profile.getAddress();
                            out.println("address ====== " + address);

                        }

                        List<AddressDTO> addresses = addressService.findByAddressOwner(profile.getPhoneNumber());

                        if (!addresses.isEmpty()) {
                            log.info("Getting Nuban account Addresses ==> " + addresses);

                            if (!addresses.isEmpty()) {
                                address = addresses.get(0).getAddress();
                                state = addresses.get(0).getState();

                                log.info("Getting nuban account State ==> " + state);

                            }
                        }

                        requestDTO.setAddress(address);
                        requestDTO.setBvn(profile.getBvn());
                        requestDTO.setDateOfBirth(String.valueOf(profile.getDateOfBirth()));
                        if (profile.getUser() != null) {

                            requestDTO.setEmail(profile.getUser().getEmail());
                            requestDTO.setFullname(profile.getFullName());
                            if (Gender.MALE.equals(profile.getGender())) {
                                requestDTO.setGender(0);
                            } else if (Gender.FEMALE.equals(profile.getGender())) {
                                requestDTO.setGender(1);
                            }

                            requestDTO.setIsControlAccount(0);
                            requestDTO.setLastname(profile.getUser().getLastName());
                            requestDTO.setOthernames(profile.getUser().getFirstName());

                            requestDTO.setPhoneNo(profile.getPhoneNumber());

                            out.println("requestDTO ===> " + requestDTO);

                            if (StringUtils.isNotEmpty(state)) {
                                requestDTO.setState(state);

                                out.println("REQUEST_DTO ====> " + requestDTO);
                                try {
                                    log.info("Before API call ===> ");
                                    ResponseEntity<GenericResponseDTO> responseEntity = cashConnectService.CreateNewAccount(requestDTO);
                                    if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                                        GenericResponseDTO body = responseEntity.getBody();

                                        log.info("After API call ===> ");

                                        if (body != null) {

                                            String data = (String) body.getData();
                                            log.info("data ++++++++++ " + data);
                                            CashConnectAccountResponseDTO cashConnectAccountResponseDTO = new ObjectMapper().readValue(data, CashConnectAccountResponseDTO.class);

                                            log.info("CashConnectAccountResponseDTO ++++++++++ " + cashConnectAccountResponseDTO);
                                            if (cashConnectAccountResponseDTO != null) {
                                                boolean isSuccessful = cashConnectAccountResponseDTO.getIsSuccessful();
                                                if (isSuccessful) {
                                                    String transactionTrackingRef = cashConnectAccountResponseDTO.getTransactionTrackingRef();
                                                    log.info("Account TransRef ==> " + transactionTrackingRef);

                                                    walletAccount.setTrackingRef(transactionTrackingRef);
                                                    WalletAccount save = walletAccountService.save(walletAccount);
                                                    log.info("Updated account with transRef ==> " + save);
                                                    return;

                                                }
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();

                                    out.println("Error Nuban Acct API CAll " + e.getLocalizedMessage());

                                }
                            }
                        }
                    }
                    log.info("TransRef is not null  ====> " + walletAccount.getTrackingRef());

                }

        }
    }

    @Scheduled(cron = "0 55 * * * *") //Every midnight plus 10mins
    public void retrieveCashConnectAccountNo() {

        String ownerPhoneNumber = "+2348035133291";

        Optional<WalletAccount> primaryWallet = utility.getPrimaryWalletByPhoneNumber(ownerPhoneNumber);

        out.println("Primary wallet accounts for Nuban ===> " + primaryWallet);

        if (primaryWallet.isPresent()) {

            WalletAccount walletAccount = primaryWallet.get();

            Profile profile = profileService.findByPhoneNumber(ownerPhoneNumber);

            if (profile != null
                && utility.checkStringIsValid(walletAccount.getTrackingRef())
                && utility.checkStringIsNotValid(walletAccount.getNubanAccountNo())) {

                out.println("Retrieving account number from TrankingRef ====> " + walletAccount.getTrackingRef());

                try {

                    ResponseEntity<GenericResponseDTO> responseEntity = cashConnectService.retrieveAccountNumber(walletAccount.getTrackingRef());

                    if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {

                        GenericResponseDTO body = responseEntity.getBody();
                        if (body != null) {

                            String data = (String) body.getData();
                            log.info("data ++++++++++ " + data);
                            CashConnectAccountNumberResponse cashConnectAccountNumberResponse = new ObjectMapper().readValue(data, CashConnectAccountNumberResponse.class);

                            log.info("cashConnectAccountNumberResponse ++++++++++ " + cashConnectAccountNumberResponse);
                            if (cashConnectAccountNumberResponse != null) {
                                String NUBAN = cashConnectAccountNumberResponse.getNuban();

                                log.info("Account NUBAN ==> " + NUBAN);

                                walletAccount.setNubanAccountNo(NUBAN);
                                WalletAccount save = walletAccountService.save(walletAccount);
                                log.info("Updated account with NubanAccountNo ==> " + save);

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    out.println("Error Nuban Acct API CAll " + e.getLocalizedMessage());
                }
            }
        }

    }*/

    //    @Scheduled(fixedDelay = 200000)
    public void setNubanAccount() {

        Profile profile = profileService.findByPhoneNumber("+2347064907683");
        if (profile != null) {
            List<WalletAccount> walletAccounts = walletAccountService.findByAccountOwnerPhoneNumber(profile.getPhoneNumber());
            User user = profile.getUser();
            String firstName = user.getFirstName();

            for (WalletAccount walletAccount : walletAccounts) {
                if (walletAccount.getAccountName().equalsIgnoreCase(firstName)) {
                    if (utility.checkStringIsNotValid(walletAccount.getTrackingRef(), walletAccount.getNubanAccountNo())) {
                        walletAccount.setNubanAccountNo("1100014234");
                        walletAccount.setTrackingRef("SYS3230038966");

                        WalletAccount save = walletAccountService.save(walletAccount);
                        log.info(String.format("Updated wallet ===> %s", save));
                    }
                }
            }
        }


        Profile profile2 = profileService.findByPhoneNumber("+2347035478515");
        if (profile2 != null) {
            List<WalletAccount> walletAccounts = walletAccountService.findByAccountOwnerPhoneNumber(profile2.getPhoneNumber());
            User user = profile2.getUser();
            String firstName = user.getFirstName();

            for (WalletAccount walletAccount : walletAccounts) {
                if (walletAccount.getAccountName().equalsIgnoreCase(firstName)) {

                    if (utility.checkStringIsNotValid(walletAccount.getTrackingRef(), walletAccount.getNubanAccountNo())) {
                        walletAccount.setNubanAccountNo("1100015334");
                        walletAccount.setTrackingRef("SYS112024872");

                        WalletAccount save = walletAccountService.save(walletAccount);
                        log.info(String.format("Updated wallet ===> %s", save));

                    }
                }

            }
        }


        Profile profile3 = profileService.findByPhoneNumber("+2347062023181");
        if (profile3 != null) {
            List<WalletAccount> walletAccounts = walletAccountService.findByAccountOwnerPhoneNumber(profile3.getPhoneNumber());
            User user = profile3.getUser();
            String firstName = user.getFirstName();

            for (WalletAccount walletAccount : walletAccounts) {
                if (walletAccount.getAccountName().equalsIgnoreCase(firstName)) {

                    if (utility.checkStringIsNotValid(walletAccount.getTrackingRef(), walletAccount.getNubanAccountNo())) {

                        walletAccount.setNubanAccountNo("1100015334");
                        walletAccount.setTrackingRef("SYS112024872");

                        WalletAccount save = walletAccountService.save(walletAccount);
                        log.info(String.format("Updated wallet ===> %s", save));

                    }
                }

            }
        }

        Profile profile4 = profileService.findByPhoneNumber("+2348035133291");
        if (profile4 != null) {

            Optional<WalletAccount> optionalWalletAccount = utility.getPrimaryWalletByPhoneNumber(profile.getPhoneNumber());

            if (optionalWalletAccount.isPresent()) {
                WalletAccount walletAccount = optionalWalletAccount.get();


                if (utility.checkStringIsNotValid(walletAccount.getTrackingRef())) {

                    walletAccount.setNubanAccountNo("1100016623");
                    walletAccount.setTrackingRef("SYS151520216");

                    WalletAccount save = walletAccountService.save(walletAccount);
                    log.info(String.format("Updated wallet ===> %s", save));

                }


            }
        }
    }
}
