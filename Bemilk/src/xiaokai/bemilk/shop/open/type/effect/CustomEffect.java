package xiaokai.bemilk.shop.open.type.effect;

import xiaokai.bemilk.shop.open.BaseDis;
import xiaokai.bemilk.shop.open.BaseSecondary;
import xiaokai.tool.Tool;
import xiaokai.tool.data.Effectrec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.potion.Effect;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class CustomEffect extends BaseSecondary {
	private Map<String, Object> Effects;

	public CustomEffect(BaseDis data) {
		super(data);
		Effects = (Map<String, Object>) Item.get("Effects");
	}

	@Override
	public boolean MakeSecondary() {
		String[] k = { "{Player}", "{MyMoney}", "{Effects}", "{Money}" };
		Object[] d = { player.getName(), data.MyMoney(), getEffects(), Money };
		String Content = msg.getSun("效果商店", "自定义效果", "内容", k, d);
		form.addLabel(Content);
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disSecondary(FormResponseCustom data) {
		if (this.data.MyMoney() < Money)
			this.data.Tip(msg.getSon("效果商店", "钱不足", this.data.k, this.data.d));
		List<String> list = new ArrayList<>(Effects.keySet());
		Map<String, Object> map = (Map<String, Object>) Effects.get(list.get(Tool.getRand(0, list.size() - 1)));
		int ID = Tool.ObjectToInt(map.get("ID"));
		int Time = Tool.ObjectToInt(map.get("Time"));
		int Level = Tool.ObjectToInt(map.get("Level"));
		Effect effect = Effectrec.getEffect(ID);
		double reduceMoney = this.data.reduceMoney(Money);
		effect.setDuration(Time * 20);
		effect.setAmplifier(Level);
		player.addEffect(effect);
		String[] k = { "{Player}", "{MyMoney}", "{EffectName}", "{EffectID}", "{Level}", "{LastTime}", "{Money}",
				"{reduceMoney}" };
		Object[] d = { player.getName(), this.data.MyMoney(), Effectrec.getName(ID), ID, Level, Time, Money,
				reduceMoney };
		return this.data.send(msg.getSun("效果商店", "自定义效果", "购买成功", k, d));
	}

	public String getEffects() {
		String string = "";
		Set<String> set = Effects.keySet();
		String[] k = { "{Player}", "{MyMoney}", "{EffectName}", "{EffectID}", "{EffectLevel}", "{EffectTime}",
				"{Money}" };
		String name = player.getName();
		double MyMoney = data.MyMoney();
		for (String ike : set) {
			Map<String, Object> map = (Map<String, Object>) Effects.get(ike);
			int ID = Tool.ObjectToInt(map.get("ID"));
			string += msg.getSun("效果商店", "自定义效果", "项目", k,
					new Object[] { name, MyMoney, Effectrec.getName(ID), ID, map.get("Level"), map.get("Time") });
		}
		return string;
	}
}
