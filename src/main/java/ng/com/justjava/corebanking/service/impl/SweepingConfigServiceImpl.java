package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.SweepingConfigRepository;
import ng.com.justjava.corebanking.service.mapper.SweepingConfigMapper;
import ng.com.justjava.corebanking.domain.SweepingConfig;
import ng.com.justjava.corebanking.service.SweepingConfigService;
import ng.com.justjava.corebanking.service.dto.SweepingConfigDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SweepingConfig}.
 */
@Service
@Transactional
public class SweepingConfigServiceImpl implements SweepingConfigService {

    private final Logger log = LoggerFactory.getLogger(SweepingConfigServiceImpl.class);

    private final SweepingConfigRepository sweepingConfigRepository;

    private final SweepingConfigMapper sweepingConfigMapper;

    public SweepingConfigServiceImpl(SweepingConfigRepository sweepingConfigRepository, SweepingConfigMapper sweepingConfigMapper) {
        this.sweepingConfigRepository = sweepingConfigRepository;
        this.sweepingConfigMapper = sweepingConfigMapper;
    }

    @Override
    public SweepingConfigDTO save(SweepingConfigDTO sweepingConfigDTO) {
        log.debug("Request to save SweepingConfig : {}", sweepingConfigDTO);
        SweepingConfig sweepingConfig = sweepingConfigMapper.toEntity(sweepingConfigDTO);
        sweepingConfig = sweepingConfigRepository.save(sweepingConfig);
        return sweepingConfigMapper.toDto(sweepingConfig);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SweepingConfigDTO> findAll() {
        log.debug("Request to get all SweepingConfigs");
        return sweepingConfigRepository.findAll().stream()
            .map(sweepingConfigMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SweepingConfigDTO> findOne(Long id) {
        log.debug("Request to get SweepingConfig : {}", id);
        return sweepingConfigRepository.findById(id)
            .map(sweepingConfigMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SweepingConfig : {}", id);
        sweepingConfigRepository.deleteById(id);
    }
}
