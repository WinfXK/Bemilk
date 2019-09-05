package xiaokai.bemilk.set;

import xiaokai.tool.Tool;
import xiaokai.tool.data.Effectrec;
import xiaokai.tool.form.SimpleForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class CustomEffect extends Baseset {
	private Map<String, Object> Items;
	private List<String> Keys;

	/**
	 * 处理自定义效果的设置类
	 * 
	 * @param player
	 */
	public CustomEffect(Player player) {
		super(player);
		config = Effectrec.getConfig();
		Items = config.getAll();
		Keys = new ArrayList<>(Items.keySet());
	}

	@Override
	public boolean makeMain() {
		SimpleForm form = new SimpleForm(FormID, "自定义效果设置");
		for (String Key : Keys) {
			Map<String, Object> map = (Map<String, Object>) Items.get(Key);
		}
		if (form.getButtonSize() < 1)
			form.setContent(Tool.getRandColor() + "当前还没有任何自定义添加的药水效果！快去添加一个吧！");
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		return true;
	}
}
