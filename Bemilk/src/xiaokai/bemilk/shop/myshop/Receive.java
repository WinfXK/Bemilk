package xiaokai.bemilk.shop.myshop;

import xiaokai.bemilk.MakeForm;
import xiaokai.bemilk.mtp.Belle;
import xiaokai.bemilk.mtp.DisPlayer;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.bemilk.shop.addItem;
import xiaokai.bemilk.tool.ItemID;
import xiaokai.bemilk.tool.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.item.Item;

import me.onebone.economyapi.EconomyAPI;

/**
 * 接收处理个人商店界面发回的数据
 * 
 * @author Winfxk
 */
public class Receive {
	private static Kick kick = Kick.kick;
	private static Message msg = Kick.kick.Message;

	/**
	 * 接收处理玩家选择创建个人商店的添加物品方式
	 * 
	 * @param player
	 * @param data
	 */
	public static void MakeMain(Player player, FormResponseSimple data) {
		if (data.getClickedButtonId() == 0)
			MyShop.InputItem(player);
		else
			MyShop.InventoryGetItem(player);
	}

	/**
	 * 处理玩家是从背包获取物品数据的页面返回的数据
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean InventoryGetItem(Player player, FormResponseCustom data) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		String[] k = { "{Player}", "{Money}" };
		Object[] d = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		int ItemCount = Tool.ObjectToInt(data.getInputResponse(0), 0);
		if (ItemCount <= 0)
			return MakeForm.Tip(player, msg.getSun("个人商店", "从背包获取物品", "物品数量不能小于零", k, d));
		String string = data.getInputResponse(1);
		double Money = string != null && !string.isEmpty() && Tool.isInteger(string) ? Double.valueOf(string) : 0;
		if (Money <= 0)
			return MakeForm.Tip(player, msg.getSun("个人商店", "从背包获取物品", "物品的价格不能小于零", k, d));
		boolean isInt = data.getToggleResponse(2);
		String ShopType = data.getDropdownResponse(3).getElementID() == 0 ? "Shop" : "Sell";
		Item item = myPlayer.addIsItemList.get(data.getDropdownResponse(4).getElementID());
		if (ItemCount > item.getCount())
			return MakeForm.Tip(player, msg.getSun("个人商店", "从背包获取物品", "所选的物品不足", k, d));
		if (ShopType.equals("Shop")) {
			Item sitem = new Item(0);
			if (item.getCount() > ItemCount) {
				sitem = item.clone();
				sitem.setCount(item.getCount() - ItemCount);
			} else if (item.getCount() == ItemCount)
				sitem = new Item(0, 0);
			else if (!Kick.isAdmin(player))
				return MakeForm.Tip(player, msg.getSun("个人商店", "从背包获取物品", "物品不足", k, d));
			if (!Kick.isAdmin(player)) {
				Map<Integer, Item> C = player.getInventory().getContents();
				C.put(myPlayer.integers.get(data.getDropdownResponse(4).getElementID()), sitem);
				player.getInventory().setContents(C);
			}
		} else if (!Kick.isAdmin(player)) {
			double MyMoney = EconomyAPI.getInstance().myMoney(player);
			if (MyMoney < Money)
				return MakeForm.Tip(player, msg.getSun("个人商店", "从背包获取物品", "金币不足", k, d));
			DisPlayer.delMoney(player, Money);
		}
		boolean isok = new addItem(player, myPlayer.file).addMyShop(item, ItemCount, Money, isInt, ShopType);
		player.sendMessage(msg.getSun("个人商店", "从背包获取物品", "物品上架结果", new String[] { "{Player}", "{Money}", "{Result}" },
				new Object[] { player.getName(), EconomyAPI.getInstance().myMoney(player), isok ? "§e成功！" : "§4失败！" }));
		DisPlayer.addSB(player.getName());
		return isok;
	}

	/**
	 * 处理玩家在手动输入物品数据页面返回的数据
	 * 
	 * @param player 玩家对象
	 * @param data
	 * @return
	 */
	public static boolean InputItem(Player player, FormResponseCustom data) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		String[] k = { "{Player}", "{Money}" };
		Object[] d = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		String ShopType = data.getDropdownResponse(0).getElementID() == 0 ? "Shop" : "Sell";
		Item item = ItemID.UnknownToItem(data.getInputResponse(1), null);
		if (item == null)
			return MakeForm.Tip(player, msg.getSun("个人商店", "手动输入物品数据", "输入的物品名称不正确", k, d));
		int ItemCount = Tool.ObjectToInt(data.getInputResponse(2), 0);
		if (ItemCount <= 0)
			return MakeForm.Tip(player, msg.getSun("个人商店", "手动输入物品数据", "物品数量不能小于零", k, d));
		String string = data.getInputResponse(3);
		double Money = string != null && !string.isEmpty() && Tool.isInteger(string) ? Double.valueOf(string) : 0;
		if (Money <= 0)
			return MakeForm.Tip(player, msg.getSun("个人商店", "手动输入物品数据", "物品的价格不能小于零", k, d));
		boolean isInt = data.getToggleResponse(4);
		if (ShopType.equals("Shop")) {
			Map<Integer, Item> C = player.getInventory().getContents();
			int sbByItemCount = 0;
			for (Integer i : C.keySet()) {
				Item item1 = C.get(i);
				if (!Belle.isMaterials(item1) && ItemID.getID(item).equals(ItemID.getID(item1)))
					sbByItemCount++;
			}
			if (!Kick.isAdmin(player) && sbByItemCount < ItemCount)
				return MakeForm.Tip(player, msg.getSun("个人商店", "手动输入物品数据", "物品不足", k, d));
			if (!Kick.isAdmin(player)) {
				int SBPlayer = 0;
				List<Integer> Keys = new ArrayList<>(C.keySet());
				for (int is = 0; is < Keys.size(); is++) {
					Integer i = Keys.get(is);
					Item item1 = C.get(i);
					if (!Belle.isMaterials(item1) && ItemID.getID(item).equals(ItemID.getID(item1)))
						if (item1.getCount() < (ItemCount - SBPlayer)) {
							SBPlayer += item1.getCount();
							C.remove(i);
							continue;
						} else if (item1.getCount() == (ItemCount - SBPlayer)) {
							C.remove(i);
							break;
						} else {
							item1.setCount((ItemCount - SBPlayer));
							C.put(i, item1);
							break;
						}
				}
				player.getInventory().setContents(C);
			}
		} else if (!Kick.isAdmin(player)) {
			double MyMoney = EconomyAPI.getInstance().myMoney(player);
			if (MyMoney < Money)
				return MakeForm.Tip(player, msg.getSun("个人商店", "手动输入物品数据", "金币不足", k, d));
			DisPlayer.delMoney(player, Money);
		}
		boolean isok = new addItem(player, myPlayer.file).addMyShop(item, ItemCount, Money, isInt, ShopType);
		player.sendMessage(msg.getSun("个人商店", "手动输入物品数据", "物品上架结果", new String[] { "{Player}", "{Money}", "{Result}" },
				new Object[] { player.getName(), EconomyAPI.getInstance().myMoney(player), isok ? "§e成功！" : "§4失败！" }));
		DisPlayer.addSB(player.getName());
		return isok;
	}
}
