package ng.com.systemspecs.apigateway.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.systemspecs.apigateway.repository.BillerCustomFieldOptionRepository;
import ng.com.systemspecs.apigateway.repository.BillerPlatformRepository;
import ng.com.systemspecs.apigateway.repository.BillerServiceOptionRepository;
import ng.com.systemspecs.apigateway.repository.CustomFieldDropdownRepository;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.dto.BillerCategoryDTO;
import ng.com.systemspecs.apigateway.service.dto.BillerDTO;
import ng.com.systemspecs.apigateway.service.dto.BillerPlatformDTO;
import ng.com.systemspecs.apigateway.service.mapper.BillerCategoryMapper;
import ng.com.systemspecs.apigateway.service.mapper.BillerMapper;
import ng.com.systemspecs.apigateway.service.mapper.BillerPlatformMapper;
import ng.com.systemspecs.remitabillinggateway.customfields.GetCustomFieldResponse;
import ng.com.systemspecs.remitabillinggateway.customfields.GetCustomFieldResponseData;
import ng.com.systemspecs.remitabillinggateway.customfields.GetCustomFieldResponseDropDown;
import ng.com.systemspecs.remitabillinggateway.servicetypes.GetServiceResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class ServicesSchedular {

    private static final int count = 0;
    private final Logger log = LoggerFactory.getLogger(ServicesSchedular.class);
    private final BillerTransactionService billerTransactionService;
    private final BillerCustomFieldOptionService billerCustomFieldOptionService;
    private final BillerCustomFieldOptionRepository billerCustomFieldOptionRepository;
    private final BillerService billerService;
    private final BillerMapper billerMapper;
    private final BillerPlatformService billerPlatformService;
    private final BillerPlatformRepository billerPlatformRepository;
    private final BillerPlatformMapper billerPlatformMapper;
    private final BillerCategoryMapper billerCategoryMapper;
    private final BillerServiceOptionService billerServiceOptionService;
    private final BillerServiceOptionRepository billerServiceOptionRepository;
    private final BillerCategoryService billerCategoryService;
    private final CustomFieldDropdownService customFieldDropdownService;
    private final CustomFieldDropdownRepository customFieldDropdownRepository;

    BillerCategoryDTO billerCategoryTechoAir;
    BillerCategoryDTO billerCategoryPower;
    BillerCategoryDTO billerCategoryUtil;
    BillerCategoryDTO billerCategoryTelcom;
    BillerCategoryDTO billerCategoryTechoDAta;

    BillerDTO DSTVBiller, GOTVBiller, SMILEBiller;


    public ServicesSchedular(BillerTransactionService billerTransactionService, BillerCustomFieldOptionService billerCustomFieldOptionService, BillerCustomFieldOptionRepository billerCustomFieldOptionRepository, BillerService billerService, BillerMapper billerMapper,
                             BillerPlatformService billerPlatformService, BillerPlatformRepository billerPlatformRepository, BillerPlatformMapper billerPlatformMapper,
                             BillerCategoryMapper billerCategoryMapper, BillerServiceOptionService billerServiceOptionService, BillerServiceOptionRepository billerServiceOptionRepository, BillerCategoryService billerCategoryService, CustomFieldDropdownService customFieldDropdownService, CustomFieldDropdownRepository customFieldDropdownRepository) {

        this.billerTransactionService = billerTransactionService;
        this.billerCustomFieldOptionService = billerCustomFieldOptionService;
        this.billerCustomFieldOptionRepository = billerCustomFieldOptionRepository;
        this.billerService = billerService;
        this.billerMapper = billerMapper;
        this.billerPlatformService = billerPlatformService;
        this.billerPlatformRepository = billerPlatformRepository;
        this.billerPlatformMapper = billerPlatformMapper;
        this.billerCategoryMapper = billerCategoryMapper;
        this.billerServiceOptionService = billerServiceOptionService;
        this.billerServiceOptionRepository = billerServiceOptionRepository;
        this.billerCategoryService = billerCategoryService;
        this.customFieldDropdownService = customFieldDropdownService;

        this.customFieldDropdownRepository = customFieldDropdownRepository;
    }


//    @Scheduled(fixedRate = 1000 * 60 * 60 * 60 * 7)
    public void populateDbWithBillers() throws Exception {
        DSTVBiller = createBiller("TV", "DSTV", "DSTV");
        GOTVBiller = createBiller("TV", "GOTV", "GOTV");
        SMILEBiller = createBiller("ISP", "SMILE", "SMILE");
//        Optional<Biller> gotvOptional = billerService.findByBillerID("GOTV");
//        Optional<Biller> gotvOptional = billerService.findByBillerID("DSTV");

        ArrayList<Biller> billerIds = new ArrayList<>();
//        billerIds.add("C0000264104");
//        billerIds.add("C0000263754");
//        billerIds.add("C0000264103");
//        billerIds.add("C0000264101");
//        billerIds.add("C0000264107");
//        billerIds.add("BP");
        billerIds.add(billerMapper.toEntity(DSTVBiller));
        billerIds.add(billerMapper.toEntity(GOTVBiller));
        billerIds.add(billerMapper.toEntity(SMILEBiller));

        for (Biller biller : billerIds) {
            getBillerPlatform(biller);
        }
    }

    private BillerDTO createBiller(String category, String billerName, String billerId) {
        BillerDTO biller = new BillerDTO();
        Optional<BillerCategory> byBillerCategory = billerCategoryService.findByBillerCategory(category);
        BillerCategory billerCategory = byBillerCategory.get();
        biller.setBillerCategoryId(billerCategory.getId());
        biller.setBiller(billerName);
        biller.setPopular(true);
        biller.setBillerID(billerId);

        return billerService.save(biller);
    }

    private void getBillerPlatform(Biller biller) {
        ArrayList<String> GOTVServiceId = new ArrayList<>();
        GOTVServiceId.add("GOTV JOLLI");
        GOTVServiceId.add("GOTV JINJA");
        GOTVServiceId.add("GOTV LITE");
        GOTVServiceId.add("GOTV MAX");

        ArrayList<String> DSTVServiceId = new ArrayList<>();
        DSTVServiceId.add("PADI");
        DSTVServiceId.add("YANGA   XTRAVIEW");
        DSTVServiceId.add("ASIAN ADD-ON");
        DSTVServiceId.add("COMPACT   ASIA");
        DSTVServiceId.add("COMPACT   FRENCH PLUS");
        DSTVServiceId.add("COMPACT   FRENCH TOUCH");
        DSTVServiceId.add("COMPACT   HD/EXTRAVIEW ");
        DSTVServiceId.add("CONFAM   XTRAVIEW");
        DSTVServiceId.add("DSTV COMPACT");
        DSTVServiceId.add("DSTV COMPACT PLUS");
        DSTVServiceId.add("DSTV PREMIUM");
        DSTVServiceId.add("DSTV PREMIUM ASIA");
        DSTVServiceId.add("ASIA BOUQET");
        DSTVServiceId.add("DSTV YANGA BOUQET E36");
        DSTVServiceId.add("DSTV CONFAM BOUQET E36");
        DSTVServiceId.add("DSTV GREAT WALL STANDALONE BOUQET");
        DSTVServiceId.add("COMPACT PLUS   ASIA");
        DSTVServiceId.add("COMPACTPLUS   FRENCH PLUS ");
        DSTVServiceId.add("COMPACTPLUS   FRENCH TOUCH ");
        DSTVServiceId.add("COMPACT PLUS   HD/EXTRAVIEW ");
        DSTVServiceId.add("HDPVR/XTRAVIEW");
        DSTVServiceId.add("FRENCH 11 BOUQUET E36");
        DSTVServiceId.add("FRENCH TOUCH");
        DSTVServiceId.add("FRENCH PLUS");
        DSTVServiceId.add("PADI   XTRAVIEW");
        DSTVServiceId.add("PREMIUM   FRENCH");
        DSTVServiceId.add("PREMIUM   XTRAVIEW");
        DSTVServiceId.add("PREMIUM ASIA   HD/EXTRAVIEW");

        List<BillerPlatform> allByBiller = billerPlatformService.findAllByBiller(biller);
        if (!allByBiller.isEmpty()) {
            for (BillerPlatform billerPlatform : allByBiller) {
                BillerCustomFieldOption billerCustomFieldOption = billerPlatform.getBillerCustomFieldOption();
                Set<BillerServiceOption> billerServiceOptions = billerCustomFieldOption.getBillerServiceOptions();
                if (!billerServiceOptions.isEmpty()) {
                    for (BillerServiceOption billerServiceOption : billerServiceOptions) {
                        Set<CustomFieldDropdown> customFieldDropdowns = billerServiceOption.getCustomFieldDropdowns();
                        if (!customFieldDropdowns.isEmpty()) {
                            customFieldDropdownRepository.deleteAll(customFieldDropdowns);
                        }
                    }
                    billerServiceOptionRepository.deleteAll(billerServiceOptions);
                }
                billerCustomFieldOptionRepository.delete(billerCustomFieldOption);
            }
            billerPlatformRepository.deleteAll(allByBiller);
        }


        Set<BillerPlatform> billerPlatforms = null;

        List<GetServiceResponseData> service = billerTransactionService.getService("BP");

        if (service != null) {

            for (GetServiceResponseData getServiceResponseData : service) {

                try {
                    if (biller.getBillerID().equalsIgnoreCase("DSTV")) {

                        for (String name : DSTVServiceId) {
                            if (getServiceResponseData.getName().equalsIgnoreCase(name)) {

                                billerPlatforms = new HashSet<>();
                                BillerPlatform billerPlatform = new BillerPlatform();
                                billerPlatform.setBiller(biller);
                                billerPlatform.setBillerPlatform(getServiceResponseData.getName());
                                billerPlatform.setBillerplatformID(Long.valueOf(getServiceResponseData.getId()));

                                BillerPlatformDTO save = billerPlatformService.save(billerPlatformMapper.toDto(billerPlatform));
                                log.error("SAVED BILLER PLATFORM ++ " + save);

                                log.info("BILLER PLATFORM == " + billerPlatform);

                                BillerPlatform e = billerPlatformMapper.toEntity(save);
                                billerPlatforms.add(e);

                                getCustomFields(e);
                            }
                        }
                    } else if (biller.getBillerID().equalsIgnoreCase("GOTV")) {

                        for (String name : GOTVServiceId) {
                            if (getServiceResponseData.getName().equalsIgnoreCase(name)) {
                                billerPlatforms = new HashSet<>();
                                BillerPlatform billerPlatform = new BillerPlatform();
                                billerPlatform.setBiller(biller);
                                billerPlatform.setBillerPlatform(getServiceResponseData.getName());
                                billerPlatform.setBillerplatformID(Long.valueOf(getServiceResponseData.getId()));

                                BillerPlatformDTO save = billerPlatformService.save(billerPlatformMapper.toDto(billerPlatform));
                                log.error("SAVED BILLER PLATFORM ++ " + save);

                                log.info("BILLER PLATFORM == " + billerPlatform);

                                BillerPlatform e = billerPlatformMapper.toEntity(save);
                                billerPlatforms.add(e);

                                getCustomFields(e);
                            }
                        }
                    } else if (biller.getBillerID().equalsIgnoreCase("SMILE")) {
                        if (getServiceResponseData.getName().startsWith("SMILE")) {
                            billerPlatforms = new HashSet<>();
                            BillerPlatform billerPlatform = new BillerPlatform();
                            billerPlatform.setBiller(biller);
                            billerPlatform.setBillerPlatform(getServiceResponseData.getName());
                            billerPlatform.setBillerplatformID(Long.valueOf(getServiceResponseData.getId()));

                            BillerPlatformDTO save = billerPlatformService.save(billerPlatformMapper.toDto(billerPlatform));
                            log.error("SAVED BILLER PLATFORM ++ " + save);

                            log.info("BILLER PLATFORM == " + billerPlatform);

                            BillerPlatform e = billerPlatformMapper.toEntity(save);
                            billerPlatforms.add(e);

                            getCustomFields(e);
                        }
                    }
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
            if (billerPlatforms != null) {

                biller.setBillerPlatforms(billerPlatforms);
                BillerDTO save = billerService.save(billerMapper.toDto(biller));

                log.error("SAVED BILLER AFTER PLATFORMS HAS BEEN ADDED +++ " + save);

                log.info("BILLER PLATFORMs == " + billerPlatforms);
            }
        }
    }

    private void getCustomFields(BillerPlatform e) {
        try {
            GetCustomFieldResponse customFieldResponse = billerTransactionService.getCustomField(String.valueOf(e.getBillerplatformID()));
            log.info("CUSTOM FIELD RESPONSE ===  " + new ObjectMapper().writeValueAsString(customFieldResponse));

            List<BillerCustomFieldOption> allByBillerPlatformId = billerCustomFieldOptionService.findAllByBillerPlatformId(e.getBillerplatformID());

            if (!allByBillerPlatformId.isEmpty()) {
                billerCustomFieldOptionRepository.deleteAll(allByBillerPlatformId);
            }

            if (allByBillerPlatformId.isEmpty()) {
                BillerCustomFieldOption billerCustomFieldOption = new BillerCustomFieldOption();
                billerCustomFieldOption.setHasFixedPrice(customFieldResponse.getFixedPrice());
                billerCustomFieldOption.setFixedAmount(customFieldResponse.getFixedAmount());
                billerCustomFieldOption.setBillerPlatform(e);
                billerCustomFieldOption.setCurrency(customFieldResponse.getCurrency());
                billerCustomFieldOption.setAcceptPartPayment(customFieldResponse.getAcceptPartPayment());

                BillerCustomFieldOption save = billerCustomFieldOptionService.save(billerCustomFieldOption);

                e.setBillerCustomFieldOptions(save);
                billerPlatformService.save(billerPlatformMapper.toDto(e));

                if (customFieldResponse.getResponseData() != null && customFieldResponse.getResponseData().size() > 0) {
                    log.info("CUSTOM FIELD RESPONSE ===  " + new ObjectMapper().writeValueAsString(customFieldResponse));

                    setCustomFieldOptionsList(save, customFieldResponse.getResponseData());
                }

            } else {
                log.info("DROPPING OUT OF LOOP, CUSTOM FIELDS ALREADY EXISTS");
                setCustomFieldOptionsList(allByBillerPlatformId.get(0), customFieldResponse.getResponseData());
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void setCustomFieldOptionsList(BillerCustomFieldOption save, List<GetCustomFieldResponseData> responseData) {

        HashSet<BillerServiceOption> billerServiceOptions = null;
        for (GetCustomFieldResponseData data : responseData) {
            List<BillerServiceOption> byServiceOptionId = billerServiceOptionService.findByServiceOptionId(Long.valueOf(data.getId()));

            if (!byServiceOptionId.isEmpty()) {
                billerServiceOptionRepository.deleteAll(byServiceOptionId);
            }

            billerServiceOptions = new HashSet<>();
            BillerServiceOption serviceOption = new BillerServiceOption();
            serviceOption.setName(data.getColumnName());
            serviceOption.setType(data.getColumnType());
            serviceOption.setRequired(data.getRequired());
            serviceOption.setLength(data.getColumnLength());
            serviceOption.setServiceOptionId(Long.parseLong(data.getId()));
            serviceOption.setBillerCustomFieldOption(save);

                BillerServiceOption billerServiceOption = billerServiceOptionService.save(serviceOption);

                billerServiceOptions.add(billerServiceOption);

                if (data.getCustomFieldDropDown() != null && data.getCustomFieldDropDown().size() > 0) {
                    setDropDownList(billerServiceOption, data.getCustomFieldDropDown());
                }
        }

        if (billerServiceOptions != null) {
            save.setBillerServiceOptions(billerServiceOptions);
            billerCustomFieldOptionService.save(save);
        }
    }

    private void setDropDownList(BillerServiceOption billerServiceOption, List<GetCustomFieldResponseDropDown> responseData) {
        HashSet<CustomFieldDropdown> customFieldDropdowns = null;

        for (GetCustomFieldResponseDropDown data : responseData) {
            CustomFieldDropdown dropdown = customFieldDropdownService.findById(Long.valueOf(data.getId()));

            if (dropdown != null) {

                customFieldDropdownRepository.delete(dropdown);
                log.info("DROPPING OUT OF LOOP, CUSTOM FIELD DROPDOWN ALREADY EXISTS " + dropdown);


            }
                customFieldDropdowns = new HashSet<>();

                CustomFieldDropdown customFieldDropdown = new CustomFieldDropdown();
                customFieldDropdown.setFieldDropdownId(data.getId());
                customFieldDropdown.setUnitprice(data.getUnitprice());
                customFieldDropdown.setFixedprice(data.getFixedprice());
                customFieldDropdown.setDescription(data.getDescription());
                customFieldDropdown.setCode(data.getCode());
                customFieldDropdown.setAccountid(data.getAccountid());
                customFieldDropdown.setBillerServiceOption(billerServiceOption);

                CustomFieldDropdown save = customFieldDropdownService.save(customFieldDropdown);
                customFieldDropdowns.add(save);

        }

        if (customFieldDropdowns != null) {
            billerServiceOption.setCustomFieldDropdowns(customFieldDropdowns);
            billerServiceOptionService.save(billerServiceOption);
        }
    }

}
