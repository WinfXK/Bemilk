package xiaokai.bemilk.set.effect;

import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.set.BasesetForm;
import xiaokai.bemilk.tool.Tool;
import xiaokai.bemilk.tool.data.Effectrec;
import xiaokai.bemilk.tool.form.SimpleForm;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseSimple;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class delEffect extends BasesetForm {
	private Map<String, Object> Items;
	private List<String> Keys;
	private List<String> kKList;

	/**
	 * 删除自定义效果
	 * 
	 * @param player
	 */
	public delEffect(Player player) {
		super(player);
		config = Effectrec.getConfig();
		Items = config.getAll();
		Keys = new ArrayList<>(Items.keySet());
	}

	@Override
	public boolean makeMain() {
		SimpleForm form = new SimpleForm(kick.formID.getID(35), "删除自定义效果设置");
		for (String Key : Keys)
			try {
				Map<String, Object> map = (Map<String, Object>) Items.get(Key);
				String Name = Tool.objToString(map.get("Name"));
				String Path = Tool.objToString(map.get("Path"));
				int ID = Tool.ObjectToInt(map.get("ID"));
				form.addButton("§6" + Name + "§f(§4" + ID + "§f)", true, Path);
				kKList.add(Key);
			} catch (Exception e) {
				player.sendMessage("§4自定义效果Key：" + Key + "§6的数据错误！");
				e.printStackTrace();
				kick.mis.getLogger().error("§4自定义效果Key：" + Key + "§6的数据错误！" + e.getMessage());
			}
		if (form.getButtonSize() < 1)
			return MakeForm.Tip(player, Tool.getRandColor() + "当前还没有任何自定义添加的药水效果！快去添加一个吧！");
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse d) {
		int ID = ((FormResponseSimple) d).getClickedButtonId();
		Items.remove(kKList.get(ID));
		config.setAll((LinkedHashMap<String, Object>) Items);
		boolean isok = config.save();
		Effectrec.reload();
		return MakeForm.Tip(player, "§6操作" + (isok ? "§e成功！" : "§4失败。"), isok);
	}
}
