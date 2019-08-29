package xiaokai.bemilk.shop.open;

import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class ItemTradeItem extends BaseDis {
	/**
	 * 处理物品兑换项目的数据
	 * 
	 * @param data
	 */
	public ItemTradeItem(ShopData data) {
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
