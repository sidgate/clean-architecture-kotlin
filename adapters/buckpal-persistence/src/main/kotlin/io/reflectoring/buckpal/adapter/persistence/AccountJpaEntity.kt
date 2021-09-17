package io.reflectoring.buckpal.adapter.persistence

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "account")
internal class AccountJpaEntity (
    @Id
    @GeneratedValue
    val id: Long? = null
)