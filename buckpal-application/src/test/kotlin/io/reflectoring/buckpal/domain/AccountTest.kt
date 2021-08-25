package io.reflectoring.buckpal.domain

import io.reflectoring.buckpal.common.AccountTestData.defaultAccount
import io.reflectoring.buckpal.common.ActivityTestData.defaultActivity
import io.reflectoring.buckpal.domain.Account.AccountId
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class AccountTest {
    @Test
    fun calculatesBalance() {
        val accountId = AccountId(1L)
        val account = defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(
                ActivityWindow(
                    defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(999L)).build(),
                    defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(1L)).build()
                )
            )
            .build()
        val balance = account.calculateBalance()
        Assertions.assertThat(balance).isEqualTo(Money.of(1555L))
    }

    @Test
    fun withdrawalSucceeds() {
        val accountId = AccountId(1L)
        val account = defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(
                ActivityWindow(
                    defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(999L)).build(),
                    defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(1L)).build()
                )
            )
            .build()
        val success = account.withdraw(Money.of(555L), AccountId(99L))
        Assertions.assertThat(success).isTrue
        Assertions.assertThat(account.activityWindow.activities).hasSize(3)
        Assertions.assertThat(account.calculateBalance()).isEqualTo(Money.of(1000L))
    }

    @Test
    fun withdrawalFailure() {
        val accountId = AccountId(1L)
        val account = defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(
                ActivityWindow(
                    defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(999L)).build(),
                    defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(1L)).build()
                )
            )
            .build()
        val success = account.withdraw(Money.of(1556L), AccountId(99L))
        Assertions.assertThat(success).isFalse
        Assertions.assertThat(account.activityWindow.activities).hasSize(2)
        Assertions.assertThat(account.calculateBalance()).isEqualTo(Money.of(1555L))
    }

    @Test
    fun depositSuccess() {
        val accountId = AccountId(1L)
        val account = defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(
                ActivityWindow(
                    defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(999L)).build(),
                    defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(1L)).build()
                )
            )
            .build()
        val success = account.deposit(Money.of(445L), AccountId(99L))
        Assertions.assertThat(success).isTrue
        Assertions.assertThat(account.activityWindow.activities).hasSize(3)
        Assertions.assertThat(account.calculateBalance()).isEqualTo(Money.of(2000L))
    }
}