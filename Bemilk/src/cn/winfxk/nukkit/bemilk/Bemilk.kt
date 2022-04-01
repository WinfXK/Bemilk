package cn.winfxk.nukkit.bemilk

import cn.nukkit.event.Listener
import cn.nukkit.plugin.PluginBase
import cn.winfxk.nukkit.bemilk.form.base.Formdispose
import cn.winfxk.nukkit.bemilk.money.MyEconomy
import cn.winfxk.nukkit.bemilk.tool.Config
import java.io.File

class Bemilk : PluginBase(), Listener {
    companion object {
        private const val ConfigName = "Config.yml"
        const val MsgConfigName = "Message.yml"
        private const val Languagename = "Language"
        lateinit var LanguageFile: File
        val Meta = arrayOf(ConfigName, MsgConfigName)
        lateinit var config: Config
        var main: Bemilk = Bemilk();
        lateinit var Economy: MyEconomy
    }

    override fun onEnable() {
        super.onEnable()
        server.pluginManager.registerEvents(Formdispose(this), this)
    }

    override fun onLoad() {
        main = this
        LanguageFile = File(dataFolder, Languagename)
        Companion.config = Config(File(dataFolder, ConfigName))
        super.onLoad()
    }

    override fun onDisable() {
        super.onDisable()
    }
}