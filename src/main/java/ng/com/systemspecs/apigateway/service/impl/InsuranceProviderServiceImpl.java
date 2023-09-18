package ng.com.systemspecs.apigateway.service.impl;

import ng.com.systemspecs.apigateway.domain.InsuranceProvider;
import ng.com.systemspecs.apigateway.repository.InsuranceProviderRepository;
import ng.com.systemspecs.apigateway.service.InsuranceProviderService;
import ng.com.systemspecs.apigateway.service.dto.InsuranceProviderDTO;
import ng.com.systemspecs.apigateway.service.mapper.InsuranceProviderMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link InsuranceProvider}.
 */
@Service
@Transactional
public class InsuranceProviderServiceImpl implements InsuranceProviderService {

    private final Logger log = LoggerFactory.getLogger(InsuranceProviderServiceImpl.class);

    private final InsuranceProviderRepository insuranceProviderRepository;

    private final InsuranceProviderMapper insuranceProviderMapper;

    public InsuranceProviderServiceImpl(InsuranceProviderRepository insuranceProviderRepository, InsuranceProviderMapper insuranceProviderMapper) {
        this.insuranceProviderRepository = insuranceProviderRepository;
        this.insuranceProviderMapper = insuranceProviderMapper;
    }

    @Override
    public InsuranceProviderDTO save(InsuranceProviderDTO insuranceProviderDTO) {
        log.debug("Request to save InsuranceProvider : {}", insuranceProviderDTO);
        InsuranceProvider insuranceProvider = insuranceProviderMapper.toEntity(insuranceProviderDTO);
        insuranceProvider = insuranceProviderRepository.save(insuranceProvider);
        return insuranceProviderMapper.toDto(insuranceProvider);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InsuranceProviderDTO> findAll() {
        log.debug("Request to get all InsuranceProviders");
        return insuranceProviderRepository.findAll().stream()
            .map(insuranceProviderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<InsuranceProviderDTO> findOne(Long id) {
        log.debug("Request to get InsuranceProvider : {}", id);
        return insuranceProviderRepository.findById(id)
            .map(insuranceProviderMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete InsuranceProvider : {}", id);
        insuranceProviderRepository.deleteById(id);
    }
}
