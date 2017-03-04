package com.masahirosaito.spigot.homes.tests.exceptions

import com.masahirosaito.spigot.homes.tests.commands.Permission

class NotHavePermissionException(permission: Permission) : Exception("You don't have permission <${permission.permission}>")
