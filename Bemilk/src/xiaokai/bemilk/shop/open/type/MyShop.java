package xiaokai.bemilk.shop.open.type;

import xiaokai.bemilk.data.DisPlayer;
import xiaokai.bemilk.shop.open.BaseDis;
import xiaokai.bemilk.shop.open.BaseSecondary;
import xiaokai.bemilk.shop.open.ShopData;
import xiaokai.bemilk.tool.Tool;

import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class MyShop extends BaseDis {
	private BaseSecondary shop;

	/**
	 * 处理玩家打开的项目是个人商店时的操作
	 * 
	 * @param data
	 */
	public MyShop(ShopData data) {
		super(data);
	}

	@Override
	public boolean MakeMain() {
		Style = (String) Item.get("Style");
		if (Style == null || Style.isEmpty())
			return Tip(msg.getSon("个人商店", "无法获取类型", k, d));
		switch (Style.toLowerCase()) {
		case "sell":
			shop = new xiaokai.bemilk.shop.open.type.myshop.Sell(this);
			break;
		case "shop":
			shop = new xiaokai.bemilk.shop.open.type.myshop.Shop(this);
			break;
		default:
			return Tip(msg.getSon("个人商店", "无法获取类型", k, d));
		}
		return shop.MakeSecondary();
	}

	@Override
	public boolean disMain(FormResponseCustom data) {
		return shop.disSecondary(data);
	}

	/**
	 * 给玩家一个物品
	 * 
	 * @param player
	 * @param item
	 * @return
	 */
	public boolean addItem(String player, cn.nukkit.item.Item item) {
		if (kick.PlayerDataMap.containsKey(player) && !kick.PlayerDataMap.get(player).player.getInventory().isFull()) {
			kick.PlayerDataMap.get(player).player.getInventory().addItem(item);
		} else
			DisPlayer.addItem(player, item);
		return true;
	}

	/**
	 * 发送一个信息给玩家
	 * 
	 * @param player
	 * @param Msg
	 * @return
	 */
	public boolean addMsg(String player, String Msg) {
		if (kick.PlayerDataMap.containsKey(player)) {
			kick.PlayerDataMap.get(player).player.sendMessage(Msg);
		} else
			DisPlayer.addMsg(player, Msg);
		return true;
	}

	/**
	 * 检查个人商店是否售罄
	 * 
	 * @param file
	 * @param Key
	 * @return
	 */
	public boolean isFull(ShopData data) {
		return Tool.ObjectToInt(data.Item.get("ItemCount")) <= 0;
	}

	/**
	 * 删除玩家已经售罄的个人商店
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public boolean delItem(String player, ShopData data) {
		if (player == null)
			return false;
		data.Shops.remove(data.Key);
		data.config.set("Items", data.Shops);
		return data.config.save() && DisPlayer.delItem(player, data.file, data.Key);
	}
}
