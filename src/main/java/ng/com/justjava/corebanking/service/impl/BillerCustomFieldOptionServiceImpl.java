package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.BillerCustomFieldOptionRepository;
import ng.com.justjava.corebanking.domain.BillerCustomFieldOption;
import ng.com.justjava.corebanking.domain.BillerPlatform;
import ng.com.justjava.corebanking.service.BillerCustomFieldOptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class BillerCustomFieldOptionServiceImpl implements BillerCustomFieldOptionService {

    private final BillerCustomFieldOptionRepository billerCustomFieldOptionRepository;

    public BillerCustomFieldOptionServiceImpl(BillerCustomFieldOptionRepository billerCustomFieldOptionRepository) {
        this.billerCustomFieldOptionRepository = billerCustomFieldOptionRepository;
    }

    @Override
    public Optional<BillerCustomFieldOption> findByBillerPlatform(BillerPlatform billerPlatform) {
        return billerCustomFieldOptionRepository.findByBillerPlatform(billerPlatform);
    }

    @Override
    public List<BillerCustomFieldOption> findAllByBillerPlatformId(long billerPlatformId) {
        return billerCustomFieldOptionRepository.findAllByBillerPlatformId(billerPlatformId);
    }

    @Override
    public List<BillerCustomFieldOption> findBillerCustomFieldOptionsByBillerPlatform(BillerPlatform billerPlatform) {
        return billerCustomFieldOptionRepository.findBillerCustomFieldOptionsByBillerPlatform(billerPlatform);
    }

    @Override
    public BillerCustomFieldOption save(BillerCustomFieldOption billerCustomFieldOption) {
        return billerCustomFieldOptionRepository.save(billerCustomFieldOption);
    }

    @Override
    public Optional<BillerCustomFieldOption> findById(Long id) {
        return billerCustomFieldOptionRepository.findById(id);
    }
}
