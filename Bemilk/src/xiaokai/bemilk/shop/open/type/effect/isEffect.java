package xiaokai.bemilk.shop.open.type.effect;

import xiaokai.bemilk.shop.open.BaseDis;
import xiaokai.bemilk.shop.open.BaseSecondary;
import xiaokai.bemilk.tool.Effectrec;
import xiaokai.bemilk.tool.Tool;

import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.potion.Effect;

/**
 * @author Winfxk
 */
public class isEffect extends BaseSecondary {
	private int ID;
	private int Level;
	private int Time;

	/**
	 * 处理打开的是定效定时定等级的效果项目
	 * 
	 * @param data
	 */
	public isEffect(BaseDis data) {
		super(data);
		ID = Tool.ObjectToInt(Item.get("ID"));
		Level = Tool.ObjectToInt(Item.get("Level"));
		Time = Tool.ObjectToInt(Item.get("EffectTime"));
	}

	@Override
	public boolean MakeSecondary() {
		String[] k = { "{Player}", "{MyMoney}", "{EffectName}", "{EffectID}", "{Level}", "{LastTime}", "{Money}" };
		Object[] d = { player.getName(), data.MyMoney(), Effectrec.getName(ID), ID, Level, Time, Money };
		String Content = msg.getSun("效果商店", "定点出售", "内容", k, d);
		form.addLabel(Content);
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disSecondary(FormResponseCustom data) {
		if (this.data.MyMoney() < Money)
			this.data.Tip(msg.getSon("效果商店", "钱不足", this.data.k, this.data.d));
		Effect effect = Effectrec.getEffect(ID);
		double reduceMoney = this.data.reduceMoney(Money);
		effect.setDuration(Time * 20);
		effect.setAmplifier(Level);
		player.addEffect(effect);
		String[] k = { "{Player}", "{MyMoney}", "{EffectName}", "{EffectID}", "{Level}", "{LastTime}", "{Money}",
				"{reduceMoney}" };
		Object[] d = { player.getName(), this.data.MyMoney(), Effectrec.getName(ID), ID, Level, Time, Money,
				reduceMoney };
		return this.data.send(msg.getSun("效果商店", "定点出售", "购买成功", k, d));
	}

}
