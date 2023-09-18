package ng.com.justjava.corebanking.service.impl;

import ng.com.justjava.corebanking.repository.RightRepository;
import ng.com.justjava.corebanking.domain.Right;
import ng.com.justjava.corebanking.service.RightService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class RightServiceImpl implements RightService {

    private final Logger log = LoggerFactory.getLogger(RightServiceImpl.class);

    private final RightRepository rightRepository;

    public RightServiceImpl(RightRepository rightRepository) {
        this.rightRepository = rightRepository;
    }

    @Override
    public Right save(Right right) {
        log.debug("Request to save Right : {}", right);
        return rightRepository.save(right);
    }

    @Override
    public List<Right> findAll() {
        log.debug("Request to find all Rights");
        return  rightRepository.findAll();
    }

    @Override
    public Optional<Right> findOne(Long id) {
        log.debug("Request to find one Right : {}", id);
        return rightRepository.findById(id);
    }

    @Override
    public void delete(Right id) {
        log.debug("Request to delete Right by Id: {}", id);
        rightRepository.delete(id);

    }

    @Override
    public Optional<Right> findOneByCode(String rightCode) {
        return rightRepository.findByCode(rightCode);
    }

    @Override
    public Page<Right> findAll(Pageable pageable) {
        log.debug("Request to find all Rights");
        return rightRepository.findAll(pageable);
    }
}
