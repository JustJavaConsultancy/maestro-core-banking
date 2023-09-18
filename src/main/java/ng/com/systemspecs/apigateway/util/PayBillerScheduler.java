package ng.com.systemspecs.apigateway.util;

import ng.com.systemspecs.apigateway.domain.BillerRecurring;
import ng.com.systemspecs.apigateway.repository.BillerRecurringRepository;
import ng.com.systemspecs.apigateway.service.PayBillerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@EnableScheduling
public class PayBillerScheduler {

    private final Logger log = LoggerFactory.getLogger(PayBillerScheduler.class);
    private final BillerRecurringRepository billerRecurringRepository;
    private final PayBillerService payBillerService;

    public PayBillerScheduler(BillerRecurringRepository billerRecurringRepository, PayBillerService payBillerService) {
        this.billerRecurringRepository = billerRecurringRepository;
        this.payBillerService = payBillerService;
    }

    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void payRecurring() {
        log.info("Checking for biller recurring payments\n");
        List<BillerRecurring> recurringList = billerRecurringRepository.findByStatus("ACTIVE");

        if (!recurringList.isEmpty()) {
            log.info("List of recurring biller payments ==> {}", recurringList);
            recurringList.forEach(payBillerService::payRecurring);
        }
    }
}
