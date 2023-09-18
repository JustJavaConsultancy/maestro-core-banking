package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.ContactUsRepository;
import ng.com.justjava.corebanking.service.mapper.ContactUsMapper;
import ng.com.justjava.corebanking.domain.ContactUs;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.enumeration.ComplaintCategory;
import ng.com.justjava.corebanking.domain.enumeration.DocumentType;
import ng.com.justjava.corebanking.service.ContactUsService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.dto.ContactUsDTO;
import ng.com.justjava.corebanking.util.Utility;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ContactUs}.
 */
@Service
@Transactional
public class ContactUsServiceImpl implements ContactUsService {

    private final Logger log = LoggerFactory.getLogger(ContactUsServiceImpl.class);

    private final ContactUsRepository contactUsRepository;
    private final ProfileService profileService;
    private final Utility utility;

    @Value("${app.document-url}")
    private String documentUrl;

    String Kazeem = "akinrinde@justjava.com.ng";
    String Bolaji = "ogeyingbo@systemspecs.com.ng";
    String walletEmail = "pouchii@systemspecs.com.ng";

    private final ContactUsMapper contactUsMapper;

    public ContactUsServiceImpl(ContactUsRepository contactUsRepository, ProfileService profileService, Utility utility, ContactUsMapper contactUsMapper) {
        this.contactUsRepository = contactUsRepository;
        this.profileService = profileService;
        this.utility = utility;
        this.contactUsMapper = contactUsMapper;
    }

    @Override
    public ContactUsDTO save(ContactUsDTO contactUsDTO, Profile senderProfile, Profile assignedToProfile) {
        log.debug("Request to save ContactUs : {}", contactUsDTO);
        ContactUs contactUs = contactUsMapper.toEntity(contactUsDTO);
        contactUs.setSender(senderProfile);
        contactUs.setAssignedTo(assignedToProfile);
        contactUs = contactUsRepository.save(contactUs);

        //todo send Email

        return contactUsMapper.toDto(contactUs);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ContactUsDTO> findAll() {
        log.debug("Request to get all Contactuses");
        return contactUsRepository.findAll().stream()
            .map(contactUsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<ContactUsDTO> findOne(Long id) {
        log.debug("Request to get ContactUs : {}", id);
        return contactUsRepository.findById(id)
            .map(contactUsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ContactUs : {}", id);
        contactUsRepository.deleteById(id);
    }

    @Override
    public ContactUsDTO createContactUs(ContactUsDTO contactUsDTO) {

        contactUsDTO.setSenderPhoneNumber(utility.formatPhoneNumber(contactUsDTO.getSenderPhoneNumber()));
        contactUsDTO.setAssignedToPhoneNumber(utility.formatPhoneNumber(contactUsDTO.getAssignedToPhoneNumber()));

        Profile senderProfile = profileService.findByPhoneNumber(contactUsDTO.getSenderPhoneNumber());
        if (senderProfile == null) {
            return null;
        }

        String outputFileName = "";

        contactUsDTO.setSender(senderProfile);
        Profile assignedToProfile;

        if (utility.checkStringIsValid(contactUsDTO.getAssignedToPhoneNumber())) {
            assignedToProfile = profileService.findByPhoneNumber(utility.formatPhoneNumber(contactUsDTO.getAssignedToPhoneNumber()));

        } else {
            assignedToProfile = profileService.findByPhoneNumber(utility.formatPhoneNumber("+2348160110390"));
        }
        contactUsDTO.setAssignedTo(assignedToProfile);

        if (!StringUtils.isEmpty(contactUsDTO.getSupportingDoc()) || !StringUtils.isEmpty(contactUsDTO.getDocType())) {

            String encodedString = contactUsDTO.getSupportingDoc();

            String photoName;

            if (DocumentType.JPG.name().equalsIgnoreCase(contactUsDTO.getDocType())) {
                photoName = "contact-document-" + contactUsDTO.getSenderPhoneNumber() + ".jpg";
            } else if (DocumentType.PDF.name().equalsIgnoreCase(contactUsDTO.getDocType())) {
                photoName = "contact-document-" + contactUsDTO.getSenderPhoneNumber() + ".pdf";
            } else {
                return null;
            }

            outputFileName = documentUrl + photoName;

            contactUsDTO = saveImage(contactUsDTO, encodedString, photoName, outputFileName);
        }

//        if (contactUsDTO != null) {
//            contactUsDTO.setSupportingDoc("");
//        }

        ContactUsDTO save = save(contactUsDTO, senderProfile, assignedToProfile);

        log.info("Contact us to ===> " + save);

        if (contactUsDTO != null) {

            String filePath = "https://wallet.remita.net/" + outputFileName;

            String msg = "Sender Name: " + senderProfile.getFullName()
                + "<br/> Sender Email: " + senderProfile.getUser().getEmail()
                + "<br/> Sender Phone Number: " + senderProfile.getPhoneNumber()
                + "<br/>"
                + "<br/>"
                + "<br/> Dear Customer support,"
                + "<br/> "
                + "<br/> "
                + "<p>" + contactUsDTO.getMessage() + "</p>"
                + "<br/>";

            if (utility.checkStringIsValid(outputFileName)) {
                msg = msg
                    + "<br/> Upload: <a href=" + filePath + ">" + filePath + "</a>"
                    + "<p>";
            }

            String subject = "";
            String enquiryCategory = contactUsDTO.getEnquiryCategory().toString();
            ComplaintCategory complaintCategory = contactUsDTO.getComplaintCategory();

            if (utility.checkStringIsValid(enquiryCategory)) {
                subject = enquiryCategory.replace("_", " ");
            } else if (utility.checkStringIsValid(complaintCategory.toString())) {
                subject = complaintCategory.toString().replace("_", " ");
            }
            utility.sendEmail(walletEmail, subject, msg);

            utility.sendEmail(Kazeem, subject, msg);
            utility.sendEmail("mabdulwasii@gmail.com", subject, msg);

            utility.sendEmail(Bolaji, subject, msg);


        }

        return save;

    }

    @Override
    public ContactUsDTO updateContactUs(ContactUsDTO contactUsDTO) {
        contactUsDTO.setSenderPhoneNumber(utility.formatPhoneNumber(contactUsDTO.getSenderPhoneNumber()));
        contactUsDTO.setAssignedToPhoneNumber(utility.formatPhoneNumber(contactUsDTO.getAssignedToPhoneNumber()));

        Profile senderProfile = profileService.findByPhoneNumber(contactUsDTO.getSenderPhoneNumber());
        if (senderProfile == null) {
            return null;
        }
        contactUsDTO.setSender(senderProfile);

        Profile assignedToProfile = profileService.findByPhoneNumber(contactUsDTO.getAssignedToPhoneNumber());
        contactUsDTO.setAssignedTo(assignedToProfile);

        String encodedString = contactUsDTO.getSupportingDoc();

        String photoName;

        if (contactUsDTO.getDocType().equalsIgnoreCase(DocumentType.JPG.name())) {
            photoName = "contact-document-" + contactUsDTO.getSenderPhoneNumber() + ".jpg";
        } else if (contactUsDTO.getDocType().equalsIgnoreCase(DocumentType.PDF.name())) {
            photoName = "contact-document-" + contactUsDTO.getSenderPhoneNumber() + ".pdf";
        } else {
            return null;
        }
        String outputFileName = documentUrl + photoName;

        if (encodedString != null && !encodedString.isEmpty()) {
            contactUsDTO = saveImage(contactUsDTO, encodedString, photoName, outputFileName);
        }

        return save(contactUsDTO, senderProfile, assignedToProfile);
    }

    private ContactUsDTO saveImage(ContactUsDTO contactUsDTO, String encodedString, String photoName, String outputFileName) {
        try {
            log.debug("ABOUT TO DECODE");
            byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
            log.debug("AFTER DECODE");

//            decodedBytes = utility.resizeImage(decodedBytes);

            File file = new File(outputFileName);
            log.debug("Path name = " + outputFileName);
            log.debug("File absolute path = " + file.getAbsolutePath());
            FileUtils.writeByteArrayToFile(file, decodedBytes);

            contactUsDTO.setSupportingDoc(photoName);
            return contactUsDTO;

        } catch (IOException e) {
            e.printStackTrace();
            log.debug(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
