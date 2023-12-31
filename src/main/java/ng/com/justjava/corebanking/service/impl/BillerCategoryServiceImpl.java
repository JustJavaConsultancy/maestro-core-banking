package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.BillerCategoryRepository;
import ng.com.justjava.corebanking.service.mapper.BillerCategoryMapper;
import ng.com.justjava.corebanking.domain.BillerCategory;
import ng.com.justjava.corebanking.service.BillerCategoryService;
import ng.com.justjava.corebanking.service.dto.BillerCategoryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link BillerCategory}.
 */
@Service
@Transactional
public class BillerCategoryServiceImpl implements BillerCategoryService {

    private final Logger log = LoggerFactory.getLogger(BillerCategoryServiceImpl.class);

    private final BillerCategoryRepository billerCategoryRepository;

    private final BillerCategoryMapper billerCategoryMapper;

    public BillerCategoryServiceImpl(BillerCategoryRepository billerCategoryRepository, BillerCategoryMapper billerCategoryMapper) {
        this.billerCategoryRepository = billerCategoryRepository;
        this.billerCategoryMapper = billerCategoryMapper;
    }

    @Override
    public BillerCategoryDTO save(BillerCategoryDTO billerCategoryDTO) {
        log.debug("Request to save BillerCategory : {}", billerCategoryDTO);
        BillerCategory billerCategory = billerCategoryMapper.toEntity(billerCategoryDTO);
        billerCategory = billerCategoryRepository.save(billerCategory);
        return billerCategoryMapper.toDto(billerCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BillerCategoryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BillerCategories");
        return billerCategoryRepository.findAll(pageable)
            .map(billerCategoryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BillerCategoryDTO> findAll() {
        log.debug("Request to get all BillerCategories");
        return billerCategoryRepository.findAll().stream()
            .map(billerCategoryMapper::toDto).collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BillerCategoryDTO> findOne(Long id) {
        log.debug("Request to get BillerCategory : {}", id);
        return billerCategoryRepository.findById(id)
            .map(billerCategoryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillerCategory : {}", id);
        billerCategoryRepository.deleteById(id);
    }

    @Override
    public void deleteAll() {
        billerCategoryRepository.deleteAll();
    }

    @Override
    public Optional<BillerCategory> findByBillerCategory(String billerCategory) {
        return billerCategoryRepository.findByBillerCategory(billerCategory.toUpperCase());
    }
}
