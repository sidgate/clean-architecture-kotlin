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
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

internal interface ActivityRepository : JpaRepository<ActivityJpaEntity?, Long?> {
    @Query(
        "select a from ActivityJpaEntity a " +
                "where a.ownerAccountId = :ownerAccountId " +
                "and a.timestamp >= :since"
    )
    fun findByOwnerSince(
        @Param("ownerAccountId") ownerAccountId: Long?,
        @Param("since") since: LocalDateTime?
    ): List<ActivityJpaEntity?>?

    @Query(
        "select sum(a.amount) from ActivityJpaEntity a " +
                "where a.targetAccountId = :accountId " +
                "and a.ownerAccountId = :accountId " +
                "and a.timestamp < :until"
    )
    fun getDepositBalanceUntil(
        @Param("accountId") accountId: Long?,
        @Param("until") until: LocalDateTime?
    ): Long?

    @Query(
        "select sum(a.amount) from ActivityJpaEntity a " +
                "where a.sourceAccountId = :accountId " +
                "and a.ownerAccountId = :accountId " +
                "and a.timestamp < :until"
    )
    fun getWithdrawalBalanceUntil(
        @Param("accountId") accountId: Long?,
        @Param("until") until: LocalDateTime?
    ): Long?
}