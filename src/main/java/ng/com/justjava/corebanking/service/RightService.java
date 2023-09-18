package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.domain.Right;
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
