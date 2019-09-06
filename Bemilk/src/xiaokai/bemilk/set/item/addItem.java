package xiaokai.bemilk.set.item;

import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.set.BasesetForm;
import xiaokai.tool.data.ItemID;
import xiaokai.tool.form.CustomForm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class addItem extends BasesetForm {
	private Map<String, Object> Items;

	/**
	 * 添加自定义物品数据
	 * 
	 * @param player
	 */
	public addItem(Player player) {
		super(player);
		config = ItemID.getConfig();
		Items = config.getAll();
	}

	@Override
	public boolean makeMain() {
		CustomForm form = new CustomForm(kick.formID.getID(35), "添加自定义物品设置");
		form.addInput("§6自定义物品的ID");
		form.addInput("§6自定义物品的名称");
		form.addInput("§6自定义物品的贴图路径");
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse d) {
		FormResponseCustom data = (FormResponseCustom) d;
		String ID = data.getInputResponse(0);
		if (ID == null || ID.isEmpty())
			return MakeForm.Tip(player, "§6请输入物品ID");
		String Name = data.getInputResponse(1);
		String Path = data.getInputResponse(2);
		Map<String, Object> map = new HashMap<>();
		map.put("ID", ID);
		map.put("Name", Name);
		map.put("Path", Path);
		Items.put(ID, map);
		config.setAll((LinkedHashMap<String, Object>) Items);
		boolean isok = config.save();
		ItemID.load();
		return MakeForm.Tip(player, "§6操作" + (isok ? "§e成功！" : "§4失败。"), isok);
	}
}
