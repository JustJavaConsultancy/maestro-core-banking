package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.domain.CorporateProfile;

import java.util.List;

public class ResponseCorpDTO {
    private CorporateProfile corporateProfile;
    private List<WalletAccountDTO> walletAccount;

    public CorporateProfile getCorporateProfile() {
        return corporateProfile;
    }

    public void setCorporateProfile(CorporateProfile corporateProfile) {
        this.corporateProfile = corporateProfile;
    }

    public List<WalletAccountDTO> getWalletAccount() {
        return walletAccount;
    }

    public void setWalletAccount(List<WalletAccountDTO> walletAccount) {
        this.walletAccount = walletAccount;
    }

    @Override
    public String toString() {
        return "ResponseCorpDTO{" +
            "corporateProfile=" + corporateProfile +
            ", walletAccount=" + walletAccount +
            '}';
    }
}
