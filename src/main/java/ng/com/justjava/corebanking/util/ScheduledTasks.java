package ng.com.justjava.corebanking.util;

import ng.com.justjava.corebanking.domain.*;
import ng.com.justjava.corebanking.domain.enumeration.AccountStatus;
import ng.com.justjava.corebanking.domain.enumeration.TransactionStatus;
import ng.com.justjava.corebanking.domain.enumeration.UserStatus;
import ng.com.justjava.corebanking.repository.JournalRepository;
import ng.com.justjava.corebanking.repository.UserRepository;
import ng.com.justjava.corebanking.repository.WalletAccountRepository;
import ng.com.justjava.corebanking.repository.WalletAccountTypeRepository;
import ng.com.justjava.corebanking.service.*;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.mapper.BillerCategoryMapper;
import ng.com.justjava.corebanking.service.mapper.BillerMapper;
import ng.com.justjava.corebanking.service.mapper.BillerPlatformMapper;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.systemspecs.remitabillinggateway.billers.GetBillerResponseData;
import ng.com.systemspecs.remitabillinggateway.customfields.GetCustomFieldResponse;
import ng.com.systemspecs.remitabillinggateway.customfields.GetCustomFieldResponseData;
import ng.com.systemspecs.remitabillinggateway.customfields.GetCustomFieldResponseDropDown;
import ng.com.systemspecs.remitabillinggateway.servicetypes.GetServiceResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.out;

@Component
@Transactional
public class ScheduledTasks {

    @Value("${app.constants.dfs.cashconnect-bvn-acct}")
    private String CASH_CONNECT_BVN_ACCT;
    @Value("${app.constants.dfs.rpsl-nin-validation-acct}")
    private String RPSL_NIN_VALIDATION_ACCT;
    @Value("${app.constants.dfs.cashconnect-interbank-services-acct}")
    private String CASH_CONNECT_INTERBANK_SERVICES_ACCT;
    @Value("${app.constants.dfs.rpsl-biller-pay-rrr-acct}")
    private String RSPL_BILLER_PAY_RRR_ACCT;
    @Value("${app.constants.dfs.rpsl-biller-electricity-acct}")
    private String RSPL_BILLER_PAY_ELECTRICITY_ACCT;
    @Value("${app.constants.dfs.rpsl-biller-cable-tv-acct}")
    private String RSPL_BILLER_PAY_TV_SUBSCRIPTUION_ACCT;
    @Value("${app.constants.dfs.itex-cable-tv-acct}")
    private String ITEX_CABLE_TV_ACCT;
    @Value("${app.constants.dfs.itex-data-acct}")
    private String ITEX_DATA_ACCT;
    @Value("${app.constants.dfs.itex-airtime-acct}")
    private String ITEX_AIRTIME_ACCT;
    @Value("${app.constants.dfs.itex-internet-acct}")
    private String ITEX_INTERNET_ACCT;
    @Value("${app.constants.dfs.remita-wallency-expense-acct}")
    private String REMITA_WALLENCY_EXPENSE_ACCT;
    @Value("${app.constants.dfs.introducer-account}")
    private String INTRODUCER_ACCT;
    @Value("${app.constants.dfs.cashconnect-commission-acct}")
    private String CASH_CONNECT_COMMISSION_ACCT;
    @Value("${app.constants.dfs.rpsl-inline-acct}")
    private String RSPL_INLINE_ACCT;
    @Value("${app.constants.dfs.telco-mtn-session-fee-acct}")
    private String TELCO_MTN_SESSION_FEE_ACCT;
    @Value("${app.constants.dfs.telco-9mobile-session-fee-acct}")
    private String TELCO_9MOBILE_SESSION_FEE_ACCT;
    @Value("${app.constants.dfs.telco-airtel-session-fee-acct}")
    private String TELCO_AIRTEL_SESSION_FEE_ACCT;
    @Value("${app.constants.dfs.telco-glo-session-fee-acct}")
    private String TELCO_GLO_SESSION_FEE_ACCT;
    @Value("${app.constants.dfs.ci-ussd-charge-acct}")
    private String CI_USSD_CHARGE_ACCT;
    @Value("${app.constants.dfs.mutual-benefits-income-acct}")
    private String MUTUAL_BENEFITS_INCOME_ACCT;
    @Value("${app.constants.dfs.coral-income-account}")
    private String CORAL_PAY_USSD_FUNDING_ACCT;
    @Value("${app.constants.dfs.remita-income-account}")
    private String RPSL_INTERBANK_SERVICES_ACCT;
    @Value("${app.constants.dfs.vat-account}")
    private String VAT_ACCOUNT;
    @Value("${app.constants.dfs.lirs-income-acct}")
    private String LIRS_INCOME_ACCT;
    @Value("${app.constants.dfs.ibile-hub-commission-acct}")
    private String IBILE_HUB_COMMISSION_ACCT;
    @Value("${app.constants.dfs.ibile-hub-income-acct}")
    private String IBILE_HUB_INCOME_ACCT;
    @Value("${app.constants.dfs.itex-commission-acct}")
    private String ITEX_COMMISSION_ACCT;
    @Value("${app.constants.dfs.charge-account}")
    private String REMITA_WALLENCY_INCOME_ACCT;
    @Value("${app.constants.dfs.itex-electricity-acct}")
    private String ITEX_ELELECTRICTY_ACCT;
    @Value("${app.constants.dfs.customer-correspondence-acct}")
    private String CUSTOMER_CORRESPONDENCE_ACCT;
    @Value("${app.constants.dfs.itex-payable-acct}")
    private String ITEX_PAYABLE_ACCT;
    @Value("${app.constants.dfs.correspondence-account}")
    private String CORRESPONDENCE_ACCOUNT;
    @Value("${app.constants.dfs.lending-disbursement-income-acct}")
    private String LENDING_DISBURSEMENT_INCOME_ACCT;
    @Value("${app.constants.dfs.insurance-commission-acct}")
    private String INSURANCE_COMMISSION_ACCT;
    @Value("${app.constants.dfs.lirs-settlement-acct}")
    private String LIRS_SETTLEMENT_ACCT;
    @Value("${app.constants.dfs.alliance-ibile-hub-settlement-acct}")
    private String ALLIANCE_IBILE_HUB_SETTLEMENT_ACCT;
    @Value("${app.constants.dfs.itex-biller-settlement-acct}")
    private String ITEX_BILLER_SETTLEMENT_ACCT;
    @Value("${app.constants.dfs.fuoye-settlement-acct}")
    private String FUOYE_SETTLEMENT_ACCT;
    @Value("${app.constants.dfs.polaris-income-account}")
    private String POLARIS_INTERBANK_SERVICES_ACCT;
    @Value("${app.constants.dfs.polaris-commission-account}")
    private String POLARIS_COMMISSION_ACCT;
    @Value("${app.constants.dfs.polaris-pomengranate-commission-account}")
    private String POLARIS_POMENGRANATE_COMMISSION_ACCT;
    @Value("${app.constants.dfs.hm-correspondence-account}")
    private String HM_CORRESPONDENCE_ACCOUNT;

    @Value("${app.constants.dfs.hm-processing-fee-account}")
    private String HM_PROCESSING_FEE_ACCT;

    @Value("${app.constants.dfs.hm-vat-payable-account}")
    private String HM_VAT_PAYABLE_ACCT;

    @Value("${app.constants.dfs.lending-hub-fee-acct}")
    private String LENDING_HUB_FEE_ACCT;

    @Value("${app.constants.dfs.polaris-debit-card-acct}")
    private String POLARIS_CARD_WALLET;


    private final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);


    private final Utility utility;
    private final WalletAccountService walletAccountService;
    private final BillerTransactionService billerTransactionService;
    private final BillerCustomFieldOptionService billerCustomFieldOptionService;
    private final BillerService billerService;
    private final BillerMapper billerMapper;
    private final BillerPlatformService billerPlatformService;
    private final WalletAccountRepository walletAccountRepository;
    private final SchemeService schemeService;
    private final WalletAccountTypeRepository walletAccountTypeRepository;
    private final JournalRepository journalRepository;
    private final TransactionLogService transactionLogService;
    private final BillerPlatformMapper billerPlatformMapper;
    private final BillerCategoryMapper billerCategoryMapper;
    private final BillerServiceOptionService billerServiceOptionService;
    private final BillerCategoryService billerCategoryService;
    private final CustomFieldDropdownService customFieldDropdownService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final WalletAccountTypeService walletAccountTypeService;
    @Value("${app.image-url}")
    private String imageUrl;
    private static int count = 0;
    BillerCategoryDTO billerCategoryTechoAir;
    BillerCategoryDTO billerCategoryPower;
    BillerCategoryDTO billerCategoryUtil;
    BillerCategoryDTO billerCategoryTelcom;
    private BillerCategoryDTO billerCategoryTechoDAta;

    public ScheduledTasks(BillerTransactionService billerTransactionService, BillerCustomFieldOptionService billerCustomFieldOptionService, BillerService billerService, BillerMapper billerMapper,
                          BillerPlatformService billerPlatformService, WalletAccountRepository walletAccountRepository, SchemeService schemeService, WalletAccountTypeRepository walletAccountTypeRepository, JournalRepository journalRepository, TransactionLogService transactionLogService, BillerPlatformMapper billerPlatformMapper,
                          BillerCategoryMapper billerCategoryMapper, BillerServiceOptionService billerServiceOptionService, BillerCategoryService billerCategoryService, CustomFieldDropdownService customFieldDropdownService, UserService userService, UserRepository userRepository, Utility utility, WalletAccountService walletAccountService, WalletAccountTypeService walletAccountTypeService, IbileService ibileService) {

        this.billerTransactionService = billerTransactionService;
        this.billerCustomFieldOptionService = billerCustomFieldOptionService;
        this.billerService = billerService;
        this.billerMapper = billerMapper;
        this.billerPlatformService = billerPlatformService;
        this.walletAccountRepository = walletAccountRepository;
        this.schemeService = schemeService;
        this.walletAccountTypeRepository = walletAccountTypeRepository;
        this.journalRepository = journalRepository;
        this.transactionLogService = transactionLogService;
        this.billerPlatformMapper = billerPlatformMapper;
        this.billerCategoryMapper = billerCategoryMapper;
        this.billerServiceOptionService = billerServiceOptionService;
        this.billerCategoryService = billerCategoryService;
        this.customFieldDropdownService = customFieldDropdownService;
        this.userService = userService;

        this.userRepository = userRepository;
        this.utility = utility;
        this.walletAccountService = walletAccountService;
        this.walletAccountTypeService = walletAccountTypeService;
        this.ibileService = ibileService;
    }

//    @Scheduled(fixedRate = 1000 * 60 * 60 * 60 * 7)
    public void populateUserStatus() {
        List<User> all = userRepository.findAll();
        all.forEach(user -> {
            user.setStatus(UserStatus.OK.getName());
            try {
                User save = userRepository.save(user);
                log.error("Updated user status ===> " + save);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

/*

        @Scheduled(fixedRate = 1000 * 60 * 60 * 60 * 7)
    public void restructureNavsaWalletAccounts() {

        List<WalletAccount> all = walletAccountRepository.findAllBySchemeId(3L);

        int counter = 0;

        for (WalletAccount walletAccount : all) {
            log.info("Wallet Account being edited ====> " + walletAccount);
            List<WalletAccount> walletAccounts =
                walletAccountRepository.findAllByAccountNumberAndAccountNameAndSchemeId(walletAccount.getAccountNumber(), walletAccount.getAccountName(), 3L);
            if (walletAccounts.size() > 1){
                for (int i = 0; i < walletAccounts.size(); i++) {
                    if (i > 0){
                        log.info("Wallet Account being deleted ====> " + walletAccounts.get(i));
                        walletAccountRepository.delete(walletAccounts.get(i));
                        counter++;
                    }
                }
            }
        }

        log.info("COUNTER FOR DELETED Accounts =====> " + counter);
    }



*/


    //    @Scheduled(cron = "0 30 9 15 * ?")
//        @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(cron = "@hourly")
//    @Scheduled(fixedRate = 1000 * 3600 * 24)
//    @Scheduled(fixedRate = 1000 * 60 * 60 * 60 * 7)
    public void populateDbWithBillers() throws Exception {

        //Create Biller Category44
        List<BillerCategoryDTO> billerCategories = billerCategoryService.findAll();
        if (billerCategories.size() == 0) {

            BillerCategoryDTO billerCategorDTO = new BillerCategoryDTO();
            billerCategorDTO.setBillercategoryID(currentTimeMillis());
            billerCategorDTO.setBillerCategory("TELCO-DATA");
            billerCategoryTechoDAta = billerCategoryService.save(billerCategorDTO);

            BillerCategoryDTO billerCategorDTOAir = new BillerCategoryDTO();
            billerCategorDTOAir.setBillercategoryID(currentTimeMillis());
            billerCategorDTOAir.setBillerCategory("TELCO-AIR");
            billerCategoryTechoAir = billerCategoryService.save(billerCategorDTOAir);

            BillerCategoryDTO billerCategorDTO2 = new BillerCategoryDTO();
            billerCategorDTO2.setBillercategoryID(currentTimeMillis());
            billerCategorDTO2.setBillerCategory("POWER");
            billerCategoryPower = billerCategoryService.save(billerCategorDTO2);

            BillerCategoryDTO billerCategorDTO3 = new BillerCategoryDTO();
            billerCategorDTO3.setBillercategoryID(currentTimeMillis());
            billerCategorDTO3.setBillerCategory("TV");
            billerCategoryUtil = billerCategoryService.save(billerCategorDTO3);

            BillerCategoryDTO billerCategorDTO4 = new BillerCategoryDTO();
            billerCategorDTO3.setBillercategoryID(currentTimeMillis());
            billerCategorDTO3.setBillerCategory("TELCOM");
            billerCategoryTelcom = billerCategoryService.save(billerCategorDTO4);

            billerCategories.add(billerCategoryTechoDAta);
            billerCategories.add(billerCategoryTechoAir);
            billerCategories.add(billerCategoryPower);
            billerCategories.add(billerCategoryUtil);
            billerCategories.add(billerCategoryTelcom);

        } else {
            log.debug("BILLER CATEGORY: BillerCategory reached");
        }


        //get Billers and save to DB

       /* try {
            getBillersToDb(billerCategories);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        // get All services associated with a biller and save as billerPlatform into the db

//        getBillerPlatform();

        // get all custom fields associated with a biller platform and save in service option model

        getCustomeFieldsToDB();


    }
    private void getCustomeFieldsToDB() throws Exception {
        List<BillerPlatform> allBillerPlatforms =
            billerPlatformService.findAll().stream().map(billerPlatformMapper::toEntity).collect(Collectors.toList());
        if (allBillerPlatforms.size() != 0) {

            for (BillerPlatform billerPlatform : allBillerPlatforms) {
                Set<BillerServiceOption> serviceOptions = new HashSet<>();
                log.error("CURRENT BILLER PLATFORM" + billerPlatform.getBillerplatformID());

                GetCustomFieldResponse customFieldResponse =
                    billerTransactionService.getCustomField(String.valueOf(billerPlatform.getBillerplatformID()));

                log.info("GetCustomFieldResponse " + customFieldResponse);
                if (customFieldResponse != null && customFieldResponse.getResponseCode() != null && customFieldResponse.getResponseCode().equalsIgnoreCase("00")) {

                    Optional<BillerCustomFieldOption> byBillerPlatform = billerCustomFieldOptionService.findByBillerPlatform(billerPlatform);

                    if (!byBillerPlatform.isPresent()) {

                        BillerCustomFieldOption billerCustomFieldOption = new BillerCustomFieldOption();
                        billerCustomFieldOption.setAcceptPartPayment(customFieldResponse.getAcceptPartPayment());
                        billerCustomFieldOption.setBillerPlatform(billerPlatform);
                        billerCustomFieldOption.setAcceptPartPayment(customFieldResponse.getAcceptPartPayment());
                        billerCustomFieldOption.setCurrency(customFieldResponse.getCurrency());
                        billerCustomFieldOption.setFixedAmount(customFieldResponse.getFixedAmount());
                        billerCustomFieldOption.setHasFixedPrice(customFieldResponse.getFixedPrice());
                        BillerCustomFieldOption savedBillerCustomFieldOption = billerCustomFieldOptionService.save(billerCustomFieldOption);

                        List<GetCustomFieldResponseData> customFieldResponseDataList = customFieldResponse.getResponseData();

                        if (customFieldResponseDataList != null && customFieldResponseDataList.size() > 0) {
                            for (GetCustomFieldResponseData responseData : customFieldResponseDataList) {
                                List<BillerServiceOption> billerServiceOptionOptional =
                                    billerServiceOptionService.findByServiceOptionId(Long.valueOf(responseData.getId()));
                                if (!billerServiceOptionOptional.isEmpty()) {
                                    log.error("DROPPING OUT OF LOOP, SERVICE_OPTION ALREADY EXISTS");
                                    continue;
                                }

                                BillerServiceOption billerServiceOption = new BillerServiceOption();
                                billerServiceOption.setServiceOptionId(Long.parseLong(responseData.getId()));
                                billerServiceOption.setName(responseData.getColumnName());
                                billerServiceOption.setLength(responseData.getColumnLength());
                                billerServiceOption.setRequired(responseData.getRequired());
                                billerServiceOption.setType(responseData.getColumnType());
                                billerServiceOption.setBillerCustomFieldOption(savedBillerCustomFieldOption);
                                BillerServiceOption save = billerServiceOptionService.save(billerServiceOption);
                                log.error("SAVED BILLER SERVICE OPTION +++ " + save);
                                List<GetCustomFieldResponseDropDown> customFieldDropDowns = responseData.getCustomFieldDropDown();

                                Set<CustomFieldDropdown> fieldDropdowns = new HashSet<>();
                                if (customFieldDropDowns != null && customFieldDropDowns.size() > 0) {

                                    for (GetCustomFieldResponseDropDown field : customFieldDropDowns) {
                                        Optional<CustomFieldDropdown> byFieldDropdownId
                                            = customFieldDropdownService.findByFieldDropdownId(field.getId());
                                        if (byFieldDropdownId.isPresent()) {
                                            log.error("DROPPING OUT OF LOOP, OBJECT ALREADY EXISTS");
                                            continue;
                                        }

                                        CustomFieldDropdown customFieldDropdown = new CustomFieldDropdown();
                                        customFieldDropdown.setAccountid(field.getAccountid());
                                        customFieldDropdown.setBillerServiceOption(save);
                                        customFieldDropdown.setCode(field.getCode());
                                        customFieldDropdown.setDescription(field.getDescription());
                                        customFieldDropdown.setFieldDropdownId(field.getId());
                                        customFieldDropdown.setFixedprice(field.getFixedprice());
                                        customFieldDropdown.setUnitprice(field.getUnitprice());
                                        customFieldDropdown.setFieldDropdownId(field.getId());

                                        CustomFieldDropdown fieldDropdown = customFieldDropdownService.save(customFieldDropdown);

                                        fieldDropdowns.add(fieldDropdown);

                                    }
                                }
                                log.info("billerServiceOption == " + billerServiceOption);
                                save.setCustomFieldDropdowns(fieldDropdowns);
                                billerServiceOptionService.save(save);
                                serviceOptions.add(billerServiceOption);
                            }

                            savedBillerCustomFieldOption.setBillerServiceOptions(serviceOptions);

                            log.info("billerServiceOptionsSET == " + serviceOptions);

                        }
                        billerPlatform.setBillerCustomFieldOptions(savedBillerCustomFieldOption);
                        billerCustomFieldOptionService.save(savedBillerCustomFieldOption);
                        try {
                            BillerPlatformDTO save = billerPlatformService.save(billerPlatformMapper.toDto(billerPlatform));
                            log.error("SAVED BILLER PLATFORM AFTER SERVICE OPTIONS HAS BEEN ADDED +++ " + save);
                        } catch (Exception e) {
                            e.getStackTrace();
                        }
                    }
                }
            }
        }
    }

    private boolean getBillerPlatform() throws Exception {
        List<Biller> allBillers =
            billerService.findAll().stream().map(billerMapper::toEntity).collect(Collectors.toList());
        if (allBillers.size() == 0) {
            log.debug("BILLER SerVICE: Biller Service sized reached");
            return true;
        }
        for (Biller billerObject : allBillers) {
            Set<BillerPlatform> billerPlatforms = new HashSet<>();
            String billerID = billerObject.getBillerID();
            try {
                List<GetServiceResponseData> service = billerTransactionService.getService(String.valueOf(billerID));
                if (service != null) {
                    for (GetServiceResponseData getServiceResponseData : service) {
                        Optional<BillerPlatform> byBillerplatformOptional =
                            billerPlatformService.findByBillerplatformID(Long.valueOf(getServiceResponseData.getId()));
                        if (byBillerplatformOptional.isPresent()) {
                            log.error("DROPPING OUT OF LOOP, OBJECT ALREADY EXISTS");
                            continue;
                        }

                        BillerPlatform billerPlatform = new BillerPlatform();
                        billerPlatform.setBiller(billerObject);
                        billerPlatform.setBillerPlatform(getServiceResponseData.getName());
                        billerPlatform.setBillerplatformID(Long.valueOf(getServiceResponseData.getId()));

                        BillerPlatformDTO save = billerPlatformService.save(billerPlatformMapper.toDto(billerPlatform));
                        log.error("SAVED BILLER PLATFORM ++ " + save);

                        log.info("BILLER PLATFORM == " + billerPlatform);

                        billerPlatforms.add(billerPlatform);
                    }
                    billerObject.setBillerPlatforms(billerPlatforms);
                    BillerDTO save = billerService.save(billerMapper.toDto(billerObject));
                    log.error("SAVED BILLER AFTER PLATFORMS HAS BEEN ADDED +++ " + save);

                    log.info("BILLER PLATFORMs == " + billerPlatforms);
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
        return false;
    }

    private boolean getBillersToDb(List<BillerCategoryDTO> billerCategories) throws Exception {
        log.info("BILLER CATEGORIES ===+++++++==== " + billerCategories.toString());

        List<GetBillerResponseData> billers = billerTransactionService.getBillers();
        if (billers == null) {
            log.debug("GET BILLERS returns null ");
            return false;
        }
        log.info("BILLERS " + billers);
        for (GetBillerResponseData billerResponseData : billers) {
            Optional<Biller> billerOptional =
                billerService.findByBillerID(billerResponseData.getId());

            if (billerOptional.isPresent()) {
                log.error("DROPPING OUT OF LOOP, BILLER ALREADY EXISTS");
                continue;
            }
            Biller biller = new Biller();
            biller.setBiller(billerResponseData.getLabel());
            biller.setBillerID(billerResponseData.getId());
            biller.setPopular(getCounter() % 3 != 0);

//            biller.setBillerCategory(billerCategoryMapper.toEntity(billerCategories.get(getCounter())));
            BillerDTO save = billerService.save(billerMapper.toDto(biller));
            log.error("SAVED BILLER ++ " + save);
            log.info("BILLER == " + biller);
        }
        return false;
    }

    private int getCounter() {
        if (count < 3) {
            int counter = count;
            count = count + 1;
            return counter;
        } else {
            count = 0;
            return count;

        }
    }

    @Scheduled(cron = "0 20 23 24 1 ?")
    public void populateActualBalanceWithCurrentBalanceAndSetAccountStatus() {
        List<WalletAccount> all = walletAccountRepository.findAll();
        log.info("All wallets retrieved from db" + all);
        all.forEach(walletAccount -> {
            walletAccount.setActualBalance(walletAccount.getCurrentBalance());
            walletAccount.setStatus(AccountStatus.ACTIVE);
            log.info("retrieved wallet Account " + walletAccount);
            WalletAccount save = walletAccountRepository.save(walletAccount);
            log.info("Saved wallet Account " + save);
        });
    }

    @Scheduled(cron = "0 20 23 24 1 ?")
    public void populateJournalStatus() {
        List<Journal> all = journalRepository.findAll();
        all.forEach(journal -> {
            journal.setTransactionStatus(TransactionStatus.OK);
            Journal save = journalRepository.save(journal);
            log.info("Saved journal " + save);
        });
    }

    @Scheduled(cron = "0 20 23 24 1 ?")
    public void populateTransactionLogStatus() {
        List<FundDTO> all = transactionLogService.findAll();
        all.forEach(fundDTO -> {
            fundDTO.setStatus(TransactionStatus.COMPLETED);
            FundDTO save = transactionLogService.save(fundDTO);
            log.info("Saved TransactionLog " + save);
        });
    }
    @Scheduled(fixedRate = 200000000)
    public void createVATWalletAccount() throws URISyntaxException {

        WalletAccount vatWalletAccount = walletAccountService.findOneByAccountNumber(VAT_ACCOUNT);
        if (vatWalletAccount == null) {
            walletAccountService.addSpecialAccount("VAT Income Account", VAT_ACCOUNT);
        }
    }

    @Scheduled(fixedRate = 200000000)
    public void createCoralPayIncomeWalletAccount() throws URISyntaxException {

        WalletAccount vatWalletAccount = walletAccountService.findOneByAccountNumber(CORAL_PAY_USSD_FUNDING_ACCT);
        if (vatWalletAccount == null) {
            walletAccountService.addSpecialAccount("CoralPay Income Account", CORAL_PAY_USSD_FUNDING_ACCT);
        }
    }

    @Scheduled(fixedRate = 200000000)
    public void AddTellerWalletAccountType() throws URISyntaxException {

        Optional<WalletAccountTypeDTO> walletAccountTypeDTOOptional = walletAccountTypeService.findByAccountTypeId(5L);

        if (!walletAccountTypeDTOOptional.isPresent()) {
            WalletAccountTypeDTO walletAccountTypeDTO = new WalletAccountTypeDTO();
            walletAccountTypeDTO.setWalletAccountType("Teller");
            walletAccountTypeDTO.setAccountypeID(5L);
            WalletAccountTypeDTO save = walletAccountTypeService.save(walletAccountTypeDTO);

            log.debug("Saved WalletAccountType === " + save);
        }
    }

    @Scheduled(fixedRate = 200000000)
    public void AddIbileWalletAccountType() throws URISyntaxException {

        Optional<WalletAccountTypeDTO> walletAccountTypeDTOOptional = walletAccountTypeService.findByAccountTypeId(6L);

        if (!walletAccountTypeDTOOptional.isPresent()) {
            WalletAccountTypeDTO walletAccountTypeDTO = new WalletAccountTypeDTO();
            walletAccountTypeDTO.setWalletAccountType("Ibile");
            walletAccountTypeDTO.setAccountypeID(6L);
            WalletAccountTypeDTO save = walletAccountTypeService.save(walletAccountTypeDTO);

            log.debug("Saved WalletAccountType === " + save);
        }
    }

    @Scheduled(fixedRate = 200000000)
    public void createSpecialWalletAccount() {

        out.println("Creating sepcial account ==> ");
        out.println("Creating sepcial account2 ==> ");

        Map<String, String> accountsMap = new HashMap<>();

        accountsMap.put(ITEX_PAYABLE_ACCT, "Itex Biller Payable Wallet");
        accountsMap.put(LENDING_DISBURSEMENT_INCOME_ACCT, "Lending Disbursement Income Wallet");
        accountsMap.put(CORAL_PAY_USSD_FUNDING_ACCT, "Coral Pay USSD Funding Receivable Wallet");
        accountsMap.put(REMITA_WALLENCY_INCOME_ACCT, "Remita Wallency Income Wallet");
        accountsMap.put(ITEX_COMMISSION_ACCT, "Itex Commission Wallet");
        accountsMap.put(IBILE_HUB_COMMISSION_ACCT, "Alliance Ibile Hub Commission Wallet");
        accountsMap.put(LIRS_INCOME_ACCT, "LIRS Collection Payable Wallet");
        accountsMap.put(IBILE_HUB_INCOME_ACCT, "Alliance Ibile Hub Payable Wallet");
        accountsMap.put(VAT_ACCOUNT, "V.A.T Payable Wallet");
        accountsMap.put(CASH_CONNECT_INTERBANK_SERVICES_ACCT, "CashConnect Interbank Services Payable Wallet");
        accountsMap.put(MUTUAL_BENEFITS_INCOME_ACCT, "Mutual Benefits Payable Wallet");
        accountsMap.put(REMITA_WALLENCY_EXPENSE_ACCT, "Remita Wallency Expense Wallet");
        accountsMap.put(RPSL_NIN_VALIDATION_ACCT, "RPSL NIN Validation Payable Wallet");
        accountsMap.put(RSPL_INLINE_ACCT, "RPSL Inline Receivable Wallet");
        accountsMap.put(TELCO_MTN_SESSION_FEE_ACCT, "Telco MTN Session Payable Wallet");
        accountsMap.put(INTRODUCER_ACCT, "Introducer Payable Wallet");
        accountsMap.put(CASH_CONNECT_BVN_ACCT, "Cash Connect BVN Verification Payable Wallet");
        accountsMap.put(CASH_CONNECT_COMMISSION_ACCT, "Cash Connect Payable Wallet");
        accountsMap.put(CORRESPONDENCE_ACCOUNT, "Correspondence Account");
        accountsMap.put(TELCO_GLO_SESSION_FEE_ACCT, "Telco GLO Session Payable Wallet");
        accountsMap.put(CI_USSD_CHARGE_ACCT, "CI USSD Charge Payable Wallet");
        accountsMap.put(TELCO_9MOBILE_SESSION_FEE_ACCT, "Telco 9MOBILE Session Payable Wallet");
        accountsMap.put(TELCO_AIRTEL_SESSION_FEE_ACCT, "Telco AIRTEL Session Payable Wallet");
        accountsMap.put(RPSL_INTERBANK_SERVICES_ACCT, "RPSL Biller Payable Wallet");
        accountsMap.put(CUSTOMER_CORRESPONDENCE_ACCT, "Customer Correspondence Account");
        accountsMap.put(RSPL_BILLER_PAY_RRR_ACCT, "RPSL Biller Pay RRR Account");
        accountsMap.put(RSPL_BILLER_PAY_ELECTRICITY_ACCT, "RPSL Biller Pay Electricity Account");
        accountsMap.put(RSPL_BILLER_PAY_TV_SUBSCRIPTUION_ACCT, "RPSL Biller Pay TV Subscription Account");
        accountsMap.put(ITEX_CABLE_TV_ACCT, "Itex CableTv Account");
        accountsMap.put(ITEX_DATA_ACCT, "Itex Data Account");
        accountsMap.put(ITEX_AIRTIME_ACCT, "Itex Airtime Account");
        accountsMap.put(ITEX_INTERNET_ACCT, "Itex Internet Account");
        accountsMap.put(INSURANCE_COMMISSION_ACCT, "Insurance Commission Account");
        accountsMap.put(LIRS_SETTLEMENT_ACCT, "LIRS Settlement Account");
        accountsMap.put(ALLIANCE_IBILE_HUB_SETTLEMENT_ACCT, "Alliance Ibile Hub Settlement Account");
        accountsMap.put(ITEX_BILLER_SETTLEMENT_ACCT, "Itex Biller Settlement Account");
        accountsMap.put(FUOYE_SETTLEMENT_ACCT, "Fuoye Settlement Account");
        accountsMap.put(POLARIS_INTERBANK_SERVICES_ACCT, "Polaris Interbank Services Payable Wallet");
        accountsMap.put(POLARIS_COMMISSION_ACCT, "Polaris Commission Account");
        accountsMap.put(POLARIS_POMENGRANATE_COMMISSION_ACCT, "Polaris Pomengranate Commission Payable Wallet");
        accountsMap.put(HM_CORRESPONDENCE_ACCOUNT, "Human Manager Correspondence Wallet");
        accountsMap.put(HM_PROCESSING_FEE_ACCT, "Human Manager Processing Fee Wallet");
        accountsMap.put(HM_VAT_PAYABLE_ACCT, "Human Manager VAT Payable Wallet");
        accountsMap.put(LENDING_HUB_FEE_ACCT, "Lending Hub Fee Account");
        accountsMap.put(POLARIS_CARD_WALLET, "Polaris Debit Card Payable Wallet");

        for (Map.Entry<String, String> entry : accountsMap.entrySet()) {
            createSpecialAccount(entry.getKey(), entry.getValue());
        }

    }

    @Scheduled(fixedRate = 200000000)
    public void renameSpecialWalletAccount() {

        Map<String, String> accountsMap = new HashMap<>();

        accountsMap.put(ITEX_PAYABLE_ACCT, "Itex Biller Payable Wallet");
        accountsMap.put(LENDING_DISBURSEMENT_INCOME_ACCT, "Lending Disbursement Income Wallet");
        accountsMap.put(CORAL_PAY_USSD_FUNDING_ACCT, "Coral Pay USSD Funding Receivable Wallet");
        accountsMap.put(REMITA_WALLENCY_INCOME_ACCT, "Pouchii Income Wallet");
        accountsMap.put(ITEX_COMMISSION_ACCT, "Itex Commission Wallet");
        accountsMap.put(IBILE_HUB_COMMISSION_ACCT, "Alliance Ibile Hub Commission Wallet");
        accountsMap.put(LIRS_INCOME_ACCT, "LIRS Collection Payable Wallet");
        accountsMap.put(IBILE_HUB_INCOME_ACCT, "Alliance Ibile Hub Payable Wallet");
        accountsMap.put(VAT_ACCOUNT, "V.A.T Payable Wallet");
        accountsMap.put(CASH_CONNECT_INTERBANK_SERVICES_ACCT, "CashConnect Interbank Services Payable Wallet");
        accountsMap.put(MUTUAL_BENEFITS_INCOME_ACCT, "Mutual Benefits Payable Wallet");
        accountsMap.put(REMITA_WALLENCY_EXPENSE_ACCT, "Pouchii Expense Wallet");
        accountsMap.put(RPSL_NIN_VALIDATION_ACCT, "RPSL NIN Validation Payable Wallet");
        accountsMap.put(RSPL_INLINE_ACCT, "RPSL Inline Receivable Wallet");
        accountsMap.put(TELCO_MTN_SESSION_FEE_ACCT, "Telco MTN Session Payable Wallet");
        accountsMap.put(INTRODUCER_ACCT, "Introducer Payable Wallet");
        accountsMap.put(CASH_CONNECT_BVN_ACCT, "Cash Connect BVN Verification Payable Wallet");
        accountsMap.put(CASH_CONNECT_COMMISSION_ACCT, "Cash Connect Payable Wallet");
        accountsMap.put(CORRESPONDENCE_ACCOUNT, "Correspondence Account");
        accountsMap.put(TELCO_GLO_SESSION_FEE_ACCT, "Telco GLO Session Payable Wallet");
        accountsMap.put(CI_USSD_CHARGE_ACCT, "CI USSD Charge Payable Wallet");
        accountsMap.put(TELCO_9MOBILE_SESSION_FEE_ACCT, "Telco 9MOBILE Session Payable Wallet");
        accountsMap.put(TELCO_AIRTEL_SESSION_FEE_ACCT, "Telco AIRTEL Session Payable Wallet");
        accountsMap.put(RPSL_INTERBANK_SERVICES_ACCT, "RPSL Biller Payable Wallet");
        accountsMap.put(CUSTOMER_CORRESPONDENCE_ACCT, "Customer Correspondence Account");
        accountsMap.put(RSPL_BILLER_PAY_RRR_ACCT, "RPSL Biller Pay RRR Account");
        accountsMap.put(RSPL_BILLER_PAY_ELECTRICITY_ACCT, "RPSL Biller Pay Electricity Account");
        accountsMap.put(RSPL_BILLER_PAY_TV_SUBSCRIPTUION_ACCT, "RPSL Biller Pay TV Subscription Account");
        accountsMap.put(ITEX_CABLE_TV_ACCT, "Itex CableTv Account");
        accountsMap.put(ITEX_DATA_ACCT, "Itex Data Account");
        accountsMap.put(ITEX_AIRTIME_ACCT, "Itex Airtime Account");
        accountsMap.put(ITEX_INTERNET_ACCT, "Itex Internet Account");
        accountsMap.put(INSURANCE_COMMISSION_ACCT, "Insurance Commission Account");
        accountsMap.put(POLARIS_COMMISSION_ACCT, "Polaris Commission Payable Wallet");


        for (Map.Entry<String, String> entry : accountsMap.entrySet()) {
            renameSpecialAccount(entry.getKey(), entry.getValue());
        }

    }

    private void createSpecialAccount(String accountNumber, String accountName) {
        WalletAccount vatWalletAccount = walletAccountService.findOneByAccountNumber(accountNumber);
        if (vatWalletAccount == null) {
            try {
                walletAccountService.addSpecialAccount(accountName, accountNumber);
            } catch (URISyntaxException e) {
                e.printStackTrace();
                out.println(" createSpecialAccount exception ===> " + e.getLocalizedMessage());
            }
        }
        out.println(" Failed to create Special wallet ===> " + accountName);

    }


    private void renameSpecialAccount(String accountNumber, String accountName) {
        try {
            walletAccountService.renameSpecialAccount(accountName, accountNumber);
        } catch (Exception e) {
            e.printStackTrace();
            out.println("renameSpecialAccount exception ===> " + e.getLocalizedMessage());
        }

    }

    @Scheduled(fixedRate = 200000000)
    public void populateSpecialAccountNuban() {
        Map<String, String> specialAccountNumbers = new HashMap<>();
        specialAccountNumbers.put(INTRODUCER_ACCT, "2700018189");
        specialAccountNumbers.put(REMITA_WALLENCY_INCOME_ACCT, "2300018187");
        specialAccountNumbers.put(REMITA_WALLENCY_EXPENSE_ACCT, "1100014368");
        specialAccountNumbers.put("1000000009", "1900018188");
        specialAccountNumbers.put(LIRS_INCOME_ACCT, "1500018186");
        specialAccountNumbers.put(IBILE_HUB_COMMISSION_ACCT, "1200018187");
        specialAccountNumbers.put(IBILE_HUB_INCOME_ACCT, "1200017049");
        specialAccountNumbers.put(ITEX_COMMISSION_ACCT, "1600018189");
        specialAccountNumbers.put(CASH_CONNECT_COMMISSION_ACCT, "2400018180");
        specialAccountNumbers.put(MUTUAL_BENEFITS_INCOME_ACCT, "2500018183");
        specialAccountNumbers.put(ITEX_PAYABLE_ACCT, "2000018188");
        specialAccountNumbers.put(VAT_ACCOUNT, "2200018184");
        specialAccountNumbers.put(CORAL_PAY_USSD_FUNDING_ACCT, "1700018182");

        specialAccountNumbers.forEach((walletAccountNumber, nubanAccountNumber) -> {

            WalletAccount walletAccount = walletAccountService.findOneByAccountNumber(walletAccountNumber);
            out.println("Retrieved walletAccount ===> " + walletAccount);
            if (walletAccount != null) {
                out.println("Wallet Account not Null ===> ");
                walletAccount.setNubanAccountNo(nubanAccountNumber);

                out.println("Wallet Account before persistence ===> " + walletAccount);
                WalletAccount save = walletAccountService.save(walletAccount);
                out.println("Wallet Account after persistence ===> " + save);

            }
        });
    }


    private final IbileService ibileService;
    @Scheduled(fixedRate = 600000)
    public void refireIbileReceipts(){
        Instant fromInstant = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant toInstant = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).atOffset(ZoneOffset.UTC).with(LocalTime.of(23,59,59,999999999))
            .toInstant();
        List<FundDTO> fundDTOS = transactionLogService.findAllByCreatedDateBetween(fromInstant, toInstant)
            .stream()
            .filter(f -> f.getSpecificChannel().trim().equalsIgnoreCase("Ibile"))
            .collect(Collectors.toList());
        for(FundDTO fundDTO : fundDTOS){
            GenericResponseDTO genericResponseDTO = ibileService.generateAgentReceipt(fundDTO.getRrr());
            out.println("Generated Receipt ==> "+genericResponseDTO);
        }
    }

}
