package xiaokai.bemilk.form;

import xiaokai.bemilk.data.MyPlayer;
import xiaokai.bemilk.form.manage.addShop;
import xiaokai.bemilk.form.manage.delShop;
import xiaokai.bemilk.form.manage.setShop;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.shop.delShopItem;
import xiaokai.bemilk.shop.add.addShopItem;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;

import me.onebone.economyapi.EconomyAPI;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Dispose {
	private static Kick kick = Kick.kick;

	/**
	 * 处理玩家点击的商店分页发挥的数据
	 * 
	 * @param data
	 * @return
	 */
	public static boolean OpenShop(Player player, FormResponseSimple data) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		if (data.getClickedButtonId() >= myPlayer.Keys.size()) {
			if (myPlayer.ExtraKeys == null || myPlayer.ExtraKeys.size() == 0
					|| (myPlayer.ExtraKeys.size() - 1) < (data.getClickedButtonId() - myPlayer.Keys.size()))
				return false;
			switch (myPlayer.ExtraKeys.get(data.getClickedButtonId() - myPlayer.Keys.size())) {
			case "myshop":
				String[] DsK = { "{Player}", "{Money}" };
				Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
				if (!Kick.isAdmin(player) && kick.config.getBoolean("限制OP使用个人商店") && player.isOp())
					return MakeForm.Tip(player, kick.Message.getSon("个人商店", "限制OP使用个人商店", DsK, DsO));
				if (!Kick.isAdmin(player) && kick.config.getBoolean("限制创造模式使用个人商店") && player.getGamemode() == 1)
					return MakeForm.Tip(player, kick.Message.getSon("个人商店", "限制创造模式使用个人商店", DsK, DsO));
				return xiaokai.bemilk.shop.myshop.MyShop.MakeMain(player, myPlayer.file);
			case "seek":
				return MakeForm.OpenShopFoSeek(player, myPlayer.file);
			case "add":
				return addShopItem.MakeForm(player, myPlayer.file);
			case "del":
				return delShopItem.MakeForm(player, myPlayer.file);
			}
			player.sendMessage(kick.Message.getSon("界面", "界面显示失败", new String[] { "{Player}", "{Error}" },
					new Object[] { player.getName(), "您的权限不足或要打开的界面不存在！" }));
			return false;
		}
		return xiaokai.bemilk.shop.OpenShop.Open(player, myPlayer.file, myPlayer.Keys.get(data.getClickedButtonId()));
	}

	/**
	 * 处理玩家点击主页的事件
	 * 
	 * @param data
	 * @return
	 */
	public static boolean Main(Player player, FormResponseSimple data) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		if (data.getClickedButtonId() >= myPlayer.Keys.size()) {
			if (myPlayer.ExtraKeys == null || myPlayer.ExtraKeys.size() == 0
					|| (myPlayer.ExtraKeys.size() - 1) < (data.getClickedButtonId() - myPlayer.Keys.size()))
				return false;
			switch (myPlayer.ExtraKeys.get(data.getClickedButtonId() - myPlayer.Keys.size())) {
			case "seek":
				return MakeForm.Seek(player);
			case "ms":
				return MakeForm.MoreSettings(player);
			case "set":
				return MakeForm.Setting(player);
			case "add":
				return addShop.MakeForm(player);
			case "ss":
				return setShop.MakeForm(player);
			case "del":
				return delShop.MakeForm(player);
			}
			player.sendMessage(kick.Message.getSon("界面", "界面显示失败", new String[] { "{Player}", "{Error}" },
					new Object[] { player.getName(), "您的权限不足或要打开的界面不存在！" }));
			return false;
		}
		String Key = myPlayer.Keys.get(data.getClickedButtonId());
		Config config = new Config(new File(kick.mis.getDataFolder(), kick.ShopConfigName), Config.YAML);
		Object object = config.get("Shops");
		Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) object;
		return xiaokai.bemilk.shop.OpenShop.ShowShop(player,
				new File(new File(kick.mis.getDataFolder(), Kick.ShopConfigPath),
						(String) ((Map<String, Object>) Shops.get(Key)).get("Config")));
	}

	/**
	 * 处理玩家点击更多设置返回的数据
	 * 
	 * @param data
	 * @return
	 */
	public static boolean MoreSettings(Player player, FormResponseSimple data) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		if (myPlayer.ExtraKeys == null || myPlayer.ExtraKeys.size() == 0)
			return false;
		if (Kick.isAdmin(player))
			switch (myPlayer.ExtraKeys.get(data.getClickedButtonId())) {
			case "seek":
				return MakeForm.Seek(player);
			case "ms":
				return MakeForm.MoreSettings(player);
			case "set":
				return MakeForm.Setting(player);
			case "add":
				return addShop.MakeForm(player);
			case "ss":
				return setShop.MakeForm(player);
			case "del":
				return delShop.MakeForm(player);
			default:
				player.sendMessage(kick.Message.getSon("界面", "界面显示失败", new String[] { "{Player}", "{Error}" },
						new Object[] { player.getName(), "您的权限不足或要打开的界面不存在！" }));
				return false;
			}
		player.sendMessage(kick.Message.getSon("界面", "界面显示失败", new String[] { "{Player}", "{Error}" },
				new Object[] { player.getName(), "您的权限不足或要打开的界面不存在！" }));
		return false;
	}
}
