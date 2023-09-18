package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.InsuranceTypeRepository;
import ng.com.justjava.corebanking.service.mapper.InsuranceTypeMapper;
import ng.com.justjava.corebanking.service.InsuranceTypeService;
import ng.com.justjava.corebanking.domain.InsuranceType;
import ng.com.justjava.corebanking.service.dto.InsuranceTypeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link InsuranceType}.
 */
@Service
@Transactional
public class InsuranceTypeServiceImpl implements InsuranceTypeService {

    private final Logger log = LoggerFactory.getLogger(InsuranceTypeServiceImpl.class);

    private final InsuranceTypeRepository insuranceTypeRepository;

    private final InsuranceTypeMapper insuranceTypeMapper;

    public InsuranceTypeServiceImpl(InsuranceTypeRepository insuranceTypeRepository, InsuranceTypeMapper insuranceTypeMapper) {
        this.insuranceTypeRepository = insuranceTypeRepository;
        this.insuranceTypeMapper = insuranceTypeMapper;
    }

    @Override
    public InsuranceTypeDTO save(InsuranceTypeDTO insuranceTypeDTO) {
        log.debug("Request to save InsuranceType : {}", insuranceTypeDTO);
        InsuranceType insuranceType = insuranceTypeMapper.toEntity(insuranceTypeDTO);
        insuranceType = insuranceTypeRepository.save(insuranceType);
        return insuranceTypeMapper.toDto(insuranceType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsuranceTypeDTO> findAll() {
        log.debug("Request to get all InsuranceTypes");
        return insuranceTypeRepository.findAll().stream()
            .map(insuranceTypeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<InsuranceTypeDTO> findOne(Long id) {
        log.debug("Request to get InsuranceType : {}", id);
        return insuranceTypeRepository.findById(id)
            .map(insuranceTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InsuranceType : {}", id);
        insuranceTypeRepository.deleteById(id);
    }
}
