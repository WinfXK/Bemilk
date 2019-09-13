package xiaokai.bemilk.shop.open.type.effect;

import xiaokai.bemilk.shop.open.BaseDis;
import xiaokai.bemilk.shop.open.BaseSecondary;
import xiaokai.bemilk.tool.Tool;
import xiaokai.bemilk.tool.data.Effectrec;

import java.util.List;

import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.potion.Effect;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class RandEffect extends BaseSecondary {
	private List<Integer> IDs;
	private int Level;
	private int Time;

	/**
	 * 随机效果但定级定时的效果项目处理类
	 * 
	 * @param data
	 */
	public RandEffect(BaseDis data) {
		super(data);
		IDs = (List<Integer>) Item.get("ID");
		Level = Tool.ObjectToInt(Item.get("Level"));
		Time = Tool.ObjectToInt(Item.get("EffectTime"));
	}

	@Override
	public boolean MakeSecondary() {
		int ID = IDs.get(Tool.getRand(0, IDs.size() - 1));
		String[] k = { "{Player}", "{MyMoney}", "{EffectName}", "{EffectID}", "{EffectCount}", "{Level}", "{LastTime}",
				"{Money}", "{Effects}" };
		Object[] d = { player.getName(), data.MyMoney(), Effectrec.getName(ID), ID, Level, Time, Money, getEffects() };
		String Content = msg.getSun("效果商店", "定级随机效果", "内容", k, d);
		form.addLabel(Content);
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disSecondary(FormResponseCustom data) {
		int ID = IDs.get(Tool.getRand(0, IDs.size() - 1));
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
		return this.data.send(msg.getSun("效果商店", "定级随机效果", "购买成功", k, d));
	}

	/**
	 * 返回效果文本列表
	 * 
	 * @return
	 */
	public String getEffects() {
		String string = "";
		String[] k = { "{Player}", "{MyMoney}", "{EffectName}", "{EffectID}", "{Level}", "{LastTime}", "{Money}" };
		for (Integer i : IDs)
			string += msg.getSun("效果商店", "定级随机效果", "效果项目", k, new Object[] { player.getName(), this.data.MyMoney(),
					Effectrec.getName(i), i, Level, Time, Money });
		return string;
	}
}
