package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.PolarisCardProfile;
import ng.com.systemspecs.apigateway.service.dto.PolarisCardProfileDTO;

import java.util.List;
import java.util.Optional;

public interface PolarisCardProfileService {

    Optional<PolarisCardProfile> findOneByScheme(String scheme);

    Optional<PolarisCardProfile> findOneBySchemeAndCardType(String scheme, String cardType);

    List<PolarisCardProfileDTO> findAllByScheme(String scheme);

    PolarisCardProfileDTO save(PolarisCardProfile polarisCardProfile);

    Optional<PolarisCardProfile> findOneBySchemeAndCardTypeAndCardName(String scheme, String cardType, String cardName);
}

