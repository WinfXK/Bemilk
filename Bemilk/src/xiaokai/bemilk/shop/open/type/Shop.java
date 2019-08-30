package xiaokai.bemilk.shop.open.type;

import xiaokai.bemilk.shop.open.BaseDis;
import xiaokai.bemilk.shop.open.ShopData;
import xiaokai.tool.Tool;
import java.util.Map;
import java.util.Set;

import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Shop extends BaseDis {
	/**
	 * 处理出售商店的数据
	 * 
	 * @param data
	 */
	public Shop(ShopData data) {
		super(data);
	}

	@Override
	public boolean MakeMain() {
		String[] myKey = { "{Player}", "{Items}", "{Money}", "{MyMoney}", "{isShopCount}" };
		int isShopCount = (int) (MyMoney() / (Money <= 0 ? 1d : Money));
		int MinCount = Tool.ObjectToInt(Item.get("MinCount"), 1);
		int MaxCount = Tool.ObjectToInt(Item.get("MaxCount"), 64);
		isShopCount = isShopCount > MaxCount ? MaxCount : isShopCount;
		Object[] myData = { player.getName(), getItems(), Money, MyMoney(), isShopCount };
		String Contxt = msg.getSun("界面", "出售商店", "内容", myKey, myData);
		form.addLabel(Contxt);
		form.addSlider(msg.getSun("界面", "出售商店", "购买数量", k, d), MinCount, MaxCount, 1, isShopCount);
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponseCustom data) {
		int Count = (int) data.getSliderResponse(1);
		if (MyMoney() < (Count * Money))
			return Tip(msg.getSun("界面", "出售商店", "钱不够", k, d));
		double MyMoney = reduceMoney(Count * Money);
		Map<String, Object> items = (Map<String, Object>) Item.get("Items");
		Set<String> kSet = items.keySet();
		for (String ike : kSet) {
			cn.nukkit.item.Item item = Tool.loadItem((Map<String, Object>) items.get(ike));
			item.setCount(item.getCount() * Count);
			Inventory.addItem(item);
		}
		return send(msg.getSun("界面", "出售商店", "购买成功",
				new String[] { "{Player}", "{Money}", "{MyMoney}", "{Items}", "{ItemCount}", "{reduceMoney}" },
				new Object[] { player.getName(), Money, MyMoney(), getItems(), Count, MyMoney }));
	}
}
