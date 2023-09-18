package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BillerTransactionMapperTest {

    private BillerTransactionMapper billerTransactionMapper;

    @BeforeEach
    public void setUp() {
        billerTransactionMapper = new BillerTransactionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(billerTransactionMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(billerTransactionMapper.fromId(null)).isNull();
    }
}
