package xiaokai.bemilk.set;

import xiaokai.bemilk.MakeForm;
import xiaokai.bemilk.tool.ItemID;
import xiaokai.bemilk.tool.SimpleForm;
import xiaokai.bemilk.tool.Tool;

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
public class delItem extends BasesetForm {
	private Map<String, Object> Items;
	private List<String> Keys;
	private ArrayList<String> kKList;

	/**
	 * 删除自定义物品数据
	 * 
	 * @param player
	 */
	public delItem(Player player) {
		super(player);
		config = ItemID.getConfig();
		Items = config.getAll();
		Keys = new ArrayList<>(Items.keySet());
	}

	@Override
	public boolean makeMain() {
		SimpleForm form = new SimpleForm(kick.formID.getID(35), "删除自定义物品设置");
		kKList = new ArrayList<>();
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
			return MakeForm.Tip(player, Tool.getRandColor() + "当前还没有任何自定义添加的物品！快去添加一个吧！");
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse d) {
		int ID = ((FormResponseSimple) d).getClickedButtonId();
		Items.remove(kKList.get(ID));
		config.setAll((LinkedHashMap<String, Object>) Items);
		boolean isok = config.save();
		ItemID.load();
		return MakeForm.Tip(player, "§6操作" + (isok ? "§e成功！" : "§4失败。"), isok);
	}
}
