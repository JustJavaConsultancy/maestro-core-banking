package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BillerMapperTest {

    private BillerMapper billerMapper;

    @BeforeEach
    public void setUp() {
        billerMapper = new BillerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(billerMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(billerMapper.fromId(null)).isNull();
    }
}
