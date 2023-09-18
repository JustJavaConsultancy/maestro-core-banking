package ng.com.justjava.corebanking.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.justjava.corebanking.config.AsyncConfiguration;
import ng.com.justjava.corebanking.domain.AppNotification;
import ng.com.justjava.corebanking.domain.enumeration.AppNotificationType;
import ng.com.justjava.corebanking.service.AppNotificationService;
import ng.com.justjava.corebanking.service.dto.AppNotificationDTO;
import ng.com.justjava.corebanking.service.dto.AppNotificationResponseDTO;
import ng.com.justjava.corebanking.service.dto.GenericResponseDTO;
import ng.com.justjava.corebanking.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;

/**
 * REST controller for managing {@link AppNotification}.
 */
@RestController
@RequestMapping("/api")
public class AppNotificationResource {

    private static final String ENTITY_NAME = "app_notification";
    private final Logger log = LoggerFactory.getLogger(AppNotificationResource.class);
    private final AppNotificationService appNotificationService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final AsyncConfiguration asyncConfiguration;

    public AppNotificationResource(AppNotificationService appNotificationService, AsyncConfiguration asyncConfiguration) {
        this.appNotificationService = appNotificationService;
        this.asyncConfiguration = asyncConfiguration;
    }

    /**
     * {@code POST  /app-notifications} : Create a new notification.
     *
     * @param notificationDTO the notificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationDTO, or with status {@code 400 (Bad Request)} if the notification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/app-notifications/send")
    public ResponseEntity<AppNotificationDTO> createNotification(@Valid @RequestBody AppNotificationDTO notificationDTO) throws URISyntaxException {
        log.debug("REST request to save Notification : {}", notificationDTO);
        if (notificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new notification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppNotificationDTO result = appNotificationService.save(notificationDTO);
        Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();
        if (asyncExecutor != null) {
            asyncExecutor.execute(() -> {
                try {
                    appNotificationService.sendAppSystemMessage(notificationDTO);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return ResponseEntity.created(new URI("/api/app-notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /app-notifications} : Updates an existing notification.
     *
     * @param notificationDTO the notificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationDTO,
     * or with status {@code 400 (Bad Request)} if the notificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/app-notifications")
    public ResponseEntity<AppNotificationDTO> updateNotification(@Valid @RequestBody AppNotificationDTO notificationDTO) throws URISyntaxException {
        log.debug("REST request to update Notification : {}", notificationDTO);
        if (notificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AppNotificationDTO result = appNotificationService.save(notificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /app-notifications} : get all the notifications.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notifications in body.
     */
    @GetMapping("/app-notifications")
    public ResponseEntity<List<AppNotificationDTO>> getAllNotifications(Pageable pageable) {
        log.debug("REST request to get a page of Notifications");
        Page<AppNotificationDTO> page = appNotificationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /app-notifications/customer/{scheme}} : get all the notifications.
     * @param scheme
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notifications in body.
     */
    @GetMapping("/app-notifications/customer/{scheme}")
    public ResponseEntity<AppNotificationResponseDTO> getCustomerNotifications(@PathVariable String scheme) {
        log.debug("REST request to get a page of Notifications");
        AppNotificationResponseDTO list = appNotificationService.findCustomerAppNotifications(scheme);
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.ok().headers(headers).body(list);
    }

    /**
     * {@code GET  /app-notifications/{scheme}} : get all the notifications.
     * @param scheme
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notifications in body.
     */
    @GetMapping("/app-notifications/scheme/{scheme}")
    public ResponseEntity<List<AppNotificationDTO>> getNotificationsbyScheme(@PathVariable String scheme) {
        log.debug("REST request to get a page of Notifications");
        List<AppNotificationDTO> list = appNotificationService.findAllByScheme(scheme);
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.ok().headers(headers).body(list);
    }

    @GetMapping("/app-notifications/type/{notificationType}")
    public ResponseEntity<List<AppNotificationDTO>> getNotificationsbyNotificationType(@PathVariable String notificationType) {
        log.debug("REST request to get a page of Notifications");
        List<AppNotificationDTO> list = appNotificationService.findAllByNotificationType(AppNotificationType.valueOf(notificationType.toUpperCase()));
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.ok().headers(headers).body(list);
    }

    @GetMapping("/app-notifications/type-scheme/{notificationType}/{scheme}")
    public ResponseEntity<List<AppNotificationDTO>> getNotificationsbyNotificationType(@PathVariable String notificationType, @PathVariable String scheme) {
        log.debug("REST request to get a page of Notifications");
        List<AppNotificationDTO> list = appNotificationService.findAllByNotificationTypeAndScheme(AppNotificationType.valueOf(notificationType.toUpperCase()), scheme);
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.ok().headers(headers).body(list);
    }

    /**
     * {@code GET  /app-notifications/:id} : get the "id" notification.
     *
     * @param id the id of the notificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/app-notifications/{id}")
    public ResponseEntity<AppNotificationDTO> getNotification(@PathVariable Long id) {
        log.debug("REST request to get Notification : {}", id);
        Optional<AppNotificationDTO> notificationDTO = appNotificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationDTO);
    }

    @GetMapping("/app-notifications/search")
    public ResponseEntity<GenericResponseDTO> searchAllAppNotificationsByKeyword(Pageable pageable, @RequestParam String key) {
        try {
            Page<AppNotificationDTO> notifications = appNotificationService.findAllWithKeyword(pageable,key);
            HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), notifications);

            Map<String, Object> metaMap = new LinkedHashMap<>();
            metaMap.put("size", notifications.getSize());
            metaMap.put("totalNumberOfRecords", notifications.getTotalElements());

            return new ResponseEntity<>(new GenericResponseDTO("00", "success", notifications.getContent(), metaMap), headers,
                HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity
                .badRequest()
                .body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Error in retrieving App Notifications!", null));
        }
    }

    @GetMapping("/app-notifications/search/{scheme}/{startDate}/{endDate}")
    public ResponseEntity<List<AppNotificationDTO>> getAllBySchemeAndCreatedDateBetween(@PathVariable String scheme, @PathVariable Instant startDate, @PathVariable Instant endDate) {
        log.debug("REST request to get a page of Notifications");
        List<AppNotificationDTO> list = appNotificationService.findAllBySchemeAndCreatedDateBetween(scheme,startDate,endDate);
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.ok().headers(headers).body(list);
    }

    @GetMapping("/app-notifications/search/{startDate}/{endDate}")
    public ResponseEntity<List<AppNotificationDTO>> getAllByCreatedDateBetween(@PathVariable Instant startDate, @PathVariable Instant endDate) {
        log.debug("REST request to get a page of Notifications");
        List<AppNotificationDTO> list = appNotificationService.findAllByCreatedDateBetween(startDate,endDate);
        HttpHeaders headers = new HttpHeaders();
        return ResponseEntity.ok().headers(headers).body(list);
    }

    @GetMapping("/app-notifications/update/{id}/{status}")
    public ResponseEntity<GenericResponseDTO> updateNotificationStatus(@PathVariable Long id, @PathVariable boolean status) {
        log.debug("REST request to get a page of Notifications");
        AppNotificationDTO  a = appNotificationService.findOneById(id);
        a.setDisplay(status);
        try {
            AppNotificationDTO update = appNotificationService.save(a);
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<>(new GenericResponseDTO("00", "success", update), headers,
                HttpStatus.OK);
        }catch (Exception ex) {
            return ResponseEntity
                .badRequest()
                .body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Error in updating App Notifications!", null));
        }
    }
}
