package xiaokai.bemilk.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

import me.onebone.economyapi.EconomyAPI;

import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class DisPlayer {
	/**
	 * 显示个人数据页面
	 * 
	 * @param player
	 * @return
	 */
	public static boolean Tip(Player player) {
		Config config = getConfig(player);
		String Content = Tool.getRandColor() + "总交易额：" + config.getDouble("总交易额") + "\n" + Tool.getRandColor() + "收入："
				+ config.getDouble("收入") + "\n" + Tool.getRandColor() + "支出：" + config.getDouble("支出") + "\n"
				+ Tool.getRandColor() + "人品：" + config.getInt("人品") + "\n" + Tool.getRandColor() + "开店次数："
				+ config.getInt("开店次数");
		return MakeForm.Tip(player, Content, true);
	}

	/**
	 * 开店次数+1
	 * 
	 * @param player
	 * @return
	 */
	public static int addSB(String player) {
		Config config = getConfig(player);
		int is = 0;
		config.set("人品", config.getInt("人品") + 1);
		config.set("开店次数", is = (config.getInt("开店次数") + 1));
		return is;
	}

	/**
	 * 检查个人数据
	 * 
	 * @param player
	 * @return
	 */
	public static boolean inspect(String player) {
		Player isPlayer = Kick.kick.PlayerDataMap.get(player).player;
		String[] k = { "{Player}", "{Money}" };
		Object[] d = { player, EconomyAPI.getInstance().myMoney(player) };
		Config config = getConfig(player);
		Message msg = Kick.kick.Message;
		Object obj = config.get("未售罄商店");
		Map<String, Object> map = (obj == null || !(obj instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) obj;
		List<String> list = new ArrayList<>(map.keySet());
		for (String ike : list) {
			Map<String, Object> map2 = (Map<String, Object>) map.get(ike);
			String File = (String) map2.get("File");
			String isKey = (String) map2.get("Key");
			File file2 = new File(new File(Kick.kick.mis.getDataFolder(), Kick.ShopConfigPath), File);
			Config config1 = new Config(file2, Config.YAML);
			Object object = config1.get("Items");
			Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<>()
					: (HashMap<String, Object>) object;
			if (Shops.size() < 1) {
				delItem(player, File, isKey);
				msg.getSon("个人商店", "无效删除", k, d);
				continue;
			}
			if (!Shops.containsKey(isKey)) {
				delItem(player, File, isKey);
				msg.getSon("个人商店", "无效删除", k, d);
				continue;
			}
			Map<String, Object> map10086 = (Map<String, Object>) Shops.get(isKey);
			int ItemCount = Tool.ObjectToInt(map10086.get("ItemCount"));
			if (ItemCount <= 0)
				if (!Shops.containsKey(isKey)) {
					delItem(player, File, isKey);
					msg.getSon("个人商店", "无效删除", k, d);
					Shops.remove(isKey);
					config1.set("Items", Shops);
					config1.save();
					continue;
				}
		}
		config = getConfig(player);
		List<String> list2 = config.getStringList("云端消息");
		if (list2 != null && list2.size() > 0) {
			for (String Msg : list2)
				isPlayer.sendMessage(Msg);
			config.set("云端消息", new ArrayList<String>());
		}
		Map<String, Object> Items = (Map<String, Object>) config.get("云端仓库");
		if (Items != null && Items.size() > 0) {
			list2 = new ArrayList<>(Items.keySet());
			for (String ike : list2) {
				Item item = Tool.loadItem((Map<String, Object>) Items.get(ike));
				if (item != null)
					isPlayer.getInventory().addItem(item);
			}
			config.set("云端仓库", new HashMap<String, Object>());
		}
		setConfig(player, config);
		return true;
	}

	/**
	 * 发送一个信息给玩家
	 * 
	 * @param player
	 * @param Msg
	 * @return
	 */
	public static boolean addMsgs(String player, String Msg) {
		if (Kick.kick.PlayerDataMap.containsKey(player)) {
			Kick.kick.PlayerDataMap.get(player).player.sendMessage(Msg);
		} else
			DisPlayer.addMsg(player, Msg);
		return true;
	}

	/**
	 * 删除已经售罄的商店
	 * 
	 * @param player
	 * @param file
	 * @param Key
	 * @return
	 */
	public static boolean delItem(String player, File file, String Key) {
		return delItem(player, file.getName(), Key);
	}

	/**
	 * 删除已经售罄的商店
	 * 
	 * @param player
	 * @param file
	 * @param Key
	 * @return
	 */
	public static boolean delItem(String player, String file, String Key) {
		Config config = getConfig(player);
		Object obj = config.get("未售罄商店");
		Map<String, Object> map = (obj == null || !(obj instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) obj;
		List<String> list = new ArrayList<>(map.keySet());
		boolean isok = false;
		for (String ike : list) {
			Map<String, Object> map2 = (Map<String, Object>) map.get(ike);
			String File = (String) map2.get("File");
			String isKey = (String) map2.get("Key");
			if (isok = (File.equals(file) && isKey.equals(Key))) {
				map.remove(map2.get("MyItemKey"));
				break;
			}
		}
		config.set("未售罄商店", map);
		setConfig(player, config);
		return isok;
	}

	/**
	 * 给玩家添加一个未售罄的商店记录
	 * 
	 * @param player
	 */
	public static int addnewShop(CommandSender player, File file, String Key) {
		if (!player.isPlayer())
			return 0;
		return addnewShop(player.getName(), file, Key);
	}

	/**
	 * 给玩家添加一个未售罄的商店记录
	 * 
	 * @param player
	 */
	public static int addnewShop(Player player, File file, String Key) {
		return addnewShop(player.getName(), file, Key);
	}

	/**
	 * 给玩家添加一个未售罄的商店记录
	 * 
	 * @param player
	 */
	public static int addnewShop(String player, File file, String Key) {
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
		config.set("未售罄商店", new HashMap<String, Object>());
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
	 * @param Money
	 * @return
	 */
	public static double addMoney(Player player, double Money) {
		return addMoney(player.getName(), Money);
	}

	/**
	 * 增加玩家收入
	 * 
	 * @param player 要增加的玩家对象
	 * @param Money
	 * @return
	 */
	public static double addMoney(String player, double Money) {
		Config config = getConfig(player);
		double M = config.getDouble("收入") + Money;
		config.set("人品", config.getDouble("人品") + Tool.getRand(1, Double.valueOf(Money).intValue()));
		config.set("收入", M);
		config.set("总交易额", config.getDouble("总交易额") + Money);
		EconomyAPI.getInstance().addMoney(player, Money);
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
	 * @param Money
	 * @return
	 */
	public static double delMoney(String player, double Money) {
		Config config = getConfig(player);
		double M = config.getDouble("支出") + Money;
		config.set("人品", config.getDouble("人品") + Tool.getRand(1, Double.valueOf(Money).intValue()));
		config.set("支出", M);
		config.set("总交易额", config.getDouble("总交易额") + Money);
		EconomyAPI.getInstance().reduceMoney(player, Money);
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
	 * 像玩家云端仓库添加东西
	 * 
	 * @param player
	 * @param item
	 * @return
	 */
	public static boolean addItem(String player, Item item) {
		Config config = getConfig(player);
		Object obj = config.get("云端仓库");
		Map<String, Object> Items = obj == null || !(obj instanceof Map) ? new HashMap<>()
				: (HashMap<String, Object>) obj;
		Items.put(getItemsKey(Items, 1), Tool.saveItem(item));
		config.set("云端仓库", Items);
		setConfig(player, config);
		return true;
	}

	/**
	 * 在玩家云端仓库里面获取一个不重复的Key
	 * 
	 * @param Items
	 * @param JJLength
	 * @return
	 */
	public static String getItemsKey(Map<String, Object> Items, int JJLength) {
		String string = "";
		for (int i = 0; i < JJLength; i++)
			string += Tool.getRandString("qwertyuiop[]asdfghjkl;'.,mnbvcxz/");
		if (Items.containsKey(string))
			return getItemsKey(Items, JJLength++);
		return string;
	}

	/**
	 * 给玩家存储一个云端消息
	 * 
	 * @param player
	 * @param Msg
	 * @return
	 */
	public static boolean addMsg(String player, String Msg) {
		Config config = getConfig(player);
		List<String> list = config.getStringList("云端消息");
		list.add(Msg);
		config.set("云端消息", list);
		setConfig(player, config);
		return true;
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
