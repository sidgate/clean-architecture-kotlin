package io.reflectoring.buckpal.adapter.persistence

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
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
import lombok.Data
import java.time.LocalDateTime
import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.*

@Entity
@Table(name = "activity")
internal class ActivityJpaEntity (
    @Id
    @GeneratedValue
    val id: Long? = null,

    @Column
    var timestamp: LocalDateTime? = null,

    @Column
    val ownerAccountId: Long? = null,

    @Column
    val sourceAccountId: Long? = null,

    @Column
    val targetAccountId: Long? = null,

    @Column
    var amount: Long? = null
)