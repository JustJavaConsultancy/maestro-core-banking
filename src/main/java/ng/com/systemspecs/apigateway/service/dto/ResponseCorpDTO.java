package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.domain.CorporateProfile;

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
