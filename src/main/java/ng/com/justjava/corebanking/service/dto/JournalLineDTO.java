package ng.com.justjava.corebanking.service.dto;

import ng.com.justjava.corebanking.domain.WalletAccount;

import java.io.Serializable;

public class JournalLineDTO implements Serializable {
    private Long id;
    private Double debit;
    private Double credit;
    private JournalDTO jounalDTO;
    private WalletAccount account;
    private String channel;
    private String journalReferences;
    private String destinationAccountNumber;

    public JournalLineDTO() {
    }

    public JournalLineDTO(Double debit, Double credit, WalletAccount account, String channel) {
        this.debit = debit;
        this.credit = credit;
        this.account = account;
        this.channel = channel;
    }

    public static String formatMoney(Double amount) {
        if (amount == null) {
            return null;
        }

        return String.format("%.2f", amount);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDebit() {
        if (debit != null) {
            return Double.valueOf(formatMoney(debit));
        }

        return 0.0;
    }

	public void setDebit(Double debit) {
		this.debit = debit;
	}

    public Double getCredit() {
        if (credit != null) {
            return Double.valueOf(formatMoney(credit));
        }

        return 0.0;
    }

    public void setCredit(Double credit) {
		this.credit = credit;
	}

    public JournalDTO getJounalDTO() {
		return jounalDTO;
	}

    public void setJounalDTO(JournalDTO jounalDTO) {
		this.jounalDTO = jounalDTO;
	}

	public String getJournalReferences() {
		return journalReferences;
	}

    public void setJournalReferences(String journalReferences) {
		this.journalReferences = journalReferences;
	}

    public String getChannel() {
		return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public WalletAccount getAccount() {
        return account;
    }

    public void setAccount(WalletAccount account) {
        this.account = account;
    }

    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    public void setDestinationAccountNumber(String destinationAccountNumber) {
        this.destinationAccountNumber = destinationAccountNumber;
    }

    @Override
    public String toString() {
        return "JournalLineDTO{" +
            "id=" + id +
            ", debit=" + debit +
            ", credit=" + credit +
            ", jounalDTO=" + jounalDTO +
            ", account=" + account +
            ", channel='" + channel + '\'' +
            ", journalReferences='" + journalReferences + '\'' +
            ", destinationAccountNumber='" + destinationAccountNumber + '\'' +
            '}';
    }
}
