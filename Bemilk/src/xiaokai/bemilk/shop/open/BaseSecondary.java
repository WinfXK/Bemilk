package xiaokai.bemilk.shop.open;

import xiaokai.bemilk.data.Message;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.tool.form.CustomForm;

import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.inventory.PlayerInventory;

/**
 * @author Winfxk
 */
public abstract class BaseSecondary {
	public BaseDis data;
	public Message msg;
	public Player player;
	public Kick kick;
	/**
	 * 打开的项目所属的数据
	 */
	public Map<String, Object> Item;
	/**
	 * 项目的价格
	 */
	public double Money;
	/**
	 * 玩家背包对象
	 */
	public PlayerInventory Inventory;
	/**
	 * 界面
	 */
	public CustomForm form;

	public BaseSecondary(BaseDis data) {
		this.data = data;
		player = data.player;
		Item = data.Item;
		msg = data.msg;
		Money = data.Money;
		Inventory = data.Inventory;
		kick = data.kick;
		form = data.form;
	}

	/**
	 * 创建二级界面
	 * 
	 * @return
	 */
	public abstract boolean MakeSecondary();

	/**
	 * 处理二级界面发回的数据
	 * 
	 * @return
	 */
	public abstract boolean disSecondary(FormResponseCustom data);
}
