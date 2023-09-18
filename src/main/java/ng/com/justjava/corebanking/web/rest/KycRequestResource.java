package ng.com.justjava.corebanking.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.justjava.corebanking.domain.KycRequest;
import ng.com.justjava.corebanking.domain.Kyclevel;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.enumeration.KycRequestStatus;
import ng.com.justjava.corebanking.repository.KycRequestRepository;
import ng.com.justjava.corebanking.service.KycRequestService;
import ng.com.justjava.corebanking.service.KyclevelService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.service.dto.KycRequestDTO;
import ng.com.justjava.corebanking.service.dto.KycRequestDecisionDTO;
import ng.com.justjava.corebanking.util.Utility;
import ng.com.justjava.corebanking.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing {@link KycRequest}.
 */
@RestController
@RequestMapping("/api")
public class KycRequestResource {

    private static final String ENTITY_NAME = "kycRequest";
    private final Logger log = LoggerFactory.getLogger(KycRequestResource.class);
    private final KycRequestService kycRequestService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final KycRequestRepository kycRequestRepository;
    private final Utility utility;
    private final ProfileService profileService;
    private final KyclevelService kyclevelService;

    public KycRequestResource(KycRequestService kycRequestService, KycRequestRepository kycRequestRepository, Utility utility,
                              ProfileService profileService, KyclevelService kyclevelService) {
        this.kycRequestService = kycRequestService;
        this.kycRequestRepository = kycRequestRepository;
        this.utility = utility;
        this.profileService = profileService;
        this.kyclevelService = kyclevelService;
    }

    /**
     * {@code POST  /kyc-requests} : Create a new kycRequest.
     *
     * @param kycRequestDTO the kycRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kycRequestDTO, or with status {@code 400 (Bad Request)} if the kycRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kyc-requests")
    public ResponseEntity<KycRequestDTO> createKycRequest(@Valid @RequestBody KycRequestDTO kycRequestDTO) throws URISyntaxException {
        log.debug("REST request to save KycRequest : {}", kycRequestDTO);
        if (kycRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new kycRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KycRequestDTO result = kycRequestService.save(kycRequestDTO);
        return ResponseEntity.created(new URI("/api/kyc-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code POST  /kyc-requests} : Approve a  kycRequest.
     *
     * @param "kycRequestId" the kycRequestId to be approved.
     * @return the {@link ResponseEntity} with status {@code 200 (Created)} and with body the kycRequestId, or with status {@code 400 (Bad Request)} if the kycRequestId was not successfully approved.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kyc-requests/approve")
    public ResponseEntity<GenericResponseDTO> approveKycRequest(@RequestBody KycRequestDecisionDTO decision) throws URISyntaxException {
        log.debug("REST request to approve a KycRequest : {}", decision);

        GenericResponseDTO result = kycRequestService.approveKycRequest(decision);
        return new ResponseEntity<>(result, result.getStatus());
    }

    /**
     * {@code PUT  /kyc-requests} : Updates an existing kycRequest.
     *
     * @param kycRequestDTO the kycRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kycRequestDTO,
     * or with status {@code 400 (Bad Request)} if the kycRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kycRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/kyc-requests")
    public ResponseEntity<KycRequestDTO> updateKycRequest(@Valid @RequestBody KycRequestDTO kycRequestDTO) throws URISyntaxException {
        log.debug("REST request to update KycRequest : {}", kycRequestDTO);
        if (kycRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        KycRequestDTO result = kycRequestService.save(kycRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kycRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /kyc-requests} : get all the kycRequests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kycRequests in body.
     */
    @GetMapping("/kyc-requests")
    public List<KycRequestDTO> getAllKycRequests() {
        log.debug("REST request to get all KycRequests");
        return kycRequestService.findAll();
    }

    /**
     * {@code GET  /kyc-requests} : get all the kycRequests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kycRequests in body.
     */
    @GetMapping("/kyc-requests-awaiting-approval")
    public List<KycRequestDTO> getAwaitingKycRequests() {
        log.debug("REST request to get all KycRequests");
        return kycRequestService.findByStatus(KycRequestStatus.AWAITING_APPROVAL);
    }

    /**
     * {@code GET  /kyc-requests/:id} : get the "id" kycRequest.
     *
     * @param id the id of the kycRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kycRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/kyc-requests/{id}")
    public ResponseEntity<KycRequestDTO> getKycRequest(@PathVariable Long id) {
        log.debug("REST request to get KycRequest : {}", id);
        Optional<KycRequestDTO> kycRequestDTO = kycRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kycRequestDTO);
    }

    /**
     * {@code DELETE  /kyc-requests/:id} : delete the "id" kycRequest.
     *
     * @param id the id of the kycRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/kyc-requests/{id}")
    public ResponseEntity<Void> deleteKycRequest(@PathVariable Long id) {
        log.debug("REST request to delete KycRequest : {}", id);
        kycRequestService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/kyc-requests/approved-and-rejected")
    public ResponseEntity<List<KycRequestDTO>> approvedAndRejected() {
        List<KycRequestDTO> kycRequestDTOS = kycRequestService.findAll().stream().filter(p -> !p.getStatus().equals(KycRequestStatus.AWAITING_APPROVAL)).collect(Collectors.toList());
        return ResponseEntity.ok(kycRequestDTOS);
    }

    @PostMapping("/kyc-requests/{phoneNumber}/{kyc}")
    public ResponseEntity<GenericResponseDTO> adminupgradekyc(@PathVariable String phoneNumber, @PathVariable String kyc){

        try {

            String phone = utility.formatPhoneNumber(phoneNumber);
            Profile profile = profileService.findByPhoneNumber(phone);
            System.out.println("Profile ====> " + profile);
            if (profile != null){
                Kyclevel kyclevel = kyclevelService.findByKyc(kyc);
                System.out.println("Profile ====> " + kyclevel);
                if (kyclevel != null){
                    profile.setKyc(kyclevel);
                    profileService.save(profile);
                    System.out.println("Profile ====> " + profile);
                    return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "Successful", null), HttpStatus.OK);
                }
                return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User kyc level not found", null), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "User profile not found", null), HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.toString(), null), HttpStatus.BAD_REQUEST);
        }

    }

}
