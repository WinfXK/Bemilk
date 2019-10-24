package xiaokai.bemilk.shop.add.effect;

import xiaokai.bemilk.shop.addItem;
import xiaokai.bemilk.tool.Effectrec;
import xiaokai.bemilk.tool.Tool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class CustomEffect extends makeBaseEffect {
	/**
	 * 处理添加的项目是可完全自定义数据的效果商店项目的事件处理类
	 * 
	 * @param player
	 * @param file
	 */
	public CustomEffect(Player player, File file) {
		super(player, file);
	}

	@Override
	public boolean makeMain() {
		form.addInput(
				"§6请输入想要添加的效果ID，多个使用；分割\n§6列：\n§8生命恢复§f>§7等级§f>§5持续时间§f;§8瞬间伤害§f>§7等级§f>§5持续时间\n§81§f>§7等级§f>§5持续时间§f;§82§f>§7等级§f>§5持续时间\n"
						+ Effectrec.getEffectString());
		form.addInput("请输入购买该效果需要的价格");
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponseCustom data) {
		Map<String, Map<String, Object>> map = new HashMap<>();
		String string = data.getInputResponse(0);
		if (string == null || string.isEmpty())
			return Tip("§4请输入想要上架的药水效果的数据！");
		String[] strings = string.split(";");
		for (String s : strings) {
			if (s == null || s.isEmpty())
				continue;
			Map<String, Object> item = new HashMap<>();
			String[] ss = s.split(">");
			if (ss[0] == null || ss[0].isEmpty())
				continue;
			int ID = Effectrec.UnknownToID(ss[0], -1);
			if (ID == -1)
				continue;
			item.put("ID", ID);
			item.put("Level", ss.length > 1 ? Tool.ObjectToInt(ss[1], 1) : 1);
			item.put("Time", ss.length > 2 ? Tool.ObjectToInt(ss[2], 10) : 10);
			map.put(getKey(map, 1), item);
		}
		if (map.size() < 1)
			return Tip("§4无法解析获取药水效果ID");
		string = data.getInputResponse(1);
		if (string == null || string.isEmpty())
			return Tip("§4请输入购买价格！");
		int Money = 0;
		if (!Tool.isInteger(string) || (Money = Tool.ObjectToInt(string)) < 1)
			return Tip("§4购买价格只能为大于零的纯整数！");
		boolean isOK = new addItem(player, file).addEffectByCustomEffect(map, Money);
		return Tip(isOK ? "您成功创建了一个随机数据的效果商店！" : "§4创建貌似出现了一点问题！", isOK);
	}

	/**
	 * 随机获取一个不重复的效果的项目Key值
	 * 
	 * @param map      附魔的项目集合
	 * @param JJlength Key的初始长度
	 * @return
	 */
	public static String getKey(Map<String, Map<String, Object>> map, int JJlength) {
		String string = "";
		for (int i = 0; i < JJlength; i++)
			string += Tool.getRandString("qwertyuiopasdfghjklzxcvbnm,./;*+'][=-");
		if (map.containsKey(string))
			return getKey(map, JJlength++);
		return string;
	}
}
