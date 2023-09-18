package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.IpgSynchTransactionRepository;
import ng.com.justjava.corebanking.service.mapper.IpgSynchTransactionMapper;
import ng.com.justjava.corebanking.domain.IpgSynchTransaction;
import ng.com.justjava.corebanking.domain.enumeration.NavsaTransactionStatus;
import ng.com.justjava.corebanking.service.IpgSynchTransactionService;
import ng.com.justjava.corebanking.service.dto.stp.IPGSynchTransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class IpgSynchTransactionServiceImpl implements IpgSynchTransactionService {

    private final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final IpgSynchTransactionRepository ipgSynchTransactionRepository;
    private final IpgSynchTransactionMapper ipgSynchTransactionMapper;

    public IpgSynchTransactionServiceImpl(IpgSynchTransactionRepository ipgSynchTransactionRepository, IpgSynchTransactionMapper ipgSynchTransactionMapper) {
        this.ipgSynchTransactionRepository = ipgSynchTransactionRepository;
        this.ipgSynchTransactionMapper = ipgSynchTransactionMapper;
    }

    @Override
    public IPGSynchTransactionDTO save(IPGSynchTransactionDTO ipgSynchTransactionDTO) {

        log.debug("Request to save IpgSynchTransaction : {}", ipgSynchTransactionDTO);

        IpgSynchTransaction ipgSynchTransaction = ipgSynchTransactionMapper.toEntity(ipgSynchTransactionDTO);
        ipgSynchTransaction.setStatus(NavsaTransactionStatus.NEW.getName());
        ipgSynchTransaction = ipgSynchTransactionRepository.save(ipgSynchTransaction);
        return ipgSynchTransactionMapper.toDto(ipgSynchTransaction);

    }

    @Override
    public List<IpgSynchTransaction> findAll() {
        log.debug("Request to get all IpgSynchTransaction");
        return ipgSynchTransactionRepository.findAll();
    }

    @Override
    public Optional<IpgSynchTransaction> findOne(Long id) {
        log.debug("Request to get IpgSynchTransaction : {}", id);
        return ipgSynchTransactionRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete IpgSynchTransaction : {}", id);
        ipgSynchTransactionRepository.deleteById(id);
    }

    @Override
    public List<IpgSynchTransaction> findByTransactionRef(String transactionRef) {
        log.debug("Request to find IpgSynchTransaction by transactionRef : {}", transactionRef);
        return ipgSynchTransactionRepository.findByTransactionRef(transactionRef);
    }

    @Override
    public List<IpgSynchTransaction> findByStatus(String status) {
        log.debug("Request to find IpgSynchTransaction by status : {}", status);
        return ipgSynchTransactionRepository.findByStatus(status);
    }

    @Override
    public List<IpgSynchTransaction> changeStatus(String transactionRef, NavsaTransactionStatus status) {
        List<IpgSynchTransaction> ipgSynchTransactionList = new ArrayList<>();
        List<IpgSynchTransaction> ipgSynchTransactions = ipgSynchTransactionRepository.findByTransactionRef(transactionRef);
        if(!ipgSynchTransactions.isEmpty()){
            for (IpgSynchTransaction ipgSynchTransaction : ipgSynchTransactions) {
                ipgSynchTransaction.setStatus(status.getName());
                IpgSynchTransaction save = ipgSynchTransactionRepository.save(ipgSynchTransaction);
                log.info("Updated ipgSynchTransaction ===> " + save);
                ipgSynchTransactionList.add(save);
            }
        }

        log.info("Updated ipgSynchTransaction list ===> " + ipgSynchTransactionList);

        return ipgSynchTransactionList;
    }

}
