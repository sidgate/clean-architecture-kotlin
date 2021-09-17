package io.reflectoring.buckpal.adapter.persistence

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import javax.persistence.GeneratedValue
import io.reflectoring.buckpal.adapter.persistence.AccountJpaEntity
import io.reflectoring.buckpal.adapter.persistence.ActivityJpaEntity
import io.reflectoring.buckpal.domain.Account
import io.reflectoring.buckpal.domain.Money
import io.reflectoring.buckpal.domain.Account.AccountId
import io.reflectoring.buckpal.domain.ActivityWindow
import io.reflectoring.buckpal.domain.Activity
import io.reflectoring.buckpal.domain.Activity.ActivityId
import lombok.RequiredArgsConstructor
import io.reflectoring.buckpal.common.PersistenceAdapter
import io.reflectoring.buckpal.application.port.out.LoadAccountPort
import io.reflectoring.buckpal.application.port.out.UpdateAccountStatePort
import io.reflectoring.buckpal.adapter.persistence.AccountRepository
import io.reflectoring.buckpal.adapter.persistence.ActivityRepository
import io.reflectoring.buckpal.adapter.persistence.AccountMapper
import java.time.LocalDateTime
import javax.persistence.EntityNotFoundException
import org.springframework.data.jpa.repository.JpaRepository

@RequiredArgsConstructor
@PersistenceAdapter
internal class AccountPersistenceAdapter : LoadAccountPort, UpdateAccountStatePort {
    private val accountRepository: AccountRepository? = null
    private val activityRepository: ActivityRepository? = null
    private val accountMapper: AccountMapper? = null
    override fun loadAccount(
        accountId: AccountId,
        baselineDate: LocalDateTime
    ): Account {
        val account = accountRepository!!.findById(accountId.value)
            .orElseThrow { EntityNotFoundException() }!!
        val activities = activityRepository!!.findByOwnerSince(
            accountId.value,
            baselineDate
        )
        val withdrawalBalance = orZero(
            activityRepository
                .getWithdrawalBalanceUntil(
                    accountId.value,
                    baselineDate
                )
        )
        val depositBalance = orZero(
            activityRepository
                .getDepositBalanceUntil(
                    accountId.value,
                    baselineDate
                )
        )
        return accountMapper!!.mapToDomainEntity(
            account,
            activities,
            withdrawalBalance,
            depositBalance
        )
    }

    private fun orZero(value: Long?): Long {
        return value ?: 0L
    }

    override fun updateActivities(account: Account) {
        for (activity in account.activityWindow.activities) {
            if (activity.id == null) {
                activityRepository!!.save(accountMapper!!.mapToJpaEntity(activity))
            }
        }
    }
}