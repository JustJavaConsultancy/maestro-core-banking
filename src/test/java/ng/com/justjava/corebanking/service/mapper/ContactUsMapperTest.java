package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ContactUsMapperTest {

    private ContactUsMapper contactUsMapper;

    @BeforeEach
    public void setUp() {
        contactUsMapper = new ContactUsMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(contactUsMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(contactUsMapper.fromId(null)).isNull();
    }
}
