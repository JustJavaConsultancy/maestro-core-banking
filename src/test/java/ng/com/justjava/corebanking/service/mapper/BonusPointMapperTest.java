package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
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
        Assertions.assertThat(bonusPointMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(bonusPointMapper.fromId(null)).isNull();
    }
}
