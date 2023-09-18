package ng.com.systemspecs.apigateway.service.dto;

import java.util.List;

public class SuperAgentDataDTO {
	private List<SuperAgentMetricsDTO> superAgentBestAgents;
	private Long totalTransactions;
	private Long totalDeposits;
	private Long totalWithdrawals;

	
	public List<SuperAgentMetricsDTO> getSuperAgentBestAgents() {
		return superAgentBestAgents;
	}


	public void setSuperAgentBestAgents(List<SuperAgentMetricsDTO> superAgentBestAgents) {
		this.superAgentBestAgents = superAgentBestAgents;
	}


	public Long getTotalTransactions() {
		return totalTransactions;
	}


	public void setTotalTransactions(Long totalTransactions) {
		this.totalTransactions = totalTransactions;
	}


	public Long getTotalDeposits() {
		return totalDeposits;
	}


	public void setTotalDeposits(Long totalDeposits) {
		this.totalDeposits = totalDeposits;
	}


	public Long getTotalWithdrawals() {
		return totalWithdrawals;
	}


	public void setTotalWithdrawals(Long totalWithdrawals) {
		this.totalWithdrawals = totalWithdrawals;
	}


	@Override
	public String toString() {
		return "SuperAgentDataDTO [superAgentBestAgents=" + superAgentBestAgents + ", totalTransactions="
				+ totalTransactions + ", totalDeposits=" + totalDeposits + ", totalWithdrawals=" + totalWithdrawals
				+ "]";
	}
	
}
