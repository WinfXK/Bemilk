package xiaokai.bemilk.shop;

import xiaokai.bemilk.data.Message;
import xiaokai.bemilk.data.MyPlayer;
import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.tool.Tool;
import xiaokai.tool.form.SimpleForm;

import java.io.File;
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
 * 商店删除
 * 
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class delShopItem {
	private static Kick kick = Kick.kick;
	private static Message msg = Kick.kick.Message;

	/**
	 * 处理玩家在确定删除页面的时候是否点击了确定
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean disOK(Player player, FormResponseSimple data) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		if (data.getClickedButtonId() != 0)
			return OpenShop.ShowShop(player, myPlayer.file);
		Config config = new Config(myPlayer.file, Config.YAML);
		Object object = config.get("Items");
		Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) object;
		Shops.remove(myPlayer.CacheKey);
		config.set("Items", Shops);
		boolean isOk = config.save();
		player.sendMessage("§6您" + (isOk ? "§e成功" : "§4未成功") + "§6删除这个项目！");
		return isOk;
	}

	/**
	 * 处理显示可以删除的按钮后，玩家点击按钮发回的数据
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean disMakeForm(Player player, FormResponseSimple data) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		int ID = data.getClickedButtonId();
		if (ID >= myPlayer.Keys.size()) {
			OpenShop.ShowShop(player, myPlayer.file);
			return false;
		}
		Config config = new Config(myPlayer.file, Config.YAML);
		Object object = config.get("Items");
		Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) object;
		String Key = myPlayer.Keys.get(ID);
		if (!Shops.containsKey(Key)) {
			OpenShop.ShowShop(player, myPlayer.file);
			return false;
		}
		myPlayer.CacheKey = Key;
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		Map<String, Object> item = (Map<String, Object>) Shops.get(Key);
		SimpleForm form = new SimpleForm(kick.formID.getID(23), kick.Message.getText(config.get("Title"), DsK, DsO),
				MapTosString(item, "", Tool.getRandColor()) + "\n\n§4是否删除该项目？");
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.addButton(Tool.getRandColor() + "删除").addButton(Tool.getRandColor() + "取消").sendPlayer(player);
		return true;
	}

	/**
	 * 将Map对象格式化为文本
	 * 
	 * @param item   要格式化的对象
	 * @param string 前置空格数
	 * @return
	 */
	public static String MapTosString(Map<String, Object> item, String string, String color) {
		String data = "";
		Set<String> keys = item.keySet();
		for (String key : keys) {
			Object obj = item.get(key);
			if (obj instanceof List)
				data += string + color + key + "§f: \n" + color
						+ ListTosString((List<Object>) obj, string + "  ", Tool.getRandColor());
			else if (obj instanceof Map)
				data += string + color + key + "§f: \n" + color
						+ MapTosString((Map<String, Object>) obj, string + "  ", Tool.getRandColor());
			else
				data += string + color + key + "§f: " + color + String.valueOf(obj);
			data += "\n";
		}
		return data.replace("\n\n", "\n").replace("\n\n", "\n");
	}

	/**
	 * 将List格式化为文本
	 * 
	 * @param item   要格式化的List对象
	 * @param string 前置空格数
	 * @return
	 */
	public static String ListTosString(List<Object> item, String string, String color) {
		String data = "";
		for (Object obj : item) {
			if (obj instanceof List)
				data += string + "\n" + color + ListTosString((List<Object>) obj, string + "  ", Tool.getRandColor());
			else if (obj instanceof Map)
				data += string + "\n" + color
						+ MapTosString((Map<String, Object>) obj, string + "  ", Tool.getRandColor());
			else
				data += string + "- " + color + String.valueOf(obj);
			data += "\n";
		}
		return data;
	}

	/**
	 * 创建界面选择要删除的按钮
	 * 
	 * @param player
	 * @param file
	 * @return
	 */
	public static boolean MakeForm(Player player, File file) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		Config config = new Config(file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		if (!Shop.isOk(player, file))
			return MakeForm.Tip(player, kick.Message.getSun("界面", "商店分页", "被过滤提示", DsK, DsO));
		if (!Shop.isOkMoney(player, file))
			return MakeForm.Tip(player,
					kick.Message.getSun("界面", "商店分页", "被过滤提示",
							new String[] { "{Player}", "{Money}", "{MoneyFloor}", "{MoneyLimit}" },
							new Object[] { player.getName(), EconomyAPI.getInstance().myMoney(player),
									config.getDouble("MoneyFloor"), config.getDouble("MoneyLimit") }));
		Object object = config.get("Items");
		Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) object;
		SimpleForm form = new SimpleForm(kick.formID.getID(22), kick.Message.getText(config.get("Title"), DsK, DsO),
				kick.Message.getText(config.get("Content"), DsK, DsO));
		Set<String> ItemsSet = Shops.keySet();
		for (String ike : ItemsSet) {
			Object ob = Shops.get(ike);
			Map<String, Object> ItemAerS = (ob == null || !(ob instanceof Map)) ? new HashMap<>()
					: (HashMap<String, Object>) ob;
			ItemAerS.put("Key", ike);
			form = OpenShop.getForm(player, form, ItemAerS);
		}
		myPlayer.file = file;
		myPlayer.Keys = form.Keys;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.addButton("取消").sendPlayer(player);
		return true;
	}
}