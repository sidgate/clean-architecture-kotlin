package io.reflectoring.buckpal.application.service

import io.reflectoring.buckpal.application.port.`in`.SendMoneyUseCase
import io.reflectoring.buckpal.application.port.out.AccountLock
import io.reflectoring.buckpal.application.port.out.LoadAccountPort
import io.reflectoring.buckpal.application.port.out.UpdateAccountStatePort
import io.reflectoring.buckpal.domain.Account
import io.reflectoring.buckpal.domain.Account.AccountId
import io.reflectoring.buckpal.domain.Money
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito
import org.mockito.BDDMockito.then
import org.mockito.Mockito
import java.time.LocalDateTime
import java.util.*
import java.util.stream.Collectors

internal class SendMoneyServiceTest {
    private val loadAccountPort = Mockito.mock(LoadAccountPort::class.java)
    private val accountLock = Mockito.mock(AccountLock::class.java)
    private val updateAccountStatePort = Mockito.mock(
        UpdateAccountStatePort::class.java
    )
    private val sendMoneyService =
        SendMoneyService(loadAccountPort, accountLock, updateAccountStatePort, moneyTransferProperties())

    @Test
    fun givenWithdrawalFails_thenOnlySourceAccountIsLockedAndReleased() {
        val sourceAccountId = AccountId(41L)
        val sourceAccount = givenAnAccountWithId(sourceAccountId)
        val targetAccountId = AccountId(42L)
        val targetAccount = givenAnAccountWithId(targetAccountId)
        givenWithdrawalWillFail(sourceAccount)
        givenDepositWillSucceed(targetAccount)
        val command = SendMoneyUseCase.SendMoneyCommand(
            sourceAccountId,
            targetAccountId,
            Money.of(300L)
        )
        val success = sendMoneyService.sendMoney(command)
        Assertions.assertThat(success).isFalse
        then(accountLock).should().lockAccount(ArgumentMatchers.eq(sourceAccountId))
        then(accountLock).should()
            .releaseAccount(ArgumentMatchers.eq(sourceAccountId))
        then(accountLock).should(Mockito.times(0))
            .lockAccount(ArgumentMatchers.eq(targetAccountId))
    }

    @Test
    fun transactionSucceeds() {
        val sourceAccount = givenSourceAccount()
        val targetAccount = givenTargetAccount()
        givenWithdrawalWillSucceed(sourceAccount)
        givenDepositWillSucceed(targetAccount)
        val money: Money = Money.of(500L)
        val command = SendMoneyUseCase.SendMoneyCommand(
            sourceAccount.id.get(),
            targetAccount.id.get(),
            money
        )
        val success = sendMoneyService.sendMoney(command)
        Assertions.assertThat(success).isTrue
        val sourceAccountId = sourceAccount.id.get()
        val targetAccountId = targetAccount.id.get()
        then(accountLock).should().lockAccount(ArgumentMatchers.eq(sourceAccountId))
        then(sourceAccount).should()
            .withdraw(ArgumentMatchers.eq(money), ArgumentMatchers.eq(targetAccountId))
        then(accountLock).should()
            .releaseAccount(ArgumentMatchers.eq(sourceAccountId))
        then(accountLock).should().lockAccount(ArgumentMatchers.eq(targetAccountId))
        then(targetAccount).should()
            .deposit(ArgumentMatchers.eq(money), ArgumentMatchers.eq(sourceAccountId))
        then(accountLock).should()
            .releaseAccount(ArgumentMatchers.eq(targetAccountId))
        thenAccountsHaveBeenUpdated(sourceAccountId, targetAccountId)
    }

    private fun thenAccountsHaveBeenUpdated(vararg accountIds: AccountId) {
        val accountCaptor: ArgumentCaptor<Account> = ArgumentCaptor.forClass(Account::class.java)
        then(updateAccountStatePort).should(Mockito.times(accountIds.size))
            .updateActivities(accountCaptor.capture())
        val updatedAccountIds: List<AccountId> = accountCaptor.allValues
            .stream()
            .map { obj: Account -> obj.id }
            .map { obj: Optional<AccountId?> -> obj.get() }
            .collect(Collectors.toList())
        for (accountId in accountIds) {
            Assertions.assertThat(updatedAccountIds).contains(accountId)
        }
    }

    private fun givenDepositWillSucceed(account: Account) {
        BDDMockito.given(
            account.deposit(
                ArgumentMatchers.any(Money::class.java), ArgumentMatchers.any(
                    AccountId::class.java
                )
            )
        )
            .willReturn(true)
    }

    private fun givenWithdrawalWillFail(account: Account) {
        BDDMockito.given(
            account.withdraw(
                ArgumentMatchers.any(Money::class.java), ArgumentMatchers.any(
                    AccountId::class.java
                )
            )
        )
            .willReturn(false)
    }

    private fun givenWithdrawalWillSucceed(account: Account) {
        BDDMockito.given(
            account.withdraw(
                ArgumentMatchers.any(Money::class.java), ArgumentMatchers.any(
                    AccountId::class.java
                )
            )
        )
            .willReturn(true)
    }

    private fun givenTargetAccount(): Account {
        return givenAnAccountWithId(AccountId(42L))
    }

    private fun givenSourceAccount(): Account {
        return givenAnAccountWithId(AccountId(41L))
    }

    private fun givenAnAccountWithId(id: AccountId): Account {
        val account = Mockito.mock(Account::class.java)
        BDDMockito.given<Optional<AccountId>>(account.id)
            .willReturn(Optional.of(id))
        BDDMockito.given(
            loadAccountPort.loadAccount(
                ArgumentMatchers.eq(account.id.get()), ArgumentMatchers.any(
                    LocalDateTime::class.java
                )
            )
        )
            .willReturn(account)
        return account
    }

    private fun moneyTransferProperties(): MoneyTransferProperties {
        return MoneyTransferProperties(Money.of(Long.MAX_VALUE))
    }
}