package ng.com.justjava.corebanking.service.impl;


import ng.com.justjava.corebanking.domain.*;
import ng.com.justjava.corebanking.repository.BillerCustomFieldOptionRepository;
import ng.com.justjava.corebanking.repository.BillerPlatformRepository;
import ng.com.justjava.corebanking.repository.BillerRepository;
import ng.com.justjava.corebanking.service.mapper.BillerMapper;
import ng.com.justjava.corebanking.domain.*;
import ng.com.justjava.corebanking.service.BillerCategoryService;
import ng.com.justjava.corebanking.service.BillerPlatformService;
import ng.com.justjava.corebanking.service.BillerService;
import ng.com.justjava.corebanking.service.BillerServiceOptionService;
import ng.com.justjava.corebanking.service.dto.BillerDTO;
import ng.com.systemspecs.remitabillinggateway.billers.GetBillerResponse;
import ng.com.systemspecs.remitabillinggateway.configuration.Credentials;
import ng.com.systemspecs.remitabillinggateway.customfields.GetCustomFieldResponse;
import ng.com.systemspecs.remitabillinggateway.service.RemitaBillingGatewayService;
import ng.com.systemspecs.remitabillinggateway.service.impl.RemitaBillingGatewayServiceImpl;
import ng.com.systemspecs.remitabillinggateway.servicetypes.GetServiceResponse;
import ng.com.systemspecs.remitabillinggateway.util.EnvironmentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing {@link Biller}.
 */
@Service
@Transactional
public class BillerServiceImpl implements BillerService {

    private final Logger log = LoggerFactory.getLogger(BillerServiceImpl.class);

    private final BillerRepository billerRepository;
    private final BillerCategoryService billerCategoryService;
    private final BillerPlatformService billerPlatformService;
    private final BillerServiceOptionService billerServiceOptionService;
    private final BillerCustomFieldOptionRepository billerCustomFieldOptionRepository;
    private final BillerPlatformRepository billerPlatformRepository;

    private final BillerMapper billerMapper;

    public BillerServiceImpl(BillerRepository billerRepository, BillerCategoryService billerCategoryService,
                             BillerPlatformService billerPlatformService, BillerServiceOptionService billerServiceOptionService, BillerCustomFieldOptionRepository billerCustomFieldOptionRepository, BillerPlatformRepository billerPlatformRepository, BillerMapper billerMapper) {
        this.billerRepository = billerRepository;
        this.billerCategoryService = billerCategoryService;
        this.billerPlatformService = billerPlatformService;
        this.billerServiceOptionService = billerServiceOptionService;
        this.billerCustomFieldOptionRepository = billerCustomFieldOptionRepository;
        this.billerPlatformRepository = billerPlatformRepository;
        this.billerMapper = billerMapper;
    }


    @Override
    public   RemitaBillingGatewayService  getRemitaBillingGatewayService() {
    	Credentials credentials = new Credentials();
    	credentials.setPublicKey("MjMyfDQwODE4MzI3fGYyNjU3N2RjMGRjZGE1ZmExYmQ4YzU2M2I0ZjIxMDE0Yzc5MzQ5NjVmYzYxNWJjOWRkZjM2NjM5ZTg3ZTE2ZjQ1MzcxMjVmZjJlMzlmOGI2MjkzMGRhZjc2NTZiNzdjYTZkZGQwZDczZjIxZjA4ZDVlZTQ0NzZiZmY3MzAyZDA0");
    	credentials.setSecretKey("80bcb41920b30f27ac0fab456c5d79d0a58e622192013649b39d690ddacc8cc2fe37348406339c97c677f4de43bb1137527c3f5e25fd8a5c4bb7ec7ca5fc24af");
    	credentials.setTransactionId(String.valueOf(System.currentTimeMillis()));
    	credentials.setEnvironment(EnvironmentType.DEMO);

    	return  new RemitaBillingGatewayServiceImpl(credentials);
    }



    @Override
    public BillerDTO save(BillerDTO billerDTO) {
        log.debug("Request to save Biller : {}", billerDTO);
        Biller biller = billerMapper.toEntity(billerDTO);
        biller = billerRepository.save(biller);
        return billerMapper.toDto(biller);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BillerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Billers");
        return billerRepository.findAll(pageable)
            .map(billerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillerDTO> findAll() {
        log.debug("Request to get all Billers");
        List<Biller> all = billerRepository.findAll();
        return all.stream().map(billerMapper::toDto).collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BillerDTO> findOne(Long id) {
        log.debug("Request to get Biller : {}", id);
        return billerRepository.findById(id)
            .map(billerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Biller : {}", id);
        billerRepository.deleteById(id);
    }


	    @Override
        public GetServiceResponse getServices(String billerId) {
            RemitaBillingGatewayService gatewayService = getRemitaBillingGatewayService();
            return gatewayService.getService(billerId);
        }

    @Override
    public GetCustomFieldResponse getServiceCustomFields(String billerServiceId) {
    	RemitaBillingGatewayService  gatewayService =  getRemitaBillingGatewayService();
        return gatewayService.getCustomField(billerServiceId);
    }


    @Override
    public GetBillerResponse getbillers(){
    	    RemitaBillingGatewayService  gatewayService =  getRemitaBillingGatewayService();
    	    return   gatewayService.getBillers();

     }


    @Override
    public List<BillerPlatform> getDataPlans(String providerId) {
        return billerPlatformRepository.findByBillerBillerID(providerId);

    }

    @Override
    public List<Biller> getAirtimeProviders() {
        //get provider by category
        Optional<BillerCategory> techo = billerCategoryService.findByBillerCategory("TELCOM");
        List<Biller> billers = techo.map(this::findAllByBillerCategory).orElse(null);
        if (billers != null) {
            HashSet<Biller> hashSet = new HashSet<>(billers);
            return new ArrayList<>(hashSet);
        }
        return new ArrayList<>();
    }


    @Override
    public List<Biller> getUtilBillers() {
        Optional<BillerCategory> tv = billerCategoryService.findByBillerCategory("TV");
        Optional<BillerCategory> power = billerCategoryService.findByBillerCategory("POWER");

        List<Biller> billers = new ArrayList<>();

        tv.ifPresent(billerCategory -> billers.addAll(billerRepository.findAllByBillerCategory(billerCategory)));
        power.ifPresent(billerCategory -> billers.addAll(billerRepository.findAllByBillerCategory(billerCategory)));

        HashSet<Biller> hashSet = new HashSet<>(billers);
        return new ArrayList<>(hashSet);
    }


    @Override
    public List<Biller> findAllByBillerCategory(BillerCategory billerCategory) {
        return billerRepository.findAllByBillerCategory(billerCategory);
    }

    @Override
    public Optional<Biller> findByBillerID(String billerId) {
        return billerRepository.findByBillerID(billerId);
    }

    @Override
    public List<BillerCustomFieldOption> getServiceOptions(String serviceTypeId) {
        return billerCustomFieldOptionRepository.findAllByBillerPlatformId(Long.parseLong(serviceTypeId));
    }

    @Override
    public List<Biller> findAllByPopular(boolean isPopular) {
        return billerRepository.findAllByisPopular(isPopular);
    }

    @Override
    public List<Biller> getTvBillers() {
        Optional<BillerCategory> tvOptional = billerCategoryService.findByBillerCategory("TV");

        List<Biller> billers = new ArrayList<>();
        tvOptional.ifPresent(tv -> billers.addAll(billerRepository.findAllByBillerCategory(tv)));

        HashSet<Biller> hashSet = new HashSet<>(billers);
        return new ArrayList<>(hashSet);
    }

    @Override
    public List<Biller> getPowerBillers() {
        Optional<BillerCategory> powerOptional = billerCategoryService.findByBillerCategory("POWER");

        List<Biller> billers = new ArrayList<>();

        powerOptional.ifPresent(power -> billers.addAll(billerRepository.findAllByBillerCategory(power)));

        HashSet<Biller> hashSet = new HashSet<>(billers);
        return new ArrayList<>(hashSet);
    }

    @Override
    public List<Biller> getInternetProviders() {
        //get provider by category
        Optional<BillerCategory> ISPOptional = billerCategoryService.findByBillerCategory("ISP");
        return ISPOptional.map(this::findAllByBillerCategory).orElse(null);

    }

    @Override
    public List<BillerCustomFieldOption> getCustomFields(String serviceTypeId) {
        Optional<BillerPlatform> billerPlatform = billerPlatformService.findByBillerplatformID(Long.valueOf(serviceTypeId));
        List<BillerCustomFieldOption> billerCustomFieldOptionList2 = null;


        if (billerPlatform.isPresent()) {
            List<BillerCustomFieldOption> billerCustomFieldOptionList =
                billerCustomFieldOptionRepository.findAllByBillerPlatformId(billerPlatform.get().getId());
            if (billerCustomFieldOptionList != null) {
                for (BillerCustomFieldOption options : billerCustomFieldOptionList) {
                    List<BillerServiceOption> allByBillerCustomFieldOptionId = billerServiceOptionService.findAllByBillerCustomFieldOptionId(options.getId());
                    options.setBillerServiceOptions(new HashSet<>(allByBillerCustomFieldOptionId));
                    billerCustomFieldOptionList2 = new ArrayList<>();
                    billerCustomFieldOptionList2.add(options);
                }
            }
        }
        return billerCustomFieldOptionList2;

    }

    @Override
    public List<Biller> getDataProviders() throws NullPointerException {
        //get provider by category
        Optional<BillerCategory> techo = billerCategoryService.findByBillerCategory("TELCOM");
        return techo.map(this::findAllByBillerCategory).orElse(null);
    }


}
