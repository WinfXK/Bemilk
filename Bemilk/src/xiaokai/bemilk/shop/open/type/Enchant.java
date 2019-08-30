package xiaokai.bemilk.shop.open.type;

import xiaokai.bemilk.shop.open.BaseDis;
import xiaokai.bemilk.shop.open.BaseSecondary;
import xiaokai.bemilk.shop.open.ShopData;
import xiaokai.bemilk.shop.open.type.enchant.EnchantByCustom;
import xiaokai.bemilk.shop.open.type.enchant.EnchantLevel;
import xiaokai.bemilk.shop.open.type.enchant.EnchantLevelRand;

import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class Enchant extends BaseDis {
	/**
	 * 处理附魔的数据的对象
	 */
	private BaseSecondary enchant;

	/**
	 * 处理点击的是附魔商店的事件
	 * 
	 * @param data
	 */
	public Enchant(ShopData data) {
		super(data);
	}

	@Override
	public boolean MakeMain() {
		Style = (String) Item.get("Style");
		if (Style == null || Style.isEmpty())
			return Tip(msg.getSon("附魔商店", "无法获取类型", k, d));
		switch (Style.toLowerCase()) {
		case "enchantbycustom":
			enchant = new EnchantByCustom(this);
			break;
		case "enchantLevelrand":
			enchant = new EnchantLevelRand(this);
			break;
		case "enchantlevel":
			enchant = new EnchantLevel(this);
			break;
		default:
			return Tip(msg.getSon("附魔商店", "无法获取类型", k, d));
		}
		return enchant.MakeSecondary();
	}

	@Override
	public boolean disMain(FormResponseCustom data) {
		return enchant.disSecondary(data);
	}
}
