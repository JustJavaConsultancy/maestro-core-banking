package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AddressMapperTest {

    private AddressMapper addressMapper;

    @BeforeEach
    public void setUp() {
        addressMapper = new AddressMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(addressMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(addressMapper.fromId(null)).isNull();
    }
}
