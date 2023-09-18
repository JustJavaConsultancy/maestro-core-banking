package ng.com.justjava.corebanking.web.rest;

import io.github.jhipster.web.util.PaginationUtil;
import ng.com.justjava.corebanking.domain.Address;
import ng.com.justjava.corebanking.service.ApprovalRequestService;
import ng.com.justjava.corebanking.service.dto.ApprovalRequestDTO;
import ng.com.justjava.corebanking.service.dto.ApprovalRequestDetailsDTO;
import ng.com.justjava.corebanking.service.dto.ApproveRequestDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * REST controller for managing {@link Address}.
 */
@RestController
@RequestMapping("/api")
public class ApprovalRequestResource {

    private final Logger log = LoggerFactory.getLogger(ApprovalRequestResource.class);

    private static final String ENTITY_NAME = "Approval Request";

    private final ApprovalRequestService approvalRequestService;

    public ApprovalRequestResource(ApprovalRequestService approvalRequestService) {
        this.approvalRequestService = approvalRequestService;
    }


    /**
     * {@code POST  /approval-requests} : Create a new ApprovalRequest.
     *
     * @param approvalRequestDTO the addressDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new approvalRequestDTO, or with status {@code 400 (Bad Request)} if the address has approvalRequestDTO an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/approval-requests")
    public ResponseEntity<GenericResponseDTO> createApprovalRequest(@Valid @RequestBody ApprovalRequestDTO approvalRequestDTO) throws URISyntaxException {
        log.debug("REST request to save ApprovalRequest : {}", approvalRequestDTO);

        GenericResponseDTO genericResponseDTO = approvalRequestService.createApprovalRequest(approvalRequestDTO);
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    @GetMapping("/approval-requests/{phoneNumber}")
    public ResponseEntity<GenericResponseDTO> getAllApprovalRequestByPhone(@PathVariable String phoneNumber){

        log.debug("REST request to get ApprovalRequest with phoneNo: {}", phoneNumber);

        List<ApprovalRequestDTO> results = approvalRequestService.getAllApprovalRequestByPhone(phoneNumber);

        GenericResponseDTO genericResponseDTO = new GenericResponseDTO("00", "success", results);
        return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/approval-requests/{requestRef}/{requestType}")
    public ResponseEntity<GenericResponseDTO> getApprovalRequestDetails(@PathVariable String requestRef, @PathVariable String requestType){

        log.debug("REST request to get ApprovalRequest with phoneNo: {}", requestRef + " " + requestType);

        ApprovalRequestDetailsDTO approvalRequestDetails = approvalRequestService.getApprovalRequestDetails(requestRef, requestType);

        if (approvalRequestDetails != null) {
            GenericResponseDTO genericResponseDTO = new GenericResponseDTO("00", "success", approvalRequestDetails);
            return new ResponseEntity<>(genericResponseDTO, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(new GenericResponseDTO("99", "failed", null), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/approval-requests/approve")
    public ResponseEntity<GenericResponseDTO> approveRequest(@RequestBody ApproveRequestDTO approveRequestDTO) {

        log.debug("REST request to approve request: {}", approveRequestDTO);

        GenericResponseDTO genericResponseDTO = approvalRequestService.approveRequest(approveRequestDTO);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());

    }

    @GetMapping("/approval-requests")
    public ResponseEntity<GenericResponseDTO> getAllRequest(Pageable pageable) {
        log.debug("REST request to get all approval requests: {}");

        Page<ApprovalRequestDTO> requests = approvalRequestService.findAll(pageable);

        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), requests);

        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("size", requests.getSize());
        metaMap.put("totalNumberOfRecords", requests.getTotalElements());

        return new ResponseEntity<>(new GenericResponseDTO("00", "success", requests.getContent(), metaMap), headers, HttpStatus.OK);


    }
}
