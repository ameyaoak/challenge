package com.db.awmd.challenge;
import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.InvalidAccountException;
import com.db.awmd.challenge.exception.InvalidBalanceException;
import com.db.awmd.challenge.repository.AccountsRepositoryInMemory;

import org.junit.Assert;

@RunWith(MockitoJUnitRunner.class)
public class AccountsRepositoryInMemoryTest {

	@InjectMocks
	private AccountsRepositoryInMemory accountsRepositoryInMemory;
	
	@Test(expected=InvalidAccountException.class)
	public void testTransferAccountBalance_withNonExistingFromAccounts() {
		accountsRepositoryInMemory.createAccount(new Account("toAccount"));
		accountsRepositoryInMemory.transferAccountBalance("fromAccount", "toAccount", BigDecimal.TEN);
	}
	
	@Test(expected=InvalidAccountException.class)
	public void testTransferAccountBalance_withNonExistingToAccounts() {
		accountsRepositoryInMemory.createAccount(new Account("fromAccount"));
		accountsRepositoryInMemory.transferAccountBalance("fromAccount", "toAccount", BigDecimal.TEN);
	}
	
	@Test(expected=InvalidBalanceException.class)
	public void testTransferAccountBalance_withInvalidBalance() { 
		accountsRepositoryInMemory.createAccount(new Account("fromAccount"));
		accountsRepositoryInMemory.createAccount(new Account("toAccount"));
		accountsRepositoryInMemory.transferAccountBalance("fromAccount", "toAccount", BigDecimal.TEN.negate());
	}
	
	@Test(expected=InvalidBalanceException.class)
	public void testTransferAccountBalance_withZeroBalance() { 
		accountsRepositoryInMemory.createAccount(new Account("fromAccount"));
		accountsRepositoryInMemory.createAccount(new Account("toAccount"));
		accountsRepositoryInMemory.transferAccountBalance("fromAccount", "toAccount", BigDecimal.ZERO);
	}
	
	@Test
	public void testTransferAccountBalance_withValidBalance() { 
		Account fromAccount = new Account("fromAccount");
		fromAccount.setBalance(BigDecimal.TEN);
		Account toAccount = new Account("toAccount");
		toAccount.setBalance(BigDecimal.TEN); 
		
		accountsRepositoryInMemory.createAccount(fromAccount);
		accountsRepositoryInMemory.createAccount(toAccount);
		accountsRepositoryInMemory.transferAccountBalance("fromAccount", "toAccount", BigDecimal.TEN);
		Assert.assertEquals(fromAccount.getBalance(), BigDecimal.ZERO);
		Assert.assertEquals(toAccount.getBalance(), BigDecimal.TEN.add(BigDecimal.TEN));
	}

}
