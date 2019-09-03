package xiaokai.bemilk.shop.open.type.effect;

import xiaokai.bemilk.shop.open.BaseDis;
import xiaokai.bemilk.shop.open.BaseSecondary;

import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class RandTime extends BaseSecondary {

	public RandTime(BaseDis data) {
		super(data);
	}

	@Override
	public boolean MakeSecondary() {
		return false;
	}

	@Override
	public boolean disSecondary(FormResponseCustom data) {
		return false;
	}

}
