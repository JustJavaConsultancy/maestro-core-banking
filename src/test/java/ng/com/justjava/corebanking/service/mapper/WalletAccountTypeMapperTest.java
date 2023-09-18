package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class WalletAccountTypeMapperTest {

    private WalletAccountTypeMapper walletAccountTypeMapper;

    @BeforeEach
    public void setUp() {
        walletAccountTypeMapper = new WalletAccountTypeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(walletAccountTypeMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(walletAccountTypeMapper.fromId(null)).isNull();
    }
}
