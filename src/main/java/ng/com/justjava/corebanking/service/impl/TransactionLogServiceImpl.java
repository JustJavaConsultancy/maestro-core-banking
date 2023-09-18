package ng.com.justjava.corebanking.service.impl;

import com.google.gson.Gson;
import ng.com.justjava.corebanking.repository.TransactionLogRepository;
import ng.com.justjava.corebanking.service.mapper.TransactionLogMapper;
import ng.com.justjava.corebanking.domain.TransactionLog;
import ng.com.justjava.corebanking.service.TransactionLogService;
import ng.com.justjava.corebanking.service.dto.FundDTO;
import ng.com.justjava.corebanking.service.dto.TransactionLogDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link TransactionLog}.
 */
@Service
@Transactional
public class TransactionLogServiceImpl implements TransactionLogService {

    private final Logger log = LoggerFactory.getLogger(TransactionLogServiceImpl.class);

    private final TransactionLogRepository transactionLogRepository;

    private final TransactionLogMapper transactionLogMapper;

    public TransactionLogServiceImpl(TransactionLogRepository transactionLogRepository, TransactionLogMapper transactionLogMapper) {
        this.transactionLogRepository = transactionLogRepository;
        this.transactionLogMapper = transactionLogMapper;
    }


    @Override
    public FundDTO save(FundDTO fundDTO) {
        log.debug("Request to save Transaction Log : {}", fundDTO);
        TransactionLog transactionLog = transactionLogMapper.toEntity(fundDTO);
        try {
            TransactionLog save = transactionLogRepository.save(transactionLog);
            log.info("Saved Transaction log ======> " + save);
            return transactionLogMapper.toDto(save);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FundDTO saveBulkTrans(FundDTO fundDTO) {
        log.debug("Request to save Transaction Log : {}", fundDTO);
        TransactionLog transactionLog = toBulkTransactionLog(fundDTO);
        try {
            TransactionLog save = transactionLogRepository.save(transactionLog);
            log.info("Saved Bulk Transaction log ======> " + save);
            return transactionLogMapper.toDto(save);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FundDTO> findAll() {
        log.debug("Request to get all Transaction logs");
        return transactionLogRepository.findAll().stream()
            .map(transactionLogMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Page<FundDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Transaction logs");
        return transactionLogRepository.findAll(pageable)
            .map(transactionLogMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<FundDTO> findOne(Long id) {
        log.debug("Request to get Address : {}", id);
        return transactionLogRepository.findById(id)
            .map(transactionLogMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Address : {}", id);
        transactionLogRepository.deleteById(id);
    }

    @Override
    public FundDTO findByTransRef(String transRef) {
        Optional<TransactionLog> byTransRef = transactionLogRepository.findByTransRefIgnoreCase(transRef);
        if (byTransRef.isPresent()) {
            TransactionLog transactionLog = byTransRef.get();
            log.info("Retrieved Transaction Log ===> " + transactionLog);
            return transactionLogMapper.toDto(transactionLog);
        }

        return null;
    }

    public List<FundDTO> findByAccountNumber(String accountNumber) {
        List<FundDTO> fundDTOS = new ArrayList<>();
        List<FundDTO> byAcct = transactionLogRepository.findByAccountNumber(accountNumber).stream().map(transactionLogMapper::toDto).collect(Collectors.toList());
        List<FundDTO> bySource = transactionLogRepository.findBySourceAccountNumber(accountNumber).stream().map(transactionLogMapper::toDto).collect(Collectors.toList());
        fundDTOS.addAll(byAcct);
        fundDTOS.addAll(bySource);
        return fundDTOS;
    }

    @Override
    public List<FundDTO> findByAccountNumberAndCreatedDateBetween(String accountNumber, Instant fromDate, Instant toDate) {
        List<FundDTO> fundDTOS = new ArrayList<>();
        List<FundDTO> byAcct = transactionLogRepository.findByAccountNumberAndCreatedDateBetweenOrderByCreatedDateDesc(accountNumber,fromDate,toDate).stream().map(transactionLogMapper::toDto).collect(Collectors.toList());
        List<FundDTO> bySource = transactionLogRepository.findBySourceAccountNumberAndCreatedDateBetweenOrderByCreatedDateDesc(accountNumber,fromDate,toDate).stream().map(transactionLogMapper::toDto).collect(Collectors.toList());
        if(byAcct!=null)fundDTOS.addAll(byAcct);
        if(bySource!=null)fundDTOS.addAll(bySource);
        return fundDTOS;
    }

    @Override
    public FundDTO findByRrr(String rrr) {
        List<TransactionLog> byRrr = transactionLogRepository.findByRrr(rrr);
        if (!byRrr.isEmpty()) {
            TransactionLog transactionLog = byRrr.get(0);
            log.info("Retrieved Transaction Log ===> " + transactionLog);
            return transactionLogMapper.toDto(transactionLog);
        }
        return null;
    }

    @Override
    public boolean existsByRrr(String rrr) {
        return transactionLogRepository.existsByRrr(rrr);
    }

    @Override
    public Page<FundDTO> findAllByKeyword(Pageable pageable, String key) {
        Page<TransactionLog> transactionLogPage = transactionLogRepository.findAllByKeywordSearch(pageable, key);
        return transactionLogPage.map(transactionLogMapper::toDto);

    }

    @Override
    public Page<FundDTO> findAllByCreatedDateBetween(Instant fromDate, Instant toDate, Pageable pageable) {
        Page<TransactionLog> fundDTOs = transactionLogRepository.findAllByCreatedDateBetweenOrderByCreatedDateDesc(fromDate, toDate, pageable);
        return fundDTOs.map(transactionLogMapper::toDto);

    }

    @Override
    public List<FundDTO> findAllByCreatedDateBetween(Instant fromDate, Instant toDate) {
        List<TransactionLog> fundDTOs = transactionLogRepository.findAllByCreatedDateBetweenOrderByCreatedDateDesc(fromDate, toDate);
        return fundDTOs.stream().map(transactionLogMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<FundDTO> findAllBySchemeAndCreatedDateBetween(String scheme, Instant fromInstant, Instant toInstant, Pageable pageable) {
        Page<TransactionLog> fundDTOs = transactionLogRepository.findAllByCreatedDateBetweenOrderByCreatedDateDesc(fromInstant, toInstant, pageable);
        return fundDTOs.map(transactionLogMapper::toDto);
    }

    @Override
    public List<FundDTO> findAllByNarrationLike(String pId) {
        List<TransactionLog> logs = transactionLogRepository.findAllByNarrationLike(pId);
        return logs.stream().map(transactionLogMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<FundDTO> findAllByNarrationContains(String pId) {
        List<TransactionLog> logs = transactionLogRepository.findAllByNarrationContains(pId);
        return logs.stream().map(transactionLogMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<FundDTO> findBySourceAccountNumberAndCreatedDateBetween(String accountNumber, Instant fromInstant, Instant toInstant) {
        return transactionLogRepository.findBySourceAccountNumberAndCreatedDateBetweenOrderByCreatedDateDesc(accountNumber,fromInstant,toInstant).stream().map(transactionLogMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public Optional<FundDTO> updateTransactionLogStatus(TransactionLogDto logDto) {
        Optional<FundDTO> fundDTO = transactionLogRepository
            .findByTransRefIgnoreCase(logDto.getTransRef())
            .map(transactionLog -> {
                transactionLog.setStatus(logDto.getStatus().name());
                return transactionLogMapper.toDto(transactionLog);
            });
        return fundDTO;
    }

    public TransactionLog toBulkTransactionLog(FundDTO dto){
        if ( dto == null ) {
            return null;
        }

        TransactionLog transactionLog = new TransactionLog();

        transactionLog.setCreatedDate( dto.getCreatedDate() );
        transactionLog.setId( dto.getId() );
        transactionLog.setAmount( dto.getAmount() );
        transactionLog.setSpecificChannel( dto.getSpecificChannel() );
        transactionLog.setAccountNumber( dto.getAccountNumber() );
        transactionLog.setChannel( dto.getChannel() );
        transactionLog.setSourceBankCode( dto.getSourceBankCode() );
        transactionLog.setSourceAccountNumber( dto.getSourceAccountNumber() );
        transactionLog.setDestBankCode( dto.getDestBankCode() );
        transactionLog.setTransRef( dto.getTransRef() );
        transactionLog.setPhoneNumber( dto.getPhoneNumber() );
        transactionLog.setNarration( dto.getNarration() );
        transactionLog.setShortComment( dto.getShortComment() );
        transactionLog.setWalletAccount( dto.isWalletAccount() );
        transactionLog.setToBeSaved( dto.isToBeSaved() );
        transactionLog.setBeneficiaryName( dto.getBeneficiaryName() );
        transactionLog.setRrr( dto.getRrr() );
        if ( dto.getStatus() != null ) {
            transactionLog.setStatus( dto.getStatus().name() );
        }
        transactionLog.setSourceAccountName( dto.getSourceAccountName() );
        transactionLog.setAgentRef( dto.getAgentRef() );
        transactionLog.setRedeemBonus( dto.isRedeemBonus() );
        transactionLog.setCharges( dto.getCharges() );
        transactionLog.setBonusAmount( dto.getBonusAmount() );
        transactionLog.setBeneficiaryAccounts(new Gson().toJson(dto.getBulkAccountNos()));
        transactionLog.setBulkTrans(dto.isBulkTrans());
        return transactionLog;
    }
}
