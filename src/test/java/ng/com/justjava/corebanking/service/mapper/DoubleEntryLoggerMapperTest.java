package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class DoubleEntryLoggerMapperTest {

    private DoubleEntryLoggerMapper doubleEntryLoggerMapper;

    @BeforeEach
    public void setUp() {
        doubleEntryLoggerMapper = new DoubleEntryLoggerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(doubleEntryLoggerMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(doubleEntryLoggerMapper.fromId(null)).isNull();
    }
}
