package cn.winfxk.nukkit.bemilk.form

import cn.nukkit.Player
import cn.winfxk.nukkit.bemilk.Bemilk
import cn.winfxk.nukkit.bemilk.form.api.RootForm
import cn.winfxk.nukkit.bemilk.form.base.BaseForm
import cn.winfxk.nukkit.bemilk.form.base.Formdispose

class MyPlayer(var player: Player) {
    companion object {
        fun getMyPlayer(player: Player): MyPlayer? {
            return Formdispose.MyPlayers[player.name];
        }

        fun getMyPlayer(player: String): MyPlayer? {
            return Formdispose.MyPlayers[player];
        }
    }

    var form: BaseForm? = null;
    var ID: Int = 0;
    var `fun`: RootForm? = null;
    fun show(form: BaseForm): Boolean {
        this.form = form;
        return form.MakeMain();
    }

    fun getName(): String {
        return player.name;
    }

    fun getMoney(): Double {
        return Bemilk.Economy.getMoney(getName())
    }

}