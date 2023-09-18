package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.Idempotent;

import java.util.Optional;

/**
 * Service Interface for managing {@link Idempotent}.
 */
public interface IdempotentService {

    Optional<Idempotent> findByIdempotentKey(String idempotentKey);

    Idempotent save(Idempotent idempotent);

}
