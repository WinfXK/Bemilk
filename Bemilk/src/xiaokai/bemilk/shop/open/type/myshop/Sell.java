package xiaokai.bemilk.shop.open.type.myshop;

import xiaokai.bemilk.mtp.Belle;
import xiaokai.bemilk.shop.open.BaseSecondary;
import xiaokai.bemilk.shop.open.type.MyShop;
import xiaokai.bemilk.tool.Tool;
import xiaokai.bemilk.tool.data.ItemID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.nbt.tag.CompoundTag;

import me.onebone.economyapi.EconomyAPI;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Sell extends BaseSecondary {
	private cn.nukkit.item.Item item;
	private boolean isInt;
	private int ItemCount;
	private String ByPlayer;
	private MyShop data;

	public Sell(MyShop data) {
		super(data);
		this.data = data;
		item = Tool.loadItem((Map<String, Object>) Item.get("Item"));
		isInt = Tool.ObjToBool(Item.get("isInt"));
		ItemCount = Tool.ObjectToInt(Item.get("ItemCount"));
		ByPlayer = (String) Item.get("Player");
	}

	@Override
	public boolean MakeSecondary() {
		if (Inventory.getContents().size() < 1)
			return data.Tip(msg.getSun("个人商店", "回收项目", "背包为空", data.k, data.d));
		String[] k = { "{Player}", "{ByPlayer}", "{ItemName}", "{ItemID}", "{Count}", "{MyMoney}", "{isInt}",
				"{Money}" };
		Object[] d = { player.getName(), ByPlayer, ItemID.getName(item), ItemID.getID(item), ItemCount, data.MyMoney(),
				isInt ? "该项目可以只购买部分" : "该项目只能一次性购买", Money };
		String Contxt = msg.getSun("个人商店", "回收项目", "内容", k, d);
		form.addLabel(Contxt);
		form.addSlider(msg.getSun("个人商店", "出售项目", "回收数量", data.k, data.d), isInt ? 1 : ItemCount, ItemCount, 1,
				isInt ? 1 : ItemCount);
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disSecondary(FormResponseCustom data) {
		if (ByPlayer == null)
			return this.data.Tip(msg.getSon("个人商店", "数据错误", new String[] { "{Player}", "{Money}", "{Error}" },
					new Object[] { player.getName(), this.data.MyMoney(), "ByPlayer： Null" }));
		int Count = (int) data.getSliderResponse(1);
		Map<Integer, cn.nukkit.item.Item> Contents = Inventory.getContents();
		List<Integer> is = new ArrayList<>(Contents.keySet());
		int MyCount = 0;
		for (Integer i : is) {
			cn.nukkit.item.Item item2 = Contents.get(i);
			CompoundTag nbt1 = item.getNamedTag();
			CompoundTag nbt2 = item2.getNamedTag();
			nbt1 = nbt1 == null ? new CompoundTag() : nbt1;
			nbt2 = nbt2 == null ? new CompoundTag() : nbt2;
			if (!Belle.isMaterials(item2) && (nbt1 == nbt2 || nbt1.equals(nbt2))
					&& ItemID.getID(item).equals(ItemID.getID(item2)))
				MyCount += item2.getCount();

		}
		if (MyCount < Count)
			return this.data.Tip(msg.getSun("个人商店", "回收项目", "物品数量不足",
					new String[] { "{Player}", "{Money}", "{ItemName}", "{ItemID}", "{ItemCount}" },
					new Object[] { player.getName(), this.data.MyMoney(), ItemID.getName(item), ItemID.getID(item),
							Count - MyCount }));
		MyCount = Count;
		for (int i = 0; i < is.size(); i++) {
			cn.nukkit.item.Item item2 = Contents.get(i);
			CompoundTag nbt1 = item.getNamedTag();
			CompoundTag nbt2 = item2.getNamedTag();
			nbt1 = nbt1 == null ? new CompoundTag() : nbt1;
			nbt2 = nbt2 == null ? new CompoundTag() : nbt2;
			if (!Belle.isMaterials(item2) && (nbt1 == nbt2 || nbt1.equals(nbt2))
					&& ItemID.getID(item).equals(ItemID.getID(item2)))
				if (item2.getCount() < MyCount) {
					Contents.remove(i);
					MyCount -= item2.getCount();
				} else if (item2.getCount() == MyCount) {
					Contents.remove(i);
					MyCount = 0;
					break;
				} else if (item2.getCount() > MyCount) {
					item2.setCount(item2.getCount() - MyCount);
					Contents.put(i, item2);
				}
		}
		Inventory.setContents(Contents);
		item.setCount(Count);
		this.data.addItem(ByPlayer, item);
		this.data.addMsg(ByPlayer,
				msg.getSun("个人商店", "回收项目", "物品到账",
						new String[] { "{Player}", "{Money}", "{MyMoney}", "{ItemName}", "{ItemID}", "{ItemCount}",
								"{ByPlayer}" },
						new Object[] { player.getName(), Money * Count, EconomyAPI.getInstance().myMoney(ByPlayer),
								ItemID.getName(item), ItemID.getID(item), Count, ByPlayer, }));
		this.data.data.Item.put("ItemCount", Tool.ObjectToInt(this.data.data.Item.get("ItemCount")) - Count);
		this.data.data.Shops.put(this.data.data.Key, this.data.data.Item);
		this.data.data.config.set("Items", this.data.data.Shops);
		this.data.data.config.save();
		if (this.data.isFull(this.data.data)) {
			this.data.delItem(ByPlayer, this.data.data);
			this.data.addMsg(ByPlayer, msg.getSon("个人商店", "售罄删除", this.data.k, this.data.d));
		}
		return this.data.send(msg.getSun("个人商店", "回收项目", "回收数量",
				new String[] { "{Player}", "{Money}", "{MyMoney}", "{addMoney}", "{ByPlayer}" },
				new Object[] { player.getName(), Money * Count, this.data.MyMoney(), this.data.addMoney(Money * Count),
						ByPlayer }));
	}

}
