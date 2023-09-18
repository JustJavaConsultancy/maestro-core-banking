package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.AccessRightRepository;
import ng.com.justjava.corebanking.domain.AccessItem;
import ng.com.justjava.corebanking.domain.AccessRight;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.domain.Right;
import ng.com.justjava.corebanking.service.AccessItemService;
import ng.com.justjava.corebanking.service.AccessRightService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.RightService;
import ng.com.justjava.corebanking.service.dto.*;
import ng.com.systemspecs.apigateway.service.dto.*;
import ng.com.justjava.corebanking.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class AccessRightServiceImpl implements AccessRightService {

    private final Logger log = LoggerFactory.getLogger(AccessRightServiceImpl.class);

    private final AccessRightRepository accessRightRepository;
    private final AccessItemService accessItemService;
    private final RightService rightService;
    private final ProfileService profileService;
    private final Utility utility;

    public AccessRightServiceImpl(AccessRightRepository accessRightRepository,
                                  AccessItemService accessItemService, RightService rightService, ProfileService profileService, Utility utility) {
        this.accessRightRepository = accessRightRepository;
        this.accessItemService = accessItemService;
        this.rightService = rightService;
        this.profileService = profileService;
        this.utility = utility;
    }

    @Override
    public AccessRight save(AccessRight accessRight) {
        log.debug("Request to save AccessRight : {}", accessRight);
        return accessRightRepository.save(accessRight);
    }

    @Override
    public List<AccessRight> findAll() {
        log.debug("Request to find all AccessRights");
        return accessRightRepository.findAll();

    }

    @Override
    public Optional<AccessRight> findOne(Long id) {
        log.debug("Request to find one AccessRight : {}", id);
        return accessRightRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete AccessRight by Id: {}", id);
        accessRightRepository.deleteById(id);

    }

    @Override
    public Page<AccessRight> findAll(Pageable pageable) {
        log.debug("Request to find all AccessRights");
        return accessRightRepository.findAll(pageable);
    }

    @Override
    public GenericResponseDTO createAccessRight(CreateAccessRightDTO createAccessRightDTO) {
        try {
            String profilePhoneNumber = createAccessRightDTO.getProfilePhoneNumber();
            profilePhoneNumber = utility.formatPhoneNumber(profilePhoneNumber);

            Profile profile = profileService.findByPhoneNumber(profilePhoneNumber);
            if (profile == null) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid user Id");
            }
            log.info("ACCESS RIGHT profile ==> " + profile);
            Optional<AccessRight> byPhoneNumber = findByPhoneNumber(profilePhoneNumber);
            AccessRight accessRight = null;

            if (byPhoneNumber.isPresent()) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Access right already defined for " + profile.getFullName());
            }

            accessRight = new AccessRight();
            accessRight.setName(createAccessRightDTO.getAccessRightName());
            accessRight.setProfile(profile);
            AccessRight savedAccessRight = accessRightRepository.save(accessRight);

            log.info("SAVED ACCESS RIGHT ==> " + savedAccessRight);

            Set<AccessItem> accessItemHashSet = new HashSet<>();

            List<AccessItemDTO> accessItems = createAccessRightDTO.getAccessItem();
            accessItems.forEach(accessItemDTO -> {
                AccessItem accessItem = new AccessItem();
                String rightCode = accessItemDTO.getRightCode();
                Optional<Right> oneByCode = rightService.findOneByCode(rightCode);
                if (oneByCode.isPresent()) {
                    accessItem.setRight(oneByCode.get());
                    accessItem.setMaker(accessItemDTO.isMaker());
                    accessItem.setAccessRight(savedAccessRight);
                    AccessItem save = accessItemService.save(accessItem);

                    accessItemHashSet.add(save);
                }
            });

            log.info("SET OF ACCESS ITEM ==> " + accessItemHashSet);

            savedAccessRight.setAccessItems(accessItemHashSet);

           AccessRight save = accessRightRepository.save(savedAccessRight);
            log.info("UPDATED ACCESS RIGHT ==> " + save);
            return new GenericResponseDTO("00", HttpStatus.OK, "success", save);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "failed", null);
    }

    @Override
    public Optional<AccessRight> findByPhoneNumber(String phoneNumber) {
        return accessRightRepository.findByProfile_PhoneNumber(phoneNumber);
    }

    @Override
    public List<AccessRightDTO> findAllAccessRights() {

        ArrayList<AccessRightDTO> accessRightDTOS = new ArrayList<>();
        List<AccessRight> all = findAll();
        log.info("SIZE of Access Rights ==> " + all.size());
        all.forEach(accessRight -> {
            AccessRightDTO accessRightDTO = new AccessRightDTO();
            accessRightDTO.setId(accessRight.getId());
            accessRightDTO.setAccessRightName(accessRight.getName());
            accessRightDTO.setProfileName(accessRight.getProfile().getFullName());

            ArrayList<AccessRightItemDTO> accessItemsList = new ArrayList<>();
            accessRight.getAccessItems().forEach(accessItem -> {
                AccessRightItemDTO accessRightItemDTO = new AccessRightItemDTO();
                accessRightItemDTO.setId(accessItem.getId());
                accessRightItemDTO.setRightName(accessItem.getRight().getName());
                accessRightItemDTO.setMaker(accessItem.isMaker());
                accessItemsList.add(accessRightItemDTO);
            });
            accessRightDTO.setAccessItems(accessItemsList);
            accessRightDTOS.add(accessRightDTO);
        });

        return accessRightDTOS;

    }

    @Override
    public GenericResponseDTO updateAccessRight(CreateAccessRightDTO createAccessRightDTO) {
        return  new GenericResponseDTO("310", HttpStatus.SERVICE_UNAVAILABLE, HttpStatus.SERVICE_UNAVAILABLE.getReasonPhrase(), null);
    }
}
