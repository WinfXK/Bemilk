package xiaokai.bemilk.shop.open;

import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class Repair extends BaseDis {
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
		return false;
	}

	@Override
	public boolean disMain(FormResponseCustom data) {
		return false;
	}
}
