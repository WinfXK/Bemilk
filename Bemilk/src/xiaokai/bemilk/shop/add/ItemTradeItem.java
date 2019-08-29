package xiaokai.bemilk.shop.add;

import xiaokai.bemilk.Bemilk;
import xiaokai.bemilk.data.Message;
import xiaokai.bemilk.data.MyPlayer;
import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.shop.addItem;
import xiaokai.tool.Tool;
import xiaokai.tool.data.ItemID;
import xiaokai.tool.form.CustomForm;
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
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

import me.onebone.economyapi.EconomyAPI;

/**
*@author Winfxk
*/
/**
 * 以物换物处理类
 * 
 * @author Winfxk
 */
public class ItemTradeItem {
	private static Kick kick = Kick.kick;
	private static Message msg = Kick.kick.Message;

	/**
	 * 玩家选择的数据页面
	 * 
	 * @param player
	 * @param file
	 * @return
	 */
	public static boolean MakeForm(Player player, File file) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		Config config = new Config(file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(7), kick.Message.getText(config.get("Title"), DsK, DsO),
				Tool.getColorFont("请输入想要添加的方式"));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.string = "ItemTradeItem";
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.addButtons(Kick.addShopType).sendPlayer(player);
		return true;
	}

	/**
	 * 处理新建一个物品兑换的项目并且添加方式为手动输入数据的时候的返回数据
	 * 
	 * @return
	 */
	public static boolean disInputItem(Player player, FormResponseCustom data) {
		String sjString = data.getInputResponse(0);
		if (sjString == null || sjString.isEmpty())
			return MakeForm.Tip(player, "§4请输入想要上架的物品");
		String sxString = data.getInputResponse(0);
		if (sxString == null || sxString.isEmpty())
			return MakeForm.Tip(player, "§4请输入兑换已上架物品所需的物品");
		String[] sjStrings = sjString.split(";");
		Map<String, Map<String, Object>> sjItems = new HashMap<>();
		for (String dgsj : sjStrings) {
			if (dgsj == null || dgsj.isEmpty())
				continue;
			String[] ItemData = dgsj.split(">");
			Item item = ItemID.UnknownToItem(ItemData[0], null);
			if (item == null)
				continue;
			if (ItemData.length > 1)
				item.setCount(Tool.ObjectToInt(ItemData[1], 1));
			item.setCustomName(ItemID.getName(item));
			sjItems.put(ItemID.getID(item), Tool.saveItem(item));
		}
		Map<String, Map<String, Object>> sxItems = new HashMap<>();
		String[] sxStrings = sxString.split(";");
		for (String dgsj : sxStrings) {
			if (dgsj == null || dgsj.isEmpty())
				continue;
			String[] ItemData = dgsj.split(">");
			Item item = ItemID.UnknownToItem(ItemData[0], null);
			if (item == null)
				continue;
			if (ItemData.length > 1)
				item.setCount(Tool.ObjectToInt(ItemData[1], 1));
			item.setCustomName(ItemID.getName(item));
			sxItems.put(ItemID.getID(item), Tool.saveItem(item));
		}
		String intCache = data.getInputResponse(2);
		int MinCount = (Tool.isInteger(intCache) && Float.valueOf(intCache).intValue() > 0)
				? Float.valueOf(intCache).intValue()
				: 1;
		intCache = data.getInputResponse(3);
		int MaxCount = (Tool.isInteger(intCache) && Float.valueOf(intCache).intValue() > 0)
				? Float.valueOf(intCache).intValue()
				: 64;
		intCache = data.getInputResponse(4);
		int Money = (Tool.isInteger(intCache) && Float.valueOf(intCache).intValue() > 0)
				? Float.valueOf(intCache).intValue()
				: 0;
		intCache = data.getInputResponse(5);
		int ItemMoney = (Tool.isInteger(intCache) && Float.valueOf(intCache).intValue() > 0)
				? Float.valueOf(intCache).intValue()
				: 0;
		boolean isOK;
		player.sendMessage("§6您"
				+ ((isOK = new addItem(player, kick.PlayerDataMap.get(player.getName()).file).addItemTradeItem(sjItems,
						sxItems, MinCount, MaxCount, Money, ItemMoney, data.getToggleResponse(6))) ? "§e成功" : "§4未成功")
				+ "§6创建一个物品兑换商店");
		return isOK;
	}

	/**
	 * 当玩家点击的是手动输入物品数据
	 * 
	 * @param player
	 * @return
	 */
	public static boolean InputItem(Player player) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		Config config = new Config(myPlayer.file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		CustomForm form = new CustomForm(kick.formID.getID(12), kick.Message.getText(config.get("Title"), DsK, DsO));
		form.addInput(Tool.getRandColor()
				+ "请输入您想要上架的物品ID/名称\n多个使用；分割\n\n可用格式：\n\n物品ID;物品ID;物品ID\n物品ID>物品数量;物品ID>物品数量\n物品名称;物品名称;物品名称\n物品名称>物品数量;物品名称>物品数量");
		form.addInput(Tool.getRandColor() + "请输入兑换所需的物品ID/名称；格式同上");
		form.addInput(Tool.getRandColor() + "请设定玩家单次购买的最少数\n小于等于零时不启用该功能", "1");
		form.addInput(Tool.getRandColor() + "请设定玩家单次购买的最大数\n小于等于零时不启用该功能", "64");
		form.addInput(Tool.getRandColor() + "请输入每次兑换需要扣除的" + Bemilk.getMoneyName() + "数量", 0, "这个是每次兑换扣除的金币数量");
		form.addInput(Tool.getRandColor() + "请输入兑换每个项目所扣除的" + Bemilk.getMoneyName() + "数量", 0, "这个是按照兑换的数量来扣除金币");
		form.addToggle("严格检查NBt", false);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 处理玩家已经选择好要上架和兑换所需的物品之后输入每次兑换最小数和最大数及其他的数据的界面发回的数据
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean disMakeFormInventoryGetItemIsOKStop(Player player, FormResponseCustom data) {
		String intCache = data.getInputResponse(2);
		int MinCount = (Tool.isInteger(intCache) && Float.valueOf(intCache).intValue() > 0)
				? Float.valueOf(intCache).intValue()
				: 1;
		intCache = data.getInputResponse(3);
		int MaxCount = (Tool.isInteger(intCache) && Float.valueOf(intCache).intValue() > 0)
				? Float.valueOf(intCache).intValue()
				: 64;
		intCache = data.getInputResponse(0);
		int Money = (Tool.isInteger(intCache) && Float.valueOf(intCache).intValue() > 0)
				? Float.valueOf(intCache).intValue()
				: 0;
		intCache = data.getInputResponse(1);
		int ItemMoney = (Tool.isInteger(intCache) && Float.valueOf(intCache).intValue() > 0)
				? Float.valueOf(intCache).intValue()
				: 0;
		Map<String, Map<String, Object>> sxItems = new HashMap<>();
		Map<String, Map<String, Object>> sjItems = new HashMap<>();
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		for (Item item : myPlayer.addIsItem) {
			if (item == null)
				continue;
			sjItems.put(ItemID.getID(item), Tool.saveItem(item));
		}
		for (Item item : myPlayer.addItemTradeItems) {
			if (item == null)
				continue;
			sxItems.put(ItemID.getID(item), Tool.saveItem(item));
		}
		boolean isOK;
		player.sendMessage("§6您" + ((isOK = new addItem(player, myPlayer.file).addItemTradeItem(sjItems, sxItems,
				MinCount, MaxCount, Money, ItemMoney, data.getToggleResponse(4))) ? "§e成功" : "§4未成功") + "§6创建一个物品兑换商店");
		return isOK;
	}

	/**
	 * 当管理员在物品列表点击的是完成按钮要处理的数据，
	 * 
	 * @param player
	 * @return
	 */
	private static boolean MakeFormInventoryGetItemIsOKStop(Player player) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		if (!myPlayer.isGetItemSSXXXX) {
			myPlayer.isGetItemSSXXXX = true;
			myPlayer.isInventoryGetItem2 = false;
			kick.PlayerDataMap.put(player.getName(), myPlayer);
			return InventoryGetItem(player);
		}
		Config config = new Config(myPlayer.file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		CustomForm form = new CustomForm(kick.formID.getID(15), kick.Message.getText(config.get("Title"), DsK, DsO));
		form.addInput(Tool.getRandColor() + "请输入每次兑换需要扣除的" + Bemilk.getMoneyName() + "数量", 0, "这个是每次兑换扣除的金币数量");
		form.addInput(Tool.getRandColor() + "请输入兑换每个项目所扣除的" + Bemilk.getMoneyName() + "数量", 0, "这个是按照兑换的数量来扣除金币");
		form.addInput(Tool.getRandColor() + "请设定玩家单次购买的最少数\n小于等于零时不启用该功能", "1");
		form.addInput(Tool.getRandColor() + "请设定玩家单次购买的最大数\n小于等于零时不启用该功能", "64");
		form.addToggle("严格检查NBt", false);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 处理玩家在设置物品兑换物品界面，玩家选择物品后输入物品数量的界面发回的数据
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean dismakeItemCount(Player player, FormResponseCustom data) {
		String string = data.getInputResponse(0);
		string = (string == null || string.isEmpty()) ? "1" : string;
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		if (myPlayer.CacheItem != null) {
			myPlayer.CacheItem.setCount(
					(Tool.isInteger(string) && Float.valueOf(string).intValue() > 0) ? Float.valueOf(string).intValue()
							: 1);
			(myPlayer.isGetItemSSXXXX ? myPlayer.addItemTradeItems : myPlayer.addIsItem).add(myPlayer.CacheItem);
			kick.PlayerDataMap.put(player.getName(), myPlayer);
		}
		return InventoryGetItem(player);
	}

	/**
	 * 创建一个界面给玩家设置物品的数量
	 * 
	 * @param player
	 * @return
	 */
	private static boolean makeItemCount(Player player, Item item) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		Config config = new Config(myPlayer.file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		CustomForm form = new CustomForm(kick.formID.getID(14), kick.Message.getText(config.get("Title"), DsK, DsO));
		form.addInput("§6请设置§4" + ItemID.getName(item) + "§6的数量", item.getCount(), item.getCount());
		myPlayer.CacheItem = item;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 处理从背包选择物品，显示的物品列表点击后发回的数据
	 * 
	 * @param data
	 * @return
	 */
	public static boolean disInventoryGetItem(Player player, FormResponseSimple data) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		int ID = data.getClickedButtonId();
		if (ID < myPlayer.addIsItemList.size()) {
			return makeItemCount(player, myPlayer.addIsItemList.get(ID));
		} else
			return MakeFormInventoryGetItemIsOKStop(player);
	}

	/**
	 * 当玩家选择的是从背包选择物品
	 * 
	 * @param player
	 * @return
	 */
	public static boolean InventoryGetItem(Player player) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		Config config = new Config(myPlayer.file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID((myPlayer.CacheInt = myPlayer.CacheInt != 13 ? 13 : 21)),
				kick.Message.getText(config.get("Title"), DsK, DsO),
				Tool.getColorFont((myPlayer.isInventoryGetItem2 ? "添加成功！\n\n" : "") + "请选择您"
						+ (myPlayer.isInventoryGetItem2 ? "还" : "") + (myPlayer.isGetItemSSXXXX ? "兑换所需" : "想要上架")
						+ "的物品"));
		if (!myPlayer.isInventoryGetItem2) {
			Map<Integer, Item> Items = player.getInventory().getContents();
			if (Items.size() < 1)
				return MakeForm.Tip(player, "§4???啊铀尅特咪？你特么毛物品没有还想上架？？");
			List<Item> PlayerItems = new ArrayList<>();
			Set<Integer> ike = Items.keySet();
			for (Integer i : ike) {
				Item item = Items.get(i);
				PlayerItems.add(item);
				form.addButton(Tool.getRandColor() + ItemID.getName(item), true, ItemID.getPath(item));
			}
			myPlayer.isInventoryGetItem2 = true;
			myPlayer.addIsItemList = PlayerItems;
		} else {
			for (Item item : myPlayer.addIsItemList)
				form.addButton(Tool.getRandColor() + ItemID.getName(item), true, ItemID.getPath(item));
			form.addButton(Tool.getRandColor() + "完成");
		}
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}
}