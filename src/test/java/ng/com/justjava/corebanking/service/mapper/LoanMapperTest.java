package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class LoanMapperTest {

    private LoanMapper loanMapper;

    @BeforeEach
    public void setUp() {
        loanMapper = new LoanMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(loanMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(loanMapper.fromId(null)).isNull();
    }
}
