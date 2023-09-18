package ng.com.systemspecs.apigateway.service.impl;

import ng.com.systemspecs.apigateway.domain.AccessItem;
import ng.com.systemspecs.apigateway.repository.AccessItemRepository;
import ng.com.systemspecs.apigateway.service.AccessItemService;
import ng.com.systemspecs.apigateway.service.RightService;
import ng.com.systemspecs.apigateway.service.dto.AccessItemDTO;
import ng.com.systemspecs.apigateway.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Transactional
@Service
public class AccessItemServiceImpl implements AccessItemService {

    private final Logger log = LoggerFactory.getLogger(AccessItemServiceImpl.class);

    private final AccessItemRepository accessItemRepository;
    private final RightService rightService;
    private final Utility utility;


    public AccessItemServiceImpl(AccessItemRepository accessItemRepository,
                                 RightService rightService, @Lazy Utility utility) {
        this.accessItemRepository = accessItemRepository;
        this.rightService = rightService;
        this.utility = utility;
    }

    @Override
    public AccessItem save(AccessItem accessItem) {
        log.debug("Request to save AccessItem : {}", accessItem);
        return accessItemRepository.save(accessItem);
    }

    @Override
    public AccessItem save(AccessItemDTO accessItemDTO) {
        log.debug("Request to save AccessItem : {}", accessItemDTO);

        String rightCode = accessItemDTO.getRightCode();


        AccessItem accessItem = new AccessItem();
        accessItem.setMaker(accessItemDTO.isMaker());
        return accessItemRepository.save(accessItem);
    }

    @Override
    public List<AccessItem> findAll() {
        log.debug("Request to find all AccessItems");
        return accessItemRepository.findAll();
    }

    @Override
    public Optional<AccessItem> findOne(Long id) {
        log.debug("Request to find one AccessItem : {}", id);
        return accessItemRepository.findById(id);
    }

    @Override
    public void delete(AccessItem id) {
        log.debug("Request to delete AccessItem by Id: {}", id);
        accessItemRepository.delete(id);
    }

    @Override
    public Page<AccessItem> findAll(Pageable pageable) {
        log.debug("Request to find all AccessItems");
        return accessItemRepository.findAll(pageable);
    }

    @Override
    public List<AccessItem> findAllByRightCode(String rightCode) {
        log.debug("Request to find all AccessItems by Right Code");
        return accessItemRepository.findByRight_Code(rightCode);
    }

    @Override
    public List<AccessItem> findAllByRightCodeAndMakerFalse(String rightCode) {
        return accessItemRepository.findByRight_CodeAndMakerIsFalse(rightCode);
    }

    @Override
    public List<AccessItem> findAllByRightCodeAndMakerTrue(String rightCode) {
        return accessItemRepository.findByRight_CodeAndMakerIsTrue(rightCode);
    }

    @Override
    public List<AccessItem> findAllByPhoneNumber(String phoneNumber) {
        return accessItemRepository.findByAccessRight_Profile_PhoneNumber(phoneNumber);
    }

    @Override
    public List<AccessItem> findByPhoneNumberAndRightCodeAndIsMaker(String phoneNumber, String rightCode) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        return accessItemRepository.findByAccessRight_Profile_PhoneNumberAndRight_CodeAndMakerTrue(phoneNumber, rightCode);
    }

    @Override
    public List<AccessItem> findByPhoneNumberAndRightCodeAndIsChecker(String phoneNumber, String rightCode) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);
        return accessItemRepository.findByAccessRight_Profile_PhoneNumberAndRight_CodeAndMakerFalse(phoneNumber, rightCode);
    }
}
