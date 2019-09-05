package xiaokai.bemilk.set;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;

/**
 * @author Winfxk
 */
public class CustomItem extends Baseset {
	/**
	 * 处理自定义物品设置类
	 * 
	 * @param player
	 */
	public CustomItem(Player player) {
		super(player);
	}

	@Override
	public boolean makeMain() {
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		return true;
	}
}
