package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.PolarisCardProfileDTO;
import ng.com.justjava.corebanking.domain.PolarisCardProfile;

import java.util.List;
import java.util.Optional;

public interface PolarisCardProfileService {

    Optional<PolarisCardProfile> findOneByScheme(String scheme);

    Optional<PolarisCardProfile> findOneBySchemeAndCardType(String scheme, String cardType);

    List<PolarisCardProfileDTO> findAllByScheme(String scheme);

    PolarisCardProfileDTO save(PolarisCardProfile polarisCardProfile);

    Optional<PolarisCardProfile> findOneBySchemeAndCardTypeAndCardName(String scheme, String cardType, String cardName);
}

