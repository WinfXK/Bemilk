package cn.winfxk.nukkit.bemilk.form.base

import cn.nukkit.event.EventHandler
import cn.nukkit.event.Listener
import cn.nukkit.event.player.PlayerFormRespondedEvent
import cn.nukkit.event.player.PlayerJoinEvent
import cn.nukkit.event.player.PlayerQuitEvent
import cn.nukkit.form.response.FormResponse
import cn.winfxk.nukkit.bemilk.Bemilk
import cn.winfxk.nukkit.bemilk.form.MyPlayer
import cn.winfxk.nukkit.bemilk.tool.MyMap
import cn.winfxk.nukkit.bemilk.tool.Tool

class Formdispose(var bemilk: Bemilk) : Listener {
    companion object {
        val MyPlayers = MyMap<String, MyPlayer>();
        var FormID = ArrayList<Int>();

        init {
            var list = Bemilk.config.getList("FormID");
            for (any: Any in list)
                FormID.add(Tool.ObjToInt(any));
            while (FormID.size < 2)
                FormID.add(Tool.getRand(10086, 1008611));
        }
    }

    @EventHandler
    fun onPlayerForm(e: PlayerFormRespondedEvent) {
        val ID = e.formID;
        if (!FormID.contains(ID)) return;
        val player = e.player;
        if (!MyPlayers.containsKey(player.name)) return;
        val myPlayer: MyPlayer = MyPlayers[player.name] ?: return;
        if (myPlayer.form == null) return;
        if (e.wasClosed()) {
            myPlayer.form!!.wasClosed();
            return;
        }
        val data: FormResponse = e.response;
        myPlayer.form!!.dispose(data)
    }

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        MyPlayers.remove(e.player.name);
    }

    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        val player = e.player;
        MyPlayers.put(player.name, MyPlayer(player));
    }
}