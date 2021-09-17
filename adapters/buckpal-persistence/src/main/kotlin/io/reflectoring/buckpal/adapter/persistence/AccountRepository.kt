package io.reflectoring.buckpal.adapter.persistence

import org.springframework.data.jpa.repository.JpaRepository

internal interface AccountRepository : JpaRepository<AccountJpaEntity?, Long?>