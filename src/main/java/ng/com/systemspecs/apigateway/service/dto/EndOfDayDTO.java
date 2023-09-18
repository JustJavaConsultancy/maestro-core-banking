package ng.com.systemspecs.apigateway.service.dto;

public class EndOfDayDTO {
    private double creditToBank;
    private double debitToBank;
    private double creditToWallets;
    private double debitToWallets;
    private double creditToTransit;
    private double debitToTransit;
    private double walletChargesBalance;
    private double remitaChargesBalance;

    public double getCreditToBank() {
        return formatMoney(creditToBank);
    }

    public void setCreditToBank(double creditToBank) {
        this.creditToBank = creditToBank;
    }

    public double getDebitToBank() {
        return formatMoney(debitToBank);
    }

    public void setDebitToBank(double debitToBank) {
        this.debitToBank = debitToBank;
    }

    public double getCreditToWallets() {
        return formatMoney(creditToWallets);
    }

    public void setCreditToWallets(double creditToWallets) {
        this.creditToWallets = creditToWallets;
    }

    public double getDebitToWallets() {
        return formatMoney(debitToWallets);
    }

    public void setDebitToWallets(double debitToWallets) {
        this.debitToWallets = debitToWallets;
    }

    public double getCreditToTransit() {
        return formatMoney(creditToTransit);
    }

    public void setCreditToTransit(double creditToTransit) {
        this.creditToTransit = creditToTransit;
    }

    public double getDebitToTransit() {
        return formatMoney(debitToTransit);
    }

    public void setDebitToTransit(double debitToTransit) {
        this.debitToTransit = debitToTransit;
    }

    public double getWalletChargesBalance() {
        return formatMoney(walletChargesBalance);
    }

    public void setWalletChargesBalance(double walletChargesBalance) {
        this.walletChargesBalance = walletChargesBalance;
    }

    public double getRemitaChargesBalance() {
        return formatMoney(remitaChargesBalance);
    }

    public void setRemitaChargesBalance(double remitaChargesBalance) {
        this.remitaChargesBalance = remitaChargesBalance;
    }

    @Override
    public String toString() {
        return "EndOfDayDTO{" +
            "creditToBank:" + getCreditToBank() +
            ", debitToBank:" + getDebitToBank() +
            ", creditToWallets:" + getCreditToWallets() +
            ", debitToWallets:" + getDebitToWallets() +
            ", creditToTransit:" + getCreditToTransit() +
            ", debitToTransit:" + getDebitToTransit() +
            ", walletChargesBalance:" + getWalletChargesBalance() +
            ", remitaChargesBalance:" + getRemitaChargesBalance() +
            '}';
    }

    public static Double formatMoney(Double amount) {
        if (amount == null) {
            return 0.00;
        }
        String format = String.format("%.2f", amount);
        try {
            return Double.parseDouble(format);
        }catch (Exception e) {
            System.out.println(e);
        }
        return amount;
    }
}
