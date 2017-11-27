package com.db.awmd.challenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.MoneyTransferDetails;
import com.db.awmd.challenge.repository.AccountsRepository;

import lombok.Getter;

@Service
public class AccountsService {

	@Getter
	private final AccountsRepository accountsRepository;

	@Autowired
	public AccountsService(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}
	
	@Autowired
	private NotificationService notificationService;

	public void createAccount(Account account) {
		this.accountsRepository.createAccount(account);
	}

	public Account getAccount(String accountId) {
		return this.accountsRepository.getAccount(accountId);
	}
 
	public void transferAmountAndUpdateAccounts(MoneyTransferDetails moneyTransferDetails) { 
			this.accountsRepository.transferAccountBalance(
					moneyTransferDetails.getFromAccount(),
					moneyTransferDetails.getToAccount(),
					moneyTransferDetails.getTransferAmount());
			
			notificationService.notifyAboutTransfer(getAccount(moneyTransferDetails.getFromAccount()), moneyTransferDetails.getTransferAmount()+" Debited");
			notificationService.notifyAboutTransfer(getAccount(moneyTransferDetails.getToAccount()), moneyTransferDetails.getTransferAmount().toString()+" Credited");

	}
	 
}
