package xiaokai.bemilk.shop.open.type;

import xiaokai.bemilk.shop.open.BaseDis;
import xiaokai.bemilk.shop.open.BaseSecondary;
import xiaokai.bemilk.shop.open.ShopData;
import xiaokai.bemilk.shop.open.type.effect.CustomEffect;
import xiaokai.bemilk.shop.open.type.effect.RandEffect;
import xiaokai.bemilk.shop.open.type.effect.RandTime;
import xiaokai.bemilk.shop.open.type.effect.isEffect;

import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class Effect extends BaseDis {
	private BaseSecondary effect;

	/**
	 * 处理打开的项目是药水效果的事件处理类
	 * 
	 * @param data
	 */
	public Effect(ShopData data) {
		super(data);
	}

	@Override
	public boolean MakeMain() {
		String style = String.valueOf(Item.get("Style")).toLowerCase();
		switch (style) {
		case "iseffect":
			effect = new isEffect(this);
			break;
		case "randeffect":
			effect = new RandEffect(this);
			break;
		case "randtime":
			effect = new RandTime(this);
			break;
		case "customeffect":
			effect = new CustomEffect(this);
			break;
		default:
			return Tip(msg.getSon("效果商店", "无法获取项目类型", k, d));
		}
		return effect.MakeSecondary();
	}

	@Override
	public boolean disMain(FormResponseCustom data) {
		return effect.disSecondary(data);
	}

}
