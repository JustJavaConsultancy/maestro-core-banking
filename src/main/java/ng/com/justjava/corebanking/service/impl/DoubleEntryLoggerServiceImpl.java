package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.DoubleEntryLoggerRepository;
import ng.com.justjava.corebanking.service.mapper.DoubleEntryLoggerMapper;
import ng.com.justjava.corebanking.service.DoubleEntryLoggerService;
import ng.com.justjava.corebanking.domain.DoubleEntryLogger;
import ng.com.justjava.corebanking.service.dto.DoubleEntryLoggerDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link DoubleEntryLogger}.
 */
@Service
@Transactional
public class DoubleEntryLoggerServiceImpl implements DoubleEntryLoggerService {

    private final Logger log = LoggerFactory.getLogger(DoubleEntryLoggerServiceImpl.class);

    private final DoubleEntryLoggerRepository doubleEntryLoggerRepository;

    private final DoubleEntryLoggerMapper doubleEntryLoggerMapper;

    public DoubleEntryLoggerServiceImpl(DoubleEntryLoggerRepository doubleEntryLoggerRepository, DoubleEntryLoggerMapper doubleEntryLoggerMapper) {
        this.doubleEntryLoggerRepository = doubleEntryLoggerRepository;
        this.doubleEntryLoggerMapper = doubleEntryLoggerMapper;
    }

    @Override
    public DoubleEntryLoggerDTO save(DoubleEntryLoggerDTO doubleEntryLoggerDTO) {
        log.debug("Request to save DoubleEntryLogger : {}", doubleEntryLoggerDTO);
        DoubleEntryLogger doubleEntryLogger = doubleEntryLoggerMapper.toEntity(doubleEntryLoggerDTO);
        doubleEntryLogger = doubleEntryLoggerRepository.save(doubleEntryLogger);
        return doubleEntryLoggerMapper.toDto(doubleEntryLogger);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DoubleEntryLoggerDTO> findAll() {
        log.debug("Request to get all DoubleEntryLoggers");
        return doubleEntryLoggerRepository.findAll().stream()
            .map(doubleEntryLoggerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<DoubleEntryLoggerDTO> findOne(Long id) {
        log.debug("Request to get DoubleEntryLogger : {}", id);
        return doubleEntryLoggerRepository.findById(id)
            .map(doubleEntryLoggerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DoubleEntryLogger : {}", id);
        doubleEntryLoggerRepository.deleteById(id);
    }
}
