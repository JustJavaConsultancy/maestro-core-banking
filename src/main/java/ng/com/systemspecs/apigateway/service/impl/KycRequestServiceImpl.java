package ng.com.systemspecs.apigateway.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ng.com.systemspecs.apigateway.domain.KycRequest;
import ng.com.systemspecs.apigateway.domain.Kyclevel;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.domain.enumeration.KycRequestDocType;
import ng.com.systemspecs.apigateway.domain.enumeration.KycRequestStatus;
import ng.com.systemspecs.apigateway.repository.KycRequestRepository;
import ng.com.systemspecs.apigateway.service.CashConnectService;
import ng.com.systemspecs.apigateway.service.KycRequestService;
import ng.com.systemspecs.apigateway.service.KyclevelService;
import ng.com.systemspecs.apigateway.service.ProfileService;
import ng.com.systemspecs.apigateway.service.dto.GenericResponseDTO;
import ng.com.systemspecs.apigateway.service.dto.KycRequestDTO;
import ng.com.systemspecs.apigateway.service.dto.KycRequestDecisionDTO;
import ng.com.systemspecs.apigateway.service.dto.ProfileDTO;
import ng.com.systemspecs.apigateway.service.dto.UpgradeTierKycDTO;
import ng.com.systemspecs.apigateway.service.mapper.KycRequestMapper;
import ng.com.systemspecs.apigateway.service.mapper.ProfileMapper;
import ng.com.systemspecs.apigateway.util.Utility;

/**
 * Service Implementation for managing {@link KycRequest}.
 */
@Service
@Transactional
public class KycRequestServiceImpl implements KycRequestService {

    private final Logger log = LoggerFactory.getLogger(KycRequestServiceImpl.class);

    private final KycRequestRepository kycRequestRepository;

    private final KycRequestMapper kycRequestMapper;

    private final ProfileService profileService;

    private final ProfileMapper profileMapper;

    private final KyclevelService kyclevelService;
    private final CashConnectService cashConnectService;
    private final Utility utility;

    public KycRequestServiceImpl(KycRequestRepository kycRequestRepository, KycRequestMapper kycRequestMapper,
                                 ProfileService profileService, KyclevelService kyclevelService,
                                 CashConnectService cashConnectService,
                                 Utility utility, ProfileMapper profileMapper) {
        this.kycRequestRepository = kycRequestRepository;
        this.kycRequestMapper = kycRequestMapper;
        this.profileService = profileService;
        this.kyclevelService = kyclevelService;
        this.cashConnectService = cashConnectService;
        this.utility = utility;
        this.profileMapper = profileMapper;
    }

    @Override
    public KycRequest save(KycRequest kycRequest) {
        log.debug("Request to save KycRequest : {}", kycRequest);
        Profile profile = profileService.findByPhoneNumber("+2348160110390");

        if (profile != null) {
            kycRequest.setApprover(profile);
        }

        return kycRequestRepository.save(kycRequest);
    }

    @Override
    public KycRequestDTO save(KycRequestDTO kycRequestDTO) {
        log.debug("Request to save KycRequest : {}", kycRequestDTO);
        KycRequest kycRequest = kycRequestMapper.toEntity(kycRequestDTO);

//        Profile profile = profileService.findByPhoneNumber("+2348035133291");
        Profile profile = profileService.findByPhoneNumber("+2349131038088");

        if (profile != null) {
            kycRequest.setApprover(profile);
        }

        kycRequest = kycRequestRepository.save(kycRequest);
        log.debug("Saved KycRequest : {}", kycRequest);
        return kycRequestMapper.toDto(kycRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<KycRequestDTO> findAll() {
        log.debug("Request to get all KycRequests");
        return kycRequestRepository.findAll().stream()
            .map(kycRequestMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<KycRequestDTO> findOne(Long id) {
        log.debug("Request to get KycRequest : {}", id);
        return kycRequestRepository.findById(id)
            .map(kycRequestMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete KycRequest : {}", id);
        kycRequestRepository.deleteById(id);
    }

    @Override
    public GenericResponseDTO approveKycRequest(KycRequestDecisionDTO decision) {

        try{

            Optional<KycRequestDTO> one = findOne(decision.getKycRequestId());

            if (!one.isPresent()) {
                return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, "Invalid request Id", null);
            }

            KycRequestDTO kycRequestDTO = one.get();
            kycRequestDTO.setStatus(decision.getStatus());

            KycRequestDTO save = save(kycRequestDTO);
            log.debug(decision.getStatus().name());

            log.info("Updated KycRequest ====> {} ", save);

            Long profileId = save.getProfileId();
            Optional<ProfileDTO> profileDTOOptional = profileService.findOne(profileId);
            Kyclevel byKycLevel = null;
            log.debug("Retrieved Profile ====> {} ", profileDTOOptional.get());
            if (profileDTOOptional.isPresent()) {
                ProfileDTO profileDTO = profileDTOOptional.get();

                Profile profile = profileMapper.toEntity(profileDTO);
                if (KycRequestStatus.APPROVED.equals(decision.getStatus())) {

                    byKycLevel = kyclevelService.findByKycLevel(kycRequestDTO.getCurrentLevel() + 1);

                    if (byKycLevel != null) {
//                	log.debug("GETTING WALLET");
                        Optional<String> nubanOptional = utility.getWalletAccountNubanByPhoneNumber(profileDTO.getPhoneNumber());
//                    log.debug("GETTING NUBAN::");
                        if (nubanOptional.isPresent()) {
                            if(profile.getValidDocType().equals(KycRequestDocType.NIN))
                                profileDTO.setNin(kycRequestDTO.getDocumentId());
                            profileDTO.setKycId(byKycLevel.getId());
                            ProfileDTO save1 = profileService.save(profileDTO);

                            log.info("KYC profileDTO ====+++> " + save1);


                            UpgradeTierKycDTO upgradeTierKycDTO = new UpgradeTierKycDTO();

                            String nubanAccountNumber = nubanOptional.get();
                            upgradeTierKycDTO.setAccountNumber(nubanAccountNumber);

                            log.info("byKycLevel.getKycLevel() ====+++> " + byKycLevel.getKycLevel());
                            upgradeTierKycDTO.setAccountTier(byKycLevel.getKycLevel());
                            if (true) {
                                utility.sendSuccessKYCEmailUpgrade(profile, byKycLevel.getKycLevel(),
                                    byKycLevel.getKycLevel() + 1);

                                return new GenericResponseDTO("00", HttpStatus.OK, "success", save);
                            }

//                        Integer nextTier = byKycLevel.getKycLevel();
//                        //upgradeTierKycDTO.setAccountTier(nextTier);
//                        upgradeTierKycDTO.setAddressOnUtilityBill("No 50, Awotan Apete Ibadan");
//                        upgradeTierKycDTO.setDateOnUtilityBill("2021-10-10");
//                        upgradeTierKycDTO.setCommentsForUtilityBill("Akinrinde's Utility Bill");
//                        upgradeTierKycDTO.setNameOnUtilityBill("Akinrinde Kazeem");
//
//                        System.out.println("upgradeTierKycDTO=== "+upgradeTierKycDTO);
//                        ResponseEntity<GenericResponseDTO> responseEntity = cashConnectService.upgradeAccountKyc(upgradeTierKycDTO);
//
//                        System.out.println(" Response from the cashConnectService KYC Upgrade =="+responseEntity);
//
//                        if (HttpStatus.OK.equals(responseEntity.getStatusCode())) {
//
//                            UpgradeTierDTO upgradeTierDTO = new UpgradeTierDTO();
//                            upgradeTierDTO.setAccountNumber(nubanAccountNumber);
//                            upgradeTierDTO.setAccountTier(nextTier);
//                            ResponseEntity<GenericResponseDTO> responseEntity1 = cashConnectService.upgradeAccountTier(upgradeTierDTO);
//                            if (HttpStatus.OK.equals(responseEntity1.getStatusCode())) {
//                                return new GenericResponseDTO("00", HttpStatus.OK, "success", save);
//                            }
//                        }
                        }

                    }

                } else {
                    byKycLevel = kyclevelService.findByKycLevel(kycRequestDTO.getCurrentLevel());
                    log.debug("KYC Request Rejected for ===> {}", save);
                    utility.sendUnsuccessfulKYCEmailUpgrade(profile, byKycLevel.getKycLevel(),
                        byKycLevel.getKycLevel() + 1, decision.getReason());
                    return new GenericResponseDTO("00", HttpStatus.OK, "Request rejected", save);
                }
            }

            return new GenericResponseDTO("99", HttpStatus.OK, "failed", null);

        }catch(Exception e){
            e.printStackTrace();
            return new GenericResponseDTO("99", HttpStatus.BAD_REQUEST, e.getLocalizedMessage(), null);
        }

    }

    @Override
    public List<KycRequestDTO> findByStatus(KycRequestStatus status) {
        // TODO Auto-generated method stub
        log.debug("Request to get all KycRequests");
        return kycRequestRepository.findByStatus(status).stream()
            .map(kycRequestMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
        //return kycRequestRepository.findByStatus(KycRequestStatus.AWAITING_APPROVAL);
    }
}
