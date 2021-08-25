package io.reflectoring.buckpal.domain

import io.reflectoring.buckpal.common.ActivityTestData.defaultActivity
import io.reflectoring.buckpal.domain.Account.AccountId
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class ActivityWindowTest {
    @Test
    fun calculatesStartTimestamp() {
        val window = ActivityWindow(
            defaultActivity().withTimestamp(startDate()).build(),
            defaultActivity().withTimestamp(inBetweenDate()).build(),
            defaultActivity().withTimestamp(endDate()).build()
        )
        Assertions.assertThat(window.startTimestamp).isEqualTo(startDate())
    }

    @Test
    fun calculatesEndTimestamp() {
        val window = ActivityWindow(
            defaultActivity().withTimestamp(startDate()).build(),
            defaultActivity().withTimestamp(inBetweenDate()).build(),
            defaultActivity().withTimestamp(endDate()).build()
        )
        Assertions.assertThat(window.endTimestamp).isEqualTo(endDate())
    }

    @Test
    fun calculatesBalance() {
        val account1 = AccountId(1L)
        val account2 = AccountId(2L)
        val window = ActivityWindow(
            defaultActivity()
                .withSourceAccount(account1)
                .withTargetAccount(account2)
                .withMoney(Money.of(999)).build(),
            defaultActivity()
                .withSourceAccount(account1)
                .withTargetAccount(account2)
                .withMoney(Money.of(1)).build(),
            defaultActivity()
                .withSourceAccount(account2)
                .withTargetAccount(account1)
                .withMoney(Money.of(500)).build()
        )
        Assertions.assertThat(window.calculateBalance(account1)).isEqualTo(Money.of(-500))
        Assertions.assertThat(window.calculateBalance(account2)).isEqualTo(Money.of(500))
    }

    private fun startDate(): LocalDateTime {
        return LocalDateTime.of(2019, 8, 3, 0, 0)
    }

    private fun inBetweenDate(): LocalDateTime {
        return LocalDateTime.of(2019, 8, 4, 0, 0)
    }

    private fun endDate(): LocalDateTime {
        return LocalDateTime.of(2019, 8, 5, 0, 0)
    }
}