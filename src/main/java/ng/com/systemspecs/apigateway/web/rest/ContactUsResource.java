package ng.com.systemspecs.apigateway.web.rest;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.systemspecs.apigateway.service.ContactUsService;
import ng.com.systemspecs.apigateway.service.dto.ContactUsDTO;
import ng.com.systemspecs.apigateway.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link ng.com.systemspecs.apigateway.domain.ContactUs}.
 */
@RestController
@RequestMapping("/api")
public class ContactUsResource {

    private static final String ENTITY_NAME = "contactUs";
    private final Logger log = LoggerFactory.getLogger(ContactUsResource.class);
    private final ContactUsService contactUsService;
    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public ContactUsResource(ContactUsService contactUsService) {
        this.contactUsService = contactUsService;
    }

    /**
     * {@code POST  /contactuses} : Create a new contactUs.
     *
     * @param contactUsDTO the contactUsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contactUsDTO, or with status {@code 400 (Bad Request)} if the contactUs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contact-us")
    public ResponseEntity<ContactUsDTO> createContactUs(@RequestBody ContactUsDTO contactUsDTO) throws URISyntaxException {
        log.debug("REST request to save ContactUs : {}", contactUsDTO);
        if (contactUsDTO.getId() != null) {
            throw new BadRequestAlertException("A new contactUs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContactUsDTO result = contactUsService.createContactUs(contactUsDTO);

        if (result == null) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.created(new URI("/api/contactuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contactuses} : Updates an existing contactUs.
     *
     * @param contactUsDTO the contactUsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contactUsDTO,
     * or with status {@code 400 (Bad Request)} if the contactUsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contactUsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contact-us")
    public ResponseEntity<ContactUsDTO> updateContactUs(@RequestBody ContactUsDTO contactUsDTO) throws URISyntaxException {
        log.debug("REST request to update ContactUs : {}", contactUsDTO);
        if (contactUsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ContactUsDTO result = contactUsService.updateContactUs(contactUsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contactUsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /contact-us} : get all the contacts.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contactuses in body.
     */
    @GetMapping("/contact-us")
    public List<ContactUsDTO> getAllContactuses() {
        log.debug("REST request to get all Contactuses");
        return contactUsService.findAll();
    }

    /**
     * {@code GET  /contactuses/:id} : get the "id" contactUs.
     *
     * @param id the id of the contactUsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contactUsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contact-us/{id}")
    public ResponseEntity<ContactUsDTO> getContactUs(@PathVariable Long id) {
        log.debug("REST request to get ContactUs : {}", id);
        Optional<ContactUsDTO> contactUsDTO = contactUsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contactUsDTO);
    }

    /**
     * {@code DELETE  /contactuses/:id} : delete the "id" contactUs.
     *
     * @param id the id of the contactUsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contact-us/{id}")
    public ResponseEntity<Void> deleteContactUs(@PathVariable Long id) {
        log.debug("REST request to delete ContactUs : {}", id);
        contactUsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
