package xiaokai.acyeterion.mtp;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.Config;
import xiaokai.acyeterion.Acyeterion;
import xiaokai.tool.Tool;
import xiaokai.tool.Update;

/**
 * @author Winfxk
 */
public class Kick {
	public static Kick kick;
	/**
	 * 插件主累对象
	 */
	public Acyeterion mis;
	/**
	 * 插件猪被子文件 <b>config</b></br>
	 * 表单ID配置文件 <b>formIdConfig</b>
	 */
	public Config config, formIdConfig;
	/**
	 * 系统配置文件的文件名
	 */
	public String ConfigName = "Config.yml";
	/**
	 * 商店配置文件名
	 */
	public String ShopConfigName = "Shop.yml";
	/**
	 * 表单ID存储类
	 */
	public FormID formID;
	/**
	 * 消息文件存储文件名
	 */
	public String MsgName = "Message.yml";
	/**
	 * 消息文件类
	 */
	public Message Message;
	/**
	 * 要初始化的表单ID键值
	 */
	public String[] FormIDName = { "主页" };
	/**
	 * 表单ID存储位置
	 */
	public String FormIDConfigName = "FormID.yml";
	/**
	 * 玩家数据库
	 */
	public LinkedHashMap<String, MyPlayer> PlayerDataMap = new LinkedHashMap<String, MyPlayer>();
	/**
	 * 要检查默认设置的配置文件
	 */
	public String[] LoadFileName = { ConfigName, ShopConfigName, MsgName };
	/**
	 * 要检查数据是否匹配的配置文件
	 */
	public String[] isLoadFileName = { ConfigName, MsgName };
	/**
	 * 能创建的商店按钮的类型
	 */
	public static final String[] ButtonTypeList = { "物品出售", "物品回收", "出售经验", "回收经验", "物品兑换" };
	/**
	 * 添加的商店的配置文件存储位置
	 */
	public static final String MenuConfigPath = "Shop/";
	/**
	 * 玩家配置文件存储路径
	 */
	public static final String PlayerConfigPath = "Players/";
	/**
	 * 在启动服务器时检查文件夹是否创建，要检查的列表
	 */
	public static final String[] LoadDirList = { MenuConfigPath, PlayerConfigPath };

	public Kick(Acyeterion knickers) {
		kick = this;
		if (!knickers.getDataFolder().exists())
			knickers.getDataFolder().mkdirs();
		mis = knickers;
		formIdConfig = new Config(new File(knickers.getDataFolder(), FormIDConfigName), Config.YAML);
		(new Belle(this)).start();
		config = new Config(new File(knickers.getDataFolder(), ConfigName), Config.YAML);
		formID = new FormID(this);
		formID.setConfig(formIdConfig.getAll());
		new Thread() {
			@Override
			public void run() {
				super.run();
				while (true) {
					try {
						sleep(Tool.ObjectToInt(kick.config.get("检测更新间隔"), 500) * 1000);
						if (config.getBoolean("检测更新"))
							(new Update(knickers)).start();
					} catch (InterruptedException e) {
						mis.getLogger().warning("自动检查更新遇到错误！" + e.getMessage());
					}
				}
			}
		}.start();
		new Thread() {
			@Override
			public void run() {
				super.run();
				while (true) {
					try {
						Object object = config.get("定时检查快捷工具间隔");
						String s = object == null ? "" : String.valueOf(object);
						int time = Tool.ObjectToInt(s, 60);
						if (time > 0) {
							Map<UUID, Player> Players = Server.getInstance().getOnlinePlayers();
							for (UUID u : Players.keySet()) {
								Player player = Players.get(u);
								if (player.isOnline())
									Belle.exMaterials(player);
							}
						}
						sleep((time < 1 ? 60 : time) * 1000);
					} catch (InterruptedException e) {
						mis.getLogger().warning("自动检查更玩家快捷工具遇到错误！" + e.getMessage());
					}
				}
			}
		}.start();
		Message = new Message(this);
	}
}
