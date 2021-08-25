package io.reflectoring.buckpal.adapter.web

import io.reflectoring.buckpal.application.port.`in`.SendMoneyUseCase
import io.reflectoring.buckpal.application.port.`in`.SendMoneyUseCase.SendMoneyCommand
import io.reflectoring.buckpal.domain.Account.AccountId
import io.reflectoring.buckpal.domain.Money
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.eq
import org.mockito.BDDMockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(controllers = [SendMoneyController::class])
internal class SendMoneyControllerTest {
    @Autowired
    private val mockMvc: MockMvc? = null

    @MockBean
    private val sendMoneyUseCase: SendMoneyUseCase? = null
    @Test
    @Throws(Exception::class)
    fun testSendMoney() {
        mockMvc!!.perform(
            MockMvcRequestBuilders.post(
                "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
                41L, 42L, 500
            )
                .header("Content-Type", "application/json")
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
        BDDMockito.then<SendMoneyUseCase>(sendMoneyUseCase).should()
            .sendMoney(
                eq(
                    SendMoneyCommand(
                        AccountId(41L),
                        AccountId(42L),
                        Money.of(500L)
                    )
                )
            )
    }
}