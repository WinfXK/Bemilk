package xiaokai.bemilk.shop.open;

import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class Enchant extends BaseDis {
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
		return true;
	}

	@Override
	public boolean disMain(FormResponseCustom data) {
		return false;
	}

}
