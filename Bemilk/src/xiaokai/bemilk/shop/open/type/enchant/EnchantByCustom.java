package xiaokai.bemilk.shop.open.type.enchant;

import xiaokai.bemilk.shop.open.BaseSecondary;
import xiaokai.bemilk.shop.open.type.Enchant;
import xiaokai.bemilk.tool.EnchantName;
import xiaokai.bemilk.tool.ItemID;
import xiaokai.bemilk.tool.Tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.item.Item;
import cn.nukkit.item.enchantment.Enchantment;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class EnchantByCustom extends BaseSecondary {
	private List<Integer> PlayerItemsIntegers;
	private Map<String, Object> map;
	private List<String> Keys = new ArrayList<>();
	private int Count = 0;

	/**
	 * 处理完全是自定义的附魔
	 * 
	 * @param data
	 */
	public EnchantByCustom(Enchant data) {
		super(data);
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
		map = (Map<String, Object>) Item.get("EnchantData");
		String[] myKey = { "{Player}", "{MyMoney}", "{EnchantList}", "{isTool}", "{Money}" };
		Object[] myData = { player.getName(), data.MyMoney(), getEnchants(),
				Tool.ObjToBool(Item.get("isTool"), false) ? "允许" : "不允许", Money };
		String Contxt = msg.getSun("附魔商店", "自定义附魔", "内容", myKey, myData);
		form.addLabel(Contxt);
		form.addDropdown(msg.getSon("附魔商店", "物品列表", data.k, data.d), list);
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disSecondary(FormResponseCustom data) {
		if (this.data.MyMoney() < Money)
			return this.data.Tip(msg.getSun("附魔商店", "自定义附魔", "钱不够", this.data.k, this.data.d));
		double reduceMoney = this.data.reduceMoney(Money);
		Map<Integer, Item> Contents = Inventory.getContents();
		Integer i = PlayerItemsIntegers.get(data.getDropdownResponse(1).getElementID());
		Item item = Contents.get(i);
		Map<String, Object> Enchant = (Map<String, Object>) map.get(Keys.get(Tool.getRand(0, Keys.size() - 1)));
		int ID = Tool.ObjectToInt(Enchant.get("EnchantID"));
		Enchantment enchant = EnchantName.getEnchantByID(ID);
		enchant.setLevel(Tool.ObjectToInt(Enchant.get("EnchantLevel"), 1));
		item.addEnchantment(enchant);
		Contents.put(i, item);
		Inventory.setContents(Contents);
		int Probs = Tool.ObjectToInt(Enchant.get("EnchantRand"));
		String Prob = ((double) ((double) 1 / (Probs <= 0 ? 1 : Probs)) * Count) + "%";
		String[] k = { "{Player}", "{MyMoney}", "{EnchantName}", "{EnchantID}", "{EnchantLevel}", "{Prob}", "{Money}",
				"{reduceMoney}" };
		Object[] d = { player.getName(), this.data.MyMoney(), EnchantName.getNameByID(ID), ID,
				Enchant.get("EnchantLevel"), Prob, Money, reduceMoney };
		return this.data.Tip(msg.getSun("附魔商店", "自定义附魔", "附魔成功", k, d));
	}

	/**
	 * 获取附魔项目列表
	 * 
	 * @return
	 */
	public String getEnchants() {
		String string = "";
		for (String ike : map.keySet()) {
			int CC = Tool.ObjectToInt(((Map<String, Object>) map.get(ike)).get("EnchantRand"));
			Count += CC;
			for (int i = 0; i < CC; i++)
				Keys.add(ike);
		}
		String[] myKey = { "{Player}", "{Money}", "{EnchantName}", "{EnchantID}", "{EnchantLevel}", "{Prob}" };
		Count = Count <= 0 ? 1 : Count;
		for (String ike : map.keySet()) {
			Map<String, Object> item = (Map<String, Object>) map.get(ike);
			int ID = Tool.ObjectToInt(item.get("EnchantID"));
			int Probs = Tool.ObjectToInt(item.get("EnchantRand"));
			String Prob = ((double) ((double) 1 / (Probs <= 0 ? 1 : Probs)) * Count) + "%";
			string += msg.getSun("附魔商店", "自定义附魔", "附魔项目", myKey, new Object[] { player.getName(), data.MyMoney(),
					EnchantName.getNameByID(ID), ID, item.get("EnchantLevel"), Prob });
		}
		return string;
	}
}
