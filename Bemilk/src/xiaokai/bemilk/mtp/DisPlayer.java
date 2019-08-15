package xiaokai.bemilk.mtp;

import java.io.File;
import java.util.ArrayList;
import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
public class DisPlayer {
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
		config.set("收入", M = config.getDouble("收入") + Moeny);
		config.set("总交易额", config.getDouble("总交易额") + Moeny);
		config.save();
		return M;
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
		config.set("支出", M = config.getDouble("支出") + Moeny);
		config.set("总交易额", config.getDouble("总交易额") + Moeny);
		config.save();
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
		Config config = getConfig(player);
		config.set("总交易额", 0);
		config.set("收入", 0);
		config.set("支出", 0);
		config.set("人品", 0);
		config.set("开店次数", 0);
		config.set("为售罄商店", new ArrayList<String>());
		config.set("云端仓库", new ArrayList<String>());
		config.set("云端消息", new ArrayList<String>());
		return config.save();
	}

	/**
	 * 判断玩家配置文件是否存在
	 * 
	 * @param player 要检查是否存在的玩家
	 * @return
	 */
	public static boolean isConfig(CommandSender player) {
		return player.isPlayer() ? isConfig(player.getName()) : null;
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
		File file = new File(new File(Kick.kick.mis.getDataFolder(), Kick.PlayerConfigPath), player);
		return file.isFile();
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
			myPlayer.config = new Config(
					new File(new File(Kick.kick.mis.getDataFolder(), Kick.PlayerConfigPath), player), Config.YAML);
			Kick.kick.PlayerDataMap.put(player, myPlayer);
		}
		return myPlayer.config;
	}
}
