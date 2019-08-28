package xiaokai.bemilk.shop.myshop;

import xiaokai.bemilk.data.Message;
import xiaokai.bemilk.data.MyPlayer;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.tool.data.ItemID;
import xiaokai.tool.form.CustomForm;
import xiaokai.tool.form.SimpleForm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

import me.onebone.economyapi.EconomyAPI;

/**
 * 创建个人商店相关的界面
 * 
 * @author Winfxk
 */
public class MyShop {
	private static Kick kick = Kick.kick;
	private static Message msg = Kick.kick.Message;

	/**
	 * 在当前页显示个人商店
	 * 
	 * @param player 要显示商店页面的玩家对象
	 * @param file   要显示的文件的位置
	 * @return
	 */
	public static boolean MakeMain(Player player, File file) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.file = file;
		String[] k = { "{Player}", "{Money}" };
		Object[] d = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		Config config = new Config(file, Config.YAML);
		if (!config.getBoolean("isMakeMyShop") && !Kick.isAdmin(player))
			return xiaokai.bemilk.form.MakeForm.Tip(player, msg.getSun("界面", "个人商店", "拒绝创建个人商店", k, d));
		SimpleForm form = new SimpleForm(kick.formID.getID(24), msg.getSun("个人商店", "选择添加物品方式", "标题",
				new String[] { "{Title}" }, new Object[] { kick.Message.getText(config.get("Title"), k, d) }));
		form.addButtons(msg.getSun("个人商店", "选择添加物品方式", "手动输入物品数据", k, d));
		form.addButtons(msg.getSun("个人商店", "选择添加物品方式", "从背包获取添加物品", k, d));
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 当玩家选择的是从背包选择物品
	 * 
	 * @param player
	 * @return
	 */
	public static boolean InventoryGetItem(Player player) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		String[] k = { "{Player}", "{Money}" };
		double Money = EconomyAPI.getInstance().myMoney(player);
		Object[] d = { player.getName(), Money };
		Config config = new Config(myPlayer.file, Config.YAML);
		List<Item> Items = new ArrayList<>();
		List<String> strings = new ArrayList<>();
		Map<Integer, Item> Contents = player.getInventory().getContents();
		String[] k2 = { "{Player}", "{Money}", "{ItemName}", "{ItemID}", "{ItemCount}" };
		List<Integer> integers = new ArrayList<>();
		for (Integer i : Contents.keySet()) {
			Item item = Contents.get(i);
			integers.add(i);
			Items.add(item);
			strings.add(msg.getSun("个人商店", "从背包获取物品", "物品列表格式", k2,
					new Object[] { player.getName(), Money, ItemID.getName(item), item.getId(), item.getCount() }));
		}
		CustomForm form = new CustomForm(kick.formID.getID(26), msg.getSun("个人商店", "从背包获取物品", "标题",
				new String[] { "{Title}" }, new Object[] { kick.Message.getText(config.get("Title"), k, d) }));
		form.addInput(msg.getSun("个人商店", "从背包获取物品", "想要出售或者回收的数量", k, d));
		form.addInput(msg.getSun("个人商店", "从背包获取物品", "输入物品的价格", k, d));
		form.addToggle(msg.getSun("个人商店", "从背包获取物品", "允许分批出售或收购", k, d), true);
		form.addDropdown(msg.getSun("个人商店", "从背包获取物品", "选择个人商店上架方式", k, d), Kick.addMyShopType, 0);
		form.addDropdown(msg.getSun("个人商店", "从背包获取物品", "选择要上架的物品", k, d), strings, 0);
		myPlayer.addIsItemList = Items;
		myPlayer.integers = integers;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 手动输入物品信息
	 * 
	 * @param player
	 * @return
	 */
	public static boolean InputItem(Player player) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		String[] k = { "{Player}", "{Money}" };
		Object[] d = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		Config config = new Config(myPlayer.file, Config.YAML);
		CustomForm form = new CustomForm(kick.formID.getID(25), msg.getSun("个人商店", "手动输入物品数据", "标题",
				new String[] { "{Title}" }, new Object[] { kick.Message.getText(config.get("Title"), k, d) }));
		form.addDropdown(msg.getSun("个人商店", "手动输入物品数据", "选择个人商店上架方式", k, d), Kick.addMyShopType, 0);
		form.addInput(msg.getSun("个人商店", "手动输入物品数据", "输入物品名称", k, d));
		form.addInput(msg.getSun("个人商店", "手动输入物品数据", "想要出售或者回收的数量", k, d));
		form.addInput(msg.getSun("个人商店", "手动输入物品数据", "输入物品的价格", k, d));
		form.addToggle(msg.getSun("个人商店", "手动输入物品数据", "允许分批出售或收购", k, d), true);
		form.sendPlayer(player);
		return true;
	}
}
