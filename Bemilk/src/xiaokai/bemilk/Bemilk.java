package xiaokai.bemilk;

import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.bemilk.tool.Tool;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;

/**
 * @author Winfxk
 */
public class Bemilk extends PluginBase {
	public static void main(String[] args) {
		System.out.println("\n尊敬的 先生/女士 您好！\n    首先感谢您使用本“程序”，但是很遗憾的告诉您，本程序需要依赖一款名为“Nukkit”的Java程序才能运行！详情请搜索引擎搜索了解！");
	}

	private Instant loadTime = Instant.now();
	/**
	 * 插件缓存数据集合
	 */
	protected static Kick kick;

	/**
	 * 明人不说暗话！这就是插件启动事件
	 */
	@Override
	public void onEnable() {
		Instant EnableTime = Instant.now();
		getServer().getPluginManager().registerEvents(new PlayerEvent(getKick()), this);
		float entime = ((Duration.between(loadTime, Instant.now()).toMillis()));
		String onEnableString = (entime > 1000 ? ((entime / 1000) + "§6s!(碉堡了) ") : entime + "§6ms");
		this.getServer().getCommandMap().register(getName(), new ShopCommand(kick));
		Map<UUID, Player> OnlinePlayers = getServer().getOnlinePlayers();
		for (UUID id : OnlinePlayers.keySet()) {
			Player player = OnlinePlayers.get(id);
			if (player.isOnline() && !kick.PlayerDataMap.containsKey(player.getName()))
				kick.PlayerDataMap.put(player.getName(), new MyPlayer(player));
		}
		this.getServer().getLogger().info(Tool.getColorFont(this.getName() + "启动！") + "§6总耗时:§9" + onEnableString
				+ " 启动耗时:§9" + ((float) (Duration.between(EnableTime, Instant.now()).toMillis())) + "§6ms");
		if (Tool.getRand(1, 5) == 1)
			getLogger().info(Tool.getColorFont("本插件完全免费，如果你是给钱了的，那你就可能被坑啦~"));
	}

	/**
	 * 返回货币的名称，如“金币”
	 * 
	 * @return
	 */
	public static String getMoneyName() {
		return kick.config.getString("货币单位");
	}

	/**
	 * ????这都看不懂？？这是插件关闭事件
	 */
	@Override
	public void onDisable() {
		Set<String> keys1 = kick.PlayerDataMap.keySet();
		for (String player : keys1)
			kick.PlayerDataMap.get(player).config.save();
		this.getServer().getLogger()
				.info(Tool.getColorFont(this.getName() + "关闭！") + TextFormat.GREEN + "本次运行时长" + TextFormat.BLUE
						+ Tool.getTimeBy(((float) (Duration.between(loadTime, Instant.now()).toMillis()) / 1000)));
		super.onDisable();
	}

	/**
	 * PY已准备好！插件加载事件
	 */
	@Override
	public void onLoad() {
		this.getServer().getLogger().info(Tool.getColorFont(this.getName() + "正在加载..."));
		kick = new Kick(this);
	}

	public static Kick getKick() {
		return kick;
	}

	/**
	 * 快来和本插件PY交易吧~
	 * 
	 * @return 插件主类对象
	 */
	public static Bemilk getPY() {
		return kick.mis;
	}
}
