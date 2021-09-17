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
import org.springframework.stereotype.Component
import java.util.ArrayList

@Component
internal class AccountMapper {
    fun mapToDomainEntity(
        account: AccountJpaEntity,
        activities: List<ActivityJpaEntity>,
        withdrawalBalance: Long?,
        depositBalance: Long?
    ): Account {
        val baselineBalance = Money.subtract(
            Money.of(depositBalance!!),
            Money.of(withdrawalBalance!!)
        )
        return Account.withId(
            AccountId(account.id),
            baselineBalance,
            mapToActivityWindow(activities)
        )
    }

    fun mapToActivityWindow(activities: List<ActivityJpaEntity>): ActivityWindow {
        val mappedActivities: MutableList<Activity> = ArrayList()
        for (activity in activities) {
            mappedActivities.add(
                Activity(
                    ActivityId(activity.id),
                    AccountId(activity.ownerAccountId),
                    AccountId(activity.sourceAccountId),
                    AccountId(activity.targetAccountId),
                    activity.timestamp,
                    Money.of(activity.amount!!)
                )
            )
        }
        return ActivityWindow(mappedActivities)
    }

    fun mapToJpaEntity(activity: Activity): ActivityJpaEntity {
        return ActivityJpaEntity(
            if (activity.id == null) null else activity.id.value,
            activity.timestamp,
            activity.ownerAccountId.value,
            activity.sourceAccountId.value,
            activity.targetAccountId.value,
            activity.money.amount.toLong()
        )
    }
}