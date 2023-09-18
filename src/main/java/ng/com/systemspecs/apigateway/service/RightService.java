package ng.com.systemspecs.apigateway.service;

import ng.com.systemspecs.apigateway.domain.AccessItem;
import ng.com.systemspecs.apigateway.domain.Right;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Right}.
 */
public interface RightService {

    Right save(Right Right);


    List<Right> findAll();


    /**
     * Get the "id" bank.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Right> findOne(Long id);

    /**
     * Delete the "id" bank.
     *
     * @param id the id of the entity.
     */
    void delete(Right id);

    Optional<Right> findOneByCode(String rightCode);


    Page<Right> findAll(Pageable pageable);

}
