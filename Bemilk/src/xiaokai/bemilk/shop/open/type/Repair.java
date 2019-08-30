package xiaokai.bemilk.shop.open.type;

import xiaokai.bemilk.shop.open.BaseDis;
import xiaokai.bemilk.shop.open.BaseSecondary;
import xiaokai.bemilk.shop.open.ShopData;
import xiaokai.bemilk.shop.open.type.repair.Random;
import xiaokai.bemilk.shop.open.type.repair.SoarTo;
import xiaokai.bemilk.shop.open.type.repair.Some;

import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class Repair extends BaseDis {
	private BaseSecondary repair;

	/**
	 * 处理耐久商店的数据
	 * 
	 * @param data
	 */
	public Repair(ShopData data) {
		super(data);
	}

	@Override
	public boolean MakeMain() {
		Style = (String) Item.get("Style");
		if (Style == null || Style.isEmpty())
			return Tip(msg.getSon("修复商店", "无法获取类型", k, d));
		switch (Style.toLowerCase()) {
		case "some":
			repair = new Some(this);
			break;
		case "soarto":
			repair = new SoarTo(this);
			break;
		case "random":
			repair = new Random(this);
			break;
		default:
			return Tip(msg.getSon("修复商店", "无法获取类型", k, d));
		}
		return repair.MakeSecondary();
	}

	@Override
	public boolean disMain(FormResponseCustom data) {
		return repair.disSecondary(data);
	}
}
