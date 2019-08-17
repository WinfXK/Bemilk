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
