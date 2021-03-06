package com.masahirosaito.spigot.homes.datas.strings

data class EconomyStringData(
        val PAY: String = "You paid &e[pay-amount]&r and now have &e[balance]&r",
        val ECONOMY_ERROR: String = "&cAn error occurred: [error-message]&r",
        val NO_ACCOUNT_ERROR: String = "&cYou are not registered as Economy&r",
        val NOT_ENOUGH_MONEY_ERROR: String = "&cYou have not enough money to execute this command (fee: [command-fee])&r"
)
