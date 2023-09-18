package ng.com.systemspecs.apigateway.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import ng.com.systemspecs.apigateway.domain.*;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.dto.BillerCategoryDTO;
import ng.com.systemspecs.apigateway.service.dto.BillerDTO;
import ng.com.systemspecs.apigateway.service.dto.BillerPlatformDTO;
import ng.com.systemspecs.apigateway.service.mapper.BillerCategoryMapper;
import ng.com.systemspecs.apigateway.service.mapper.BillerMapper;
import ng.com.systemspecs.apigateway.service.mapper.BillerPlatformMapper;
import ng.com.systemspecs.remitabillinggateway.billers.GetBillerResponse;
import ng.com.systemspecs.remitabillinggateway.billers.GetBillerResponseData;
import ng.com.systemspecs.remitabillinggateway.customfields.GetCustomFieldResponse;
import ng.com.systemspecs.remitabillinggateway.customfields.GetCustomFieldResponseData;
import ng.com.systemspecs.remitabillinggateway.customfields.GetCustomFieldResponseDropDown;
import ng.com.systemspecs.remitabillinggateway.servicetypes.GetServiceResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetTelcoServicesSchedular {

    private static final int count = 0;
    private final Logger log = LoggerFactory.getLogger(GetTelcoServicesSchedular.class);
    private final BillerTransactionService billerTransactionService;
    private final BillerCustomFieldOptionService billerCustomFieldOptionService;
    private final BillerService billerService;
    private final BillerMapper billerMapper;
    private final BillerPlatformService billerPlatformService;
    private final BillerPlatformMapper billerPlatformMapper;
    private final BillerCategoryMapper billerCategoryMapper;
    private final BillerServiceOptionService billerServiceOptionService;
    private final BillerCategoryService billerCategoryService;
    private final CustomFieldDropdownService customFieldDropdownService;

    BillerCategoryDTO billerCategoryTechoAir;
    BillerCategoryDTO billerCategoryPower;
    BillerCategoryDTO billerCategoryUtil;
    BillerCategoryDTO billerCategoryTelcom;
    BillerCategoryDTO billerCategoryTechoDAta;


    public GetTelcoServicesSchedular(BillerTransactionService billerTransactionService, BillerCustomFieldOptionService billerCustomFieldOptionService, BillerService billerService, BillerMapper billerMapper,
                                     BillerPlatformService billerPlatformService, BillerPlatformMapper billerPlatformMapper,
                                     BillerCategoryMapper billerCategoryMapper, BillerServiceOptionService billerServiceOptionService, BillerCategoryService billerCategoryService, CustomFieldDropdownService customFieldDropdownService) {

        this.billerTransactionService = billerTransactionService;
        this.billerCustomFieldOptionService = billerCustomFieldOptionService;
        this.billerService = billerService;
        this.billerMapper = billerMapper;
        this.billerPlatformService = billerPlatformService;
        this.billerPlatformMapper = billerPlatformMapper;
        this.billerCategoryMapper = billerCategoryMapper;
        this.billerServiceOptionService = billerServiceOptionService;
        this.billerCategoryService = billerCategoryService;
        this.customFieldDropdownService = customFieldDropdownService;

    }


//    @Scheduled(fixedRate = 1000 * 60 * 60 * 60 * 7)
    public void populateDbWithBillersFromIPG() throws Exception {
        ArrayList<String> billerIds = new ArrayList<>();
        billerIds.add("C0000288503");


        for (String billerId : billerIds) {
            GetBillerResponse allBillersFromIpg = billerTransactionService.getAllBillersFromIpg();
            if (allBillersFromIpg != null) {
                List<GetBillerResponseData> responseData = allBillersFromIpg.getResponseData();
                if (responseData != null) {
                    for (GetBillerResponseData response : responseData) {
                        if (billerId.equalsIgnoreCase(response.getId())) {
                            BillerDTO billerDTO = new BillerDTO();
                            billerDTO.setBillerCategoryId(52053L);
                            billerDTO.setBiller(response.getLabel());
                            billerDTO.setBillerID(response.getId());
                            billerDTO.setPopular(true);
                            BillerDTO save = billerService.save(billerDTO);

                            log.info("SAVED BILLER ======> " + save);

                            getBillerPlatform(billerId);
                        }
                    }
                }
            }
        }
    }


    //    @Scheduled(fixedRate = 1000 * 60 * 60 * 60 * 7)
    public void populateDbWithBillers() throws Exception {
        ArrayList<String> billerIds = new ArrayList<>();
//        billerIds.add("C0000264104");
//        billerIds.add("C0000263754");
//        billerIds.add("C0000264103");
//        billerIds.add("C0000264101");
//        billerIds.add("C0000264107");
        billerIds.add("C0000268894");

        ArrayList<String> billerIDs = new ArrayList<>();

        /*List<GetBillerResponseData> billers = billerTransactionService.getBillers();
        billers.forEach(data -> {
            String id = data.getId();
            billerIDs.add(id);
        });
*/
        for (String billerId : billerIds) {
            getBillerPlatform(billerId);
        }
    }


    private void getBillerPlatform(String billerId) {

        Optional<Biller> billerOptional = billerService.findByBillerID(billerId);
        Biller biller = billerOptional.get();

        Set<BillerPlatform> billerPlatforms = null;

        List<GetServiceResponseData> service = billerTransactionService.getService(String.valueOf(billerId));
        if (service != null) {

            for (GetServiceResponseData getServiceResponseData : service) {

                try {
                    Optional<BillerPlatform> byBillerplatformID = billerPlatformService.findByBillerplatformID(Long.valueOf(getServiceResponseData.getId()));
                    if (byBillerplatformID.isPresent()) {
                        log.info("DROPPING OUT OF LOOP, BILLER PLATFORM ALREADY EXISTS " + byBillerplatformID.get());
                        getCustomFields(byBillerplatformID.get());
                    } else {
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

            if (allByBillerPlatformId.size() < 1) {
                BillerCustomFieldOption billerCustomFieldOption = new BillerCustomFieldOption();
                billerCustomFieldOption.setHasFixedPrice(customFieldResponse.getFixedPrice());
                billerCustomFieldOption.setFixedAmount(customFieldResponse.getFixedAmount());
                billerCustomFieldOption.setBillerPlatform(e);
                billerCustomFieldOption.setCurrency(customFieldResponse.getCurrency());
                billerCustomFieldOption.setAcceptPartPayment(customFieldResponse.getAcceptPartPayment());

                BillerCustomFieldOption save = billerCustomFieldOptionService.save(billerCustomFieldOption);

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
                log.info("DROPPING OUT OF LOOP, CUSTOM FIELD OPTION ALREADY EXISTS " + byServiceOptionId.get(0));
                if (data.getCustomFieldDropDown() != null && data.getCustomFieldDropDown().size() > 0) {
                    setDropDownList(byServiceOptionId.get(0), data.getCustomFieldDropDown());
                }
            } else {

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

                log.info("DROPPING OUT OF LOOP, CUSTOM FIELD DROPDOWN ALREADY EXISTS " + dropdown);


            } else {
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

        }

        if (customFieldDropdowns != null) {
            billerServiceOption.setCustomFieldDropdowns(customFieldDropdowns);
            billerServiceOptionService.save(billerServiceOption);
        }
    }

}
