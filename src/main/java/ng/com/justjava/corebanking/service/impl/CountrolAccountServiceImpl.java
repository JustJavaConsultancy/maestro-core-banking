package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.CountrolAccountRepository;
import ng.com.justjava.corebanking.service.mapper.CountrolAccountMapper;
import ng.com.justjava.corebanking.service.CountrolAccountService;
import ng.com.justjava.corebanking.domain.CountrolAccount;
import ng.com.justjava.corebanking.service.dto.CountrolAccountDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CountrolAccount}.
 */
@Service
@Transactional
public class CountrolAccountServiceImpl implements CountrolAccountService {

    private final Logger log = LoggerFactory.getLogger(CountrolAccountServiceImpl.class);

    private final CountrolAccountRepository countrolAccountRepository;

    private final CountrolAccountMapper countrolAccountMapper;

    public CountrolAccountServiceImpl(CountrolAccountRepository countrolAccountRepository, CountrolAccountMapper countrolAccountMapper) {
        this.countrolAccountRepository = countrolAccountRepository;
        this.countrolAccountMapper = countrolAccountMapper;
    }

    @Override
    public CountrolAccountDTO save(CountrolAccountDTO countrolAccountDTO) {
        log.debug("Request to save CountrolAccount : {}", countrolAccountDTO);
        CountrolAccount countrolAccount = countrolAccountMapper.toEntity(countrolAccountDTO);
        countrolAccount = countrolAccountRepository.save(countrolAccount);
        return countrolAccountMapper.toDto(countrolAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CountrolAccountDTO> findAll() {
        log.debug("Request to get all CountrolAccounts");
        return countrolAccountRepository.findAll().stream()
            .map(countrolAccountMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<CountrolAccountDTO> findOne(Long id) {
        log.debug("Request to get CountrolAccount : {}", id);
        return countrolAccountRepository.findById(id)
            .map(countrolAccountMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CountrolAccount : {}", id);
        countrolAccountRepository.deleteById(id);
    }
}
