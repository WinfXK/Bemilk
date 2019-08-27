package xiaokai.bemilk.shop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.onebone.economyapi.EconomyAPI;
import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.mtp.ItemID;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.tool.EnchantName;
import xiaokai.tool.SimpleForm;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class OpenShop {
	private static Kick kick = Kick.kick;
	private static Message msg = Kick.kick.Message;

	public static boolean ShowShop(Player player, File file) {
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
		Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<String, Object>()
				: (HashMap<String, Object>) object;
		SimpleForm form = new SimpleForm(kick.formID.getID(2), kick.Message.getText(config.get("Title"), DsK, DsO),
				kick.Message.getText(config.get("Content"), DsK, DsO));
		Set<String> ItemsSet = Shops.keySet();
		List<String> Keys = new ArrayList<String>();
		for (String ike : ItemsSet) {
			Object ob = Shops.get(ike);
			Map<String, Object> ItemAerS = (ob == null || !(ob instanceof Map)) ? new HashMap<String, Object>()
					: (HashMap<String, Object>) ob;
			ItemAerS.put("Key", ike);
			form = getForm(player, form, ItemAerS);
		}
		Keys = form.Keys;
		List<String> AdminList = new ArrayList<String>();
		if (Shops.size() > 0) {
			AdminList.add("seek");
			form.addButton(msg.getSun("界面", "主页", "搜索按钮", DsK, DsO), true, (String) kick.config.get("搜索按钮图标"));
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
		myPlayer.Keys = Keys;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 将数据写入表单
	 * 
	 * @param form 表单对象
	 * @param item 要写入的数据对象
	 * @return
	 */
	public static SimpleForm getForm(Player player, SimpleForm form, Map<String, Object> item) {
		String type = (String) item.get("Type");
		if (item == null || type == null || type.isEmpty())
			return form;
		String[] k;
		Object[] d;
		String Style;
		String Icon;
		switch (type.toLowerCase()) {
		case "shop":
			Map<String, Object> ShopItems = (Map<String, Object>) item.get("Items");
			List<String> ShopItemsKeys = new ArrayList<String>(ShopItems.keySet());
			String ShopItemsID = ShopItemsKeys.get(Tool.getRand(0, ShopItemsKeys.size() - 1));
			k = new String[] { "{Player}", "{Money}", "{ItemName}", "{ItemID}", "{IsMultiMsg}" };
			d = new Object[] { player.getName(), item.get("Money"), ItemID.getNameByID(ShopItemsID), ShopItemsID,
					(ShopItemsKeys.size() > 1) ? ("等§4" + ShopItemsKeys.size() + "§6个物品") : "" };
			form.addButton(msg.getSon("按钮", "物品出售", k, d), true, ItemID.getPathByID(ShopItemsID));
			break;
		case "sell":
			Map<String, Object> Items = (Map<String, Object>) item.get("Items");
			List<String> ItemsKeys = new ArrayList<String>(Items.keySet());
			String ItemsID = ItemsKeys.get(Tool.getRand(0, ItemsKeys.size() - 1));
			k = new String[] { "{Player}", "{Money}", "{ItemName}", "{ItemID}", "{IsMultiMsg}" };
			d = new Object[] { player.getName(), item.get("Money"), ItemID.getNameByID(ItemsID), ItemsID,
					(ItemsKeys.size() > 1) ? ("等§4" + ItemsKeys.size() + "§6个物品") : "" };
			form.addButton(msg.getSon("按钮", "物品回收", k, d), true, ItemID.getPathByID(ItemsID));
			break;
		case "itemtradeitem":
			k = new String[] { "{Player}", "{Money}", "{ItemName}", "{ItemID}", "{ItemsCount}", "{IsMultiMsg}",
					"{ByItemName}", "{ByItemID}", "{ByIsMultiMsg}", "{ByItemsCount}" };
			Map<String, Object> ShopItem = (Map<String, Object>) item.get("ShopItem");
			List<String> ShopItemKeys = new ArrayList<String>(ShopItem.keySet());
			String ShopItemID = ShopItemKeys.get(Tool.getRand(0, ShopItemKeys.size() - 1));
			Map<String, Object> MoneyItem = (Map<String, Object>) item.get("MoneyItem");
			List<String> MoneyItemKeys = new ArrayList<String>(MoneyItem.keySet());
			String MoneyItemID = MoneyItemKeys.get(Tool.getRand(0, MoneyItemKeys.size() - 1));
			d = new Object[] { player.getName(), item.get("Money"), ItemID.getNameByID(ShopItemID, "未知"), ShopItemID,
					ShopItemKeys.size(), (ShopItemKeys.size() > 0) ? ("等§4" + ShopItemKeys.size() + "§6个物品") : "",
					ItemID.getNameByID(MoneyItemID, "未知"), MoneyItemID,
					(MoneyItemKeys.size() > 1) ? ("等§4" + MoneyItemKeys.size() + "§6个物品") : "", MoneyItemKeys.size() };
			form.addButton(msg.getSon("按钮", "物品兑换", k, d), true, ItemID.getPathByID(ShopItemID));
			break;
		case "enchant":
			Style = (String) item.get("Style");
			if (Style == null || Style == null || Style.isEmpty())
				return form;
			switch (Style.toLowerCase()) {
			case "enchantlevel":
				k = new String[] { "{Player}", "{Money}", "{EnchantName}", "{EnchantID}", "{EnchantLevel}" };
				d = new Object[] { player.getName(), item.get("Money"),
						EnchantName.UnknownToName(item.get("EnchantID"), "未知"), item.get("EnchantID"),
						item.get("EnchantLevel") };
				Icon = (String) kick.config.get("物品附魔项目图标");
				form.addButton(msg.getSun("按钮", "物品附魔", "定等附魔", k, d), true, Icon);
				break;
			case "enchantlevelrand":
				int EnchantRand = Tool.ObjectToInt(item.get("EnchantRand"), 100);
				String Success = ((double) ((double) (1 / EnchantRand)) * 100) + "%";
				k = new String[] { "{Player}", "{Money}", "{EnchantName}", "{EnchantID}", "{EnchantLevel}", "{Success}",
						"{SBEnchantID}", "{SBEnchantName}", "{SBEnchantLevel}" };
				d = new Object[] { player.getName(), item.get("Money"),
						EnchantName.UnknownToName(item.get("EnchantID"), "未知"), item.get("EnchantID"),
						item.get("EnchantLevel"), Success, item.get("SBEnchantID"),
						EnchantName.UnknownToName(item.get("SBEnchantID"), "未知"), item.get("SBEnchantLevel") };
				Icon = (String) kick.config.get("物品附魔项目图标");
				form.addButton(msg.getSun("按钮", "物品附魔", "随机定级附魔", k, d), true, Icon);
				break;
			case "enchantbycustom":
				k = new String[] { "{Player}", "{Money}" };
				d = new Object[] { player.getName(), item.get("Money") };
				Icon = (String) kick.config.get("物品附魔项目图标");
				form.addButton(msg.getSun("按钮", "物品附魔", "自定义附魔", k, d), true, Icon);
				break;
			default:
				return form;
			}
			break;
		case "repair":
			Style = (String) item.get("Style");
			if (Style == null || Style == null || Style.isEmpty())
				return form;
			switch (Style.toLowerCase()) {
			case "random":
				int RepairCount = Tool.ObjectToInt(item.get("RepairCount"), 100);
				String Success = ((double) ((double) (1 / RepairCount)) * 100) + "%";
				k = new String[] { "{Player}", "{Money}", "{MinRepair}", "{MaxRepair}", "{Success}", "{SBMinRepair}",
						"{SBMaxRepair}" };
				d = new Object[] { player.getName(), item.get("Money"), item.get("MinRepair"), item.get("MaxRepair"),
						Success, item.get("SBMinRepair"), item.get("SBMaxRepair") };
				Icon = (String) kick.config.get("物品修复项目图标");
				form.addButton(msg.getSun("按钮", "物品修复", "随机增加", k, d), true, Icon);
				break;
			case "soarto":
				k = new String[] { "{Player}", "{Count}", "{Money}" };
				d = new Object[] { player.getName(), item.get("Repair"), item.get("Money") };
				Icon = (String) kick.config.get("物品修复项目图标");
				form.addButton(msg.getSun("按钮", "物品修复", "总量增加", k, d), true, Icon);
				break;
			case "some":
				k = new String[] { "{Player}", "{Count}", "{Money}" };
				d = new Object[] { player.getName(), item.get("Repair"), item.get("Money") };
				Icon = (String) kick.config.get("物品修复项目图标");
				form.addButton(msg.getSun("按钮", "物品修复", "定量增加", k, d), true, Icon);
				break;
			default:
				return form;
			}
			break;
		default:
			return form;
		}
		form.Keys.add((String) item.get("Key"));
		return form;
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
		return true;
	}
}
