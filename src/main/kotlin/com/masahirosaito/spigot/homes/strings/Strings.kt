package com.masahirosaito.spigot.homes.strings

import com.masahirosaito.spigot.homes.Configs
import com.masahirosaito.spigot.homes.Homes.Companion.homes
import com.masahirosaito.spigot.homes.strings.commands.*

object Strings {
    val PLAYER_NAME = "[player-name]"
    val PERMISSION_NAME = "[permission-name]"
    val HOME_NAME = "[home-name]"
    val HOME_LIMIT_NUM = "[home-limit-num]"
    val COMMAND_NAME = "[command-name]"
    val COMMAND_USAGE = "[command-usage]"
    val PAY_AMOUNT = "[pay-amount]"
    val BALANCE = "[balance]"
    val ERROR_MESSAGE = "[error-message]"
    val COMMAND_FEE = "[command-fee]"
    val DELAY = "[delay]"

    fun load() {

        "${homes.dataFolder}/languages/${Configs.language}".apply {
            ErrorStrings.load(this)
            HomeDisplayStrings.load(this)
            EconomyStrings.load(this)
            TeleportStrings.load(this)

            "$this/commands".apply {
                HomeCommandStrings.load(this)
                DeleteCommandStrings.load(this)
                InviteCommandStrings.load(this)
                HelpCommandStrings.load(this)
                PrivateCommandStrings.load(this)
                SetCommandStrings.load(this)
                ListCommandStrings.load(this)
                ReloadCommandStrings.load(this)
            }
        }
    }
}
