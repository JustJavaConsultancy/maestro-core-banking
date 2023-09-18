package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.CorporateProfileRepository;
import ng.com.justjava.corebanking.service.mapper.CorporateProfileMapper;
import ng.com.justjava.corebanking.domain.CorporateProfile;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.service.CorporateProfileService;
import ng.com.justjava.corebanking.service.dto.CorporateProfileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

/**
 * Service Implementation for managing {@link CorporateProfile}.
 */
@Service
@Transactional
public class CorporateProfileServiceImpl implements CorporateProfileService {

    private final Logger log = LoggerFactory.getLogger(CorporateProfileServiceImpl.class);

    private final CorporateProfileRepository corporateProfileRepository;

    private final CorporateProfileMapper corporateProfileMapper;

    public CorporateProfileServiceImpl(CorporateProfileRepository corporateProfileRepository, CorporateProfileMapper corporateProfileMapper) {
        this.corporateProfileRepository = corporateProfileRepository;
        this.corporateProfileMapper = corporateProfileMapper;
    }

    @Override
    public CorporateProfileDto save(CorporateProfileDto profileDTO) {
        log.debug("Request to save Profile : {}", profileDTO);
        CorporateProfile corporateProfile = corporateProfileMapper.toEntity(profileDTO);
        corporateProfile = corporateProfileRepository.save(corporateProfile);
        return corporateProfileMapper.toDto(corporateProfile);
    }

    @Override
    public CorporateProfile save(CorporateProfile corporateProfile) {
        log.debug("Request to save Profile : {}", corporateProfile);
        corporateProfile = corporateProfileRepository.save(corporateProfile);
        return corporateProfile;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CorporateProfileDto> findAll(Pageable pageable) {
        log.debug("Request to get all Corporate Profiles");
        return corporateProfileRepository.findAll(pageable).map(corporateProfileMapper::toDto);
    }

    @Override
    public Page<CorporateProfileDto> findAllWithKeyword(Pageable pageable, String keyword) {
        log.debug("Request to get all Corporate Profiles with keyword");
        return corporateProfileRepository.searchProfileByKeyword(pageable, keyword).map(corporateProfileMapper::toDto);
    }

    @Override
    public Optional<CorporateProfileDto> findOne(Long id) {
        return corporateProfileRepository.findById(id).map(corporateProfileMapper::toDto);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Optional<CorporateProfileDto> findOneByPhoneNumber(String phoneNumber) {
        CorporateProfile corporateProfile = corporateProfileRepository.findOneByPhoneNo(phoneNumber);
        return Optional.of(corporateProfileMapper.toDto(corporateProfile));
    }

    @Override
    public CorporateProfile findByPhoneNumber(String phoneNumber) {
        return corporateProfileRepository.findOneByPhoneNo(phoneNumber);
    }

    @Override
    public CorporateProfile findOneByProfile(Profile profileI) {
        return corporateProfileRepository.findOneByProfile(profileI);
    }

    @Override
    public CorporateProfile findOneByProfilePhoneNumber(String phoneNumber) {
        return corporateProfileRepository.findOneByProfile_PhoneNumber(phoneNumber);
    }

    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        return corporateProfileRepository.existsByPhoneNo(phoneNumber);
    }

    @Override
    public boolean existsByRcNO(String rcNo) {
        return corporateProfileRepository.existsByRcNO(rcNo);
    }

    @Override
    public Page<CorporateProfileDto> findAllByCreatedDateBetween(Pageable pageable, LocalDate fromDate, LocalDate toDate) {
        Instant fromInstant = fromDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        Instant toInstant = toDate.atStartOfDay().toInstant(ZoneOffset.UTC);
        return corporateProfileRepository.findAllByCreatedDateBetween(pageable, fromInstant, toInstant).map(corporateProfileMapper::toDto);
    }
}
