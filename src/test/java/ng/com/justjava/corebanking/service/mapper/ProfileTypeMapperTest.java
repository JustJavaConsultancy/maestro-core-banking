package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ProfileTypeMapperTest {

    private ProfileTypeMapper profileTypeMapper;

    @BeforeEach
    public void setUp() {
        profileTypeMapper = new ProfileTypeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(profileTypeMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(profileTypeMapper.fromId(null)).isNull();
    }
}
