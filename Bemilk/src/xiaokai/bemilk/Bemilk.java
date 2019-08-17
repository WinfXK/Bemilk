package xiaokai.bemilk;

import java.time.Duration;
import java.time.Instant;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import cn.nukkit.utils.TextFormat;
import xiaokai.bemilk.event.Monitor;
import xiaokai.bemilk.event.PlayerEvent;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
public class Bemilk extends PluginBase {
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
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerEvent(getKick()), this);
		pm.registerEvents(new Monitor(getKick()), this);
		this.getServer().getLogger()
				.info(Tool.getColorFont(this.getName() + "启动！") + "§6总耗时:§9"
						+ ((float) (Duration.between(loadTime, Instant.now()).toMillis())) + "§6ms 启动耗时:§9"
						+ ((float) (Duration.between(EnableTime, Instant.now()).toMillis())) + "§6ms");
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

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!sender.isPlayer())
			return true;
		return true;
	}
}