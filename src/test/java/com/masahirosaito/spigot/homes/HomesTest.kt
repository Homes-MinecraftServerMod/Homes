package com.masahirosaito.spigot.homes

import com.masahirosaito.spigot.homes.commands.maincommands.homecommands.HomeCommand
import com.masahirosaito.spigot.homes.datas.FeeData
import com.masahirosaito.spigot.homes.exceptions.NoConsoleCommandException
import com.masahirosaito.spigot.homes.strings.ErrorStrings.NO_RECEIVED_INVITATION
import com.masahirosaito.spigot.homes.strings.commands.DeleteCommandStrings.DELETE_DEFAULT_HOME
import com.masahirosaito.spigot.homes.strings.commands.DeleteCommandStrings.DELETE_NAMED_HOME
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.RECEIVE_DEFAULT_HOME_INVITATION_FROM
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.RECEIVE_NAMED_HOME_INVITATION_FROM
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.SEND_DEFAULT_HOME_INVITATION_TO
import com.masahirosaito.spigot.homes.strings.commands.InviteCommandStrings.SEND_NAMED_HOME_INVITATION_TO
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.SET_DEFAULT_HOME_PRIVATE
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.SET_DEFAULT_HOME_PUBLIC
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.SET_NAMED_HOME_PRIVATE
import com.masahirosaito.spigot.homes.strings.commands.PrivateCommandStrings.SET_NAMED_HOME_PUBLIC
import com.masahirosaito.spigot.homes.strings.commands.SetCommandStrings.SET_DEFAULT_HOME
import com.masahirosaito.spigot.homes.strings.commands.SetCommandStrings.SET_NAMED_HOME
import com.masahirosaito.spigot.homes.testutils.*
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.command
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.defaultLocation
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.feeFile
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homeConsoleCommandSender
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.homes
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.minene
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.namedLocation
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.nepian
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.pluginCommand
import com.masahirosaito.spigot.homes.testutils.TestInstanceCreator.pluginFolder
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Server
import org.bukkit.World
import org.bukkit.command.PluginCommand
import org.bukkit.entity.Player
import org.bukkit.plugin.PluginDescriptionFile
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.RegisteredServiceProvider
import org.bukkit.plugin.ServicesManager
import org.bukkit.plugin.java.JavaPluginLoader
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.io.File


@RunWith(PowerMockRunner::class)
@PrepareForTest(Homes::class, JavaPluginLoader::class, PluginDescriptionFile::class,
        Server::class, PluginCommand::class, Player::class, Location::class, World::class, Bukkit::class,
        PluginManager::class, ServicesManager::class, RegisteredServiceProvider::class)
class HomesTest {

    @Before
    fun setUp() {
        assertTrue(TestInstanceCreator.setUp())
    }

    @After
    fun tearDown() {
        assertTrue(TestInstanceCreator.tearDown())
    }

    @Test
    fun プラグインフォルダが生成されている() {
        assertTrue(pluginFolder.exists())
    }

    @Test
    fun 設定ファイルが生成されている() {
        assertTrue(File(pluginFolder, "configs.json").exists())
    }

    @Test
    fun データファイルが生成されている() {
        assertTrue(File(pluginFolder, "playerhomes.json").exists())
    }

    @Test
    fun 料金設定ファイルが生成されている() {
        assertTrue(File(pluginFolder, "fee.json").exists())
    }

    @Test
    fun 料金設定ファイルが読み込まれている() {
        assertThat(homes.fee, `is`(loadData(feeFile, FeeData::class.java)))
    }

    @Ignore
    @Test
    fun 設定ファイルが読み込まれている() {
    }

    @Ignore
    @Test
    fun 言語ファイルが読み込まれている() {
    }

    @Ignore
    @Test
    fun メッセンジャーが読み込まれている() {
    }

    @Test
    fun 経済プラグインが読み込まれている() {
        assertThat(homes.econ, `is`(notNullValue()))
    }

    @Test
    fun ホームコマンドが登録されている() {
        assertTrue(homes.getCommand("home").executor is HomeCommand)
    }

    @Test
    fun ホームへ移動するコマンドの実行ができる() {
        nepian.executeHomeCommand()
        nepian.getDelayThread()?.join()
        assertThat(nepian.location, `is`(defaultLocation))
    }

    @Test
    fun 名前付きホームへ移動するコマンドの実行ができる() {
        nepian.executeHomeCommand("home1")
        nepian.getDelayThread()?.join()
        assertThat(nepian.location, `is`(namedLocation))
    }

    @Test
    fun 他人のホームへ移動するコマンドの実行ができる() {
        minene.executeHomeCommand("-p", "Nepian")
        minene.getDelayThread()?.join()
        assertThat(minene.location, `is`(defaultLocation))
    }

    @Test
    fun 他人の名前付きホームへ移動するコマンドの実行ができる() {
        minene.executeHomeCommand("home1", "-p", "Nepian")
        minene.getDelayThread()?.join()
        assertThat(minene.location, `is`(namedLocation))
    }

    @Test
    fun ホームを設定するコマンドの実行ができる() {
        nepian.executeHomeCommand("set")
        assertThat(nepian.lastMsg(), `is`(SET_DEFAULT_HOME()))
    }

    @Test
    fun 名前付きホームを設定するコマンドの実行ができる() {
        nepian.executeHomeCommand("set", "home1")
        assertThat(nepian.lastMsg(), `is`(SET_NAMED_HOME("home1")))
    }

    @Test
    fun 設定されたホームを削除するコマンドの実行ができる() {
        nepian.executeHomeCommand("delete")
        assertThat(nepian.lastMsg(), `is`(DELETE_DEFAULT_HOME()))
    }

    @Test
    fun 設定された名前付きホームを削除するコマンドの実行ができる() {
        nepian.executeHomeCommand("delete", "home1")
        assertThat(nepian.lastMsg(), `is`(DELETE_NAMED_HOME("home1")))
    }

    @Test
    fun 設定されたホームをプライベート化するコマンドの実行ができる() {
        nepian.executeHomeCommand("private", "on")
        assertThat(nepian.lastMsg(), `is`(SET_DEFAULT_HOME_PRIVATE()))
    }

    @Test
    fun 設定された名前付きホームをプライベート化するコマンドの実行ができる() {
        nepian.executeHomeCommand("private", "on", "home1")
        assertThat(nepian.lastMsg(), `is`(SET_NAMED_HOME_PRIVATE("home1")))
    }

    @Test
    fun 設定されたホームをパブリック化するコマンドの実行ができる() {
        nepian.executeHomeCommand("private", "off")
        assertThat(nepian.lastMsg(), `is`(SET_DEFAULT_HOME_PUBLIC()))
    }

    @Test
    fun 設定された名前付きホームをパブリック化するコマンドの実行ができる() {
        nepian.executeHomeCommand("private", "off", "home1")
        assertThat(nepian.lastMsg(), `is`(SET_NAMED_HOME_PUBLIC("home1")))
    }

    @Test
    fun プレイヤーから設定されたホームの一覧を表示するコマンドの実行ができる() {
        assertTrue(nepian.executeHomeCommand("list"))
    }

    @Test
    fun プレイヤーから他の人の設定されたホームの一覧を表示するコマンドの実行ができる() {
        assertTrue(minene.executeHomeCommand("list", "Nepian"))
    }

    @Test
    fun コンソールから他の人の設定されたホームの一覧を表示するコマンドの実行ができる() {
        assertTrue(homeConsoleCommandSender.executeHomeCommand("list", "Nepian"))
    }

    @Test
    fun 他の人を自分のホームに招待するコマンドの実行ができる() {
        nepian.executeHomeCommand("invite", "Minene")
        assertThat(nepian.lastMsg(), `is`(SEND_DEFAULT_HOME_INVITATION_TO("Minene")))
        assertThat(minene.lastMsg(), `is`(RECEIVE_DEFAULT_HOME_INVITATION_FROM("Nepian")))
        minene.acceptInvitation()
    }

    @Test
    fun 他の人を自分の名前付きホームに招待するコマンドの実行ができる() {
        nepian.executeHomeCommand("invite", "Minene", "home1")
        assertThat(nepian.lastMsg(), `is`(SEND_NAMED_HOME_INVITATION_TO("Minene", "home1")))
        assertThat(minene.lastMsg(), `is`(RECEIVE_NAMED_HOME_INVITATION_FROM("Nepian", "home1")))
        minene.acceptInvitation()
    }

    @Test
    fun 他の人からの招待を受けるコマンドの実行ができる() {
        nepian.executeHomeCommand("invite")
        assertThat(nepian.lastMsg(), `is`(NO_RECEIVED_INVITATION()))
    }

    @Test
    fun プレイヤーからコマンドの説明一覧を表示するコマンドの実行ができる() {
        assertTrue(nepian.executeHomeCommand("help"))
    }

    @Test
    fun コンソールからコマンドの説明一覧を表示するコマンドの実行ができる() {
        assertTrue(homeConsoleCommandSender.executeHomeCommand("help"))
    }

    @Test
    fun コマンドの使い方を表示するコマンドの実行ができる() {
        assertTrue(nepian.executeHomeCommand("help", "home"))
    }

    @Test
    fun コンソールからプラグインをリロードするコマンドの実行ができる() {
        assertTrue(homeConsoleCommandSender.executeHomeCommand("reload"))
    }

    @Test
    fun プレイヤーからプラグインをリロードするコマンドの実行ができる() {
        assertTrue(nepian.executeHomeCommand("reload"))
    }

    @Test
    fun コンソールからホームコマンドを実行した場合にエラーを表示する() {
        homeConsoleCommandSender.executeHomeCommand()
        assertThat(homeConsoleCommandSender.lastMsg(), `is`(NoConsoleCommandException().message))
    }
}
