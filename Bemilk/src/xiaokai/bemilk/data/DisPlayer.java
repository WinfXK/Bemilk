package xiaokai.bemilk.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class DisPlayer {
	/**
	 * 给玩家添加一个未售罄的商店记录
	 * 
	 * @param player
	 */
	public static int newShop(CommandSender player, File file, String Key) {
		if (!player.isPlayer())
			return 0;
		return newShop(player.getName(), file, Key);
	}

	/**
	 * 给玩家添加一个未售罄的商店记录
	 * 
	 * @param player
	 */
	public static int newShop(Player player, File file, String Key) {
		return newShop(player.getName(), file, Key);
	}

	/**
	 * 给玩家添加一个未售罄的商店记录
	 * 
	 * @param player
	 */
	public static int newShop(String player, File file, String Key) {
		Config config = getConfig(player);
		int i;
		String ItemKey = getNewShopKey(config, 1);
		Object obj = config.get("未售罄商店");
		Map<String, Object> map = (obj != null && obj instanceof Map) ? (HashMap<String, Object>) config.get("未售罄商店")
				: new HashMap<>();
		config.set("开店次数", i = (config.getInt("开店次数") + 1));
		Map<String, Object> map2 = new HashMap<>();
		map2.put("File", file.getName());
		map2.put("Key", Key);
		map2.put("MyItemKey", ItemKey);
		map.put(ItemKey, map2);
		config.set("未售罄商店", map);
		setConfig(player, config);
		return i;
	}

	/**
	 * 在增加个人商店项目时获取一个不重复的Key
	 * 
	 * @param config   玩家的配置文件
	 * @param JJLength Key的初始长度
	 * @return
	 */
	public static String getNewShopKey(Config config, int JJLength) {
		String string = "";
		for (int i = 0; i < JJLength; i++)
			string += Tool.getRandString("qwertyuiop[]asdfghjkl;zxcvbnm,./");
		Object obj = config.get("未售罄商店");
		Map<String, Object> map = (obj != null && obj instanceof Map) ? (HashMap<String, Object>) config.get("未售罄商店")
				: new HashMap<>();
		return map.containsKey(string) ? getNewShopKey(config, JJLength++) : string;
	}

	/**
	 * 增加玩家收入
	 * 
	 * @param player 要增加的玩家对象
	 * @param Moeny
	 * @return
	 */
	public static double addMoney(String player, double Moeny) {
		Config config = getConfig(player);
		double M;
		config.set("人品", config.getDouble("人品") + Tool.getRand(1, Double.valueOf(Moeny).intValue()));
		config.set("收入", M = (config.getDouble("收入") + Moeny));
		config.set("总交易额", config.getDouble("总交易额") + Moeny);
		setConfig(player, config);
		return M;
	}

	/**
	 * 增加玩家支出
	 * 
	 * @param player 要增加的玩家对象
	 * @param Moeny
	 * @return
	 */
	public static double delMoney(Player player, double Moeny) {
		return delMoney(player.getName(), Moeny);
	}

	/**
	 * 增加玩家支出
	 * 
	 * @param player 要增加的玩家对象
	 * @param Moeny
	 * @return
	 */
	public static double delMoney(String player, double Moeny) {
		Config config = getConfig(player);
		double M;
		config.set("人品", config.getDouble("人品") + Tool.getRand(1, Double.valueOf(Moeny).intValue()));
		config.set("支出", M = (config.getDouble("支出") + Moeny));
		config.set("总交易额", config.getDouble("总交易额") + Moeny);
		setConfig(player, config);
		return M;
	}

	/**
	 * 初始化玩家配置文件
	 * 
	 * @param player 要初始化的玩家对象
	 * @return
	 */
	public static boolean initializePlayerConfig(CommandSender player) {
		return player.isPlayer() ? initializePlayerConfig(player.getName()) : false;
	}

	/**
	 * 初始化玩家配置文件
	 * 
	 * @param player 要初始化的玩家对象
	 * @return
	 */
	public static boolean initializePlayerConfig(Player player) {
		return initializePlayerConfig(player.getName());
	}

	/**
	 * 初始化玩家配置文件
	 * 
	 * @param player 要初始化的玩家对象
	 * @return
	 */
	public static boolean initializePlayerConfig(String player) {
		Config config = new Config(getFile(player), Config.YAML);
		config.set("总交易额", 0);
		config.set("收入", 0);
		config.set("支出", 0);
		config.set("人品", 0);
		config.set("开店次数", 0);
		config.set("未售罄商店", new HashMap<String, Object>());
		config.set("云端仓库", new HashMap<String, Object>());
		config.set("云端消息", new ArrayList<String>());
		setConfig(player, config);
		config.save();
		return true;
	}

	/**
	 * 判断玩家配置文件是否存在
	 * 
	 * @param player 要检查是否存在的玩家
	 * @return
	 */
	public static boolean isConfig(CommandSender player) {
		return player.isPlayer() ? isConfig(player.getName()) : false;
	}

	/**
	 * 判断玩家配置文件是否存在
	 * 
	 * @param player 要检查是否存在的玩家
	 * @return
	 */
	public static boolean isConfig(Player player) {
		return isConfig(player.getName());
	}

	/**
	 * 判断玩家配置文件是否存在
	 * 
	 * @param player 要检查是否存在的玩家名称
	 * @return
	 */
	public static boolean isConfig(String player) {
		return getFile(player).exists();
	}

	/**
	 * 获取一个玩家的配置文件对象
	 * 
	 * @param player
	 * @return
	 */
	public static Config getConfig(CommandSender player) {
		return player.isPlayer() ? getConfig(player.getName()) : null;
	}

	/**
	 * 获取一个玩家的配置文件对象
	 * 
	 * @param player
	 * @return
	 */
	public static Config getConfig(Player player) {
		return getConfig(player.getName());
	}

	/**
	 * 获取一个玩家的配置文件对象
	 * 
	 * @param player
	 * @return
	 */
	public static Config getConfig(String player) {
		MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player);
		if (myPlayer.config == null) {
			myPlayer.config = new Config(getFile(player), Config.YAML);
			Kick.kick.PlayerDataMap.put(player, myPlayer);
		}
		return myPlayer.config;
	}

	/**
	 * 设置一个玩家的配置文件
	 * 
	 * @param player
	 * @param config
	 * @return
	 */
	public static Config setConfig(CommandSender player, Config config) {
		if (!player.isPlayer())
			return null;
		return setConfig(player.getName(), config);
	}

	/**
	 * 设置一个玩家的配置文件
	 * 
	 * @param player
	 * @param config
	 * @return
	 */
	public static Config setConfig(Player player, Config config) {
		return setConfig(player.getName(), config);
	}

	/**
	 * 设置一个玩家的配置文件
	 * 
	 * @param player
	 * @param config
	 * @return
	 */
	public static Config setConfig(String player, Config config) {
		MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player);
		myPlayer.config = config;
		Kick.kick.PlayerDataMap.put(player, myPlayer);
		return config;
	}

	/**
	 * 获取玩家的配置文件的文件对象
	 * 
	 * @param player
	 * @return
	 */
	public static File getFile(CommandSender player) {
		if (!player.isPlayer())
			return null;
		return getFile(player.getName());
	}

	/**
	 * 获取玩家的配置文件的文件对象
	 * 
	 * @param player
	 * @return
	 */
	public static File getFile(Player player) {
		return getFile(player.getName());
	}

	/**
	 * 获取玩家的配置文件的文件对象
	 * 
	 * @param player
	 * @return
	 */
	public static File getFile(String player) {
		return new File(new File(Kick.kick.mis.getDataFolder(), Kick.PlayerConfigPath), player + ".yml");
	}
}
