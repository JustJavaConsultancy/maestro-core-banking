package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.PaymentTransactionRepository;
import ng.com.justjava.corebanking.service.mapper.PaymentTransactionMapper;
import ng.com.justjava.corebanking.domain.PaymentTransaction;
import ng.com.justjava.corebanking.service.PaymentTransactionService;
import ng.com.justjava.corebanking.service.dto.PaymentTransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.lang.System.out;

/**
 * Service Implementation for managing {@link PaymentTransaction}.
 */
@Service
@Transactional
public class PaymentTransactionServiceImpl implements PaymentTransactionService {

    private final Logger log = LoggerFactory.getLogger(PaymentTransactionServiceImpl.class);

    private final PaymentTransactionRepository paymentTransactionRepository;

    private final PaymentTransactionMapper paymentTransactionMapper;

    public PaymentTransactionServiceImpl(PaymentTransactionRepository paymentTransactionRepository, PaymentTransactionMapper paymentTransactionMapper) {
        this.paymentTransactionRepository = paymentTransactionRepository;
        this.paymentTransactionMapper = paymentTransactionMapper;
    }

    @Override
    public PaymentTransactionDTO save(PaymentTransactionDTO paymentTransactionDTO) {
        log.debug("Request to save PaymentTransaction : {}", paymentTransactionDTO);
        PaymentTransaction paymentTransaction = paymentTransactionMapper.toEntity(paymentTransactionDTO);
        paymentTransaction = paymentTransactionRepository.save(paymentTransaction);
        return paymentTransactionMapper.toDto(paymentTransaction);
    }
    @Override
    public PaymentTransaction save(PaymentTransaction paymentTransaction) {
        log.debug("Request to save PaymentTransaction : {}", paymentTransaction);
        paymentTransaction = paymentTransactionRepository.save(paymentTransaction);
        return paymentTransaction;
    }
    @Override
    @Transactional(readOnly = true)
    public Page<PaymentTransactionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PaymentTransactions");
        return paymentTransactionRepository.findAll(pageable)
            .map(paymentTransactionMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<PaymentTransactionDTO> findOne(Long id) {
        log.debug("Request to get PaymentTransaction : {}", id);
        return paymentTransactionRepository.findById(id)
            .map(paymentTransactionMapper::toDto);
    }

    @Override
    public Optional<PaymentTransactionDTO> findOneByTransactionRef(String transactionRef) {
        PaymentTransaction p = paymentTransactionRepository.findByTransactionRef(transactionRef);
        out.println("Returned PaymentTrnx by reference ==>> "+p);
        if(p!=null){
            return Optional.of(paymentTransactionMapper.toDto(p));
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PaymentTransaction : {}", id);
        paymentTransactionRepository.deleteById(id);
    }



	@Override
	public List<PaymentTransactionDTO> findBySourceAccountName(String sourceAccountName) {
        log.debug("Request to get all customer's PaymentTransactions");
        return paymentTransactionMapper.toDto(paymentTransactionRepository.findBySourceAccountName(sourceAccountName));
	}

	@Override
	public List<PaymentTransactionDTO> findBySourceAccount(String sourceAccount) {
	       log.debug("Request to get send money PaymentTransactions");
	        return paymentTransactionMapper.toDto(paymentTransactionRepository.
	        		findBySourceAccount(sourceAccount));

	}

	@Override
	public List<PaymentTransactionDTO> findByDestinationAccount(String destinationAccount) {
	       log.debug("Request to get funding PaymentTransactions");
	        return paymentTransactionMapper.toDto(paymentTransactionRepository.
	        		findByDestinationAccount(destinationAccount));
	}
}
