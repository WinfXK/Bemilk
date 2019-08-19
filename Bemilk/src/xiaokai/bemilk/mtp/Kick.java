package xiaokai.bemilk.mtp;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.Config;
import xiaokai.bemilk.Bemilk;
import xiaokai.tool.Update;

/**
 * @author Winfxk
 */
public class Kick {
	public static Kick kick;
	/**
	 * 插件主累对象
	 */
	public Bemilk mis;
	/**
	 * 插件猪被子文件 <b>config</b></br>
	 * 表单ID配置文件 <b>formIdConfig</b> </br>
	 * 商店配置文件 <b>ShopConfig</b>
	 */
	public Config config, formIdConfig, ShopConfig;
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
	public String[] FormIDName = { /* 0 */ "主页", /* 1 */"更多设置", /* 2 */"商店分页", /* 3 */"添加分页", /* 4 */ "修改分页",
			/* 5 */ "删除分页", /* 6 */"删除分页确认", /* 7 */"出售回收添加商店项目选择添加方式页", /* 8 */"管理员选择的是从背包选择物品，列出的物品列表界面",
			/* 9 */"管理员选择从背包选择物品，选择后输入上架数量的界面", /* 10 */"选择物品完毕，要求玩家输入项目价格的界面" };
	/**
	 * 表单ID存储位置
	 */
	public String FormIDConfigName = "FormID.yml";
	/**
	 * 用户存储自定义ID名称的地方
	 */
	public String ItemIDConfigName = "ItemID.yml";
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
	 * 过滤玩家进入的模式
	 */
	public static final String[] FilteredModel = { "无", "白名单", "黑名单" };
	/**
	 * 过滤的权限
	 */
	public static final String[] FilterPermissions = { "无", "服务器管理员", "普通玩家" };
	/**
	 * 按钮显示的贴图类型
	 */
	public static final String[] IconType = { "无图标", "本地资源", "网络资源" };
	/**
	 * 能创建的商店按钮的类型
	 */
	public static final String[] ButtonTypeList = { "物品出售", "物品回收", "物品兑换", "物品附魔", "工具修复" };
	/**
	 * 添加的商店的配置文件存储位置
	 */
	public static final String ShopConfigPath = "Shop/";
	/**
	 * 玩家配置文件存储路径
	 */
	public static final String PlayerConfigPath = "Players/";
	/**
	 * 在启动服务器时检查文件夹是否创建，要检查的列表
	 */
	public static final String[] LoadDirList = { ShopConfigPath, PlayerConfigPath };
	/**
	 * 异步线程类
	 */
	public startThread sThread;

	public Kick(Bemilk knickers) {
		kick = this;
		if (!knickers.getDataFolder().exists())
			knickers.getDataFolder().mkdirs();
		mis = knickers;
		formIdConfig = new Config(new File(knickers.getDataFolder(), FormIDConfigName), Config.YAML);
		(new Belle(this)).start();
		config = new Config(new File(knickers.getDataFolder(), ConfigName), Config.YAML);
		ShopConfig = new Config(new File(mis.getDataFolder(), ShopConfigName), Config.YAML);
		formID = new FormID(this);
		formID.setConfig(formIdConfig.getAll());
		Message = new Message(this);
		mis.getLogger().info("§6已加载§9" + ItemID.load() + "§6个物品数据~");
		sThread = new startThread();
		sThread.start();
	}

	/**
	 * 异步类
	 * 
	 * @author Winfxk
	 */
	public class startThread extends Thread {
		public int 定时保存间隔, 定时检查快捷工具间隔, 检测更新间隔;

		public startThread() {
			定时保存间隔 = config.getInt("定时保存间隔");
			定时检查快捷工具间隔 = config.getInt("定时检查快捷工具间隔");
			检测更新间隔 = config.getInt("检测更新间隔");
		}

		public void load() {
			定时保存间隔 = config.getInt("定时保存间隔");
			定时检查快捷工具间隔 = config.getInt("定时检查快捷工具间隔");
			检测更新间隔 = config.getInt("检测更新间隔");
		}

		@Override
		public void run() {
			while (true) {
				try {
					sleep(1000);
					if (config.getBoolean("检测更新")) {
						if (检测更新间隔 < 0) {
							检测更新间隔 = config.getInt("检测更新间隔");
							new Update(mis).start();
						} else
							检测更新间隔--;
					}
					if (定时检查快捷工具间隔 < 0) {
						定时检查快捷工具间隔 = config.getInt("定时检查快捷工具间隔");
						Map<UUID, Player> Players = mis.getServer().getOnlinePlayers();
						Set<UUID> keys = Players.keySet();
						for (UUID id : keys)
							Belle.exMaterials(Players.get(id));
					} else
						定时检查快捷工具间隔--;
					if (定时保存间隔 < 0) {
						定时保存间隔 = config.getInt("定时保存间隔");
						if (mis.getServer().getOnlinePlayers().size() < 1)
							continue;
						mis.getLogger().info("§6正在保存数据");
						Set<String> keys1 = PlayerDataMap.keySet();
						int count = keys1.size(), i = 0;
						for (String player : keys1)
							i += (PlayerDataMap.get(player).config.save() ? 1 : 0);
						mis.getLogger().info("§6保存成功§5" + i + "§6项，失败§4" + (count - i) + "§6项。");
					} else
						定时保存间隔--;
				} catch (InterruptedException e) {
					mis.getLogger().error("§4异步线程类出现问题！" + e.getMessage());
				}
			}
		}
	}

	/**
	 * 判断一个沙雕是不是管理员
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isAdmin(CommandSender player) {
		if (!player.isPlayer())
			return true;
		return isAdmin((Player) player);
	}

	/**
	 * 判断一个沙雕是不是管理员
	 * 
	 * @param player
	 * @return
	 */
	public static boolean isAdmin(Player player) {
		if (player == null)
			return false;
		if (!player.isPlayer())
			return true;
		if (kick.config.getBoolean("管理员白名单")) {
			return kick.config.getList("管理员").contains(player.getName());
		} else
			return player.isOp();
	}
}
