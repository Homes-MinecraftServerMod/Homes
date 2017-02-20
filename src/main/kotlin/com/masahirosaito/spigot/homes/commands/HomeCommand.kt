package com.masahirosaito.spigot.homes.commands

import com.masahirosaito.spigot.homes.Homes
import com.masahirosaito.spigot.homes.commands.subcommands.SetCommand
import com.masahirosaito.spigot.homes.commands.subcommands.SubCommand
import com.masahirosaito.spigot.homes.homedata.PlayerHome
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class HomeCommand(override val plugin: Homes) : CommandExecutor, SubCommand {
    override val name: String = "home"
    override val permission = "home.command"
    val subCommands = listOf<SubCommand>(SetCommand(plugin))

    override fun onCommand(sender: CommandSender?, command: Command?,
                           label: String?, args: Array<out String>?): Boolean {

        if (sender !is Player) return true

        if (!hasPermission(sender)) return sendPermissionMsg(sender)

        if (args == null || args.isEmpty()) return execute(sender, emptyList())

        subCommands.find { it.name == args[0] }?.let {
            return if (it.hasPermission(sender)) {
                it.execute(sender, args.drop(1))
            } else {
                it.sendPermissionMsg(sender)
            }
        }

        return execute(sender, args.toList())
    }

    override fun execute(player: Player, args: List<String>): Boolean = true.apply {
        plugin.homedata.playerHomes[player.uniqueId]?.let { playerHome ->
            if (args.isEmpty()) {
                playerHome.defaultHome?.let { player.teleport(it.toLocation()) }
            } else {
                if (!plugin.configs.onNamedHome) return true

                playerHome.namedHomes[args[0]].let {
                    if (it != null) player.teleport(it.toLocation())
                    else plugin.messenger.send(player, buildString {
                        append(ChatColor.RED)
                        append("Home <${ChatColor.RESET}${args[0]}${ChatColor.RED}> is not set")
                        append(ChatColor.RESET)
                    })
                }
            }
        }
    }
}