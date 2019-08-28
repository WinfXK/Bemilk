package xiaokai.bemilk.shop;

import xiaokai.bemilk.data.DisPlayer;
import xiaokai.bemilk.data.MyPlayer;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.tool.Tool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

/**
*@author Winfxk
*/
/**
 * 往商店内添加项目
 * 
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class addItem {
	private Map<String, Object> map = new HashMap<>();
	private Config config;
	private Player player;

	/**
	 * 往商店内添加项目
	 * 
	 * @param player 要添加项目的玩家对象
	 * @param file   要添加项目的商店文件对象
	 */
	public addItem(Player player, File file) {
		config = new Config(file, Config.YAML);
		this.player = player;
		map.put("Player", player.getName());
		map.put("Time", Tool.getDate() + " " + Tool.getTime());
		map.put("Key", getKey(1, file));
	}

	/**
	 * 添加一个个人商店项目
	 * 
	 * @param item      要添加的物品
	 * @param ItemCount 要添加的物品数量
	 * @param Money     交易价格
	 * @param isInt     是否可以分批交易
	 * @param ShopType  商店类型
	 * @return
	 */
	public boolean addMyShop(Item item, int ItemCount, double Money, boolean isInt, String ShopType) {
		MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
		ShopType = ShopType.toLowerCase().equals("sell") ? "Sell" : "Shop";
		item.setCount(ItemCount);
		if (ShopType.equals("Sell"))
			DisPlayer.delMoney(player, Money);
		map.put("ItemCount", ItemCount);
		map.put("Money", Money);
		map.put("isInt", isInt);
		map.put("Item", Tool.saveItem(item));
		map.put("Type", "MyShop");
		map.put("Style", ShopType);
		DisPlayer.newShop(player, myPlayer.file, (String) map.get("Key"));
		return save();
	}

	/**
	 * 添加的商店项目是为手持物品增加或者减少一段特殊值
	 * 
	 * @param Repair 要增加的特殊值大小
	 * @param Money  每次使用的价格
	 * @param isTool 是否非工具可用
	 * @return
	 */
	public boolean addItemRepairSome(int Repair, int Money, boolean isTool) {
		map.put("Repair", Repair);
		map.put("Money", Money);
		map.put("isTool", isTool);
		map.put("Type", "Repair");
		map.put("Style", "Some");
		return save();
	}

	/**
	 * 添加的商店项目是设置手持物品的特殊值为指定值
	 * 
	 * @param Repair 要设置的值
	 * @param Money  每次使用的价格
	 * @param isTool 是否非工具可用
	 * @return
	 */
	public boolean addItemRepairSoarTo(int Repair, int Money, boolean isTool) {
		map.put("Repair", Repair);
		map.put("Money", Money);
		map.put("isTool", isTool);
		map.put("Type", "Repair");
		map.put("Style", "SoarTo");
		return save();
	}

	/**
	 * 添加的项目是随机增加或者减少工具或者物品的特殊值得商店项目
	 * 
	 * @param MinRepair   随机增加得特殊值最小值
	 * @param MaxRepair   随机增加的特殊值最大值
	 * @param RepairCount 能成功的占比
	 * @param SBMinRepair 失败后将会减少的特殊值最小值
	 * @param SBMaxRepair 失败后会减少的特殊值最大值
	 * @param Money       每次使用的价格
	 * @param isTool      非工具是否可以使用
	 * @return
	 */
	public boolean addItemRepairRandom(int MinRepair, int MaxRepair, int RepairCount, int SBMinRepair, int SBMaxRepair,
			int Money, boolean isTool) {
		map.put("MinRepair", MinRepair);
		map.put("MaxRepair", MaxRepair);
		map.put("RepairCount", RepairCount);
		map.put("SBMinRepair", SBMinRepair);
		map.put("SBMaxRepair", SBMaxRepair);
		map.put("Money", Money);
		map.put("isTool", isTool);
		map.put("Type", "Repair");
		map.put("Style", "Random");
		return save();
	}

	/**
	 * 添加的项目是玩家自定义的附魔商店项目
	 * 
	 * @param EnchantData 自定义的数据集
	 * @param Money       附魔的价格
	 * @param isTool      允许非工具附魔
	 * @return
	 */
	public boolean addEnchantByCustom(Map<String, Map<String, Object>> EnchantData, double Money, boolean isTool) {
		map.put("Money", Money);
		map.put("isTool", isTool);
		map.put("Type", "Enchant");
		map.put("Style", "EnchantByCustom");
		map.put("EnchantData", EnchantData);
		return save();
	}

	/**
	 * 添加的项目是定附魔当不确定成功性的商店项目
	 * 
	 * @param EnchantID      要出售的附魔ID
	 * @param EnchantLevel   要出售的附魔的等级
	 * @param EnchantRand    附魔的成功比
	 * @param SBEnchantID    附魔失败后默认要附魔的ID（-1时失败不附魔）
	 * @param SBEnchantLevel 附魔失败后给的附魔的等级
	 * @param isTool         是否可以给非工具物品附魔
	 * @param Money          附魔的价格
	 * @return
	 */
	public boolean addEnchantLevelRand(int EnchantID, int EnchantLevel, double EnchantRand, int SBEnchantID,
			int SBEnchantLevel, boolean isTool, double Money) {
		map.put("EnchantID", EnchantID);
		map.put("EnchantLevel", EnchantLevel);
		map.put("EnchantRand", EnchantRand);
		map.put("SBEnchantID", SBEnchantID);
		map.put("SBEnchantLevel", SBEnchantLevel);
		map.put("Money", Money);
		map.put("isTool", isTool);
		map.put("Type", "Enchant");
		map.put("Style", "EnchantLevelRand");
		return save();
	}

	/**
	 * 添加的项目类型是定等的附魔的项目
	 * 
	 * @param EnchantID    附魔的ID
	 * @param EnchantLevel 附魔的等级
	 * @param Money        附魔的价格
	 * @param isTool       是否可以给非工具物品附魔
	 * @return
	 */
	public boolean addEnchantLevel(int EnchantID, int EnchantLevel, double Money, boolean isTool) {
		map.put("EnchantID", EnchantID);
		map.put("EnchantLevel", EnchantLevel);
		map.put("Money", Money);
		map.put("isTool", isTool);
		map.put("Type", "Enchant");
		map.put("Style", "EnchantLevel");
		return save();
	}

	/**
	 * 添加一个物品兑换物品的商店项目
	 * 
	 * @param ShopItem  可以兑换的物品的列表数据
	 * @param MoneyItem 兑换所需的物品列表
	 * @param MinCount  最少每次兑换多少
	 * @param MaxCount  最多每次兑换多少
	 * @param Money     每次兑换会扣除的金币数量
	 * @param ItemMoney 每次兑换回扣除的金币数量，这个是按照兑换的数量来扣除
	 * @return
	 */
	public boolean addItemTradeItem(Map<String, Map<String, Object>> ShopItem,
			Map<String, Map<String, Object>> MoneyItem, int MinCount, int MaxCount, double Money, double ItemMoney) {
		map.put("MinCount", MinCount);
		map.put("MaxCount", MaxCount);
		map.put("Money", Money);
		map.put("ItemMoney", ItemMoney);
		map.put("Type", "ItemTradeItem");
		map.put("ShopItem", ShopItem);
		map.put("MoneyItem", MoneyItem);
		return save();
	}

	/**
	 * 添加一个回收类型的商店
	 * 
	 * @param Money    项目的价格
	 * @param items    项目所包含的物品列表
	 * @param MinCount 最低兑换数
	 * @param MaxCount 最高兑换数
	 * @return
	 */
	public boolean addSell(double Money, Map<String, Map<String, Object>> items, int MinCount, int MaxCount) {
		map.put("Money", Money);
		map.put("MinCount", MinCount);
		map.put("MaxCount", MaxCount);
		map.put("Type", "Sell");
		map.put("Items", items);
		return save();
	}

	/**
	 * 添加一个出售类型的商店
	 * 
	 * @param Money    项目的价格
	 * @param items    项目所包含的物品列表
	 * @param MinCount 最低兑换数
	 * @param MaxCount 最高兑换数
	 * @return
	 */
	public boolean addShop(double Money, Map<String, Map<String, Object>> items, int MinCount, int MaxCount) {
		map.put("Money", Money);
		map.put("MinCount", MinCount);
		map.put("MaxCount", MaxCount);
		map.put("Type", "Shop");
		map.put("Items", items);
		return save();
	}

	/**
	 * 保存数据
	 * 
	 * @return
	 */
	public boolean save() {
		Object obj = config.get("Items");
		Map<String, Object> ShopItems = (obj == null || !(obj instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) obj;
		ShopItems.put((String) map.get("Key"), map);
		config.set("Items", ShopItems);
		return config.save();
	}

	/**
	 * 添加商店项目的时候获取一个不重复的商店key
	 * 
	 * @param JJLength
	 * @param file
	 * @return
	 */
	public static String getKey(int JJLength, File file) {
		String key = "";
		for (int i = 0; i < JJLength; i++)
			key += Tool.getRandString("qwertyuiop[]asdfghjkl;'\\zxcvbnm,./");
		Config config = new Config(file, Config.YAML);
		Object obj = config.get("Items");
		Map<String, Object> map = (obj == null || !(obj instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) obj;
		if (map.containsKey(key))
			return getKey(JJLength++, file);
		return key;
	}
}