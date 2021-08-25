package io.reflectoring.buckpal.adapter.web

import io.reflectoring.buckpal.common.WebAdapter
import org.springframework.web.bind.annotation.RestController
import io.reflectoring.buckpal.application.port.`in`.SendMoneyUseCase
import io.reflectoring.buckpal.domain.Account
import io.reflectoring.buckpal.domain.Money
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@WebAdapter
@RestController
internal class SendMoneyController(private val sendMoneyUseCase: SendMoneyUseCase) {
    @PostMapping(path = ["/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}"])
    fun sendMoney(
        @PathVariable("sourceAccountId") sourceAccountId: Long,
        @PathVariable("targetAccountId") targetAccountId: Long,
        @PathVariable("amount") amount: Long
    ) {
        val command = SendMoneyUseCase.SendMoneyCommand(
            Account.AccountId(sourceAccountId),
            Account.AccountId(targetAccountId),
            Money.of(amount)
        )
        sendMoneyUseCase.sendMoney(command)
    }
}