package io.reflectoring.buckpal.adapter.persistence

import io.reflectoring.buckpal.common.AccountTestData.defaultAccount
import io.reflectoring.buckpal.common.ActivityTestData.defaultActivity
import io.reflectoring.buckpal.domain.Account
import io.reflectoring.buckpal.domain.Account.AccountId
import io.reflectoring.buckpal.domain.ActivityWindow
import io.reflectoring.buckpal.domain.Money
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime

@DataJpaTest
@Import(AccountPersistenceAdapter::class, AccountMapper::class)
internal class AccountPersistenceAdapterTest {
    @Autowired
    private val adapterUnderTest: AccountPersistenceAdapter? = null

    @Autowired
    private val activityRepository: ActivityRepository? = null
    @Test
    @Sql("AccountPersistenceAdapterTest.sql")
    fun loadsAccount() {
        val account = adapterUnderTest!!.loadAccount(AccountId(1L), LocalDateTime.of(2018, 8, 10, 0, 0))
        Assertions.assertThat(account.activityWindow.activities).hasSize(2)
        Assertions.assertThat(account.calculateBalance()).isEqualTo(Money.of(500))
    }

    @Test
    fun updatesActivities() {
        val account: Account = defaultAccount()
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(
                ActivityWindow(
                    defaultActivity()
                        .withId(null)
                        .withMoney(Money.of(1L)).build()
                )
            )
            .build()
        adapterUnderTest!!.updateActivities(account)
        Assertions.assertThat(activityRepository!!.count()).isEqualTo(1)
        val savedActivity = activityRepository.findAll()[0]
        Assertions.assertThat(savedActivity.amount).isEqualTo(1L)
    }
}