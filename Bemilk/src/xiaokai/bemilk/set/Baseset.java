package xiaokai.bemilk.set;
/**
*@author Winfxk
*/

import xiaokai.bemilk.mtp.Kick;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.utils.Config;

public abstract class Baseset {
	public Player player;
	public Kick kick;
	public Config config;
	public int FormID;

	/**
	 * 构建的处理类
	 * 
	 * @param player
	 */
	public Baseset(Player player) {
		this.player = player;
		kick = Kick.kick;
		FormID = kick.formID.getID(34);
	}

	/**
	 * 构建界面
	 * 
	 * @return
	 */
	public abstract boolean makeMain();

	/**
	 * 处理数据
	 * 
	 * @return
	 */
	public abstract boolean disMain(FormResponse data);
}
