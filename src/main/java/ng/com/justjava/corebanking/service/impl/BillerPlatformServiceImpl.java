package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.BillerPlatformRepository;
import ng.com.justjava.corebanking.service.mapper.BillerPlatformMapper;
import ng.com.justjava.corebanking.domain.Biller;
import ng.com.justjava.corebanking.domain.BillerPlatform;
import ng.com.justjava.corebanking.service.BillerPlatformService;
import ng.com.justjava.corebanking.service.dto.BillerPlatformDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BillerPlatform}.
 */
@Service
@Transactional
public class BillerPlatformServiceImpl implements BillerPlatformService {

    private final Logger log = LoggerFactory.getLogger(BillerPlatformServiceImpl.class);

    private final BillerPlatformRepository billerPlatformRepository;

    private final BillerPlatformMapper billerPlatformMapper;

    public BillerPlatformServiceImpl(BillerPlatformRepository billerPlatformRepository, BillerPlatformMapper billerPlatformMapper) {
        this.billerPlatformRepository = billerPlatformRepository;
        this.billerPlatformMapper = billerPlatformMapper;
    }

    @Override
    public BillerPlatformDTO save(BillerPlatformDTO billerPlatformDTO) {
        log.debug("Request to save BillerPlatform : {}", billerPlatformDTO);
        BillerPlatform billerPlatform = billerPlatformMapper.toEntity(billerPlatformDTO);
        billerPlatform = billerPlatformRepository.save(billerPlatform);
        return billerPlatformMapper.toDto(billerPlatform);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillerPlatformDTO> findAll() {
        log.debug("Request to get all BillerPlatforms");
        return billerPlatformRepository.findAll().stream()
            .map(billerPlatformMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BillerPlatformDTO> findOne(Long id) {
        log.debug("Request to get BillerPlatform : {}", id);
        return billerPlatformRepository.findById(id)
            .map(billerPlatformMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillerPlatform : {}", id);
        billerPlatformRepository.deleteById(id);
    }

    @Override
    public List<BillerPlatform> findAllByBiller(Biller biller) {
        return billerPlatformRepository.findAllByBiller(biller);
    }

    @Override
    public Optional<BillerPlatform> findByBillerplatformID(Long billerPlatformID) {
        return billerPlatformRepository.findByBillerplatformID(billerPlatformID);
    }

    @Override
    public List<BillerPlatform> findBillerPlatformsByBiller(Biller biller) {

        return billerPlatformRepository.findBillerPlatformsByBiller(biller);
    }
}
