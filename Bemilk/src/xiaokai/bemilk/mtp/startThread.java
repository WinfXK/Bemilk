package xiaokai.bemilk.mtp;

import xiaokai.bemilk.cmd.ShopCommand;
import xiaokai.bemilk.data.DisPlayer;
import xiaokai.bemilk.data.MyPlayer;
import xiaokai.bemilk.tool.Update;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import cn.nukkit.Player;

/**
*@author Winfxk
*/
/**
 * 异步类
 * 
 * @author Winfxk
 */
public class startThread extends Thread {
	/**
	 * 
	 */
	private final Kick kick;
	public int 定时保存间隔, 定时检查快捷工具间隔, 检测更新间隔, 数据刷新间隔;

	public startThread(Kick kick) {
		this.kick = kick;
		定时保存间隔 = this.kick.config.getInt("定时保存间隔");
		定时检查快捷工具间隔 = this.kick.config.getInt("定时检查快捷工具间隔");
		检测更新间隔 = this.kick.config.getInt("检测更新间隔");
	}

	public void load() {
		定时保存间隔 = this.kick.config.getInt("定时保存间隔");
		定时检查快捷工具间隔 = this.kick.config.getInt("定时检查快捷工具间隔");
		检测更新间隔 = this.kick.config.getInt("检测更新间隔");
		数据刷新间隔 = this.kick.config.getInt("数据刷新间隔");
	}

	@Override
	public void run() {
		while (true)
			try {
				sleep(1000);
				if (this.kick.config.getInt("检测更新间隔") > 0)
					if (this.kick.config.getBoolean("检测更新")) {
						if (检测更新间隔 < 0) {
							检测更新间隔 = this.kick.config.getInt("检测更新间隔");
							new Update(this.kick.mis).start();
						} else
							检测更新间隔--;
					}
				if (this.kick.config.getInt("定时检查快捷工具间隔") > 0)
					if (定时检查快捷工具间隔 < 0) {
						定时检查快捷工具间隔 = this.kick.config.getInt("定时检查快捷工具间隔");
						Map<UUID, Player> Players = this.kick.mis.getServer().getOnlinePlayers();
						Set<UUID> keys = Players.keySet();
						for (UUID id : keys)
							Belle.exMaterials(Players.get(id));
					} else
						定时检查快捷工具间隔--;
				if (this.kick.config.getInt("定时保存间隔") > 0)
					if (定时保存间隔 < 0) {
						定时保存间隔 = this.kick.config.getInt("定时保存间隔");
						if (this.kick.mis.getServer().getOnlinePlayers().size() < 1)
							continue;
						Set<String> keys1 = this.kick.PlayerDataMap.keySet();
						for (String player : keys1) {
							MyPlayer myPlayer = this.kick.PlayerDataMap.get(player);
							if (myPlayer.config == null)
								myPlayer.config = DisPlayer.getConfig(player);
							myPlayer.config.save();
						}
					} else
						定时保存间隔--;
				if (kick.config.getInt("数据刷新间隔") > 0)
					if (数据刷新间隔 < 0) {
						ShopCommand.reload();
						数据刷新间隔 = this.kick.config.getInt("数据刷新间隔");
					} else
						数据刷新间隔--;
			} catch (InterruptedException e) {
				this.kick.mis.getLogger().error("§4异步线程类出现问题！" + e.getMessage());
			}
	}
}