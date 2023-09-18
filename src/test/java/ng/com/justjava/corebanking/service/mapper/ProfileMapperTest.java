package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProfileMapperTest {

    private ProfileMapper profileMapper;

    @BeforeEach
    public void setUp() {
        profileMapper = new ProfileMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(profileMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(profileMapper.fromId(null)).isNull();
    }
}
