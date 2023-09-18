package ng.com.systemspecs.apigateway.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.systemspecs.apigateway.service.MyDeviceService;
import ng.com.systemspecs.apigateway.service.dto.ChangeMyDeviceStatusDTO;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.MyDeviceDTO;
import ng.com.systemspecs.apigateway.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ng.com.systemspecs.apigateway.domain.MyDevice}.
 */
@RestController
@RequestMapping("/api")
public class MyDeviceResource {

    private final Logger log = LoggerFactory.getLogger(MyDeviceResource.class);

    private static final String ENTITY_NAME = "myDevice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MyDeviceService myDeviceService;

    public MyDeviceResource(MyDeviceService myDeviceService) {
        this.myDeviceService = myDeviceService;
    }

    /**
     * {@code POST  /my-devices} : Create a new myDevice.
     *
     * @param myDeviceDTO the myDeviceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new myDeviceDTO, or with status {@code 400 (Bad Request)} if the myDevice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/my-devices")
    public ResponseEntity<GenericResponseDTO> updateMyDeviceOrCreateNew(@Valid @RequestBody MyDeviceDTO myDeviceDTO, HttpSession session) throws URISyntaxException {
        log.debug("REST request to save MyDevice : {}", myDeviceDTO);
        if (myDeviceDTO.getId() != null) {
            throw new BadRequestAlertException("A new myDevice cannot already have an ID", ENTITY_NAME, "idexists");
        }

        GenericResponseDTO genericResponseDTO = myDeviceService.updateMyDeviceOrCreateNew(myDeviceDTO, session);

        if (HttpStatus.OK.equals(genericResponseDTO.getStatus())) {

            MyDeviceDTO result = (MyDeviceDTO) genericResponseDTO.getData();

            if (result != null) {
                return ResponseEntity.created(new URI("/api/my-devices/" + result.getId()))
                    .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(genericResponseDTO);
            }
        }

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    /**
     * {@code PUT  /my-devices} : Updates an existing myDevice.
     *
     * @param myDeviceDTO the myDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated myDeviceDTO,
     * or with status {@code 400 (Bad Request)} if the myDeviceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the myDeviceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/my-devices")
    public ResponseEntity<MyDeviceDTO> updateMyDevice(@Valid @RequestBody MyDeviceDTO myDeviceDTO) throws URISyntaxException {
        log.debug("REST request to update MyDevice : {}", myDeviceDTO);
        if (myDeviceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MyDeviceDTO result = myDeviceService.save(myDeviceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, myDeviceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /my-devices} : get all the myDevices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of myDevices in body.
     */
    @GetMapping("/my-devices")
    public List<MyDeviceDTO> getAllMyDevices() {
        log.debug("REST request to get all MyDevices");
        return myDeviceService.findAll();
    }

    /**
     * {@code GET  /my-devices/:id} : get the "id" myDevice.
     *
     * @param id the id of the myDeviceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myDeviceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/my-devices/{id}")
    public ResponseEntity<MyDeviceDTO> getMyDevice(@PathVariable Long id) {
        log.debug("REST request to get MyDevice : {}", id);
        Optional<MyDeviceDTO> myDeviceDTO = myDeviceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(myDeviceDTO);
    }

    /**
     * {@code GET  /my-devices/:id} : get the "id" myDevice.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the myDeviceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/my-devices/self")
    public ResponseEntity<GenericResponseDTO> getMyPersonalDevices() {
        log.debug("REST request to get list of MyDevices : {}");
        GenericResponseDTO genericResponseDTO = myDeviceService.getMyPersonalDevices();
        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }

    /**
     * {@code DELETE  /my-devices/:id} : delete the "id" myDevice.
     *
     * @param id the id of the myDeviceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/my-devices/{id}")
    public ResponseEntity<Void> deleteMyDevice(@PathVariable Long id) {
        log.debug("REST request to delete MyDevice : {}", id);
        myDeviceService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }


    /**
     * {@code POST  /my-devices/status} : Change myDevice status.
     *
     * @param changeMyDeviceStatusDTO the myDeviceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the new myDeviceDTO, or with status {@code 400 (Bad Request)} if the myDevice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/my-devices/status")
    public ResponseEntity<GenericResponseDTO> changeDeviceStatus(@Valid @RequestBody ChangeMyDeviceStatusDTO changeMyDeviceStatusDTO) throws URISyntaxException {
        log.debug("REST request to change MyDevice status : {}", changeMyDeviceStatusDTO);

        GenericResponseDTO genericResponseDTO = myDeviceService.changeDeviceStatus(changeMyDeviceStatusDTO);

        return new ResponseEntity<>(genericResponseDTO, genericResponseDTO.getStatus());
    }
}
