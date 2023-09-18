package ng.com.systemspecs.apigateway.service.impl;


import ng.com.systemspecs.apigateway.domain.Beneficiary;
import ng.com.systemspecs.apigateway.domain.Biller;
import ng.com.systemspecs.apigateway.domain.Profile;
import ng.com.systemspecs.apigateway.repository.BeneficiaryRepository;
import ng.com.systemspecs.apigateway.service.BeneficiaryService;
import ng.com.systemspecs.apigateway.service.dto.BeneficiaryDTO;
import ng.com.systemspecs.apigateway.service.mapper.BeneficiaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Biller}.
 */

@Service
@Transactional
public class BeneficiaryServiceImpl implements BeneficiaryService {

    private final Logger log = LoggerFactory.getLogger(BeneficiaryServiceImpl.class);

    private final BeneficiaryRepository beneficiaryRepository;
    private final BeneficiaryMapper beneficiaryMapper;

    public BeneficiaryServiceImpl(BeneficiaryRepository beneficiaryRepository, BeneficiaryMapper beneficiaryMapper) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.beneficiaryMapper = beneficiaryMapper;
    }

    @Override
    public BeneficiaryDTO save(BeneficiaryDTO beneficiaryDTO, Profile accountOwner) {
        return null;
    }

    @Override
    public Beneficiary save(BeneficiaryDTO beneficiaryDTO) {
        log.debug("Request to save Beneficiary : {}", beneficiaryDTO);
        Beneficiary beneficiary = beneficiaryMapper.toEntity(beneficiaryDTO);
        return beneficiaryRepository.save(beneficiary);
    }

    @Override
    public Beneficiary save(Beneficiary beneficiary) {
        log.debug("Request to save Beneficiary : {}", beneficiary);
        return beneficiaryRepository.save(beneficiary);
    }

    @Override
    public List<BeneficiaryDTO> findAll() {
        List<Beneficiary> all = beneficiaryRepository.findAll();
        return beneficiaryMapper.toDto(all);
    }

    @Override
    public Optional<BeneficiaryDTO> findOne(Long id) {
        return beneficiaryRepository.findById(id)
            .map(beneficiaryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        beneficiaryRepository.deleteById(id);
    }

    @Override
    public List<BeneficiaryDTO> findCustomerBeneficiaries(String phoneNumber) {
        return beneficiaryRepository.findAllByAccountOwner_PhoneNumber(phoneNumber)
            .stream()
            .map(beneficiaryMapper::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<Beneficiary> findByAccountOwnerAndBankCode(Profile accountOwner, String accountNumber, String bankCode) {
        return beneficiaryRepository.findByAccountOwnerAndAccountNumberAndBankCode(accountOwner, accountNumber, bankCode);
    }

    @Override
    public Optional<Beneficiary> findByAccountOwnerAndAccountNumber(Profile accountOwner, String accountNumber) {
        return beneficiaryRepository.findByAccountOwnerAndAccountNumber(accountOwner, accountNumber);
    }

}
