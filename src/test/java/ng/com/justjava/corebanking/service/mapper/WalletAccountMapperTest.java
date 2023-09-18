package ng.com.justjava.corebanking.service.mapper;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class WalletAccountMapperTest {

    private WalletAccountMapper walletAccountMapper;

    @BeforeEach
    public void setUp() {
        walletAccountMapper = new WalletAccountMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        Assertions.assertThat(walletAccountMapper.fromId(id).getId()).isEqualTo(id);
        Assertions.assertThat(walletAccountMapper.fromId(null)).isNull();
    }
}
