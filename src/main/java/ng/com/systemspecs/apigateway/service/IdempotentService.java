package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.Idempotent;

import java.util.Optional;

/**
 * Service Interface for managing {@link ng.com.systemspecs.apigateway.domain.Idempotent}.
 */
public interface IdempotentService {

    Optional<Idempotent> findByIdempotentKey(String idempotentKey);

    Idempotent save(Idempotent idempotent);

}
