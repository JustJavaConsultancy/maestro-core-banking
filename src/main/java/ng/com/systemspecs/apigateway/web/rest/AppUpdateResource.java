package ng.com.systemspecs.apigateway.web.rest;

import ng.com.systemspecs.apigateway.service.AppUpdateService;
import ng.com.systemspecs.apigateway.web.rest.errors.BadRequestAlertException;
import ng.com.systemspecs.apigateway.service.dto.AppUpdateDTO;
import ng.com.systemspecs.apigateway.service.dto.PushNotificationRequest;
import ng.com.systemspecs.apigateway.service.fcm.PushNotificationService;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ng.com.systemspecs.apigateway.domain.AppUpdate}.
 */
@RestController
@RequestMapping("/api")
public class AppUpdateResource {

    private final Logger log = LoggerFactory.getLogger(AppUpdateResource.class);

    private static final String ENTITY_NAME = "appUpdate";
    
    private final PushNotificationService pushNotificationService;


    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppUpdateService appUpdateService;

    public AppUpdateResource(AppUpdateService appUpdateService, PushNotificationService pushNotificationService) {
        this.appUpdateService = appUpdateService;
        this.pushNotificationService =  pushNotificationService;
    }

    /**
     * {@code POST  /app-updates} : Create a new appUpdate.
     *
     * @param appUpdateDTO the appUpdateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appUpdateDTO, or with status {@code 400 (Bad Request)} if the appUpdate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/app-updates")
    public ResponseEntity<AppUpdateDTO> createAppUpdate(@RequestBody AppUpdateDTO appUpdateDTO) throws URISyntaxException {
        log.debug("REST request to save AppUpdate : {}", appUpdateDTO);
        if (appUpdateDTO.getId() != null) {
            throw new BadRequestAlertException("A new appUpdate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppUpdateDTO result = appUpdateService.save(appUpdateDTO);
        if(result.getDevice() != null) {
        	PushNotificationRequest pushNotificationRequest  = new PushNotificationRequest();
        	pushNotificationRequest.setMessage(appUpdateDTO.getComments() !=null? appUpdateDTO.getComments(): "A new update is available!");
        	pushNotificationRequest.setTitle("Update Available");
        	pushNotificationRequest.setTopic(appUpdateDTO.getDevice().toString());
        	pushNotificationService.sendPushNotificationWithoutData(pushNotificationRequest);
        	
        }
        return ResponseEntity.created(new URI("/api/app-updates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /app-updates} : Updates an existing appUpdate.
     *
     * @param appUpdateDTO the appUpdateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appUpdateDTO,
     * or with status {@code 400 (Bad Request)} if the appUpdateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appUpdateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/app-updates")
    public ResponseEntity<AppUpdateDTO> updateAppUpdate(@RequestBody AppUpdateDTO appUpdateDTO) throws URISyntaxException {
        log.debug("REST request to update AppUpdate : {}", appUpdateDTO);
        if (appUpdateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AppUpdateDTO result = appUpdateService.save(appUpdateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appUpdateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /app-updates} : get all the appUpdates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appUpdates in body.
     */
    @GetMapping("/app-updates")
    public List<AppUpdateDTO> getAllAppUpdates() {
        log.debug("REST request to get all AppUpdates");
        return appUpdateService.findAll();
    }
    
    @GetMapping("/get-last")
    public ResponseEntity<AppUpdateDTO>  getLatestUpdate() {
    	AppUpdateDTO theLatest = appUpdateService.getLatestUpdate();
    	 return ResponseEntity.ok()
    	            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, theLatest.getId().toString()))
    	            .body(theLatest);
    }

    /**
     * {@code GET  /app-updates/:id} : get the "id" appUpdate.
     *
     * @param id the id of the appUpdateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appUpdateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/app-updates/{id}")
    public ResponseEntity<AppUpdateDTO> getAppUpdate(@PathVariable Long id) {
        log.debug("REST request to get AppUpdate : {}", id);
        Optional<AppUpdateDTO> appUpdateDTO = appUpdateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appUpdateDTO);
    }

    /**
     * {@code DELETE  /app-updates/:id} : delete the "id" appUpdate.
     *
     * @param id the id of the appUpdateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/app-updates/{id}")
    public ResponseEntity<Void> deleteAppUpdate(@PathVariable Long id) {
        log.debug("REST request to delete AppUpdate : {}", id);
        appUpdateService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
