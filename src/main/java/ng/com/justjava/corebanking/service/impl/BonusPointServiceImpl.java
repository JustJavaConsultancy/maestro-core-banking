package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.BonusPointRepository;
import ng.com.justjava.corebanking.service.mapper.BonusPointMapper;
import ng.com.justjava.corebanking.config.AsyncConfiguration;
import ng.com.justjava.corebanking.domain.BonusPoint;
import ng.com.justjava.corebanking.domain.Journal;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.service.BonusPointService;
import ng.com.justjava.corebanking.service.ProfileService;
import ng.com.justjava.corebanking.service.dto.BonusPointDTO;
import ng.com.justjava.corebanking.service.dto.FundDTO;
import ng.com.justjava.corebanking.util.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BonusPoint}.
 */
@Service
@Transactional
public class BonusPointServiceImpl implements BonusPointService {

    private final Logger log = LoggerFactory.getLogger(BonusPointServiceImpl.class);

    private final BonusPointRepository bonusPointRepository;
    private final AsyncConfiguration asyncConfiguration;
    private final ProfileService profileService;
    private final Utility utility;

    @Value("${app.percentage.bonus}")
    private double bonusPercentage;

    @Value("${app.constants.dfs.biller-payment-account}")
    private String BILLER_PAYMENT_ACCOUNT;

    private final BonusPointMapper bonusPointMapper;

    public BonusPointServiceImpl(BonusPointRepository bonusPointRepository, AsyncConfiguration asyncConfiguration, ProfileService profileService, Utility utility, BonusPointMapper bonusPointMapper) {
        this.bonusPointRepository = bonusPointRepository;
        this.asyncConfiguration = asyncConfiguration;
        this.profileService = profileService;
        this.utility = utility;
        this.bonusPointMapper = bonusPointMapper;
    }

    @Override
    public BonusPointDTO save(BonusPointDTO bonusPointDTO) {
        log.debug("Request to save BonusPoint : {}", bonusPointDTO);
        BonusPoint bonusPoint = bonusPointMapper.toEntity(bonusPointDTO);
        bonusPoint = bonusPointRepository.save(bonusPoint);
        return bonusPointMapper.toDto(bonusPoint);
    }

    @Override
    public BonusPointDTO save(BonusPoint bonusPoint) {
        return bonusPointMapper.toDto(bonusPointRepository.save(bonusPoint));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BonusPointDTO> findAll() {
        log.debug("Request to get all BonusPoints");
        return bonusPointRepository.findAll().stream()
            .map(bonusPointMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BonusPointDTO> findOne(Long id) {
        log.debug("Request to get BonusPoint : {}", id);
        return bonusPointRepository.findById(id)
            .map(bonusPointMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BonusPoint : {}", id);
        bonusPointRepository.deleteById(id);
    }

    @Override
    public List<BonusPointDTO> getCustomerBonusPoints(String phoneNumber) {
        log.debug("Request to get all BonusPoints of a customer");

        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        return bonusPointRepository.findAllByProfile_PhoneNumber(phoneNumber).stream()
            .map(bonusPointMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public void calculateBonusPoint(Journal journal, Profile profile, FundDTO fundDTO) {

        Executor asyncExecutor = asyncConfiguration.getAsyncExecutor();

        if (asyncExecutor != null) {
            asyncExecutor.execute(() -> {
                BonusPoint bonusPoint = new BonusPoint();

                Double bonusPointAmount = getBonusPointAmount(journal);

                if (bonusPointAmount > 0) {
                    bonusPoint.setProfile(profile);
                    bonusPoint.setJournal(journal);
                    bonusPoint.setRemark("Ok");

                    bonusPoint.setAmount(bonusPointAmount);
                    BonusPointDTO save = save(bonusPoint);

                    System.out.println(" The resulting entity \n\n\n\n\n\n\n\n====bonusPoint==" + save);

                    profile.setTotalBonus(profile.getTotalBonus() + bonusPoint.getAmount());

                    Profile savedProfile = profileService.save(profile);

                    System.out.println(" The saved profile \n\n\n\n\n\n\n\n====Profile==" + savedProfile);
                }
            });
        }
    }

    private Double getBonusPointAmount(Journal journal) {

        Double charges = journal.getChanges();
        return charges * bonusPercentage;
    }
}
