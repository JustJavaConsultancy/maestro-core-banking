package ng.com.justjava.corebanking.service;

import ng.com.justjava.corebanking.service.dto.BillerCategoryDTO;
import ng.com.justjava.corebanking.domain.BillerCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link BillerCategory}.
 */
public interface BillerCategoryService {

    /**
     * Save a billerCategory.
     *
     * @param billerCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    BillerCategoryDTO save(BillerCategoryDTO billerCategoryDTO);

    /**
     * Get all the billerCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BillerCategoryDTO> findAll(Pageable pageable);

    List<BillerCategoryDTO> findAll();

    /**
     * Get the "id" billerCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillerCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" billerCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    void deleteAll();

    Optional<BillerCategory> findByBillerCategory(String category);
}
