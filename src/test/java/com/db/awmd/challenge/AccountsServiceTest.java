package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.MoneyTransferDetails;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InvalidAccountException;
import com.db.awmd.challenge.service.AccountsService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

  @Autowired
  private AccountsService accountsService;

  @Test
  public void addAccount() throws Exception {
    Account account = new Account("Id-123");
    account.setBalance(new BigDecimal(1000));
    this.accountsService.createAccount(account);

    assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
  }

  @Test
  public void addAccount_failsOnDuplicateId() throws Exception {
    String uniqueId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueId);
    this.accountsService.createAccount(account);

    try {
      this.accountsService.createAccount(account);
      fail("Should have failed when adding duplicate account");
    } catch (DuplicateAccountIdException ex) {
      assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
    }

  }
  
  @Test
	public void validFromAccountToAccountAndTransferAmount() throws Exception {
		Account accountA = new Account("A", new BigDecimal("123.45"));
		this.accountsService.createAccount(accountA);

		Account accountB = new Account("B", new BigDecimal("123.45"));
		this.accountsService.createAccount(accountB);

		MoneyTransferDetails transferDetails = new MoneyTransferDetails();
		transferDetails.setFromAccount(accountA.getAccountId());
		transferDetails.setToAccount(accountB.getAccountId());
		transferDetails.setTransferAmount(BigDecimal.ONE);

		this.accountsService.transferAmountAndUpdateAccounts(transferDetails);
	}
  
  @Test(expected=InvalidAccountException.class)
	public void nonExistantAccountTransfer() throws Exception {

		MoneyTransferDetails transferDetails = new MoneyTransferDetails();
		transferDetails.setFromAccount("accountA");
		transferDetails.setToAccount("accountB");
		transferDetails.setTransferAmount(BigDecimal.ONE);

		this.accountsService.transferAmountAndUpdateAccounts(transferDetails);
	}
}
