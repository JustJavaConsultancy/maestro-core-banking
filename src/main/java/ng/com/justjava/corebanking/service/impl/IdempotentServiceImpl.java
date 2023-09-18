package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.IdempotentRepository;
import ng.com.justjava.corebanking.domain.Idempotent;
import ng.com.justjava.corebanking.service.IdempotentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class IdempotentServiceImpl implements IdempotentService {

    private final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);

    private final IdempotentRepository idempotentRepository;

    public IdempotentServiceImpl(IdempotentRepository idempotentRepository) {
        this.idempotentRepository = idempotentRepository;
    }

    @Override
    public Optional<Idempotent> findByIdempotentKey(String idempotentKey) {
        log.info("IDEMPOTENT KEY sent === " + idempotentKey);
        return idempotentRepository.findByIdempotentKey(idempotentKey);
    }

    @Override
    public Idempotent save(Idempotent idempotent) {
        return idempotentRepository.save(idempotent);
    }
}
