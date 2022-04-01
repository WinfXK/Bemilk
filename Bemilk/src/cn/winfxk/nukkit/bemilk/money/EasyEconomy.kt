package cn.winfxk.nukkit.bemilk.money

import cn.winfxk.nukkit.bemilk.Bemilk
import cn.winfxk.nukkit.bemilk.tool.Config
import cn.winfxk.nukkit.bemilk.tool.Tool
import java.io.File

class EasyEconomy : MyEconomy() {
    companion object {
        const val ConfigName: String = "EasyEconomy.yml";
        val config: Config = Config(File(Bemilk.main.dataFolder, ConfigName));
    }

    constructor() {
        var map: Map<String, Any> = Bemilk.config.getMap("EasyEconomy");
        economyName = Tool.objToString(map.get("ID"));
        moneyName = Tool.objToString(map.get("Name"));
    }

    override fun getMoney(player: String?): Double {
        return Tool.objToDouble(config.get(player))
    }

    override fun reduceMoney(player: String?, Money: Double): Double {
        var M: Double = getMoney(player) - Money
        config.set(player, M);
        config.save();
        return M;
    }

    override fun setMoney(player: String?, Money: Double): Double {
        config.set(player, Money);
        config.save();
        return Money;
    }

    override fun allowArrears(): Boolean {
        return false;
    }

    override fun addMoney(player: String?, Money: Double): Double {
        var M: Double = getMoney(player) + Money
        config.set(player, M);
        config.save();
        return M;
    }
}