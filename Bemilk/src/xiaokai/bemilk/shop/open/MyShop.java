package xiaokai.bemilk.shop.open;

import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class MyShop extends BaseDis {

	/**
	 * 处理玩家打开的项目是个人商店时的操作
	 * 
	 * @param data
	 */
	public MyShop(ShopData data) {
		super(data);
	}

	/**
	 * 开始处理
	 * 
	 * @return
	 */
	@Override
	public boolean MakeMain() {
		return true;
	}

	@Override
	public boolean disMain(FormResponseCustom data) {
		return false;
	}
}
