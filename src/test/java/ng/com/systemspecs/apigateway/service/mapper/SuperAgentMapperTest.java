package ng.com.systemspecs.apigateway.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SuperAgentMapperTest {

    private SuperAgentMapper superAgentMapper;

    @BeforeEach
    public void setUp() {
        superAgentMapper = new SuperAgentMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(superAgentMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(superAgentMapper.fromId(null)).isNull();
    }
}
