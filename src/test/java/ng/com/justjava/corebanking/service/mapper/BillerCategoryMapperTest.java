package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class BillerCategoryMapperTest {

    private BillerCategoryMapper billerCategoryMapper;

    @BeforeEach
    public void setUp() {
        billerCategoryMapper = new BillerCategoryMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(billerCategoryMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(billerCategoryMapper.fromId(null)).isNull();
    }
}
