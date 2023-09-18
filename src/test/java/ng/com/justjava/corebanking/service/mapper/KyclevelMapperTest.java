package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class KyclevelMapperTest {

    private KyclevelMapper kyclevelMapper;

    @BeforeEach
    public void setUp() {
        kyclevelMapper = new KyclevelMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(kyclevelMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(kyclevelMapper.fromId(null)).isNull();
    }
}
