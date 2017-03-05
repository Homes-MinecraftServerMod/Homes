package com.masahirosaito.spigot.homes.tests.commands

object DeleteCommandData : CommandData {

    override fun name(): String = "delete"

    override fun usages(): List<Pair<String, String>> = listOf(
            "/home delete" to "Delete your default home",
            "/home delete <home_name>" to "Delete your named home"
    )

    fun getResultMessage(name: String? = null): String = prefix(buildString {
        append("Successfully delete your ")
        append(if (name == null) "default home" else "named home <$name>")
    })
}