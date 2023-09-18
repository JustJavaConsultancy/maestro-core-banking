package ng.com.systemspecs.apigateway.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.math.BigDecimal;

public class SummaryDTO {

    private Long customerCount;
    private Long agentTransactionCount;
    private Long transactionCount;
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private Long walletCount;
    private BigDecimal profit;
    @JsonIgnore
    private Long notificationCount;

    public Long getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(Long customerCount) {
        this.customerCount = customerCount;
    }

    public Long getAgentTransactionCount() {
        return agentTransactionCount;
    }

    public void setAgentTransactionCount(Long agentTransactionCount) {
        this.agentTransactionCount = agentTransactionCount;
    }

    public Long getTransactionCount() {
        return transactionCount;
    }

    public void setTransactionCount(Long transactionCount) {
        this.transactionCount = transactionCount;
    }

    public BigDecimal getTotalDeposits() {
        return totalDeposits;
    }

    public void setTotalDeposits(BigDecimal totalDeposits) {
        this.totalDeposits = totalDeposits;
    }

    public BigDecimal getTotalWithdrawals() {
        return totalWithdrawals;
    }

    public void setTotalWithdrawals(BigDecimal totalWithdrawals) {
        this.totalWithdrawals = totalWithdrawals;
    }

    public Long getWalletCount() {
        return walletCount;
    }

    public void setWalletCount(Long walletCount) {
        this.walletCount = walletCount;
    }

    public Long getNotificationCount() {
        return notificationCount;
    }

    public void setNotificationCount(Long notificationCount) {
        this.notificationCount = notificationCount;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    @Override
    public String toString() {
        return "SummaryDTO{" +
            "customerCount=" + customerCount +
            ", agentTransactionCount=" + agentTransactionCount +
            ", transactionCount=" + transactionCount +
            ", totalDeposits=" + totalDeposits +
            ", totalWithdrawals=" + totalWithdrawals +
            ", walletCount=" + walletCount +
            ", profit=" + profit +
            ", notificationCount=" + notificationCount +
            '}';
    }
}
