package xiaokai.bemilk.shop.add;

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
 * 当添加的商店类型为出售或者回收时的处理类
 * 
 * @author Winfxk
 */
public class ShopOrSell {
	private static Kick kick = Kick.kick;
	private static Message msg = Kick.kick.Message;

	/**
	 * 处理玩家手动输入物品数据界面传回的数据
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean disInputItem(Player player, FormResponseCustom data) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		String string = data.getInputResponse(0);
		if (string == null || string.isEmpty())
			return MakeForm.Tip(player, "§4请输入正确的数值！！");
		double Money = 0;
		if (!Tool.isInteger(string) || ((Money = Double.valueOf(string).intValue()) < 1))
			return MakeForm.Tip(player, "§4请输入正确的数值！！");
		String iString = data.getInputResponse(1);
		if (iString == null || iString.isEmpty())
			return MakeForm.Tip(player, "§4请输入物品信息");
		Map<String, Map<String, Object>> Items = new HashMap<>();
		String[] iStrings = iString.split(";");
		for (String string2 : iStrings) {
			if (string2 == null || string2.isEmpty())
				continue;
			String[] countStrings = string2.split(">");
			Item item = ItemID.UnknownToItem(countStrings[0], null);
			if (item == null)
				continue;
			if (countStrings.length > 1)
				item.setCount(Tool.ObjectToInt(countStrings[1], 1));
			item.setCustomName(ItemID.getName(item));
			Items.put(ItemID.getID(item), Tool.saveItem(item));
		}
		int MinCount = Tool.ObjectToInt(data.getInputResponse(2), 1);
		int MaxCount = Tool.ObjectToInt(data.getInputResponse(3), 64);
		boolean isok;
		if (myPlayer.isShopOrSell)
			isok = new addItem(player, myPlayer.file).addShop(Money, Items, MinCount, MaxCount);
		else
			isok = new addItem(player, myPlayer.file).addSell(Money, Items, MinCount, MaxCount);
		player.sendMessage(
				"§6您" + (isok ? "§9成功" : "§4失败") + "§6了一个" + (myPlayer.isShopOrSell ? "§a出售" : "§c回收") + "§6类型的商店");
		return isok;
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
		CustomForm form = new CustomForm(kick.formID.getID(11), kick.Message.getText(config.get("Title"), DsK, DsO));
		form.addInput(Tool.getRandColor() + "§6请输入每份商品的单价");
		form.addInput(Tool.getRandColor()
				+ "请输入您想要上架的物品ID/名称\n多个使用；分割\n\n可用格式：\n\n物品ID;物品ID;物品ID\n物品ID>物品数量;物品ID>物品数量\n物品名称;物品名称;物品名称\n物品名称>物品数量;物品名称>物品数量");
		form.addInput(Tool.getRandColor() + "请设定玩家单次购买的最少数\n小于等于零时不启用该功能", "1");
		form.addInput(Tool.getRandColor() + "请设定玩家单次购买的最大数\n小于等于零时不启用该功能", "64");
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 处理手动选择物品得到最后数据
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean startAddShopItemInventory(Player player, FormResponseCustom data) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		String string = data.getInputResponse(0);
		string = (string == null || string.isEmpty()) ? "1" : string;
		double Money = 0;
		if (!Tool.isInteger(string) || (Money = Double.valueOf(string).intValue()) < 1)
			return MakeForm.Tip(player,
					"§4都说了要输入正确的数值！！\n你这个的不是大于零的纯整数怎么回事？？\n之前若是还有数据全部作废，\n自己重新再创建一遍项目吧！！！！\n你怕不是$￥@~*%");
		int MinCount = Tool.ObjectToInt(data.getInputResponse(1), 1);
		int MaxCount = Tool.ObjectToInt(data.getInputResponse(2), 64);
		Map<String, Map<String, Object>> Items = new HashMap<>();
		for (Item item : myPlayer.addIsItem)
			Items.put(ItemID.getID(item), Tool.saveItem(item));
		boolean isok;
		if (myPlayer.isShopOrSell)
			isok = new addItem(player, myPlayer.file).addShop(Money, Items, MinCount, MaxCount);
		else
			isok = new addItem(player, myPlayer.file).addSell(Money, Items, MinCount, MaxCount);
		player.sendMessage(
				"§6您" + (isok ? "§9成功" : "§4失败") + "§6了一个" + (myPlayer.isShopOrSell ? "§a出售" : "§c回收") + "§6类型的商店");
		return isok;
	}

	/**
	 * 玩家已经从背包选择好了物品，确认无误准备添加
	 * 
	 * @param player
	 * @return
	 */
	public static boolean disInventoryGetItemOKMakeMoney(Player player) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		Config config = new Config(myPlayer.file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		CustomForm form = new CustomForm(kick.formID.getID(10), kick.Message.getText(config.get("Title"), DsK, DsO));
		form.addInput(Tool.getRandColor() + "请输入玩家购买这个项目所需的" + kick.mis.getName()
				+ "数量\n§4请输入一个正确的数值(大于零的纯整数)！\n§4否则之前的数据将会失效！");
		form.addInput(Tool.getRandColor() + "请设定玩家单次购买的最少数\n小于等于零时不启用该功能", "1");
		form.addInput(Tool.getRandColor() + "请设定玩家单次购买的最大数\n小于等于零时不启用该功能", "64");
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 处理玩家输入的物品数量到底是多少，然后继续下一步
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean disInventoryGetItemIsData(Player player, FormResponseCustom data) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		String string = data.getInputResponse(0);
		string = (string == null || string.isEmpty()) ? "1" : string;
		int Count = (!Tool.isInteger(string) || Float.valueOf(string).intValue() < 1) ? 1
				: Float.valueOf(string).intValue();
		myPlayer.CacheItem.setCount(Count);
		if (!myPlayer.isInventoryGetItem2)
			myPlayer.addIsItem = new ArrayList<>();
		myPlayer.addIsItem.add(myPlayer.CacheItem);
		myPlayer.isInventoryGetItem2 = true;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		return ShopOrSell.InventoryGetItem(player);
	}

	/**
	 * 处理玩家点击的数据，这个是来至玩家从背包选择物品，获取的是玩家点击的是哪一个按钮
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean disInventoryGetItem(Player player, FormResponseSimple data) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		int ID = data.getClickedButtonId();
		if ((ID >= myPlayer.addIsItemList.size() && !myPlayer.isInventoryGetItem2)
				|| (myPlayer.isInventoryGetItem2 && ID > myPlayer.addIsItemList.size()))
			return false;
		else if (myPlayer.isInventoryGetItem2 && ID == myPlayer.addIsItemList.size())
			return ShopOrSell.disInventoryGetItemOKMakeMoney(player);
		Item item = myPlayer.addIsItemList.get(ID);
		myPlayer.CacheItem = item;
		Config config = new Config(myPlayer.file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		CustomForm form = new CustomForm(kick.formID.getID(9), kick.Message.getText(config.get("Title"), DsK, DsO));
		String Color = Tool.getRandColor();
		form.addInput(Color + "请输入您想要上架的的" + Tool.getRandColor() + ItemID.getName(item) + Color + "的数量！",
				item.getCount());
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
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		Config config = new Config(myPlayer.file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(8), kick.Message.getText(config.get("Title"), DsK, DsO),
				Tool.getColorFont((myPlayer.isInventoryGetItem2 ? "添加成功！\n\n" : "") + "请选择您"
						+ (myPlayer.isInventoryGetItem2 ? "还" : "") + "想要上架的物品"));
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
			kick.PlayerDataMap.put(player.getName(), myPlayer);
		} else {
			for (Item item : myPlayer.addIsItemList)
				form.addButton(Tool.getRandColor() + ItemID.getName(item), true, ItemID.getPath(item));
			form.addButton(Tool.getRandColor() + "完成");
		}
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 玩家选择了是出售还是回收商店，创建界面填写数据
	 * 
	 * @param player
	 * @param file
	 * @return
	 */
	public static boolean ShopAndSellMakeForm(Player player, File file) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		Config config = new Config(file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(7), kick.Message.getText(config.get("Title"), DsK, DsO),
				Tool.getColorFont("请输入想要添加的方式"));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.addButtons(Kick.addShopType).sendPlayer(player);
		return true;
	}
}