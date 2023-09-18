package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.BillerServiceOptionRepository;
import ng.com.justjava.corebanking.service.mapper.BillerPlatformMapper;
import ng.com.justjava.corebanking.domain.BillerPlatform;
import ng.com.justjava.corebanking.domain.BillerServiceOption;
import ng.com.justjava.corebanking.service.BillerServiceOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link BillerPlatform}.
 */
@Service
@Transactional
public class BillerServiceOptionServiceImpl implements BillerServiceOptionService {

    private final Logger log = LoggerFactory.getLogger(BillerServiceOptionServiceImpl.class);

    private final BillerServiceOptionRepository billerServiceOptionRepository;

    private final BillerPlatformMapper billerPlatformMapper;

    public BillerServiceOptionServiceImpl(BillerServiceOptionRepository billerServiceOptionRepository,
                                          BillerPlatformMapper billerPlatformMapper) {
        this.billerServiceOptionRepository = billerServiceOptionRepository;
        this.billerPlatformMapper = billerPlatformMapper;
    }

    @Override
    public BillerServiceOption save(BillerServiceOption billerServiceOption) {
        log.debug("Request to save BillerPlatform : {}", billerServiceOption);
        BillerServiceOption save = billerServiceOptionRepository.save(billerServiceOption);
        return save;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillerServiceOption> findAll() {
        log.debug("Request to get all BillerPlatforms");
        return billerServiceOptionRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BillerServiceOption> findOne(Long id) {
        log.debug("Request to get BillerPlatform : {}", id);
        return billerServiceOptionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillerPlatform : {}", id);
        billerServiceOptionRepository.deleteById(id);
    }

    @Override
    public List<BillerServiceOption> findAllByBillerCustomFieldOptionId(long billerPlatformId) {
        return billerServiceOptionRepository.findAllByBillerCustomFieldOptionId(billerPlatformId);
    }

    @Override
    public List<BillerServiceOption> findByServiceOptionId(Long serviceOptionId) {
        return billerServiceOptionRepository.findByServiceOptionId(serviceOptionId);
    }
}
