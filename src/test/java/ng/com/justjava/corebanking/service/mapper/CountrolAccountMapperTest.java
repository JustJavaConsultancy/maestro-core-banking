package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CountrolAccountMapperTest {

    private CountrolAccountMapper countrolAccountMapper;

    @BeforeEach
    public void setUp() {
        countrolAccountMapper = new CountrolAccountMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(countrolAccountMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(countrolAccountMapper.fromId(null)).isNull();
    }
}
