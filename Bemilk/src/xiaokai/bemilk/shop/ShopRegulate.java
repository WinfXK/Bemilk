package xiaokai.bemilk.shop;

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
import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.mtp.ItemID;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.tool.CustomForm;
import xiaokai.tool.SimpleForm;
import xiaokai.tool.Tool;

/**
 * 商店项目管理类
 * 
 * @author Winfxk
 */
public class ShopRegulate {
	private static final String[] addShopType = { "从背包选择物品", "手动输入数据" };
	private static Kick kick;
	private static Message msg = Kick.kick.Message;

	/**
	 * 商店添加
	 * 
	 * @author Winfxk
	 */
	public static class addShop {

		/**
		 * 处理主页传回的数据
		 * 
		 * @param player
		 * @param file
		 * @return
		 */
		public static boolean disMakeForm(Player player, FormResponseSimple data) {
			// { "物品出售", "物品回收", "物品兑换", "物品附魔" ,"工具修复"};
			MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
			switch (Kick.ButtonTypeList[data.getClickedButtonId()]) {
			case "物品回收":
				myPlayer.isShopOrSell = false;
				kick.PlayerDataMap.put(player.getName(), myPlayer);
				return ShopOrSell.ShopAndSellMakeForm(player, myPlayer.file);
			case "物品出售":
			default:
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
			SimpleForm form = new SimpleForm(kick.formID.getID(7), kick.Message.getText(config.get("Title"), DsK, DsO),
					Tool.getColorFont("请输入想要添加的商店类型"));
			kick.PlayerDataMap.put(player.getName(), myPlayer);
			form.addButtons(Kick.ButtonTypeList).sendPlayer(player);
			return true;
		}

		/**
		 * 当添加的商店类型为出售或者回收时的处理类
		 * 
		 * @author Winfxk
		 */
		public static class ShopOrSell {

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
				myPlayer.isInventoryGetItem2 = false;
				kick.PlayerDataMap.put(player.getName(), myPlayer);
				switch (addShopType[data.getClickedButtonId()]) {
				case "手动输入数据":
					return ShopOrSell.InputItem(player);
				case "从背包选择物品":
				default:
					return ShopOrSell.InventoryGetItem(player);
				}
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
				if (string == null || string.isEmpty())
					return MakeForm.Tip(player,
							"§4都说了要输入正确的数值！！\n你这个直接不输入是怎么回事？？\n之前若是还有数据全部作废，\n自己重新再创建一遍项目吧！！！！\n你简直是$￥@~*%");
				double Money = 0;
				if (!Tool.isInteger(string) || ((Money = Double.valueOf(string).intValue()) < 1))
					return MakeForm.Tip(player,
							"§4都说了要输入正确的数值！！\n你这个的不是大于零的纯整数怎么回事？？\n之前若是还有数据全部作废，\n自己重新再创建一遍项目吧！！！！\n你简直是$￥@~*%");
				Map<String, Map<String, Object>> Items = new HashMap<String, Map<String, Object>>();
				for (Item item : myPlayer.addIsItem)
					Items.put(ItemID.getID(item), Tool.saveItem(item));
				boolean isok;
				if (myPlayer.isShopOrSell)
					isok = new Shop.addItem(player, myPlayer.file).addShop(Money, Items);
				else
					isok = new Shop.addItem(player, myPlayer.file).addSell(Money, Items);
				player.sendMessage("§6您" + (isok ? "§9成功" : "§4失败") + "§6了一个"
						+ (myPlayer.isShopOrSell ? "§a出售" : "§c回收") + "§6类型的商店");
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
				CustomForm form = new CustomForm(kick.formID.getID(10),
						kick.Message.getText(config.get("Title"), DsK, DsO));
				form.addInput(Tool.getRandColor() + "请输入玩家购买这个项目所需的" + kick.mis.getName()
						+ "数量\n§4请输入一个正确的数值(大于零的纯整数)！\n§4否则之前的数据将会失效！");
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
				if (string == null || string.isEmpty())
					return MakeForm.Tip(player,
							"§4都说了要输入正确的数值！！\n你这个直接不输入是怎么回事？？\n之前若是还有数据全部作废，\n自己重新再创建一遍项目吧！！！！\n你简直是$￥@~*%");
				int Count = 0;
				if (!Tool.isInteger(string) || ((Count = Float.valueOf(string).intValue()) < 1))
					return MakeForm.Tip(player,
							"§4都说了要输入正确的数值！！\n你这个的不是大于零的纯整数怎么回事？？\n之前若是还有数据全部作废，\n自己重新再创建一遍项目吧！！！！\n你简直是$￥@~*%");
				myPlayer.CacheItem.setCount(Count);
				if (!myPlayer.isInventoryGetItem2)
					myPlayer.addIsItem = new ArrayList<Item>();
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
					return addShop.ShopOrSell.disInventoryGetItemOKMakeMoney(player);
				Item item = myPlayer.addIsItemList.get(ID);
				myPlayer.CacheItem = item;
				Config config = new Config(myPlayer.file, Config.YAML);
				String[] DsK = { "{Player}", "{Money}" };
				Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
				CustomForm form = new CustomForm(kick.formID.getID(9),
						kick.Message.getText(config.get("Title"), DsK, DsO));
				String Color = Tool.getRandColor();
				form.addInput(
						Color + "请输入您想要上架的的" + Tool.getRandColor() + ItemID.getName(item) + Color
								+ "的数量\n§4请输入一个正确的数值(大于零的纯整数)！\n§4否则之前的数据将会失效！",
						item.getCount(), "请输入一个大于零的纯整数(仅支持阿拉伯数字)！");
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
				SimpleForm form = new SimpleForm(kick.formID.getID(8),
						kick.Message.getText(config.get("Title"), DsK, DsO),
						Tool.getColorFont((myPlayer.isInventoryGetItem2 ? "添加成功！\n\n" : "") + "请选择您"
								+ (myPlayer.isInventoryGetItem2 ? "还" : "") + "想要上架的物品"));
				if (!myPlayer.isInventoryGetItem2) {
					Map<Integer, Item> Items = player.getInventory().getContents();
					if (Items.size() < 1)
						return MakeForm.Tip(player, "§4???啊铀尅特咪？你特么毛物品没有还想上架？？");
					List<Item> PlayerItems = new ArrayList<Item>();
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
				SimpleForm form = new SimpleForm(kick.formID.getID(7),
						kick.Message.getText(config.get("Title"), DsK, DsO), Tool.getColorFont("请输入想要添加的方式"));
				form.addButtons(addShopType).sendPlayer(player);
				return true;
			}
		}
	}

	/**
	 * 商店删除
	 * 
	 * @author Winfxk
	 */
	public static class delShop {
		/**
		 * 创建界面选择要删除的按钮
		 * 
		 * @param player
		 * @param file
		 * @return
		 */
		public static boolean MakeForm(Player player, File file) {
			return true;
		}
	}
}
