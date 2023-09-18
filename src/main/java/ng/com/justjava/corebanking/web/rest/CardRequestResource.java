package ng.com.justjava.corebanking.web.rest;

import io.github.jhipster.web.util.PaginationUtil;
import ng.com.justjava.corebanking.service.*;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.justjava.corebanking.service.mapper.CardRequestMapper;
import ng.com.justjava.corebanking.service.mapper.ProfileMapper;
import ng.com.systemspecs.apigateway.service.*;
import ng.com.systemspecs.apigateway.service.dto.*;
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

import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CardRequestResource {

    private static final String ENTITY_NAME = "card_request";
    private final Logger log = LoggerFactory.getLogger(AppNotificationResource.class);
    private final CardRequestService cardRequestService;
    private final PolarisCardService polarisCardService;
    private final UserCardsService userCardsService;
    private final SchemeService schemeService;
    private final ProfileService profileService;
    private final ProfileMapper profileMapper;
    private final CardRequestMapper cardRequestMapper;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public CardRequestResource(CardRequestService cardRequestService, PolarisCardService polarisCardService, UserCardsService userCardsService, SchemeService schemeService, ProfileService profileService, ProfileMapper profileMapper, CardRequestMapper cardRequestMapper) {
        this.cardRequestService = cardRequestService;
        this.polarisCardService = polarisCardService;
        this.userCardsService = userCardsService;
        this.schemeService = schemeService;
        this.profileService = profileService;
        this.profileMapper = profileMapper;
        this.cardRequestMapper = cardRequestMapper;
    }

    @PostMapping("/card-requests/new/{phoneNumber}")
    public ResponseEntity<GenericResponseDTO> newRequest(@RequestBody CardRequestDTO cardRequestDTO, @PathVariable String phoneNumber) throws URISyntaxException {
        log.debug("REST request to update Card Request : {}", cardRequestDTO);
        ProfileDTO p = profileMapper.toDto(profileService.findByPhoneNumber(phoneNumber));
        cardRequestDTO.setOwner(p);
        CardRequestDTO result = cardRequestService.save(cardRequestDTO);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", result), headers,
            HttpStatus.OK);
    }

    @PostMapping("/card-requests/approve")
    public ResponseEntity<GenericResponseDTO> approve(@RequestBody CardRequestDTO cardRequestDTO) throws URISyntaxException {
        log.debug("REST request to update Card Request : {}", cardRequestDTO);
        CardRequestDTO result = cardRequestService.save(cardRequestDTO);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", result), headers,
            HttpStatus.OK);
    }

    @PostMapping("/card-requests/approve/list")
    public ResponseEntity<GenericResponseDTO> approvelist(@RequestBody List<CardRequestDTO> cardRequestDTO) throws URISyntaxException {
        log.debug("REST request to update List of Card Request : {}", cardRequestDTO);
        List<CardRequestDTO> result = cardRequestService.save(cardRequestDTO);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", result), headers,
            HttpStatus.OK);
    }

    @GetMapping("/card-requests")
    public ResponseEntity<GenericResponseDTO> getAllCardRequests(Pageable pageable) {
        log.debug("REST request to get all CArd request");
        Page<CardRequestDTO> result = cardRequestService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), result);
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", result.getContent()), headers,
            HttpStatus.OK);
    }

    @GetMapping("/card-requests/active")
    public ResponseEntity<GenericResponseDTO> getActiveCardRequests() {
        log.debug("REST request to get Active Card Request");
        List<CardRequestDTO> result = cardRequestService.findAllByStatus("NEW");
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", result), headers,
            HttpStatus.OK);
    }

    /**
     * {@code GET  /app-notifications/customer/{scheme}} : get all the notifications.
     * @param scheme
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notifications in body.
     */
    @GetMapping("/card-requests/scheme/{scheme}")
    public ResponseEntity<GenericResponseDTO> getSchemeCardRequests(@PathVariable String scheme) {
        log.debug("REST request to get Card Request by scheme");
        List<CardRequestDTO> result = cardRequestService.findAllByScheme(scheme);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", result), headers,
            HttpStatus.OK);
    }


    @GetMapping("/card-requests/cardtype/{cardtype}")
    public ResponseEntity<GenericResponseDTO> getCardRequestByCardtype(@PathVariable String cardtype) {
        log.debug("REST request to get Card Request by Card Type");
        List<CardRequestDTO> result = cardRequestService.findAllByCardtype(cardtype);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", result), headers,
            HttpStatus.OK);
    }

    @GetMapping("/card-requests/scheme-card/{scheme}/{cardtype}")
    public ResponseEntity<GenericResponseDTO> getCardRequestBySchemeAndCardtype(@PathVariable String scheme, @PathVariable String cardtype) {
        log.debug("REST request to get Card Request by Card Type");
        List<CardRequestDTO> result = cardRequestService.findAllBySchemeAndCardtype(scheme, cardtype);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", result), headers,
            HttpStatus.OK);
    }

    @GetMapping("/card-requests/scheme-card-date/{scheme}/{cardtype}/{startDate}/{endDate}")
    public ResponseEntity<GenericResponseDTO> getAllBySchemeAndCardtypeCreatedDateBetween(@PathVariable String scheme, @PathVariable  String cardtype, @PathVariable LocalDate startDate, @PathVariable LocalDate endDate) {
        log.debug("REST request to get Card Request by Card Type");
        try{
            Instant sd = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
            Instant ed = endDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        List<CardRequestDTO> result = cardRequestService.findAllBySchemeAndCardtypeAndCreatedDateBetween(scheme, cardtype, sd, ed);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", result), headers,
            HttpStatus.OK);
        }catch(Exception ex){
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("99", "failed", ex.getMessage()), headers,
                HttpStatus.OK);
        }
    }

    @GetMapping("/card-requests/scheme-date/{scheme}/{startDate}/{endDate}")
    public ResponseEntity<GenericResponseDTO> getAllBySchemeAndCreatedDateBetween(@PathVariable String scheme, @PathVariable LocalDate startDate, @PathVariable LocalDate endDate) {
        log.debug("REST request to get a page of Notifications");
        try{
            Instant sd = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
            Instant ed = endDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        List<CardRequestDTO> result = cardRequestService.findAllBySchemeAndCreatedDateBetween(scheme, sd, ed);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", result), headers,
            HttpStatus.OK);
        }catch(Exception ex){
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("99", "failed", ex.getMessage()), headers,
                HttpStatus.OK);
        }
    }

    @GetMapping("/card-requests/card-date/{cardtype}/{startDate}/{endDate}")
    public ResponseEntity<GenericResponseDTO> getAllByCardtypeAndCreatedDateBetween(@PathVariable String cardtype, @PathVariable LocalDate startDate, @PathVariable LocalDate endDate) {
        log.debug("REST request to get a page of Notifications");
        try{
            Instant sd = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
            Instant ed = endDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        List<CardRequestDTO> result = cardRequestService.findAllByCardtypeAndCreatedDateBetween(cardtype, sd, ed);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", result), headers,
            HttpStatus.OK);
    }catch(Exception ex){
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new GenericResponseDTO("99", "failed", ex.getMessage()), headers,
            HttpStatus.OK);
    }
    }

    @GetMapping("/card-requests/date/{startDate}/{endDate}")
    public ResponseEntity<GenericResponseDTO> getAllByCreatedDateBetween(@PathVariable LocalDate startDate, @PathVariable LocalDate endDate) {
        log.debug("REST request to get a page of Notifications");
        try {
            Instant sd = startDate.atStartOfDay().toInstant(ZoneOffset.UTC);
            Instant ed = endDate.atStartOfDay().toInstant(ZoneOffset.UTC);
            List<CardRequestDTO> result = cardRequestService.findAllByCreatedDateBetween(sd, ed);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", result), headers,
                HttpStatus.OK);
        }catch(Exception ex){
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("99", "failed", ex.getMessage()), headers,
                HttpStatus.OK);
        }
    }

    @GetMapping("/card-management/summary")
    public ResponseEntity<GenericResponseDTO> getCardsSummary() {
        log.debug("REST request to get a page of Notifications");
        HashMap<String, Object> report = new HashMap<>();
        List<Object> schemeSummary = new ArrayList<>();

        report.put("all_requests", cardRequestService.countAllRequests());
        report.put("active_cards", userCardsService.countByStatus("ACTIVE"));
        report.put("expired_cards", 0);

        List<SchemeDTO> schemes = schemeService.findAll();
        for (SchemeDTO s : schemes){
            HashMap<String, Object> o = new HashMap<>();
            o.put("total",userCardsService.countByScheme(s.getSchemeID()));
            o.put("active",userCardsService.countBySchemeAndStatus(s.getSchemeID(), "ACTIVE"));
            o.put("inactive",userCardsService.countBySchemeAndStatus(s.getSchemeID(), "INACTIVE"));
            o.put("name",s.getScheme());
            schemeSummary.add(o);
        }
        report.put("scheme_summary", schemeSummary);

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", report), headers,
            HttpStatus.OK);
    }

    @GetMapping("/card-management/report/{scheme}")
    public ResponseEntity<GenericResponseDTO> getCardsReport(@PathVariable String scheme) {
        log.debug("REST request to get a page of Notifications");
        List<UserCardsDTO> cards = userCardsService.findAllByScheme(scheme);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(new GenericResponseDTO("00", "success", cards), headers,
            HttpStatus.OK);
    }

    @PostMapping("/card-management/reset-pin")
    public ResponseEntity<GenericResponseDTO> resetpin(@RequestBody PolarisCardOperationsDTO polarisCardOperationsDTO) {
        log.debug("REST request to get activate customer card : {}", polarisCardOperationsDTO);
        GenericResponseDTO response = polarisCardService.resetCardPin(polarisCardOperationsDTO);
        if (response != null) {
            return new ResponseEntity<>(response, response.getStatus());
        }
        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null),
            HttpStatus.BAD_REQUEST);
    }
}
