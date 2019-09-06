package xiaokai.bemilk.set;

import xiaokai.bemilk.mtp.Kick;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
public abstract class BasesetForm {
	public Player player;
	public Kick kick;
	public Config config;

	/**
	 * 基本界面处理类
	 * 
	 * @param player
	 */
	public BasesetForm(Player player) {
		this.player = player;
		kick = Kick.kick;
	}

	/**
	 * 构建界面
	 * 
	 * @return
	 */
	public abstract boolean makeMain();

	/**
	 * 处理界面事件
	 * 
	 * @param d
	 * @return
	 */
	public abstract boolean disMain(FormResponse d);
}
