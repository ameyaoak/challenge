package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

@Data
public class MoneyTransferDetails {

	@NotNull
	@NotEmpty
	String fromAccount;
	
	@NotNull
	@NotEmpty
	String toAccount;
	
	@NotNull
	@Min(value = 0, message = "Transfer Amount must be greater than 0.")
	BigDecimal transferAmount;
	
}
