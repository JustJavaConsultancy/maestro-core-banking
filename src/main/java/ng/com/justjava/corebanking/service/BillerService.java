package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.BillerDTO;
import ng.com.justjava.corebanking.domain.Biller;
import ng.com.justjava.corebanking.domain.BillerCategory;
import ng.com.justjava.corebanking.domain.BillerCustomFieldOption;
import ng.com.justjava.corebanking.domain.BillerPlatform;
import ng.com.systemspecs.remitabillinggateway.billers.GetBillerResponse;
import ng.com.systemspecs.remitabillinggateway.customfields.GetCustomFieldResponse;
import ng.com.systemspecs.remitabillinggateway.service.RemitaBillingGatewayService;
import ng.com.systemspecs.remitabillinggateway.servicetypes.GetServiceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Biller}.
 */
public interface BillerService {

    /**
     * Save a biller.
     *
     * @param billerDTO the entity to save.
     * @return the persisted entity.
     */
    BillerDTO save(BillerDTO billerDTO);

    /**
     * Get all the billers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BillerDTO> findAll(Pageable pageable);

    List<BillerDTO> findAll();


    /**
     * Get the "id" biller.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillerDTO> findOne(Long id);

    /**
     * Delete the "id" biller.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);


    RemitaBillingGatewayService getRemitaBillingGatewayService();

    GetServiceResponse getServices(String billerId);

    GetCustomFieldResponse getServiceCustomFields(String serviceTypeId);

    GetBillerResponse getbillers();


    List<Biller> getDataProviders();

    List<Biller> getUtilBillers();

    List<BillerPlatform> getDataPlans(String providerId);

    List<Biller> getAirtimeProviders();

    List<Biller> findAllByBillerCategory(BillerCategory billerCategory);

    Optional<Biller> findByBillerID(String billerId);

    List<BillerCustomFieldOption> getServiceOptions(String serviceTypeId);

    List<Biller> findAllByPopular(boolean isPopular);

    List<Biller> getTvBillers();

    List<Biller> getPowerBillers();

    List<Biller> getInternetProviders();

    List<BillerCustomFieldOption> getCustomFields(String serviceTypeId);
}
