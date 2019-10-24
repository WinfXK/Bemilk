package xiaokai.bemilk.shop;

import xiaokai.bemilk.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.bemilk.tool.ItemID;
import xiaokai.bemilk.tool.SimpleForm;
import xiaokai.bemilk.tool.Tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;

import me.onebone.economyapi.EconomyAPI;

/**
*@author Winfxk
*/
/**
 * 删除商店分页
 * 
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class delShop {
	private static Message msg = Kick.kick.Message;
	private static Kick kick = Kick.kick;

	/**
	 * 处理玩家点击的是删除界面还是不删除
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean dis(Player player, FormResponseSimple data) {
		if (data.getClickedButtonId() == 1) {
			MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
			Map<String, Object> Shops = (HashMap<String, Object>) kick.ShopConfig.get("Shops");
			Shops.remove(myPlayer.CacheKey);
			kick.ShopConfig.set("Shops", Shops);
			kick.ShopConfig.save();
			boolean isok = myPlayer.file.delete();
			player.sendMessage("§6删除" + (isok ? "§e成功" : "§4失败"));
			return MakeForm.Main(player);
		}
		return false;
	}

	/**
	 * 处理玩家点击的是哪一个按钮，并且创建对应的界面
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean start(Player player, FormResponseSimple data) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.CacheKey = myPlayer.Keys.get(data.getClickedButtonId());
		Map<String, Object> Shops = (HashMap<String, Object>) kick.ShopConfig.get("Shops");
		Map<String, Object> Item = (Map<String, Object>) Shops.get(myPlayer.CacheKey);
		File file = new File(new File(kick.mis.getDataFolder(), Kick.ShopConfigPath), (String) Item.get("Config"));
		myPlayer.file = file;
		Config config = new Config(file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(6), kick.Message.getText(config.get("Title"), DsK, DsO));
		Map<String, Object> Items = (config.get("Items") == null || !(config.get("Items") instanceof Map))
				? new HashMap<>()
				: (HashMap<String, Object>) config.get("Items");
		String string = "";
		Set<String> set = config.getAll().keySet();
		for (String ike : set) {
			if (ike.equals("Items"))
				continue;
			String RandColor = Tool.getRandColor();
			string += RandColor + ike + "§f:" + RandColor + config.getString(ike) + "\n";
		}
		String itemString = "";
		if (Items.size() > 0) {
			Set<String> ItemKey = Items.keySet();
			int i = 0;
			for (String ike : ItemKey) {
				Map<String, Object> map = (Map<String, Object>) Items.get(ike);
				Map<String, Object> items = (map.get("Items") != null) && (map.get("Items") instanceof Map)
						? (HashMap<String, Object>) map.get("Items")
						: new HashMap<>();
				Set<String> temsKey = items.keySet();
				int j = 0;
				for (String kie : temsKey) {
					itemString += "§9" + ItemID.getNameByID(kie)
							+ (((i + 1) < ItemKey.size()) ? ((j + 1) < temsKey.size() ? "§f," : "") : "");
					j++;
				}
				i++;
			}
			itemString = "§f[§9" + itemString + "§f]";
		} else
			itemString = "§4没有上架任何物品";
		string += Tool.getRandColor() + "已上架物品: " + itemString + "\n\n§4您确定要删除这个分页吗？";
		form.setContent(string);
		form.addButton("§a取消");
		form.addButton("§4确定删除");
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 创建一个界面供玩家选择要删除的商店分页
	 * 
	 * @param player
	 * @return
	 */
	public static boolean MakeForm(Player player) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		Config config = kick.ShopConfig;
		Map<String, Object> Shops = (config.get("Shops") == null || !(config.get("Shops") instanceof Map))
				? new HashMap<>()
				: (HashMap<String, Object>) config.get("Shops");
		if (Shops.size() < 1)
			return MakeForm.Tip(player, "§4当前还没有任何一个商店分页");
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(5), kick.Message.getText(config.get("Title"), DsK, DsO),
				kick.Message.getText(config.get("Content"), DsK, DsO));
		List<String> Keys = new ArrayList<>();
		for (String ike : Shops.keySet()) {
			Map<String, Object> map = (Map<String, Object>) Shops.get(ike);
			File file = new File(new File(kick.mis.getDataFolder(), Kick.ShopConfigPath), (String) map.get("Config"));
			Config config2 = new Config(file, Config.YAML);
			String Button = msg.getText(map.get("Text"), DsK, DsO);
			String ShopTitle = msg.getText(config2.get("Title"), DsK, DsO);
			String Content = msg.getText(config2.get("Content"), DsK, DsO);
			int IconType = Integer.valueOf(String.valueOf(map.get("IconType")));
			form.addButton(
					msg.getSun("界面", "主页", "商店分页按钮格式",
							new String[] { "{Player}", "{ButtonName}", "{ShopTitle}", "{ShopContent}" },
							new String[] { player.getName(), Button, ShopTitle, Content }),
					IconType == 2 ? false : true, IconType != 0 ? (String) map.get("IconPath") : null);
			Keys.add(ike);
		}
		myPlayer.Keys = Keys;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}
}