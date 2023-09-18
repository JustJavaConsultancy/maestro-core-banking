package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.PolarisCardProfileRepository;
import ng.com.justjava.corebanking.service.mapper.PolarisCardProfileMapper;
import ng.com.justjava.corebanking.domain.PolarisCardProfile;
import ng.com.justjava.corebanking.service.PolarisCardProfileService;
import ng.com.justjava.corebanking.service.dto.PolarisCardProfileDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class PolarisCardProfileServiceImpl implements PolarisCardProfileService {

    private final PolarisCardProfileRepository polarisCardProfileRepository;
    private final PolarisCardProfileMapper polarisCardProfileMapper;

    public PolarisCardProfileServiceImpl(PolarisCardProfileRepository polarisCardProfileRepository, PolarisCardProfileMapper polarisCardProfileMapper) {
        this.polarisCardProfileRepository = polarisCardProfileRepository;
        this.polarisCardProfileMapper = polarisCardProfileMapper;
    }

    @Override
    public Optional<PolarisCardProfile> findOneByScheme(String scheme) {
        return polarisCardProfileRepository.findOneByScheme(scheme);
    }

    @Override
    public Optional<PolarisCardProfile> findOneBySchemeAndCardType(String scheme, String cardType) {
        return polarisCardProfileRepository.findOneBySchemeAndCardType(scheme, cardType);
    }

    @Override
    public List<PolarisCardProfileDTO> findAllByScheme(String scheme) {
        return polarisCardProfileRepository.findAllByScheme(scheme).stream().map(polarisCardProfileMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public PolarisCardProfileDTO save(PolarisCardProfile polarisCardProfile) {
        return polarisCardProfileMapper.toDto(polarisCardProfileRepository.save(polarisCardProfile));
    }

    @Override
    public Optional<PolarisCardProfile> findOneBySchemeAndCardTypeAndCardName(String scheme, String cardType, String cardName) {
        return polarisCardProfileRepository.findOneBySchemeAndCardTypeAndCardName(scheme,cardType, cardName);
    }
}
