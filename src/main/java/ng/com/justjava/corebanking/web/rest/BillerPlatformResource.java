package ng.com.justjava.corebanking.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.justjava.corebanking.domain.Biller;
import ng.com.justjava.corebanking.domain.BillerCustomFieldOption;
import ng.com.justjava.corebanking.domain.BillerPlatform;
import ng.com.justjava.corebanking.domain.BillerServiceOption;
import ng.com.justjava.corebanking.repository.BillerCustomFieldOptionRepository;
import ng.com.justjava.corebanking.repository.BillerServiceOptionRepository;
import ng.com.justjava.corebanking.service.BillerPlatformService;
import ng.com.justjava.corebanking.service.BillerService;
import ng.com.justjava.corebanking.service.dto.BillerPlatformDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.mapper.BillerPlatformMapper;
import ng.com.justjava.corebanking.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link BillerPlatform}.
 */
@RestController
@RequestMapping("/api")
public class BillerPlatformResource {

    private final Logger log = LoggerFactory.getLogger(BillerPlatformResource.class);

    private static final String ENTITY_NAME = "billerPlatform";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillerPlatformService billerPlatformService;
    private final BillerCustomFieldOptionRepository billerCustomFieldOptionRepository;
    private final BillerServiceOptionRepository billerServiceOptionRepository;
    private final BillerService billerService;
    private final BillerPlatformMapper platformMapper;

    public BillerPlatformResource(BillerPlatformService billerPlatformService, BillerCustomFieldOptionRepository billerCustomFieldOptionRepository, BillerServiceOptionRepository billerServiceOptionRepository, BillerService billerService, BillerPlatformMapper platformMapper) {
        this.billerPlatformService = billerPlatformService;
        this.billerCustomFieldOptionRepository = billerCustomFieldOptionRepository;
        this.billerServiceOptionRepository = billerServiceOptionRepository;
        this.billerService = billerService;
        this.platformMapper = platformMapper;
    }

    /**
     * {@code POST  /biller-platforms} : Create a new billerPlatform.
     *
     * @param billerPlatformDTO the billerPlatformDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billerPlatformDTO, or with status {@code 400 (Bad Request)} if the billerPlatform has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/biller-platforms")
    public ResponseEntity<BillerPlatformDTO> createBillerPlatform(@RequestBody BillerPlatformDTO billerPlatformDTO) throws URISyntaxException {
        log.debug("REST request to save BillerPlatform : {}", billerPlatformDTO);
        if (billerPlatformDTO.getId() != null) {
            throw new BadRequestAlertException("A new billerPlatform cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillerPlatformDTO result = billerPlatformService.save(billerPlatformDTO);
        return ResponseEntity.created(new URI("/api/biller-platforms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /biller-platforms} : Updates an existing billerPlatform.
     *
     * @param billerPlatformDTO the billerPlatformDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billerPlatformDTO,
     * or with status {@code 400 (Bad Request)} if the billerPlatformDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billerPlatformDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/biller-platforms")
    public ResponseEntity<BillerPlatformDTO> updateBillerPlatform(@RequestBody BillerPlatformDTO billerPlatformDTO) throws URISyntaxException {
        log.debug("REST request to update BillerPlatform : {}", billerPlatformDTO);
        if (billerPlatformDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BillerPlatformDTO result = billerPlatformService.save(billerPlatformDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billerPlatformDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /biller-platforms} : get all the billerPlatforms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billerPlatforms in body.
     */
    @GetMapping("/biller-platforms")
    public List<BillerPlatformDTO> getAllBillerPlatforms() {
        log.debug("REST request to get all BillerPlatforms");
        return billerPlatformService.findAll();
    }

    /**
     * {@code GET  /biller-platforms/:id} : get the "id" billerPlatform.
     *
     * @param id the id of the billerPlatformDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billerPlatformDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/biller-platforms/{id}")
    public ResponseEntity<BillerPlatformDTO> getBillerPlatform(@PathVariable Long id) {
        log.debug("REST request to get BillerPlatform : {}", id);
        Optional<BillerPlatformDTO> billerPlatformDTO = billerPlatformService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billerPlatformDTO);
    }


    /**
     * {@code DELETE  /biller-platforms/:id} : delete the "id" billerPlatform.
     *
     * @param id the id of the billerPlatformDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/biller-platforms/{id}")
    public ResponseEntity<Void> deleteBillerPlatform(@PathVariable Long id) {
        log.debug("REST request to delete BillerPlatform : {}", id);
        billerPlatformService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }


    @GetMapping("/billers/{billerId}/services")
    public ResponseEntity<GenericResponseDTO> getAllServices(@PathVariable String billerId) throws JsonProcessingException {
        Optional<Biller> biller = billerService.findByBillerID(billerId);
        if (biller.isPresent()) {
            List<BillerPlatform> billerPlatforms2 = new ArrayList<>();
            List<BillerPlatform> billerPlatforms = billerPlatformService.findAllByBiller(biller.get());
//            List<BillerPlatformDTO> billerPlatformDTOS = platformMapper.toDto(billerPlatforms);
            for (BillerPlatform billerPlatform : billerPlatforms) {
                Optional<BillerCustomFieldOption> byBillerPlatform = billerCustomFieldOptionRepository.findByBillerPlatform(billerPlatform);
                byBillerPlatform.ifPresent(billerCustomFieldOption -> {
                    BillerCustomFieldOption customFieldOption = byBillerPlatform.get();
                    billerPlatform.setBillerCustomFieldOptions(customFieldOption);
                    List<BillerServiceOption> allByBillerCustomFieldOptionId = billerServiceOptionRepository.findAllByBillerCustomFieldOptionId(customFieldOption.getId());
                    customFieldOption.setBillerServiceOptions(new HashSet<>(allByBillerCustomFieldOptionId));
                });
                billerPlatforms2.add(billerPlatform);
            }
            log.info("EXPORTED BILLER PLATFORM " + new ObjectMapper().writeValueAsString(billerPlatforms2));
            return new ResponseEntity<>(new GenericResponseDTO("success", "Successful", billerPlatforms2), HttpStatus.OK);
        } else {

            return new ResponseEntity<>(new GenericResponseDTO("failed", "failed", null), HttpStatus.BAD_REQUEST);
        }
    }
}
