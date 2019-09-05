package xiaokai.bemilk.shop.open.type.effect;

import xiaokai.bemilk.shop.open.BaseDis;
import xiaokai.bemilk.shop.open.BaseSecondary;
import xiaokai.tool.Tool;
import xiaokai.tool.data.Effectrec;

import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.potion.Effect;

/**
 * @author Winfxk
 */
public class RandTime extends BaseSecondary {
	private int ID;
	private int Level;
	private int MinTime, MaxTime;

	/**
	 * 定级定效随机时间的效果项目处理类
	 * 
	 * @param data
	 */
	public RandTime(BaseDis data) {
		super(data);
		ID = Tool.ObjectToInt(Item.get("ID"));
		Level = Tool.ObjectToInt(Item.get("Level"));
		MinTime = Tool.ObjectToInt(Item.get("MinTime"));
		MaxTime = Tool.ObjectToInt(Item.get("MaxTime"));
	}

	@Override
	public boolean MakeSecondary() {
		String[] k = { "{Player}", "{MyMoney}", "{EffectName}", "{EffectID}", "{Level}", "{MinTime}", "{MaxTime}",
				"{Money}" };
		Object[] d = { player.getName(), data.MyMoney(), Effectrec.getName(ID), ID, Level, MinTime, MaxTime, Money };
		String Content = msg.getSun("效果商店", "定级定效随机时间", "内容", k, d);
		form.addLabel(Content);
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disSecondary(FormResponseCustom data) {
		if (this.data.MyMoney() < Money)
			this.data.Tip(msg.getSon("效果商店", "钱不足", this.data.k, this.data.d));
		int Time = Tool.getRand(MinTime, MaxTime);
		Effect effect = Effectrec.getEffect(ID);
		double reduceMoney = this.data.reduceMoney(Money);
		effect.setDuration(Time * 20);
		effect.setAmplifier(Level);
		player.addEffect(effect);
		String[] k = { "{Player}", "{MyMoney}", "{EffectName}", "{EffectID}", "{Level}", "{LastTime}", "{Money}",
				"{reduceMoney}" };
		Object[] d = { player.getName(), this.data.MyMoney(), Effectrec.getName(ID), ID, Level, Time, Money,
				reduceMoney };
		return this.data.send(msg.getSun("效果商店", "定级定效随机时间", "购买成功", k, d));
	}
}
