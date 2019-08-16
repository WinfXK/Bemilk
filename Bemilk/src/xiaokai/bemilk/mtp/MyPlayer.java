package xiaokai.bemilk.mtp;
/**
 * @author Winfxk
 */

import java.io.File;
import java.time.Instant;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;

public class MyPlayer {
	/**
	 * 玩家上次成功使用快捷菜单的时间
	 */
	public Instant loadTime;
	public Player player;
	/**
	 * 某些界面用来存储给管理员显示的管理员按钮的key
	 */
	public List<String> AdminKeys;
	/**
	 * 存储玩家看到的界面的按钮的Key信息
	 */
	public List<String> Keys;
	/**
	 * 玩家配置文件对象
	 */
	public Config config;
	/**
	 * 当前显示页面的文件对象
	 */
	public File file;

	public MyPlayer(Player player) {
		this.player = player;
		config = new Config(new File(new File(Kick.kick.mis.getDataFolder(), Kick.PlayerConfigPath), player.getName()),
				Config.YAML);
	}
}
