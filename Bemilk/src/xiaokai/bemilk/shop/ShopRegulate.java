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
import xiaokai.bemilk.Bemilk;
import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.mtp.ItemID;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.tool.CustomForm;
import xiaokai.tool.EnchantName;
import xiaokai.tool.SimpleForm;
import xiaokai.tool.Tool;

/**
 * 商店项目管理类
 * 
 * @author Winfxk
 */
public class ShopRegulate {
	private static final String[] addShopType = { "从背包选择物品", "手动输入数据" };
	private static final String[] addItemEnchantType = { "自定义概率", "定级概率", "定级出售" };
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
			case "物品附魔":
				return ItemEnchant.MakeMain(player, myPlayer.file);
			case "物品兑换":
				return ItemTradeItem.MakeForm(player, myPlayer.file);
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
		 * 物品附魔处理类
		 * 
		 * @author Winfxk
		 */
		public static class ItemEnchant {
			/**
			 * 处理附魔项目设置界面回调的数据
			 * 
			 * @author Winfxk
			 */
			public static class Dis {
				private Player player;
				private FormResponseCustom data;
				private MyPlayer myPlayer;

				/**
				 * 处理附魔项目设置界面回调的数据
				 * 
				 * @param player
				 * @param data
				 */
				public Dis(Player player, FormResponseCustom data) {
					this.player = player;
					this.data = data;
					myPlayer = kick.PlayerDataMap.get(player.getName());
				}

				/**
				 * 处理万家添加附魔项目发回的数据
				 * 
				 * @param player
				 * @param data
				 * @return
				 */
				public boolean disMakeForm() {
					switch (myPlayer.string) {
					case "MakeFormLevelRand":
						return MakeFormLevelRand();
					case "MakeFormByCustom":
						return MakeFormByCustom();
					case "MakeFormLevel":
						return MakeFormLevel();
					}
					return true;
				}

				/**
				 * 添加的是定级附魔
				 * 
				 * @return
				 */
				private boolean MakeFormLevel() {
					String string = data.getInputResponse(2);
					if (string == null || string.isEmpty())
						return MakeForm.Tip(player, "§4请输入附魔的价格");
					double Money = 0;
					if (!Tool.isInteger(string) || (Money = Double.valueOf(string)) < 1)
						return MakeForm.Tip(player, "§4附魔价格仅支持大于零的纯数字！");
					int EnchantID = data.getDropdownResponse(0).getElementID();
					int EnchantLevel = data.getDropdownResponse(1).getElementID();
					boolean isTool = data.getToggleResponse(3);
					return new Shop.addItem(player, myPlayer.file).addEnchantLevel(EnchantID, EnchantLevel, Money,
							isTool);
				}

				/**
				 * 添加的是定级但是可以设置概率的附魔
				 * 
				 * @param player
				 * @return
				 */
				private boolean MakeFormLevelRand() {
					String string = data.getInputResponse(5);
					if (string == null || string.isEmpty())
						return MakeForm.Tip(player, "§4请输入附魔的价格");
					double Money = 0;
					if (!Tool.isInteger(string) || (Money = Double.valueOf(string)) < 1)
						return MakeForm.Tip(player, "§4附魔价格仅支持大于零的纯数字！");
					int EnchantID = data.getDropdownResponse(0).getElementID();
					int EnchantLevel = data.getDropdownResponse(1).getElementID();
					string = data.getInputResponse(2);
					if (string == null || string.isEmpty())
						return MakeForm.Tip(player, "§4请输入附魔的概率");
					double EnchantRand = 0;
					if (!Tool.isInteger(string) || (EnchantRand = Double.valueOf(string)) < 1)
						return MakeForm.Tip(player, "§4附魔概率仅支持大于零的纯数字！");
					int SBEnchantID = data.getDropdownResponse(3).getElementID();
					SBEnchantID = (SBEnchantID > (EnchantName.getNameList().size() - 1)) ? -1 : SBEnchantID;
					int SBEnchantLevel = data.getDropdownResponse(4).getElementID();
					boolean isTool = data.getToggleResponse(5);
					return new Shop.addItem(player, myPlayer.file).addEnchantLevelRand(EnchantID, EnchantLevel,
							EnchantRand, SBEnchantID, SBEnchantLevel, isTool, Money);
				}

				/**
				 * 添加的是自定义附魔类型的
				 * 
				 * @param player
				 * @return
				 */
				private boolean MakeFormByCustom() {
					String string = data.getInputResponse(2);
					if (string == null || string.isEmpty())
						return MakeForm.Tip(player, "§4请输入附魔的价格");
					double Money = 0;
					if (!Tool.isInteger(string) || (Money = Double.valueOf(string)) < 1)
						return MakeForm.Tip(player, "§4附魔价格仅支持大于零的纯数字！");
					String EnchantData = data.getInputResponse(0);
					if (EnchantData == null || EnchantData.isEmpty())
						return MakeForm.Tip(player, "§4请输入附魔的数据！");
					boolean isTool = data.getToggleResponse(1);
					String[] EnchantDatas = EnchantData.split(";");
					Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
					for (String EnchData : EnchantDatas) {
						Map<String, Object> item = new HashMap<String, Object>();
						String[] EnchDatas = EnchData.split(">");
						item.put("EnchantID", EnchantName.UnknownToID(EnchDatas.length > 0 ? EnchDatas[0] : null, -1));
						item.put("EnchantLevel", EnchDatas.length > 1
								? (Tool.isInteger(EnchDatas[1]) && Float.valueOf(EnchDatas[1]).intValue() > 0)
										? Float.valueOf(EnchDatas[1]).intValue()
										: 1
								: 1);
						item.put("EnchantRand", EnchDatas.length > 2
								? (Tool.isInteger(EnchDatas[2]) && Float.valueOf(EnchDatas[2]).intValue() > 0)
										? Float.valueOf(EnchDatas[2]).intValue()
										: 10
								: 10);
						map.put(getKey(map, 1), item);
					}
					return new Shop.addItem(player, myPlayer.file).addEnchantByCustom(map, Money, isTool);
				}

				/**
				 * 随机获取一个不重复的附魔的项目Key值
				 * 
				 * @param map      附魔的项目集合
				 * @param JJlength Key的初始长度
				 * @return
				 */
				public static String getKey(Map<String, Map<String, Object>> map, int JJlength) {
					String string = "";
					for (int i = 0; i < JJlength; i++)
						string += Tool.getRandString("qwertyuiopasdfghjklzxcvbnm,./;*+'][=-");
					if (map.containsKey(string))
						return getKey(map, JJlength++);
					return string;
				}
			}

			/**
			 * 添加的是定级附魔
			 * 
			 * @param player
			 * @return
			 */
			private static boolean MakeFormLevel(Player player) {
				MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
				myPlayer.string = "MakeFormLevel";
				Config config = new Config(myPlayer.file, Config.YAML);
				String[] DsK = { "{Player}", "{Money}" };
				Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
				CustomForm form = new CustomForm(kick.formID.getID(17),
						kick.Message.getText(config.get("Title"), DsK, DsO));
				form.addDropdown("请选择要添加的附魔", EnchantName.getNameList());
				form.addDropdown("请选择附魔等级", new String[] { "I", "II", "III", "IV", "V" });
				form.addInput("请输入附魔的价格");
				form.addToggle("允许非工具附魔", true);
				kick.PlayerDataMap.put(player.getName(), myPlayer);
				form.sendPlayer(player);
				return true;
			}

			/**
			 * 添加的是定级但是可以设置概率的附魔
			 * 
			 * @param player
			 * @return
			 */
			private static boolean MakeFormLevelRand(Player player) {
				MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
				myPlayer.string = "MakeFormLevelRand";
				Config config = new Config(myPlayer.file, Config.YAML);
				String[] DsK = { "{Player}", "{Money}" };
				Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
				CustomForm form = new CustomForm(kick.formID.getID(17),
						kick.Message.getText(config.get("Title"), DsK, DsO));
				form.addDropdown("请选择要添加的附魔", EnchantName.getNameList());
				form.addDropdown("请选择附魔等级", new String[] { "I", "II", "III", "IV", "V" });
				form.addInput("请输入成功比\n当输入10时，成功概率为1/10，\n当输入100时，成功概率为1/100,\n以此类推");
				List<String> list = EnchantName.getNameList();
				list.add("无");
				form.addDropdown("请选择失败后的附魔", list, list.size() - 1);
				form.addDropdown("请选择失败后的附魔等级", new String[] { "I", "II", "III", "IV", "V" });
				form.addInput("请输入附魔的价格");
				form.addToggle("允许非工具附魔", true);
				kick.PlayerDataMap.put(player.getName(), myPlayer);
				form.sendPlayer(player);
				return true;
			}

			/**
			 * 添加的是自定义附魔类型的
			 * 
			 * @param player
			 * @return
			 */
			private static boolean MakeFormByCustom(Player player) {
				MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
				myPlayer.string = "MakeFormByCustom";
				Config config = new Config(myPlayer.file, Config.YAML);
				String[] DsK = { "{Player}", "{Money}" };
				Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
				CustomForm form = new CustomForm(kick.formID.getID(17),
						kick.Message.getText(config.get("Title"), DsK, DsO));
				form.addInput(
						"§6请输入附魔数据，多个使用;分割\n§6例(附魔名称、附魔ID均可): \n§6附魔ID§f>§4附魔等级§f>§6概率占比§e;§6附魔ID§f>§4附魔等级§f>§6概率占比§e\n\n"
								+ getEnchantString());
				form.addToggle("允许附魔非工具物品", true);
				form.addInput("请输入附魔的价格");
				kick.PlayerDataMap.put(player.getName(), myPlayer);
				form.sendPlayer(player);
				return true;
			}

			/**
			 * 处理主页发过来的数据，即选择的是啥玩意方式附魔
			 * 
			 * @param player
			 * @param data
			 * @return
			 */
			public static boolean disMakeMain(Player player, FormResponseSimple data) {
				// { "自定义概率", "定级概率", "定级出售" }
				switch (addItemEnchantType[data.getClickedButtonId()]) {
				case "定级概率":
					return MakeFormLevelRand(player);
				case "自定义概率":
					return MakeFormByCustom(player);
				case "定级出售":
				default:
					return MakeFormLevel(player);
				}
			}

			/**
			 * 添加项目的主页
			 * 
			 * @param player
			 * @return
			 */
			public static boolean MakeMain(Player player, File file) {
				if (!Kick.isAdmin(player))
					return MakeForm.Tip(player,
							msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
				Config config = new Config(file, Config.YAML);
				String[] DsK = { "{Player}", "{Money}" };
				Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
				SimpleForm form = new SimpleForm(kick.formID.getID(16),
						kick.Message.getText(config.get("Title"), DsK, DsO), Tool.getColorFont("请输入想要添加的方式"));
				MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
				myPlayer.string = "ShopOrSell";
				kick.PlayerDataMap.put(player.getName(), myPlayer);
				form.addButtons(addItemEnchantType).sendPlayer(player);
				return true;
			}

			/**
			 * 获取一个附魔列表的字符串
			 * 
			 * @return
			 */
			public static String getEnchantString() {
				String string = "";
				List<EnchantName> EnchantS = EnchantName.getAll();
				int a = -1;
				for (EnchantName Enchant : EnchantS)
					string += "§2" + Enchant.getID() + "§f>§5" + Enchant.getName()
							+ (((a++) - 1) % 3 == 0 ? "\n" : "  ");
				return string;
			}
		}

		/**
		 * 以物换物处理类
		 * 
		 * @author Winfxk
		 */
		public static class ItemTradeItem {
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
				SimpleForm form = new SimpleForm(kick.formID.getID(7),
						kick.Message.getText(config.get("Title"), DsK, DsO), Tool.getColorFont("请输入想要添加的方式"));
				MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
				myPlayer.string = "ItemTradeItem";
				kick.PlayerDataMap.put(player.getName(), myPlayer);
				form.addButtons(addShopType).sendPlayer(player);
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
				Map<String, Map<String, Object>> sjItems = new HashMap<String, Map<String, Object>>();
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
				Map<String, Map<String, Object>> sxItems = new HashMap<String, Map<String, Object>>();
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
				return new Shop.addItem(player, kick.PlayerDataMap.get(player.getName()).file).addItemTradeItem(sjItems,
						sxItems, MinCount, MaxCount, Money, ItemMoney);
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
				CustomForm form = new CustomForm(kick.formID.getID(12),
						kick.Message.getText(config.get("Title"), DsK, DsO));
				form.addInput(Tool.getRandColor()
						+ "请输入您想要上架的物品ID/名称\n多个使用；分割\n\n可用格式：\n\n物品ID;物品ID;物品ID\n物品ID>物品数量;物品ID>物品数量\n物品名称;物品名称;物品名称\n物品名称>物品数量;物品名称>物品数量");
				form.addInput(Tool.getRandColor() + "请输入兑换所需的物品ID/名称；格式同上");
				form.addInput(Tool.getRandColor() + "请设定玩家单次购买的最少数\n小于等于零时不启用该功能", "1");
				form.addInput(Tool.getRandColor() + "请设定玩家单次购买的最大数\n小于等于零时不启用该功能", "64");
				form.addInput(Tool.getRandColor() + "请输入每次兑换需要扣除的" + Bemilk.getMoneyName() + "数量", 0, "这个是每次兑换扣除的金币数量");
				form.addInput(Tool.getRandColor() + "请输入兑换每个项目所扣除的" + Bemilk.getMoneyName() + "数量", 0,
						"这个是按照兑换的数量来扣除金币");
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
				Map<String, Map<String, Object>> sxItems = new HashMap<String, Map<String, Object>>();
				Map<String, Map<String, Object>> sjItems = new HashMap<String, Map<String, Object>>();
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
				return new Shop.addItem(player, myPlayer.file).addItemTradeItem(sjItems, sxItems, MinCount, MaxCount,
						Money, ItemMoney);
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
					return InventoryGetItem(player);
				}
				Config config = new Config(myPlayer.file, Config.YAML);
				String[] DsK = { "{Player}", "{Money}" };
				Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
				CustomForm form = new CustomForm(kick.formID.getID(15),
						kick.Message.getText(config.get("Title"), DsK, DsO));
				form.addInput(Tool.getRandColor() + "请输入每次兑换需要扣除的" + Bemilk.getMoneyName() + "数量", 0, "这个是每次兑换扣除的金币数量");
				form.addInput(Tool.getRandColor() + "请输入兑换每个项目所扣除的" + Bemilk.getMoneyName() + "数量", 0,
						"这个是按照兑换的数量来扣除金币");
				form.addInput(Tool.getRandColor() + "请设定玩家单次购买的最少数\n小于等于零时不启用该功能", "1");
				form.addInput(Tool.getRandColor() + "请设定玩家单次购买的最大数\n小于等于零时不启用该功能", "64");
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
					myPlayer.CacheItem.setCount((Tool.isInteger(string) && Float.valueOf(string).intValue() > 0)
							? Float.valueOf(string).intValue()
							: 1);
					(myPlayer.isGetItemSSXXXX ? myPlayer.addItemTradeItems : myPlayer.addIsItem)
							.add(myPlayer.CacheItem);
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
				CustomForm form = new CustomForm(kick.formID.getID(14),
						kick.Message.getText(config.get("Title"), DsK, DsO));
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
				SimpleForm form = new SimpleForm(kick.formID.getID(13),
						kick.Message.getText(config.get("Title"), DsK, DsO),
						Tool.getColorFont((myPlayer.isInventoryGetItem2 ? "添加成功！\n\n" : "") + "请选择您"
								+ (myPlayer.isInventoryGetItem2 ? "还" : "")
								+ (myPlayer.isGetItemSSXXXX ? "兑换所需" : "想要上架") + "的物品"));
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
			switch (addShopType[data.getClickedButtonId()]) {
			case "手动输入数据":
				return myPlayer.string.equals("ItemTradeItem") ? ItemTradeItem.InputItem(player)
						: ShopOrSell.InputItem(player);
			case "从背包选择物品":
			default:
				myPlayer.isGetItemSSXXXX = myPlayer.isInventoryGetItem2 = false;
				myPlayer.addIsItem = myPlayer.addIsItemList = myPlayer.addItemTradeItems = new ArrayList<Item>();
				kick.PlayerDataMap.put(player.getName(), myPlayer);
				return myPlayer.string.equals("ItemTradeItem") ? ItemTradeItem.InventoryGetItem(player)
						: ShopOrSell.InventoryGetItem(player);
			}
		}

		/**
		 * 当添加的商店类型为出售或者回收时的处理类
		 * 
		 * @author Winfxk
		 */
		public static class ShopOrSell {

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
				Map<String, Map<String, Object>> Items = new HashMap<String, Map<String, Object>>();
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
					isok = new Shop.addItem(player, myPlayer.file).addShop(Money, Items, MinCount, MaxCount);
				else
					isok = new Shop.addItem(player, myPlayer.file).addSell(Money, Items, MinCount, MaxCount);
				player.sendMessage("§6您" + (isok ? "§9成功" : "§4失败") + "§6了一个"
						+ (myPlayer.isShopOrSell ? "§a出售" : "§c回收") + "§6类型的商店");
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
				CustomForm form = new CustomForm(kick.formID.getID(11),
						kick.Message.getText(config.get("Title"), DsK, DsO));
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
				Map<String, Map<String, Object>> Items = new HashMap<String, Map<String, Object>>();
				for (Item item : myPlayer.addIsItem)
					Items.put(ItemID.getID(item), Tool.saveItem(item));
				boolean isok;
				if (myPlayer.isShopOrSell)
					isok = new Shop.addItem(player, myPlayer.file).addShop(Money, Items, MinCount, MaxCount);
				else
					isok = new Shop.addItem(player, myPlayer.file).addSell(Money, Items, MinCount, MaxCount);
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
				MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
				myPlayer.string = "ShopOrSell";
				kick.PlayerDataMap.put(player.getName(), myPlayer);
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
