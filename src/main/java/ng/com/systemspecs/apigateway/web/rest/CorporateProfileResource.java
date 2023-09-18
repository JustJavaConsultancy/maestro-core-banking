package ng.com.systemspecs.apigateway.web.rest;

import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import ng.com.systemspecs.apigateway.domain.CorporateProfile;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.enumeration.DocumentType;
import ng.com.systemspecs.apigateway.service.CorporateProfileService;
import ng.com.systemspecs.apigateway.service.ProfileService;
import ng.com.systemspecs.apigateway.service.dto.CorporateProfileDto;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.UpdateCorporate;
import ng.com.systemspecs.apigateway.service.dto.UploadCorporateDocument;
import ng.com.systemspecs.apigateway.util.Utility;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static java.lang.System.out;

/**
 * REST controller for managing
 * {@link ng.com.systemspecs.apigateway.domain.CorporateProfile}.
 */
@RestController
@RequestMapping("/api")
public class CorporateProfileResource {

    private final Logger log = LoggerFactory.getLogger(CorporateProfileResource.class);
    private final CorporateProfileService corporateProfileService;
    private final ProfileService profileService;
    private final Utility utility;

    @Value("${app.document-url}")
    private String documentUrl;

    public CorporateProfileResource(CorporateProfileService corporateProfileService, ProfileService profileService, Utility utility) {
        this.corporateProfileService = corporateProfileService;
        this.profileService = profileService;
        this.utility = utility;
    }


    @GetMapping("/corporate-profiles")
    public ResponseEntity<GenericResponseDTO> getCorporateProfiles(Pageable pageable){
        try {
            Page<CorporateProfileDto> profiles = corporateProfileService.findAll(pageable);
            HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), profiles);

            Map<String, Object> metaMap = new LinkedHashMap<>();
            metaMap.put("size", profiles.getSize());
            metaMap.put("totalNumberOfRecords", profiles.getTotalElements());

            return new ResponseEntity<>(new GenericResponseDTO("00", "success", profiles.getContent(), metaMap), headers,
                HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity
                .badRequest()
                .body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Error in retrieving Profiles!", null));
        }
    }

    @GetMapping("/corporate-profiles/search")
    public ResponseEntity<GenericResponseDTO> searchAllProfilesByKeyword(Pageable pageable, @RequestParam String key) {
        try {
            Page<CorporateProfileDto> profiles = corporateProfileService.findAllWithKeyword(pageable,key);
            HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), profiles);

            Map<String, Object> metaMap = new LinkedHashMap<>();
            metaMap.put("size", profiles.getSize());
            metaMap.put("totalNumberOfRecords", profiles.getTotalElements());

            return new ResponseEntity<>(new GenericResponseDTO("00", "success", profiles.getContent(), metaMap), headers,
                HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity
                .badRequest()
                .body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Error in retrieving Profiles!", null));
        }
    }

    @GetMapping("/corporate-profiles/{phoneNumber}")
    public ResponseEntity<CorporateProfileDto> getProfileByPhoneNumber(@PathVariable String phoneNumber) {
        log.debug("REST request to get Profile : {}", phoneNumber);
        Optional<CorporateProfileDto> profileDTO = corporateProfileService.findOneByPhoneNumber(phoneNumber);
        return ResponseUtil.wrapOrNotFound(profileDTO);
    }

    @PostMapping("/corporate-profiles/update-phone")
    public ResponseEntity<GenericResponseDTO> updatePhoneNumber(@RequestBody UpdateCorporate updateCorporate) {
        log.debug("REST request to Update Corporate Profile Phone Number ");
        CorporateProfile c = corporateProfileService.findByPhoneNumber(updateCorporate.getPhoneNumber());
        if(c!=null){
            c.setPhoneNo(updateCorporate.getNewPhoneNumber());
            CorporateProfile corporateProfile = corporateProfileService.save(c);
            return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", corporateProfile),
                HttpStatus.OK);
        }
        return ResponseEntity
            .badRequest()
            .body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Error in updating Profiles!", null));
    }

    @PostMapping("/corporate-profiles/update-address")
    public ResponseEntity<GenericResponseDTO> updateAddress(@RequestBody UpdateCorporate updateCorporate) {
        log.debug("REST request to Update Corporate Profile Address ");
        CorporateProfile c = corporateProfileService.findByPhoneNumber(updateCorporate.getPhoneNumber());
        if(c!=null){
            c.setAddress(updateCorporate.getNewAddress());
            CorporateProfile corporateProfile = corporateProfileService.save(c);
            return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", corporateProfile),
                HttpStatus.OK);
        }
        return ResponseEntity
            .badRequest()
            .body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Error in updating Profiles!", null));
    }

    @PostMapping("/corporate-profiles/update")
    public ResponseEntity<GenericResponseDTO> updateCorporateProfile(@RequestBody CorporateProfileDto corporateProfileDto) {
        log.debug("REST request to Update Corporate Profile Address ");
        CorporateProfile c = corporateProfileService.findByPhoneNumber(corporateProfileDto.getPhoneNo());
        if(c!=null){
            CorporateProfileDto corporateProfile = corporateProfileService.save(corporateProfileDto);
            return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", corporateProfile),
                HttpStatus.OK);
        }
        return ResponseEntity
            .badRequest()
            .body(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Error in updating Profiles!", null));
    }



    @PostMapping("/corporate-profiles/doc/upload")
    public ResponseEntity<GenericResponseDTO> uploadProfilePicture(
        @RequestBody UploadCorporateDocument uploadCorporateDocument) {

        Optional<Profile> byUserIsCurrentUser = profileService.findCurrentUserProfile();
        Profile profile = null;
        CorporateProfile corporateProfile = null;
        if (byUserIsCurrentUser.isPresent()) {
            Profile profileDTO = byUserIsCurrentUser.get();
            out.println("The Profile retrieved here  ====== " + profileDTO);

            profile = profileService.findByPhoneNumber(profileDTO.getPhoneNumber());

            if (profile != null) {
                corporateProfile = corporateProfileService.findOneByProfilePhoneNumber(profile.getPhoneNumber());
                out.println("The KYC retrieved here ====== " + corporateProfile.toString2());
                String profilePictureName = "";
                if (uploadCorporateDocument.getPhotoFormat().equalsIgnoreCase(DocumentType.JPG.name())) {
                    profilePictureName = "corporate-docs-"+ uploadCorporateDocument.getDocType().toUpperCase() + utility.returnPhoneNumberFormat(corporateProfile.getPhoneNo())
                        + ".jpg";
                } else if (uploadCorporateDocument.getPhotoFormat().equalsIgnoreCase(DocumentType.PDF.name())) {
                    profilePictureName = "corporate-docs-" + uploadCorporateDocument.getDocType().toUpperCase() + utility.returnPhoneNumberFormat(corporateProfile.getPhoneNo())
                        + ".pdf";
                } else {
                    return new ResponseEntity<>(
                        new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid Document type", null),
                        HttpStatus.BAD_REQUEST);
                }

                String outputFileName = documentUrl + profilePictureName;
                String encodedString = uploadCorporateDocument.getPhoto();

                if (encodedString != null && !encodedString.isEmpty()) {
                    boolean saved = utility.saveImage(encodedString, outputFileName);

                    if (saved) {
                        switch (uploadCorporateDocument.getDocType().toLowerCase()) {
                            case "co7": corporateProfile.setCacCo7(true);
                            case "co2": corporateProfile.setCacCo2(true);
                            case "certificate": corporateProfile.setCacCertificate(true);
                            case "utility": corporateProfile.setUtilityBill(true);
                        }
                        corporateProfile = corporateProfileService.save(corporateProfile);
                        log.info("Saved profile picture ==> " + profile);
                        log.info("Saved corporate profile docs ==> " + corporateProfile.toString2());
                        return new ResponseEntity<>(new GenericResponseDTO("00", HttpStatus.OK, "success", corporateProfile),
                            HttpStatus.OK);
                    }
                }
            }

        }

        return new ResponseEntity<>(new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed"),
            HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/corporate-profiles/doc/{phoneNumber}/{type}")
    public ResponseEntity<byte[]> getDocument(@PathVariable String phoneNumber, @PathVariable String type) {

        byte[] image = new byte[0];
        phoneNumber = utility.returnPhoneNumberFormat(phoneNumber);

        String filename = "corporate-docs-"+ type.toUpperCase() + phoneNumber + ".jpg";
        String filename2 = "corporate-docs-"+ type.toUpperCase() + phoneNumber + ".pdf";

        String pathname = documentUrl + filename;
        String pathname2 = documentUrl + filename2;

        try {
            File file = new File(pathname);
            log.debug("Image Pathname : " + pathname);
            log.debug("Image Byte : " + documentUrl);
            image = FileUtils.readFileToByteArray(file);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
        } catch (IOException e) {
            e.printStackTrace();
            try {
                File file = new File(pathname2);
                image = FileUtils.readFileToByteArray(file);
                log.debug("Image Pathname : " + pathname2);
                log.debug("Image Byte : " + documentUrl);
                return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(image);
            } catch (Exception xe) {
                xe.getSuppressed();
            }
        }
        return ResponseEntity.badRequest().contentType(MediaType.IMAGE_JPEG).body(image);
    }

}
