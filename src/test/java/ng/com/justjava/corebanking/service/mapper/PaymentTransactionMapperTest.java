package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class PaymentTransactionMapperTest {

    private PaymentTransactionMapper paymentTransactionMapper;

    @BeforeEach
    public void setUp() {
        paymentTransactionMapper = new PaymentTransactionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(paymentTransactionMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(paymentTransactionMapper.fromId(null)).isNull();
    }
}
