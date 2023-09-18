package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SchemeMapperTest {

    private SchemeMapper schemeMapper;

    @BeforeEach
    public void setUp() {
        schemeMapper = new SchemeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(schemeMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(schemeMapper.fromId(null)).isNull();
    }
}
