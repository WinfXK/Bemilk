package xiaokai.bemilk.shop.open.type.myshop;

import xiaokai.bemilk.shop.open.BaseSecondary;
import xiaokai.bemilk.shop.open.type.MyShop;
import xiaokai.bemilk.tool.ItemID;
import xiaokai.bemilk.tool.Tool;

import java.util.Map;

import cn.nukkit.form.response.FormResponseCustom;

import me.onebone.economyapi.EconomyAPI;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Shop extends BaseSecondary {
	private cn.nukkit.item.Item item;
	private boolean isInt;
	private int ItemCount;
	private String ByPlayer;
	private MyShop data;

	public Shop(MyShop data) {
		super(data);
		this.data = data;
		item = Tool.loadItem((Map<String, Object>) Item.get("Item"));
		isInt = Tool.ObjToBool(Item.get("isInt"));
		ItemCount = Tool.ObjectToInt(Item.get("ItemCount"));
		ByPlayer = (String) Item.get("Player");
	}

	@Override
	public boolean MakeSecondary() {
		if (Inventory.isFull())
			return data.Tip(msg.getSun("个人商店", "出售项目", "背包已满", data.k, data.d));
		int CC = (int) (data.MyMoney() / Money);
		CC = CC > ItemCount ? ItemCount : CC;
		String[] k = { "{Player}", "{ByPlayer}", "{ItemName}", "{ItemID}", "{Count}", "{MyMoney}", "{isInt}", "{Money}",
				"{MoneyItemCount}", "{Mydata}" };
		Object[] d = { player.getName(), ByPlayer, ItemID.getName(item), ItemID.getID(item), ItemCount, data.MyMoney(),
				isInt ? "该项目可以只购买部分" : "该项目只能一次性购买", Money, CC, Item.get("Mydata") };
		String Contxt = msg.getSun("个人商店", "出售项目", "内容", k, d);
		form.addLabel(Contxt);
		form.addSlider(msg.getSun("个人商店", "出售项目", "购买数量", data.k, data.d), isInt ? 1 : ItemCount, ItemCount, 1, CC);
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disSecondary(FormResponseCustom data) {
		int Count = (int) data.getSliderResponse(1);
		if (this.data.MyMoney() < (Count * Money))
			return this.data.Tip(msg.getSun("个人商店", "出售项目", "回收项目", this.data.k, this.data.d));
		if (ByPlayer == null)
			return this.data.Tip(msg.getSon("个人商店", "数据错误", new String[] { "{Player}", "{Money}", "{Error}" },
					new Object[] { player.getName(), this.data.MyMoney(), "ByPlayer： Null" }));
		double reduceMoney = this.data.reduceMoney(Count * Money);
		item.setCount(Count);
		Inventory.addItem(item);
		String[] k = { "{Player}", "{ByPlayer}", "{ItemName}", "{ItemID}", "{Count}", "{MyMoney}", "{reduceMoney}",
				"{Money}" };
		Object[] d = { player.getName(), ByPlayer, ItemID.getName(item), ItemID.getID(item), Count, this.data.MyMoney(),
				reduceMoney, Count * Money };
		EconomyAPI.getInstance().addMoney(ByPlayer, Count * Money);
		this.data.data.Item.put("ItemCount", Tool.ObjectToInt(this.data.data.Item.get("ItemCount")) - Count);
		this.data.data.Shops.put(this.data.data.Key, this.data.data.Item);
		this.data.data.config.set("Items", this.data.data.Shops);
		this.data.data.config.save();
		if (this.data.isFull(this.data.data)) {
			this.data.delItem(ByPlayer, this.data.data);
			this.data.addMsg(ByPlayer, msg.getSon("个人商店", "售罄删除", this.data.k, this.data.d));
		}
		return this.data.send(msg.getSun("个人商店", "出售项目", "购买成功", k, d));
	}

}
