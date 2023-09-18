package ng.com.systemspecs.apigateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class BonusPointMapperTest {

    private BonusPointMapper bonusPointMapper;

    @BeforeEach
    public void setUp() {
        bonusPointMapper = new BonusPointMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(bonusPointMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(bonusPointMapper.fromId(null)).isNull();
    }
}
