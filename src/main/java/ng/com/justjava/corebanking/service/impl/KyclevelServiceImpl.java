package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.KyclevelRepository;
import ng.com.justjava.corebanking.service.mapper.KyclevelMapper;
import ng.com.justjava.corebanking.service.KyclevelService;
import ng.com.justjava.corebanking.domain.Kyclevel;
import ng.com.justjava.corebanking.service.dto.KyclevelDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Kyclevel}.
 */
@Service
@Transactional
public class KyclevelServiceImpl implements KyclevelService {

    private final Logger log = LoggerFactory.getLogger(KyclevelServiceImpl.class);

    private final KyclevelRepository kyclevelRepository;

    private final KyclevelMapper kyclevelMapper;

    public KyclevelServiceImpl(KyclevelRepository kyclevelRepository, KyclevelMapper kyclevelMapper) {
        this.kyclevelRepository = kyclevelRepository;
        this.kyclevelMapper = kyclevelMapper;
    }

    @Override
    public KyclevelDTO save(KyclevelDTO kyclevelDTO) {
        log.debug("Request to save Kyclevel : {}", kyclevelDTO);
        Kyclevel kyclevel = kyclevelMapper.toEntity(kyclevelDTO);
        kyclevel = kyclevelRepository.save(kyclevel);
        return kyclevelMapper.toDto(kyclevel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KyclevelDTO> findAll() {
        log.debug("Request to get all Kyclevels");
        return kyclevelRepository.findAll().stream()
            .map(kyclevelMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<KyclevelDTO> findOne(Long id) {
        log.debug("Request to get Kyclevel : {}", id);
        return kyclevelRepository.findById(id)
            .map(kyclevelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Kyclevel : {}", id);
        kyclevelRepository.deleteById(id);
    }

	@Override
	public Kyclevel findByKycLevel(Integer kycLevel) {
		// TODO Auto-generated method stub
		return kyclevelRepository.findByKycLevel(kycLevel);
	}

    @Override
    public Kyclevel findByKyc(String kyc) {
        // TODO Auto-generated method stub
        return kyclevelRepository.findByKyc(kyc);
    }
}
