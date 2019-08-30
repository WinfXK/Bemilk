package xiaokai.bemilk.shop.open.type.enchant;

import xiaokai.bemilk.shop.open.BaseSecondary;
import xiaokai.bemilk.shop.open.type.Enchant;
import xiaokai.tool.Tool;
import xiaokai.tool.data.EnchantName;
import xiaokai.tool.data.ItemID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;

/**
 * @author Winfxk
 */
public class EnchantLevel extends BaseSecondary {
	private List<Integer> PlayerItemsIntegers;
	private int EnchantID;

	/**
	 * 处理定级附魔(无概率，直接购买某个附魔项目某个等级)
	 * 
	 * @param enchant
	 */
	public EnchantLevel(Enchant enchant) {
		super(enchant);
		EnchantID = Tool.ObjectToInt(Item.get("EnchantID"));
	}

	@Override
	public boolean MakeSecondary() {
		Map<Integer, Item> Contents = Inventory.getContents();
		if (Contents.size() < 1)
			return data.Tip(msg.getSon("附魔商店", "背包为空", data.k, data.d));
		List<String> list = new ArrayList<>();
		List<Integer> is = new ArrayList<>(Contents.keySet());
		PlayerItemsIntegers = new ArrayList<>();
		for (Integer i : is) {
			Item item = Contents.get(i);
			if (Tool.ObjToBool(Item.get("isTool"), false) || item.isArmor() || item.isTool()) {
				PlayerItemsIntegers.add(i);
				list.add(msg.getSon("附魔商店", "物品列表名",
						new String[] { "{Player}", "{Money}", "{ItemName}", "{ItemID}", "{ItemCount}" },
						new Object[] { player.getName(), data.MyMoney(), ItemID.getName(item), ItemID.getID(item),
								item.getCount() }));
			}
		}
		if (PlayerItemsIntegers.size() < 1)
			return data.Tip(msg.getSon("附魔商店", "没有可附魔的物品", new String[] { "{Player}", "{Money}", "{ItemCount}" },
					new Object[] { player.getName(), data.MyMoney(), Contents.size() }));
		String[] myKey = { "{Player}", "{MyMoney}", "{EnchantName}", "{EnchantLevel}", "{isTool}", "{Money}" };
		Object[] myData = { player.getName(), data.MyMoney(), EnchantName.getNameByID(EnchantID),
				Item.get("EnchantLevel"), Tool.ObjToBool(Item.get("isTool"), false) ? "允许" : "不允许", Money };
		String Contxt = msg.getSun("附魔商店", "定级附魔", "内容", myKey, myData);
		form.addLabel(Contxt);
		form.addDropdown(msg.getSon("附魔商店", "物品列表", data.k, data.d), list);
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disSecondary(FormResponseCustom data) {
		if (this.data.MyMoney() < Money)
			return this.data.Tip(msg.getSun("附魔商店", "定级附魔", "钱不够", this.data.k, this.data.d));
		double reduceMoney = this.data.reduceMoney(Money);
		Map<Integer, Item> Contents = Inventory.getContents();
		Integer i = PlayerItemsIntegers.get(data.getDropdownResponse(1).getElementID());
		Item item = Contents.get(i);
		Enchantment enchant = EnchantName.getEnchantByID(EnchantID);
		enchant.setLevel(Tool.ObjectToInt(Item.get("EnchantLevel"), 0) + 1);
		item.addEnchantment(enchant);
		Contents.put(i, item);
		Inventory.setContents(Contents);
		return this.data.send(msg.getSun("附魔商店", "定级附魔", "购买成功",
				new String[] { "{Player}", "{MyMoney}", "{EnchantName}", "{Money}", "{reduceMoney}" },
				new Object[] { player.getName(), this.data.MyMoney(), EnchantName.getNameByID(EnchantID),
						this.data.MyMoney(), Money, reduceMoney }));
	}

}
