package ng.com.justjava.corebanking.service.dto;

import java.io.Serializable;

public class PinDTO implements Serializable{
	private String pin;

    public String getPin() {
		return pin;
	}
	public void setPin(String pin) {
		this.pin = pin;
	}

    @Override
    public String toString() {
        return "PinDTO{" +
            "pin='" + pin + '\'' +
            '}';
    }
}
