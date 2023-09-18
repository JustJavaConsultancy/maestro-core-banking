package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CustomersubscriptionMapperTest {

    private CustomersubscriptionMapper customersubscriptionMapper;

    @BeforeEach
    public void setUp() {
        customersubscriptionMapper = new CustomersubscriptionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(customersubscriptionMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(customersubscriptionMapper.fromId(null)).isNull();
    }
}
