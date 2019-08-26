package xiaokai.bemilk.shop;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.onebone.economyapi.EconomyAPI;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Shop {
	/**
	 * 往商店内添加项目
	 * 
	 * @author Winfxk
	 */
	public static class addItem {
		private Map<String, Object> map = new HashMap<String, Object>();
		private Config config;

		/**
		 * 往商店内添加项目
		 * 
		 * @param player 要添加项目的玩家对象
		 * @param file   要添加项目的商店文件对象
		 */
		public addItem(Player player, File file) {
			config = new Config(file, Config.YAML);
			map.put("Player", player.getName());
			map.put("Time", Tool.getDate() + " " + Tool.getTime());
			map.put("Key", getKey(1, file));
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
			map.put("EnchantData", EnchantData);
			map.put("Type", "Enchant");
			map.put("Style", "EnchantByCustom");
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
				Map<String, Map<String, Object>> MoneyItem, int MinCount, int MaxCount, double Money,
				double ItemMoney) {
			map.put("ShopItem", ShopItem);
			map.put("MoneyItem", MoneyItem);
			map.put("MinCount", MinCount);
			map.put("Money", Money);
			map.put("ItemMoney", ItemMoney);
			map.put("MaxCount", MaxCount);
			map.put("Type", "ItemTradeItem");
			return true;
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
			map.put("Items", items);
			map.put("MinCount", MinCount);
			map.put("MaxCount", MaxCount);
			map.put("Type", "Sell");
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
			map.put("Items", items);
			map.put("MinCount", MinCount);
			map.put("MaxCount", MaxCount);
			map.put("Type", "Shop");
			return save();
		}

		/**
		 * 保存数据
		 * 
		 * @return
		 */
		public boolean save() {
			Object obj = config.get("Items");
			Map<String, Object> ShopItems = (obj == null || !(obj instanceof Map)) ? new HashMap<String, Object>()
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
			Map<String, Object> map = (obj == null || !(obj instanceof Map)) ? new HashMap<String, Object>()
					: (HashMap<String, Object>) obj;
			if (map.containsKey(key))
				return getKey(JJLength++, file);
			return key;
		}
	}

	/**
	 * 添加一个商店分页
	 * 
	 * @param player            添加这个商店分页的玩家对象
	 * @param buttonName        添加的商店分页的按钮名称
	 * @param shopTitle         添加的商店分页的标题
	 * @param shopContent       添加的商店分页的文字内容
	 * @param moneyFloor        添加的商店商分页最低要有多少钱才能进入，小于零或者大于最高值不启用该功能
	 * @param moneyLimit        添加的商店分页最高只能有多少钱才能进入，小于零或者小于最小值不启用该功能
	 * @param filterPermissions 过滤玩家权限的模式<b><无|服务器管理员|普通玩家权限><i><0|1|2></i></b>
	 * @param filteredModel     玩家过滤的模式<b><无|白名单|黑名单><i><0|1|2></i></b>
	 * @param filteredList      过滤的列表
	 * @param isMakeMyShop      是否允许玩家在这个页面创建个人商店
	 * @param iconType          这个商店分页的按钮显示的图标模式<b><无|本地图片|网络贴图><i><0|1|2></i></b>
	 * @param iconPath          这个商店分页的按钮的显示图标的路径
	 * @param Key               这个商店分页的按钮的Key
	 * @param isMsg             是否要显示给创建商店分页的玩家对象提示创建信息
	 * @param isMake            玩家是否是创建商店分页
	 * @return
	 */
	public static boolean addShopWindow(Player player, String ButtonName, String ShopTitle, String ShopContent,
			double MoneyFloor, double MoneyLimit, int FilterPermissions, int FilteredModel, List<String> FilteredList,
			boolean isMakeMyShop, int IconType, String IconPath, String FileName, String Key, boolean isMsg,
			boolean isMake) {
		Map<String, Object> item = new HashMap<String, Object>();
		Config config = new Config(new File(new File(Kick.kick.mis.getDataFolder(), Kick.ShopConfigPath), FileName),
				Config.YAML);
		item.put("Text", ButtonName);
		item.put("IconType", IconType);
		item.put("IconPath", IconPath);
		item.put("Key", Key);
		item.put("Config", FileName);
		config.set("Title", ShopTitle);
		config.set("Content", ShopContent);
		config.set("FilterPermissions", FilterPermissions);
		config.set("FilteredModel", FilteredModel);
		config.set("FilterList", FilteredList);
		config.set("MoneyFloor", MoneyFloor);
		config.set("MoneyLimit", MoneyLimit);
		config.set("Key", Key);
		config.set("isMakeMyShop", isMakeMyShop);
		if (isMake) {
			config.set("CreationTime", Tool.getDate() + " " + Tool.getTime());
			config.set("CreationPlayer", player.getName());
		} else {
			config.set("AlterTime", Tool.getDate() + " " + Tool.getTime());
			config.set("AlterPlayer", player.getName());
		}
		config.save();
		Object object = Kick.kick.ShopConfig.get("Shops");
		Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<String, Object>()
				: (HashMap<String, Object>) object;
		Shops.put(Key, item);
		Kick.kick.ShopConfig.set("Shops", Shops);
		Kick.kick.ShopConfig.save();
		if (isMsg)
			player.sendMessage("§6您成功" + (isMake ? "§4创建§6一个商店" : "§9修改§6了这个商店"));
		return true;
	}

	/**
	 * 判断玩家是否有权使用这个商店
	 * 
	 * @param player 要检测判断的玩家对象
	 * @param file   要判断的商店的配置文件
	 * @return
	 */
	public static boolean isOk(Player player, File file) {
		if (Kick.isAdmin(player))
			return true;
		Config config = new Config(file, Config.YAML);
		if (config.getInt("FilterPermissions") == 0 && config.getInt("FilteredModel") == 0)
			return true;
		if (config.getInt("FilterPermissions") == 1 && !player.isOp())
			return false;
		else if (config.getInt("FilterPermissions") == 2 && player.isOp())
			return false;
		List<String> list = config.getList("FilterList");
		if (config.getInt("FilteredModel") == 1 && !list.contains(player.getName()))
			return false;
		if (config.getInt("FilteredModel") == 2 && list.contains(player.getName()))
			return false;
		return true;
	}

	/**
	 * 检查玩家的金币数是不是在限定的范围内
	 * 
	 * @param player 要检查的玩家对象
	 * @param file   要检查的商店文件对象
	 * @return
	 */
	public static boolean isOkMoney(Player player, File file) {
		if (Kick.isAdmin(player))
			return true;
		Config config = new Config(file, Config.YAML);
		double MoneyFloor = config.getDouble("MoneyFloor");
		double MoneyLimit = config.getDouble("MoneyLimit");
		if ((MoneyFloor == 0 && MoneyLimit == 0) || (MoneyFloor < 0 && MoneyLimit < 0) || (MoneyLimit < MoneyFloor))
			return true;
		double Money = EconomyAPI.getInstance().myMoney(player);
		if (Money > MoneyFloor && Money < MoneyLimit)
			return true;
		return false;
	}
}
