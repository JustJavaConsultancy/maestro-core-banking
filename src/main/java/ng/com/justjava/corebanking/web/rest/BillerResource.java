package ng.com.justjava.corebanking.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.justjava.corebanking.domain.Biller;
import ng.com.justjava.corebanking.domain.BillerCustomFieldOption;
import ng.com.justjava.corebanking.domain.BillerPlatform;
import ng.com.justjava.corebanking.repository.BillerCustomFieldOptionRepository;
import ng.com.justjava.corebanking.service.BillerPlatformService;
import ng.com.justjava.corebanking.service.BillerService;
import ng.com.justjava.corebanking.service.BillerServiceOptionService;
import ng.com.justjava.corebanking.service.dto.BillerDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.mapper.BillerMapper;
import ng.com.justjava.corebanking.service.mapper.BillerServiceOptionMapper;
import ng.com.justjava.corebanking.web.rest.errors.BadRequestAlertException;
import ng.com.systemspecs.remitabillinggateway.billers.GetBillerResponse;
import ng.com.systemspecs.remitabillinggateway.billers.GetBillerResponseData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.util.ListUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link Biller}.
 */
@RestController
@RequestMapping("/api")
public class BillerResource {

    private final Logger log = LoggerFactory.getLogger(BillerResource.class);

    private static final String ENTITY_NAME = "biller";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillerService billerService;
    private final BillerMapper billerMapper;
    private final BillerPlatformService billerPlatformService;
    private final BillerServiceOptionService billerServiceOptionService;
    private final BillerCustomFieldOptionRepository billerCustomFieldOptionRepository;
    private final BillerServiceOptionMapper billerServiceOptionMapper;

    public BillerResource(BillerService billerService, BillerMapper billerMapper, BillerPlatformService billerPlatformService, BillerServiceOptionService billerServiceOptionService, BillerCustomFieldOptionRepository billerCustomFieldOptionRepository, BillerServiceOptionMapper billerServiceOptionMapper) {
        this.billerService = billerService;
        this.billerMapper = billerMapper;
        this.billerPlatformService = billerPlatformService;
        this.billerServiceOptionService = billerServiceOptionService;
        this.billerCustomFieldOptionRepository = billerCustomFieldOptionRepository;
        this.billerServiceOptionMapper = billerServiceOptionMapper;
    }

    /**
     * {@code POST  /billers} : Create a new biller.
     *
     * @param billerDTO the billerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billerDTO, or with status {@code 400 (Bad Request)} if the biller has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/billers")
    public ResponseEntity<BillerDTO> createBiller(@RequestBody BillerDTO billerDTO) throws URISyntaxException {
        log.debug("REST request to save Biller : {}", billerDTO);
        if (billerDTO.getId() != null) {
            throw new BadRequestAlertException("A new biller cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillerDTO result = billerService.save(billerDTO);
        return ResponseEntity.created(new URI("/api/billers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /billers} : Updates an existing biller.
     *
     * @param billerDTO the billerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billerDTO,
     * or with status {@code 400 (Bad Request)} if the billerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/billers")
    public ResponseEntity<BillerDTO> updateBiller(@RequestBody BillerDTO billerDTO) throws URISyntaxException {
        log.debug("REST request to update Biller : {}", billerDTO);
        if (billerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BillerDTO result = billerService.save(billerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /billers} : get all the billers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billers in body.
     */
    @GetMapping("/all-billers")
    public ResponseEntity<List<BillerDTO>> getAllBillers(Pageable pageable) {
        log.debug("REST request to get a page of Billers");
        Page<BillerDTO> page = billerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/billers")
    public ResponseEntity<List<BillerDTO>> getBillers() {
        log.debug("REST request to get all Billers");
        List<BillerDTO> billerDTOS = billerService.findAll();
        return ResponseEntity.ok().body(billerDTOS);
    }


    @GetMapping("/billers/{id}")
    public ResponseEntity<BillerDTO> getBiller(@PathVariable Long id) {
        log.debug("REST request to get Biller : {}", id);
        Optional<BillerDTO> billerDTO = billerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billerDTO);
    }

    /**
     * {@code DELETE  /billers/:id} : delete the "id" biller.
     *
     * @param id the id of the billerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */

    @DeleteMapping("/billers/{id}")
    public ResponseEntity<Void> deleteBiller(@PathVariable Long id) {
        log.debug("REST request to delete Biller : {}", id);
        billerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/save-billers")
    public  List<BillerDTO>   getbillers() {
        BillerDTO billerDTO = null;
        List<BillerDTO> savedBillerList = new ArrayList<>();
        GetBillerResponse getBillerResponse = billerService.getbillers();
        if (!ListUtils.isEmpty(getBillerResponse.getResponseData())) {
            for (GetBillerResponseData billerResponseData : getBillerResponse.getResponseData()) {
                billerDTO = new BillerDTO();
                billerDTO.setBillerID(billerResponseData.getId());
                billerDTO.setBiller(billerResponseData.getDescription());
                savedBillerList.add(billerService.save(billerDTO));
            }
        }
        return savedBillerList;
    }

    @GetMapping("/data/providers")
    public List<BillerDTO> getDataProviders() {
        return billerMapper.toDto(billerService.getDataProviders());
    }

    @GetMapping("/data/provider/{providerId}/plans")
    public List<BillerPlatform> getDataPlans(@PathVariable String providerId) {
        return billerService.getDataPlans(providerId);
    }


    @GetMapping("/airtime/providers")
    public List<BillerDTO> getAirtimeProviders() {
        return billerMapper.toDto(billerService.getDataProviders());
    }

    @GetMapping("/util/billers")
    public List<BillerDTO> getUtilBillers() {
        return billerMapper.toDto(billerService.getUtilBillers());
    }

    @GetMapping("/tv/billers")
    public List<BillerDTO> getTvBillers() {
        return billerMapper.toDto(billerService.getTvBillers());
    }

    @GetMapping("/power/billers")
    public List<BillerDTO> getPowerBillers() {
        return billerMapper.toDto(billerService.getPowerBillers());
    }

    @GetMapping("/isp/billers")
    public List<BillerDTO> getInternetProviders() {
        return billerMapper.toDto(billerService.getInternetProviders());
    }

    @GetMapping("/billers/popular")
    public List<BillerDTO> getPopularBillers() {
        return billerMapper.toDto(billerService.findAllByPopular(true));
    }

    @GetMapping("/billers/{serviceTypeId}/options")
    public ResponseEntity<GenericResponseDTO> getServiceCustomFields(@PathVariable String serviceTypeId) {

        List<BillerCustomFieldOption> customFields = billerService.getCustomFields(serviceTypeId);

        if (customFields != null){
            return new ResponseEntity<>(new GenericResponseDTO("success", "Successful", customFields), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new GenericResponseDTO("failed", "Failed!", null), HttpStatus.BAD_REQUEST);
        }
    }

}
