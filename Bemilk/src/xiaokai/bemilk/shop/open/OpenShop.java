package xiaokai.bemilk.shop.open;

import xiaokai.bemilk.MakeForm;
import xiaokai.bemilk.mtp.DisPlayer;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.bemilk.shop.Shop;
import xiaokai.bemilk.shop.open.type.Command;
import xiaokai.bemilk.shop.open.type.Effect;
import xiaokai.bemilk.shop.open.type.Enchant;
import xiaokai.bemilk.shop.open.type.ItemTradeItem;
import xiaokai.bemilk.shop.open.type.MyShop;
import xiaokai.bemilk.shop.open.type.Repair;
import xiaokai.bemilk.shop.open.type.Sell;
import xiaokai.bemilk.tool.Effectrec;
import xiaokai.bemilk.tool.EnchantName;
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
import cn.nukkit.Server;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class OpenShop {
	private static Kick kick = Kick.kick;
	private static Message msg = Kick.kick.Message;

	/**
	 * 开始判断玩家打开的商店是什么类型
	 * 
	 * @param data
	 * @return
	 */
	private static boolean Switch(ShopData data) {
		MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(data.player.getName());
		try {
			switch (data.Type) {
			case "command":
				myPlayer.OpenShopDis = new Command(data);
				break;
			case "effect":
				myPlayer.OpenShopDis = new Effect(data);
				break;
			case "shop":
				myPlayer.OpenShopDis = new xiaokai.bemilk.shop.open.type.Shop(data);
				break;
			case "sell":
				myPlayer.OpenShopDis = new Sell(data);
				break;
			case "itemtradeitem":
				myPlayer.OpenShopDis = new ItemTradeItem(data);
				break;
			case "enchant":
				myPlayer.OpenShopDis = new Enchant(data);
				break;
			case "repair":
				myPlayer.OpenShopDis = new Repair(data);
				break;
			case "myshop":
				myPlayer.OpenShopDis = new MyShop(data);
				break;
			default:
				return MakeForm.Tip(myPlayer.player,
						msg.getSun("界面", "商店分页", "无法获取项目类型", new String[] { "{Player}", "{Money}" },
								new Object[] { data.player.getName(), DisPlayer.getMoney(data.player.getName()) }));
			}
			kick.PlayerDataMap.put(data.player.getName(), myPlayer);
			return myPlayer.OpenShopDis.MakeMain();
		} catch (Exception e) {
			e.printStackTrace();
			return MakeForm.Tip(data.player, msg.getSon("界面", "商店项目打开失败",
					new String[] { "{Player}", "{Money}", "{Error}" },
					new Object[] { data.player.getName(), DisPlayer.getMoney(data.player.getName()), e.getMessage() }));
		}
	}

	/**
	 * 当在商店分页打开一个项目
	 * 
	 * @param player 打开项目的玩家对象
	 * @param file   要打开的项目所在的文件对象
	 * @param Key    打开的项目Key
	 * @return
	 */
	public static boolean Open(Player player, File file, String Key) {
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), DisPlayer.getMoney(player.getName()) };
		if (!Kick.isAdmin(player) && kick.config.getBoolean("限制创造模式使用商店") && player.getGamemode() == 1)
			return MakeForm.Tip(player, msg.getSon("界面", "限制创造模式使用商店", DsK, DsO));
		Config config = new Config(file, Config.YAML);
		Object object = config.get("Items");
		Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) object;
		if (Shops.size() < 1)
			return MakeForm.Tip(player, msg.getSun("界面", "商店分页", "不存在项目", DsK, DsO));
		if (!Shops.containsKey(Key))
			return MakeForm.Tip(player, msg.getSun("界面", "商店分页", "打开的项目不存在", DsK, DsO));
		Object ob = Shops.get(Key);
		Map<String, Object> Item = (ob == null || !(ob instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) ob;
		if (Item.size() < 1)
			return MakeForm.Tip(player, msg.getSun("界面", "商店分页", "打开的项目数据错误", DsK, DsO));
		if (Item.get("Type") == null)
			return MakeForm.Tip(player, msg.getSun("界面", "商店分页", "无法获取项目类型", DsK, DsO));
		ShopData data = new ShopData();
		data.Type = Item.get("Type").toString().toLowerCase();
		data.config = config;
		data.file = file;
		data.Item = Item;
		data.Shops = Shops;
		data.player = player;
		data.Key = Key;
		return Switch(data);
	}

	/**
	 * 打开一个商店分页
	 * 
	 * @param player 要显示商店分页的玩家对象
	 * @param file   要显示的商店分页的玩家对象
	 * @return
	 */
	public static boolean ShowShop(Player player, File file) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		Config config = new Config(file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), DisPlayer.getMoney(player.getName()) };
		if (!Shop.isOk(player, file))
			return MakeForm.Tip(player, msg.getSun("界面", "商店分页", "被过滤提示", DsK, DsO));
		if (!Shop.isOkMoney(player, file))
			return MakeForm.Tip(player,
					kick.Message.getSun("界面", "商店分页", "被过滤提示",
							new String[] { "{Player}", "{Money}", "{MoneyFloor}", "{MoneyLimit}" },
							new Object[] { player.getName(), DisPlayer.getMoney(player.getName()),
									config.getDouble("MoneyFloor"), config.getDouble("MoneyLimit") }));
		Object object = config.get("Items");
		Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) object;
		SimpleForm form = new SimpleForm(kick.formID.getID(2), kick.Message.getText(config.get("Title"), DsK, DsO),
				kick.Message.getText(config.get("Content"), DsK, DsO));
		Set<String> ItemsSet = Shops.keySet();
		for (String ike : ItemsSet) {
			Object ob = Shops.get(ike);
			Map<String, Object> ItemAerS = (ob == null || !(ob instanceof Map)) ? new HashMap<>()
					: (HashMap<String, Object>) ob;
			ItemAerS.put("Key", ike);
			form = getForm(player, form, ItemAerS, kick.config.getBoolean("显示错误按钮"));
		}
		List<String> AdminList = new ArrayList<>();
		if (Shops.size() > 0) {
			AdminList.add("seek");
			form.addButton(msg.getSun("界面", "主页", "搜索按钮", DsK, DsO), true, (String) kick.config.get("搜索按钮图标"));
		}
		if (config.getBoolean("isMakeMyShop") || Kick.isAdmin(player)) {
			AdminList.add("myshop");
			form.addButton(msg.getSun("界面", "个人商店", "新建个人商店", DsK, DsO), true, (String) kick.config.get("个人商店创建按钮图标"));
		}
		if (Kick.isAdmin(player)) {
			AdminList.add("add");
			form.addButton(Tool.getRandColor() + "添加商店");
			if (Shops.size() > 0) {
				AdminList.add("del");
				form.addButton(Tool.getRandColor() + "删除商店");
			}
		}
		myPlayer.ExtraKeys = AdminList;
		myPlayer.file = file;
		myPlayer.Keys = form.Keys;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 将数据写入表单
	 * 
	 * @param player 打开商店的玩家对象
	 * @param form   表单对象
	 * @param item   要写入的数据对象
	 * @return
	 */
	public static SimpleForm getForm(Player player, SimpleForm form, Map<String, Object> item) {
		return getForm(player, form, item, false);
	}

	/**
	 * 将数据写入表单
	 * 
	 * @param player 打开商店的玩家对象
	 * @param form   表单对象
	 * @param item   要写入的数据对象
	 * @param isBS   是否显示错误按钮
	 * @return
	 */
	public static SimpleForm getForm(Player player, SimpleForm form, Map<String, Object> item, boolean isBS) {
		String type = (String) item.get("Type");
		if (type == null || type.isEmpty())
			return form;
		String[] k;
		Object[] d;
		int ID;
		try {
			List<String> strings;
			String Style, Icon, ike;
			Object ItemMoney = Float.valueOf(String.valueOf(item.get("Money"))) <= 0 ? "不需要" : item.get("Money");
			switch (type.toLowerCase()) {
			case "command":
				Object obj = item.get("ButtonContent");
				String Content = Tool.objToString(obj, "");
				if (obj == null || Content.isEmpty())
					Content = kick.config.getString("命令商店显示内容");
				obj = item.get("Command");
				String Command = Tool.objToString(obj, "");
				if (obj == null || Command.isEmpty())
					kick.mis.getLogger().warning("无法获取命令项数据！" + item);
				int CommandSender = Tool.ObjectToInt(item.get("CommandSender"), 0);
				Icon = kick.config.getString("命令商店按钮图标");
				strings = new ArrayList<>();
				for (Player player2 : Server.getInstance().getOnlinePlayers().values())
					if (player2.isOnline())
						strings.add(player2.getName());
				k = new String[] { "{Player}", "{Money}", "{Players}" };
				d = new Object[] { player.getName(), ItemMoney,
						strings.get(Tool.getRand(0, strings.size() - 1)) + "等" + strings.size() + "个玩家" };
				Command = msg.getText(Command, k, d);
				k = new String[] { "{Player}", "{Money}", "{Permission}", "{Command}" };
				d = new Object[] { player.getName(), ItemMoney,
						xiaokai.bemilk.shop.add.Command.getPermission(CommandSender), Command };
				ike = msg.getText(Content, k, d);
				form.addButton(ike, true, Icon);
				break;
			case "effect":
				Style = (String) item.get("Style");
				if (Style == null || Style.isEmpty())
					return form;
				switch (Style.toLowerCase()) {
				case "iseffect":
					k = new String[] { "{Player}", "{Money}", "{EffectName}", "{EffectID}", "{EffectLevel}",
							"{LastTimes}" };
					ID = Tool.ObjectToInt(item.get("ID"));
					ike = msg.getSun("按钮", "药水效果", "效果", k, new Object[] { player.getName(), ItemMoney,
							Effectrec.getName(ID), ID, item.get("Level"), item.get("EffectTime") });
					form.addButton(ike, true, Effectrec.getPath(ID));
					break;
				case "randeffect":
					k = new String[] { "{Player}", "{Money}", "{EffectName}", "{EffectCount}", "{EffectLevel}",
							"{LastTimes}" };
					List<Integer> IDs = (List<Integer>) item.get("ID");
					ID = IDs.get(Tool.getRand(0, IDs.size() - 1));
					ike = msg.getSun("按钮", "药水效果", "随机效果", k, new Object[] { player.getName(), ItemMoney,
							Effectrec.getName(ID), IDs.size(), item.get("Level"), item.get("EffectTime") });
					form.addButton(ike, true, Effectrec.getPath(ID));
					break;
				case "randtime":
					ID = Tool.ObjectToInt(item.get("ID"));
					k = new String[] { "{Player}", "{Money}", "{EffectName}", "{EffectID}", "{EffectLevel}",
							"{MinTime}", "{MaxTime}" };
					ike = msg.getSun("按钮", "药水效果", "随机时间", k, new Object[] { player.getName(), ItemMoney,
							Effectrec.getName(ID), ID, item.get("Level"), item.get("MinTime"), item.get("MaxTime") });
					form.addButton(ike, true, Effectrec.getPath(ID));
					break;
				case "customeffect":
					Map<String, Object> Effects = (Map<String, Object>) item.get("Effects");
					strings = new ArrayList<>(Effects.keySet());
					ike = strings.get(Tool.getRand(0, strings.size() - 1));
					ID = Tool.ObjectToInt(((Map<String, Object>) Effects.get(ike)).get("ID"));
					k = new String[] { "{Player}", "{Money}", "{EffectName}", "{EffectCount}" };
					ike = msg.getSun("按钮", "药水效果", "自定义效果", k,
							new Object[] { player.getName(), ItemMoney, Effectrec.getName(ID), Effects.size() });
					form.addButton(ike, true, Effectrec.getPath(ID));
					break;
				default:
					if (isBS) {
						form.Keys.add((String) item.get("Key"));
						form.addButton(msg.getSon("界面", "错误按钮文本"));
					}
					return form;
				}
				break;
			case "myshop":
				Item item2 = Tool.loadItem((Map<String, Object>) item.get("Item"));
				k = new String[] { "{Player}", "{Money}", "{ShopType}", "{ItemCount}", "{ItemName}", "{ItemID}" };
				ike = String.valueOf(item.get("Style")).toLowerCase().equals("sell") ? "回收" : "出售";
				form.addButton(
						msg.getSon("按钮", "个人商店", k, new Object[] { player.getName(), ItemMoney, ike,
								item.get("ItemCount"), ItemID.getName(item2), ItemID.getID(item2) }),
						true, ItemID.getPath(item2));
				break;
			case "shop":
				Map<String, Object> ShopItems = (Map<String, Object>) item.get("Items");
				List<String> ShopItemsKeys = new ArrayList<>(ShopItems.keySet());
				String ShopItemsID = ShopItemsKeys.get(Tool.getRand(0, ShopItemsKeys.size() - 1));
				Item item3 = Tool.loadItem((Map<String, Object>) ShopItems.get(ShopItemsID));
				k = new String[] { "{Player}", "{Money}", "{ItemName}", "{ItemID}", "{IsMultiMsg}" };
				d = new Object[] { player.getName(), ItemMoney, ItemID.getName(item3), ShopItemsID,
						(ShopItemsKeys.size() > 1) ? ("等§4" + ShopItemsKeys.size() + "§6个物品") : "" };
				form.addButton(msg.getSon("按钮", "物品出售", k, d), true, ItemID.getPathByID(ShopItemsID));
				break;
			case "sell":
				Map<String, Object> Items = (Map<String, Object>) item.get("Items");
				List<String> ItemsKeys = new ArrayList<>(Items.keySet());
				String ItemsID = ItemsKeys.get(Tool.getRand(0, ItemsKeys.size() - 1));
				Item item31 = Tool.loadItem((Map<String, Object>) Items.get(ItemsID));
				k = new String[] { "{Player}", "{Money}", "{ItemName}", "{ItemID}", "{IsMultiMsg}" };
				d = new Object[] { player.getName(), ItemMoney, ItemID.getName(item31), ItemsID,
						(ItemsKeys.size() > 1) ? ("等§4" + ItemsKeys.size() + "§6个物品") : "" };
				form.addButton(msg.getSon("按钮", "物品回收", k, d), true, ItemID.getPathByID(ItemsID));
				break;
			case "itemtradeitem":
				k = new String[] { "{Player}", "{Money}", "{ItemName}", "{ItemID}", "{ItemsCount}", "{IsMultiMsg}",
						"{ByItemName}", "{ByItemID}", "{ByIsMultiMsg}", "{ByItemsCount}" };
				Map<String, Object> ShopItem = (Map<String, Object>) item.get("ShopItem");
				List<String> ShopItemKeys = new ArrayList<>(ShopItem.keySet());
				String ShopItemID = ShopItemKeys.get(Tool.getRand(0, ShopItemKeys.size() - 1));
				Map<String, Object> MoneyItem = (Map<String, Object>) item.get("MoneyItem");
				List<String> MoneyItemKeys = new ArrayList<>(MoneyItem.keySet());
				String MoneyItemID = MoneyItemKeys.get(Tool.getRand(0, MoneyItemKeys.size() - 1));
				Item Shopitem311 = Tool.loadItem((Map<String, Object>) ShopItem.get(ShopItemID));
				Item Moneyitem311 = Tool.loadItem((Map<String, Object>) MoneyItem.get(MoneyItemID));
				d = new Object[] { player.getName(), ItemMoney, ItemID.getName(Shopitem311), ShopItemID,
						ShopItemKeys.size(), (ShopItemKeys.size() > 0) ? ("等§4" + ShopItemKeys.size() + "§6个物品") : "",
						ItemID.getName(Moneyitem311), MoneyItemID,
						(MoneyItemKeys.size() > 1) ? ("等§4" + MoneyItemKeys.size() + "§6个物品") : "",
						MoneyItemKeys.size() };
				form.addButton(msg.getSon("按钮", "物品兑换", k, d), true, ItemID.getPathByID(ShopItemID));
				break;
			case "enchant":
				Style = (String) item.get("Style");
				if (Style == null || Style.isEmpty())
					return form;
				switch (Style.toLowerCase()) {
				case "enchantlevel":
					k = new String[] { "{Player}", "{Money}", "{EnchantName}", "{EnchantID}", "{EnchantLevel}" };
					d = new Object[] { player.getName(), ItemMoney,
							EnchantName.UnknownToName(item.get("EnchantID"), "未知"), item.get("EnchantID"),
							item.get("EnchantLevel") };
					Icon = (String) kick.config.get("物品附魔项目图标");
					form.addButton(msg.getSun("按钮", "物品附魔", "定等附魔", k, d), true, Icon);
					break;
				case "enchantlevelrand":
					int EnchantRand = Tool.ObjectToInt(item.get("EnchantRand"), 100);
					String Success = ((1 / EnchantRand) * 100) + "%";
					k = new String[] { "{Player}", "{Money}", "{EnchantName}", "{EnchantID}", "{EnchantLevel}",
							"{Success}", "{SBEnchantID}", "{SBEnchantName}", "{SBEnchantLevel}" };
					d = new Object[] { player.getName(), ItemMoney,
							EnchantName.UnknownToName(item.get("EnchantID"), "未知"), item.get("EnchantID"),
							item.get("EnchantLevel"), Success, item.get("SBEnchantID"),
							EnchantName.UnknownToName(item.get("SBEnchantID"), "未知"), item.get("SBEnchantLevel") };
					Icon = (String) kick.config.get("物品附魔项目图标");
					form.addButton(msg.getSun("按钮", "物品附魔", "随机定级附魔", k, d), true, Icon);
					break;
				case "enchantbycustom":
					k = new String[] { "{Player}", "{Money}" };
					d = new Object[] { player.getName(), ItemMoney };
					Icon = (String) kick.config.get("物品附魔项目图标");
					form.addButton(msg.getSun("按钮", "物品附魔", "自定义附魔", k, d), true, Icon);
					break;
				default:
					if (isBS) {
						form.Keys.add((String) item.get("Key"));
						form.addButton(msg.getSon("界面", "错误按钮文本"));
					}
					return form;
				}
				break;
			case "repair":
				Style = (String) item.get("Style");
				if (Style == null || Style.isEmpty())
					return form;
				switch (Style.toLowerCase()) {
				case "random":
					int RepairCount = Tool.ObjectToInt(item.get("RepairCount"), 100);
					String Success = ((1 / RepairCount) * 100) + "%";
					k = new String[] { "{Player}", "{Money}", "{MinRepair}", "{MaxRepair}", "{Success}",
							"{SBMinRepair}", "{SBMaxRepair}" };
					d = new Object[] { player.getName(), ItemMoney, item.get("MinRepair"), item.get("MaxRepair"),
							Success, item.get("SBMinRepair"), item.get("SBMaxRepair") };
					Icon = (String) kick.config.get("物品修复项目图标");
					form.addButton(msg.getSun("按钮", "物品修复", "随机增加", k, d), true, Icon);
					break;
				case "soarto":
					k = new String[] { "{Player}", "{Count}", "{Money}" };
					d = new Object[] { player.getName(), item.get("Repair"), ItemMoney };
					Icon = (String) kick.config.get("物品修复项目图标");
					form.addButton(msg.getSun("按钮", "物品修复", "总量增加", k, d), true, Icon);
					break;
				case "some":
					k = new String[] { "{Player}", "{Count}", "{Money}" };
					d = new Object[] { player.getName(), item.get("Repair"), ItemMoney };
					Icon = (String) kick.config.get("物品修复项目图标");
					form.addButton(msg.getSun("按钮", "物品修复", "定量增加", k, d), true, Icon);
					break;
				default:
					form.Keys.add((String) item.get("Key"));
					return isBS ? form.addButton(msg.getSon("界面", "错误按钮文本")) : form;
				}
				break;
			default:
				if (isBS) {
					form.Keys.add((String) item.get("Key"));
					form.addButton(msg.getSon("界面", "错误按钮文本"));
				}
				return form;
			}
		} catch (Exception e) {
			k = new String[] { "{Player}", "{Money}" };
			d = new Object[] { player.getName(), DisPlayer.getMoney(player.getName()) };
			e.printStackTrace();
			kick.mis.getLogger()
					.error("§4出现错误！一个项目可能没有正常显示！请检查您的数据！\n错误数据：" + e.getMessage() + "\n项目数据：" + item.toString());
			player.sendMessage(msg.getSon("界面", "项目错误", k, d));
			if (isBS) {
				form.Keys.add((String) item.get("Key"));
				form.addButton(msg.getSon("界面", "错误按钮文本"));
			}
			return form;
		}
		form.Keys.add((String) item.get("Key"));
		return form;
	}
}
