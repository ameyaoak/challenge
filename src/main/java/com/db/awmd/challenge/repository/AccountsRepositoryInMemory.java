package com.db.awmd.challenge.repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.InvalidAccountException;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

	private final Map<String, Account> accounts = new ConcurrentHashMap<>();

	@Override
	public void createAccount(Account account) throws DuplicateAccountIdException {
		Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
		if (previousAccount != null) {
			throw new DuplicateAccountIdException("Account id " + account.getAccountId() + " already exists!");
		}
	}

	@Override
	public Account getAccount(String accountId) {
		return accounts.get(accountId);
	}

	@Override
	public void clearAccounts() {
		accounts.clear();
	}

	@Override
	public synchronized void transferAccountBalance(String fromAccountId, String toAccountId, BigDecimal transferAmount) { 
		Account fromAccount = getAccount(fromAccountId);
		Account toAccount = getAccount(toAccountId);  
		
		if (fromAccount == null) {
			throw new InvalidAccountException("Account id " + fromAccountId + " dosenot exists!");
		}
		if (toAccount == null) {
			throw new InvalidAccountException("Account id " + toAccountId + " dosenot exists!");
		}

		fromAccount.setBalance(fromAccount.getBalance().add(transferAmount.negate()));
		toAccount.setBalance(toAccount.getBalance().add(transferAmount));
	}

}
