package ng.com.systemspecs.apigateway.service.dto;

import java.io.Serializable;

public class DataPlanDTO  implements Serializable {
	
	private String id;
	private String name;
	private double amount;  
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	 
	 
	
 
	
 
	
	

}
