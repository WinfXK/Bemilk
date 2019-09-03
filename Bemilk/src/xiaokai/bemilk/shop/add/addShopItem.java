package xiaokai.bemilk.shop.add;

import xiaokai.bemilk.data.Message;
import xiaokai.bemilk.data.MyPlayer;
import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.shop.add.effect.addEffect;
import xiaokai.tool.Tool;
import xiaokai.tool.form.SimpleForm;

import java.io.File;
import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;

import me.onebone.economyapi.EconomyAPI;

/**
*@author Winfxk
*/
/**
 * 商店添加
 * 
 * @author Winfxk
 */
public class addShopItem {
	private static Kick kick = Kick.kick;
	private static Message msg = Kick.kick.Message;

	/**
	 * 处理主页传回的数据
	 * 
	 * @param player
	 * @param file
	 * @return
	 */
	public static boolean disMakeForm(Player player, FormResponseSimple data) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		switch (Kick.ButtonTypeList[data.getClickedButtonId()]) {
		case "药水效果":
			return addEffect.makeMain(player, myPlayer.file);
		case "工具修复":
			return ItemRepair.MakeMain(player, myPlayer.file);
		case "物品附魔":
			return ItemEnchant.MakeMain(player, myPlayer.file);
		case "物品兑换":
			myPlayer.string = "ItemTradeItem";
			kick.PlayerDataMap.put(player.getName(), myPlayer);
			return ItemTradeItem.MakeForm(player, myPlayer.file);
		case "物品回收":
			myPlayer.string = "ShopOrSell";
			myPlayer.isShopOrSell = false;
			kick.PlayerDataMap.put(player.getName(), myPlayer);
			return ShopOrSell.ShopAndSellMakeForm(player, myPlayer.file);
		case "物品出售":
		default:
			myPlayer.string = "ShopOrSell";
			myPlayer.isShopOrSell = true;
			kick.PlayerDataMap.put(player.getName(), myPlayer);
			return ShopOrSell.ShopAndSellMakeForm(player, myPlayer.file);
		}
	}

	/**
	 * 创建界面填写数据
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
		myPlayer.file = file;
		Config config = new Config(file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(20), kick.Message.getText(config.get("Title"), DsK, DsO),
				Tool.getColorFont("请输入想要添加的商店类型"));
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.addButtons(Kick.ButtonTypeList).sendPlayer(player);
		return true;
	}

	/**
	 * 判断玩家到底点击的是哪个类型的添加方式
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean disShellOrSellMakeForm(Player player, FormResponseSimple data) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		switch (Kick.addShopType[data.getClickedButtonId()]) {
		case "手动输入数据":
			return myPlayer.string.equals("ItemTradeItem") ? ItemTradeItem.InputItem(player)
					: ShopOrSell.InputItem(player);
		case "从背包选择物品":
		default:
			myPlayer.isGetItemSSXXXX = myPlayer.isInventoryGetItem2 = false;
			myPlayer.addIsItem = myPlayer.addIsItemList = myPlayer.addItemTradeItems = new ArrayList<>();
			kick.PlayerDataMap.put(player.getName(), myPlayer);
			return myPlayer.string.equals("ItemTradeItem") ? ItemTradeItem.InventoryGetItem(player)
					: ShopOrSell.InventoryGetItem(player);
		}
	}
}