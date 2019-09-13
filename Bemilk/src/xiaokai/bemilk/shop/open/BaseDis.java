package xiaokai.bemilk.shop.open;

import xiaokai.bemilk.data.DisPlayer;
import xiaokai.bemilk.data.Message;
import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.tool.Tool;
import xiaokai.bemilk.tool.data.ItemID;
import xiaokai.bemilk.tool.form.CustomForm;

import java.util.Map;
import java.util.Set;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.utils.Config;

import me.onebone.economyapi.EconomyAPI;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public abstract class BaseDis {
	public Kick kick;
	public Message msg;
	public String[] k;
	public Object[] d;
	/**
	 * 项目子类型
	 */
	public String Style;
	/**
	 * 主交互界面ID
	 */
	public int MainFormID = 27;
	/**
	 * 交互界面的标题
	 */
	public String Title;
	/**
	 * 打开商店项目的玩家对象
	 */
	public Player player;
	/**
	 * 打开的项目的Key
	 */
	public String Key;
	/**
	 * 打开的商店所在的配置文件的对象
	 */
	public Config config;
	/**
	 * 打开的项目所属的数据
	 */
	public Map<String, Object> Item;
	public ShopData data;
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

	/**
	 * 开始处理点击的商店项目的数据
	 * 
	 * @param data
	 */
	public BaseDis(ShopData data) {
		this.data = data;
		player = data.player;
		Key = data.Key;
		config = data.config;
		Item = data.Item;
		kick = Kick.kick;
		msg = kick.Message;
		k = new String[] { "{Player}", "{Money}" };
		d = new Object[] { player.getName(), MyMoney() };
		String Style = (String) Item.get("Style");
		Title = msg.getSon("界面", "交互界面标题", new String[] { "{Player}", "{Money}", "{Title}" }, new Object[] {
				player.getName(), MyMoney(),
				Tool.getColorFont(data.Type.toUpperCase() + ((Style == null || Style.isEmpty()) ? "" : "-" + Style)) });
		Money = Double.valueOf(String.valueOf(Item.get("Money")));
		Inventory = player.getInventory();
		form = new CustomForm(kick.formID.getID(MainFormID), Title);
	}

	/**
	 * 开始处理点击事件
	 * 
	 * @return
	 */
	public abstract boolean MakeMain();

	/**
	 * 处理主页发回的数据
	 * 
	 * @param data
	 * @return
	 */
	public abstract boolean disMain(FormResponseCustom data);

	/**
	 * 获取物品列表
	 * 
	 * @return
	 */
	public String getItems() {
		return getItems((Map<String, Object>) Item.get("Items"));
	}

	/**
	 * 获取物品列表
	 * 
	 * @return
	 */
	public String getItems(Map<String, Object> items) {
		String string = "";
		Set<String> list = items.keySet();
		for (String ID : list) {
			cn.nukkit.item.Item item = Tool.loadItem((Map<String, Object>) items.get(ID));
			string += (string.isEmpty() ? "  §f[" : "§f]\n  §f[") + "§a" + ItemID.getName(item) + "§f(§4"
					+ ItemID.getID(item) + "§f)§e*§a" + item.getCount();
		}
		return string + "§f]\n";
	}

	/**
	 * 获取自己的金币数量
	 * 
	 * @return
	 */
	public double MyMoney() {
		return EconomyAPI.getInstance().myMoney(player);
	}

	/**
	 * 发送提示
	 * 
	 * @param string
	 * @return
	 */
	public boolean Tip(String string) {
		return Tip(string, false);
	}

	/**
	 * 发送提示
	 * 
	 * @param string
	 * @return
	 */
	public boolean Tip(String string, boolean isOK) {
		return MakeForm.Tip(player, string, isOK);
	}

	/**
	 * 扣钱
	 * 
	 * @param Money
	 * @return
	 */
	public double reduceMoney(double Money) {
		return DisPlayer.delMoney(player, Money);
	}

	/**
	 * 加钱
	 * 
	 * @param Money
	 * @return
	 */
	public double addMoney(double Money) {
		return DisPlayer.addMoney(player, Money);
	}

	/**
	 * 发送消息
	 * 
	 * @param string
	 * @return
	 */
	public boolean send(String string) {
		return send(string, true);
	}

	/**
	 * 发送消息
	 * 
	 * @param string
	 * @param isok
	 * @return
	 */
	public boolean send(String string, boolean isok) {
		player.sendMessage(string);
		return isok;
	}

	/**
	 * 取得调用的类型
	 * 
	 * @return
	 */
	public String getType() {
		return data.Type;
	}
}
