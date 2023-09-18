package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MyDeviceMapperTest {

    private MyDeviceMapper myDeviceMapper;

    @BeforeEach
    public void setUp() {
        myDeviceMapper = new MyDeviceMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(myDeviceMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(myDeviceMapper.fromId(null)).isNull();
    }
}
