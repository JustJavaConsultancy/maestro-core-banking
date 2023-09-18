package ng.com.justjava.corebanking.repository;

import ng.com.justjava.corebanking.domain.ApprovalGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ApprovalGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApprovalGroupRepository extends JpaRepository<ApprovalGroup, Long> {

    ApprovalGroup findByName(String name);
}
