package xiaokai.bemilk.form;

import java.io.File;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.MyPlayer;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Dispose {
	private Player player;
	private Kick kick;

	public Dispose(Player player) {
		this.player = player;
		kick = Kick.kick;
	}

	/**
	 * 处理玩家点击主页的事件
	 * 
	 * @param data
	 * @return
	 */
	public boolean Main(FormResponseSimple data) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		if (data.getClickedButtonId() >= myPlayer.Keys.size()) {
			if (myPlayer.AdminKeys == null || myPlayer.AdminKeys.size() == 0)
				return false;
			switch (myPlayer.AdminKeys.get(data.getClickedButtonId() - myPlayer.Keys.size())) {
			case "ms":
				return MakeForm.MoreSettings(player);
			case "set":
				return MakeForm.Setting(player);
			case "add":
				return Paging.addShop.MakeForm(player);
			case "ss":
				return Paging.setShop.MakeForm(player);
			case "del":
				return Paging.delShop.MakeForm(player);
			default:
				player.sendMessage(kick.Message.getSon("界面", "界面显示失败", new String[] { "{Player}", "{Error}" },
						new Object[] { player.getName(), "您的权限不足或要打开的界面不存在！" }));
				return false;
			}
		}
		String Key = myPlayer.Keys.get(data.getClickedButtonId());
		Config config = new Config(new File(kick.mis.getDataFolder(), kick.ShopConfigName), Config.YAML);
		return MakeForm.OpenShop(player, new File(new File(kick.mis.getDataFolder(), Kick.ShopConfigPath),
				(String) ((Map<String, Object>) config.get(Key)).get("Config")));
	}
}
