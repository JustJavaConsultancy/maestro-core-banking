package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BillerPlatformMapperTest {

    private BillerPlatformMapper billerPlatformMapper;

    @BeforeEach
    public void setUp() {
        billerPlatformMapper = new BillerPlatformMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(billerPlatformMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(billerPlatformMapper.fromId(null)).isNull();
    }
}
