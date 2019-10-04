package xiaokai.bemilk.shop.open.type.repair;

import xiaokai.bemilk.shop.open.BaseSecondary;
import xiaokai.bemilk.shop.open.type.Repair;
import xiaokai.bemilk.tool.Tool;
import xiaokai.bemilk.tool.data.ItemID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class Random extends BaseSecondary {
	private List<Integer> PlayerItemsIntegers;
	private boolean isTool;
	private int MinRepair, MaxRepair, Prob;

	/**
	 * 概率增长特殊值，允许失败
	 * 
	 * @param data
	 */
	public Random(Repair data) {
		super(data);
	}

	@Override
	public boolean MakeSecondary() {
		isTool = Tool.ObjToBool(Item.get("isTool"));
		MinRepair = Tool.ObjectToInt(Item.get("MinRepair"));
		MaxRepair = Tool.ObjectToInt(Item.get("MaxRepair"));
		Prob = Tool.ObjectToInt(Item.get("RepairCount"));
		String ProbString = ((double) 1 / (Prob <= 0 ? 1 : Prob) * 100) + "%";
		String[] myKey = { "{Player}", "{Items}", "{Money}", "{MyMoney}", "{isTool}", "{MinCount}", "{MaxCount}",
				"{Prob}" };
		Object[] myData = { player.getName(), Money, data.MyMoney(), isTool ? "允许" : "不允许", MinRepair, MaxRepair,
				ProbString };
		String Contxt = msg.getSun("修复商店", "概率修复", "内容", myKey, myData);
		Map<Integer, cn.nukkit.item.Item> Contents = Inventory.getContents();
		PlayerItemsIntegers = new ArrayList<>();
		List<String> list = new ArrayList<>();
		String[] listK = { "{Player}", "{Money}", "{ItemName}", "{ItemID}", "{ItemCount}" };
		Set<Integer> set = Contents.keySet();
		for (Integer i : set) {
			cn.nukkit.item.Item item = Contents.get(i);
			if (Tool.ObjToBool(Item.get("isTool"), false) || item.isArmor() || item.isTool()) {
				list.add(msg.getSun("修复商店", "概率修复", "列表格式", listK, new Object[] { player.getName(), data.MyMoney(),
						ItemID.getName(item), ItemID.getID(item), item.getCount() }));
				PlayerItemsIntegers.add(i);
			}
		}
		if (PlayerItemsIntegers.size() < 1)
			return data.Tip(msg.getSon("修复商店", "没有可修复的物品", new String[] { "{Player}", "{Money}", "{ItemCount}" },
					new Object[] { player.getName(), data.MyMoney(), Contents.size() }));
		form.addLabel(Contxt);
		form.addDropdown(msg.getSon("修复商店", "选择物品", data.k, data.d), list);
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disSecondary(FormResponseCustom data) {
		if (this.data.MyMoney() < Money)
			return this.data.Tip(msg.getSon("修复商店", "钱不够", this.data.k, this.data.d));
		double reduceMoney = this.data.reduceMoney(Money);
		int Repair = 0;
		boolean isOK = false;
		int i = PlayerItemsIntegers.get(data.getDropdownResponse(1).getElementID());
		Map<Integer, cn.nukkit.item.Item> Contents = Inventory.getContents();
		cn.nukkit.item.Item item = Contents.get(i);
		int isAll = item.getDamage();
		if (isOK = (Tool.getRand(0, Prob) == 1)) {
			Repair = Tool.getRand(MinRepair, MaxRepair);
			item.setDamage(item.getDamage() - Repair);
		} else {
			Repair = Tool.getRand(MinRepair, MaxRepair);
			isAll = item.getDamage() + Repair;
			isAll = isAll <= 0 ? 1 : isAll;
			item.setDamage(isAll);
		}
		Contents.put(i, item);
		Inventory.setContents(Contents);
		if (isOK)
			return this.data.send(msg.getSun("修复商店", "概率修复", "购买成功",
					new String[] { "{Player}", "{MyMoney}", "{Money}", "{reduceMoney}", "{Repair}" },
					new Object[] { player.getName(), this.data.MyMoney(), Money, reduceMoney, Repair }));
		else
			return this.data.send(msg.getSun("修复商店", "概率修复", "购买失败",
					new String[] { "{Player}", "{MyMoney}", "{Money}", "{reduceMoney}", "{Repair}", "{isRepair}" },
					new Object[] { player.getName(), this.data.MyMoney(), Money, reduceMoney, Repair, isAll }));

	}

}
