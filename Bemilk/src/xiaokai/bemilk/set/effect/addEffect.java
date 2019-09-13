package xiaokai.bemilk.set.effect;

import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.set.BasesetForm;
import xiaokai.bemilk.tool.Tool;
import xiaokai.bemilk.tool.data.Effectrec;
import xiaokai.bemilk.tool.form.CustomForm;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class addEffect extends BasesetForm {
	private Map<String, Object> Items;

	/**
	 * 添加自定义效果设置
	 * 
	 * @param player
	 */
	public addEffect(Player player) {
		super(player);
		config = Effectrec.getConfig();
		Items = config.getAll();
	}

	@Override
	public boolean makeMain() {
		CustomForm form = new CustomForm(kick.formID.getID(35), "添加自定义效果设置");
		form.addInput("§6自定义效果的ID");
		form.addInput("§6自定义效果的名称");
		form.addInput("§6自定义效果的贴图路径");
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse d) {
		FormResponseCustom data = (FormResponseCustom) d;
		String string = data.getInputResponse(0);
		if (string == null || string.isEmpty() || !Tool.isInteger(string))
			return MakeForm.Tip(player, "§6请输入一个纯整数的效果ID");
		int ID = Tool.ObjectToInt(string);
		String Name = data.getInputResponse(1);
		String Path = data.getInputResponse(2);
		Map<String, Object> map = new HashMap<>();
		map.put("ID", ID);
		map.put("Name", Name);
		map.put("Path", Path);
		Items.put(getKey(1), map);
		config.setAll((LinkedHashMap<String, Object>) Items);
		boolean isok = config.save();
		Effectrec.reload();
		return MakeForm.Tip(player, "§6操作" + (isok ? "§e成功！" : "§4失败。"), isok);
	}

	/**
	 * 取得一个随机Key
	 * 
	 * @param JJLength
	 * @return
	 */
	public String getKey(int JJLength) {
		String string = "";
		for (int i = 0; i < JJLength; i++)
			string += Tool.getRandString("qwertyuiop[]asdfghjkl;zxcvbnm,./*-+~!@#$%^&*()_+");
		if (Items.containsKey(string))
			return getKey(JJLength++);
		return string;
	}
}
