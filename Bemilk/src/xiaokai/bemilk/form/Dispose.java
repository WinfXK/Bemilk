package xiaokai.bemilk.form;

import xiaokai.bemilk.data.DisPlayer;
import xiaokai.bemilk.data.Message;
import xiaokai.bemilk.data.MyPlayer;
import xiaokai.bemilk.data.SeekData;
import xiaokai.bemilk.form.manage.addShop;
import xiaokai.bemilk.form.manage.delShop;
import xiaokai.bemilk.form.manage.setShop;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.set.CustomEffect;
import xiaokai.bemilk.set.CustomItem;
import xiaokai.bemilk.set.SetConfig;
import xiaokai.bemilk.shop.delShopItem;
import xiaokai.bemilk.shop.add.addShopItem;
import xiaokai.bemilk.shop.open.OpenShop;
import xiaokai.tool.data.Effectrec;
import xiaokai.tool.data.EnchantName;
import xiaokai.tool.data.ItemID;
import xiaokai.tool.form.SimpleForm;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;

import me.onebone.economyapi.EconomyAPI;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Dispose {
	private static Kick kick = Kick.kick;
	private static Message msg = Kick.kick.Message;

	/**
	 * 处理子页搜索返回的关键字
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean disSHopSeek(Player player, FormResponseCustom data) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		String string = data.getInputResponse(0);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(28), msg.getSon("搜索", "标题", DsK, DsO));
		if (string == null || string.isEmpty())
			return MakeForm.Tip(player, msg.getSon("搜索", "空关键字", DsK, DsO));
		List<SeekData> seekData = new ArrayList<>();
		Config config = new Config(myPlayer.file, Config.YAML);
		Object object = config.get("Items");
		Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) object;
		for (String ike : Shops.keySet()) {
			Map<String, Object> map = (Map<String, Object>) Shops.get(ike);
			if (isOK(map, string)) {
				form = OpenShop.getForm(player, form, map);
				SeekData adata = new SeekData();
				adata.file = myPlayer.file;
				adata.Key = ike;
				seekData.add(adata);
			}
		}
		if (form.getButtonSize() < 1)
			return MakeForm.Tip(player, msg.getSon("搜索", "找不到数据", DsK, DsO));
		myPlayer.SeekData = seekData;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 处理在主页搜索的事件
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean disSeek(Player player, FormResponseCustom data) {
		String string = data.getInputResponse(0);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(28), msg.getSon("搜索", "标题", DsK, DsO));
		if (string == null || string.isEmpty())
			return MakeForm.Tip(player, msg.getSon("搜索", "空关键字", DsK, DsO));
		File dir = new File(kick.mis.getDataFolder(), Kick.ShopConfigPath);
		List<SeekData> seekData = new ArrayList<>();
		for (String FileName : dir.list(new MakeForm())) {
			File file = new File(dir, FileName);
			Config config = new Config(file, Config.YAML);
			Object object = config.get("Items");
			Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<>()
					: (HashMap<String, Object>) object;
			for (String ike : Shops.keySet()) {
				Map<String, Object> map = (Map<String, Object>) Shops.get(ike);
				if (isOK(map, string)) {
					form = OpenShop.getForm(player, form, map);
					SeekData adata = new SeekData();
					adata.file = file;
					adata.Key = ike;
					seekData.add(adata);
				}
			}
		}
		if (form.getButtonSize() < 1)
			return MakeForm.Tip(player, msg.getSon("搜索", "找不到数据", DsK, DsO));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.SeekData = seekData;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 判断是否存在！
	 * 
	 * @param map
	 * @param data
	 * @return
	 */
	public static boolean isOK(Map<String, Object> map, String g) {
		Set<String> keys = map.keySet();
		for (String Key : keys) {
			Object obj = map.get(Key);
			if (Key.equals(g) || Key.contains(g))
				return true;
			if ((obj instanceof List))
				if (isOK((List<Object>) obj, g))
					return true;
			if ((obj instanceof Map))
				if (isOK((Map<String, Object>) obj, g))
					return true;
			String string = String.valueOf(obj);
			if (string.equals(g) || string.contains(g))
				return true;
			String[] objs = { ItemID.UnknownToName(g, null), Effectrec.UnknownToName(g, null),
					EnchantName.UnknownToName(g, null) };
			for (String string1 : objs) {
				if (string1 == null || string1.isEmpty())
					continue;
				if (string.equals(string1) || string.contains(string1))
					return true;
			}
			objs = new String[] { ItemID.UnknownToName(string, null), Effectrec.UnknownToName(string, null),
					EnchantName.UnknownToName(string, null) };
			for (String string1 : objs) {
				if (string1 == null || string1.isEmpty())
					continue;
				if (g.equals(string1) || g.contains(string1) || string1.equals(g) || string1.contains(g))
					return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否存在！
	 * 
	 * @param list
	 * @param data
	 * @return
	 */
	public static boolean isOK(List<Object> list, String g) {
		for (Object obj : list) {
			if ((obj instanceof List))
				if (isOK((List<Object>) obj, g))
					return true;
			if ((obj instanceof Map))
				if (isOK((Map<String, Object>) obj, g))
					return true;
			String string = String.valueOf(obj);
			if (string.equals(g) || string.contains(g))
				return true;
			String[] objs = { ItemID.UnknownToName(g, null), Effectrec.UnknownToName(g), EnchantName.UnknownToName(g) };
			for (String string1 : objs) {
				if (string1 == null || string1.isEmpty())
					continue;
				if (string.equals(string1) || string.contains(string1))
					return true;
			}
			objs = new String[] { ItemID.UnknownToName(string, null), Effectrec.UnknownToName(string, null),
					EnchantName.UnknownToName(string, null) };
			for (String string1 : objs) {
				if (string1 == null || string1.isEmpty())
					continue;
				if (g.equals(string1) || g.contains(string1) || string1.equals(g) || string1.contains(g))
					return true;
			}
		}
		return false;
	}

	/**
	 * 处理主页的搜索结果
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean ddisSeek(Player player, FormResponseSimple data) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		SeekData data2 = myPlayer.SeekData.get(data.getClickedButtonId());
		return OpenShop.Open(player, data2.file, data2.Key);
	}

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
			case "mydata":
				return DisPlayer.Tip(player);
			}
			player.sendMessage(kick.Message.getSon("界面", "界面显示失败", new String[] { "{Player}", "{Error}" },
					new Object[] { player.getName(), "您的权限不足或要打开的界面不存在！" }));
			return false;
		}
		return xiaokai.bemilk.shop.open.OpenShop.Open(player, myPlayer.file,
				myPlayer.Keys.get(data.getClickedButtonId()));
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
			case "mydata":
				return DisPlayer.Tip(player);
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
		return xiaokai.bemilk.shop.open.OpenShop.ShowShop(player,
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
			case "mydata":
				return DisPlayer.Tip(player);
			default:
				player.sendMessage(kick.Message.getSon("界面", "界面显示失败", new String[] { "{Player}", "{Error}" },
						new Object[] { player.getName(), "您的权限不足或要打开的界面不存在！" }));
				return false;
			}
		player.sendMessage(kick.Message.getSon("界面", "界面显示失败", new String[] { "{Player}", "{Error}" },
				new Object[] { player.getName(), "您的权限不足或要打开的界面不存在！" }));
		return false;
	}

	/**
	 * 构建设置的处理对象
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean SettingSwitch(Player player, FormResponseSimple data) {
		MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
		switch (myPlayer.ExtraKeys.get(data.getClickedButtonId())) {
		case "set":
			myPlayer.baseset = new SetConfig(player);
			break;
		case "item":
			myPlayer.baseset = new CustomItem(player);
			break;
		case "effect":
			myPlayer.baseset = new CustomEffect(player);
			break;
		default:
			return MakeForm.Tip(player, "§4无法获取项目类型！");
		}
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		return myPlayer.baseset.makeMain();
	}
}
