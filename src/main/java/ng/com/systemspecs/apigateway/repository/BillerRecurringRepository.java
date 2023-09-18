package ng.com.systemspecs.apigateway.repository;

import ng.com.systemspecs.apigateway.domain.BillerRecurring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillerRecurringRepository extends JpaRepository<BillerRecurring, Long> {
    List<BillerRecurring> findByStatus(String active);

    BillerRecurring findByRrr(String rrr);
}
