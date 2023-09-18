package ng.com.systemspecs.apigateway.service.impl;

import ng.com.systemspecs.apigateway.domain.Scheme;
import ng.com.systemspecs.apigateway.repository.SchemeRepository;
import ng.com.systemspecs.apigateway.security.AuthoritiesConstants;
import ng.com.systemspecs.apigateway.service.SchemeService;
import ng.com.systemspecs.apigateway.service.WalletAccountService;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.SchemeCallBackDTO;
import ng.com.systemspecs.apigateway.service.dto.SchemeDTO;
import ng.com.systemspecs.apigateway.service.dto.UpdateKeysDTO;
import ng.com.systemspecs.apigateway.service.mapper.SchemeMapper;
import ng.com.systemspecs.apigateway.util.Utility;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.util.encoders.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Scheme}.
 */
@Service
@Transactional
public class SchemeServiceImpl implements SchemeService {

    private final Logger log = LoggerFactory.getLogger(SchemeServiceImpl.class);

    private final SchemeRepository schemeRepository;
    private final Utility utility;
    private final WalletAccountService walletAccountService;

    private final SchemeMapper schemeMapper;

    @Value("${app.constants.dfs.debit-account}")
    String debitAccountNumber;

    @Value("${app.scheme.systemspecs}")
    private String SYSTEMSPECS_SCHEME;

    @Value("${app.constants.dfs.bank-code}")
    String bankCode;

    public SchemeServiceImpl(SchemeRepository schemeRepository, @Lazy Utility utility, @Lazy WalletAccountService walletAccountService, SchemeMapper schemeMapper) {
        this.schemeRepository = schemeRepository;
        this.utility = utility;
        this.walletAccountService = walletAccountService;
        this.schemeMapper = schemeMapper;
    }

    @Override
    public SchemeDTO save(SchemeDTO schemeDTO) {
        log.debug("Request to save Scheme : {}", schemeDTO);
        Scheme scheme = schemeMapper.toEntity(schemeDTO);
        scheme.setCallbackUrl(schemeDTO.getCallBackUrl());
        scheme = schemeRepository.save(scheme);
        return schemeMapper.toDto(scheme);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SchemeDTO> findAll() {
        log.debug("Request to get all Schemes");
        return schemeRepository.findAllByOrderById().stream()
            .map(schemeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Scheme> findAllSchemes() {
        return schemeRepository.findAll();
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<SchemeDTO> findOne(Long id) {
        log.debug("Request to get Scheme : {}", id);
        return schemeRepository.findById(id)
            .map(schemeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Scheme> findSchemeId(Long id) {
        return schemeRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Scheme : {}", id);
        schemeRepository.deleteById(id);
    }

    @Override
    public Scheme findBySchemeID(String schemeID) {
        return schemeRepository.findBySchemeID(schemeID);
    }

    @Override
    public SchemeDTO createScheme(SchemeDTO schemeDTO) {
        byte[] encode = Hex.encode(schemeDTO.getScheme().getBytes());
        String schemeId = new String(encode);
        schemeDTO.setSchemeID(schemeId);

        Scheme bySchemeID = findBySchemeID(schemeId);
        if (bySchemeID == null) {

            if (schemeDTO.getAccountNumber() == null || schemeDTO.getAccountNumber().isEmpty()) {
                schemeDTO.setAccountNumber(debitAccountNumber);
            }
            if (schemeDTO.getBankCode() == null || schemeDTO.getBankCode().isEmpty()) {
                schemeDTO.setBankCode(bankCode);
            }
            return save(schemeDTO);
        }

        return null;
    }

    @Override
    public GenericResponseDTO getAdminScheme(String phoneNumber) {
        phoneNumber = utility.formatPhoneNumber(phoneNumber);

        Set<Scheme> schemeSet = new HashSet<>();
        List<SchemeDTO> schemeDTOS = new ArrayList<>();
        List<SchemeDTO> allSchemes = findAll();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(AuthoritiesConstants.SUPER_ADMIN))) {
            return new GenericResponseDTO("00", HttpStatus.OK, "success", allSchemes);
        }

        if (auth != null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(AuthoritiesConstants.ADMIN))) {
            Scheme scheme = findBySchemeID(SYSTEMSPECS_SCHEME);
            SchemeDTO schemeDTO = schemeMapper.toDto(scheme);
            schemeDTOS.add(schemeDTO);
        }

        for (SchemeDTO schemeDTO : allSchemes) {
            if (auth != null
                && auth.getAuthorities().
                stream()
                .anyMatch(a -> a.getAuthority()
                    .toUpperCase()
                    .startsWith("ROLE_" + schemeDTO.getScheme()
                        .substring(0, 3).toUpperCase()))) {
                schemeDTOS.add(schemeDTO);
            }
        }

        return new GenericResponseDTO("00", HttpStatus.OK, "success", schemeDTOS);
    }

    @Override
    public GenericResponseDTO updateSchemeCallBack(SchemeCallBackDTO schemeCallBackDTO) {

        Scheme scheme = findBySchemeID(schemeCallBackDTO.getScheme());
        if (scheme == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Scheme not found.", null);
        }
        scheme.setCallbackUrl(schemeCallBackDTO.getCallBackUrl());

        schemeRepository.save(scheme);

        return new GenericResponseDTO("00", HttpStatus.OK, "success", null);
    }

    @Override
    public GenericResponseDTO updateKeys(UpdateKeysDTO updateKeysDTO) {

        Scheme scheme = findBySchemeID(updateKeysDTO.getScheme());
        if (scheme == null){
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Scheme not found.", null);
        }
        scheme.setApiKey(StringUtils.defaultIfBlank(updateKeysDTO.getApiKey(), scheme.getApiKey()));
        scheme.setSecretKey(StringUtils.defaultIfBlank(updateKeysDTO.getSecretKey(), scheme.getSecretKey()));
        scheme.setScheme(StringUtils.defaultIfBlank(updateKeysDTO.getScheme(), scheme.getScheme()));
        scheme.setBankCode(StringUtils.defaultIfBlank(updateKeysDTO.getBankCode(), scheme.getBankCode()));
        scheme.setAccountNumber(StringUtils.defaultIfBlank(updateKeysDTO.getAccountNumber(), scheme.getAccountNumber()));
        scheme.setCallbackUrl(StringUtils.defaultIfBlank(updateKeysDTO.getCallBackUrl(), scheme.getCallbackUrl()));

        schemeRepository.save(scheme);
        return new GenericResponseDTO("00", HttpStatus.OK, "success", null);
    }

}
