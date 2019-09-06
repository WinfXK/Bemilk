package xiaokai.bemilk.set;

import xiaokai.bemilk.data.MyPlayer;
import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.set.item.addItem;
import xiaokai.bemilk.set.item.delItem;
import xiaokai.tool.Tool;
import xiaokai.tool.data.ItemID;
import xiaokai.tool.form.SimpleForm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseSimple;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class CustomItem extends Baseset {
	private Map<String, Object> Items;
	private List<String> Keys;
	private List<String> kKList;

	/**
	 * 处理自定义物品设置类
	 * 
	 * @param player
	 */
	public CustomItem(Player player) {
		super(player);
		config = ItemID.getConfig();
		Items = config.getAll();
		Keys = new ArrayList<>(Items.keySet());
	}

	@Override
	public boolean makeMain() {
		SimpleForm form = new SimpleForm(FormID, "自定义物品设置");
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
				player.sendMessage("§4自定义物品Key：" + Key + "§6的数据错误！");
				e.printStackTrace();
				kick.mis.getLogger().error("§4自定义物品Key：" + Key + "§6的数据错误！" + e.getMessage());
			}
		if (form.getButtonSize() < 1)
			form.setContent(Tool.getRandColor() + "当前还没有任何自定义添加的物品！快去添加一个吧！");
		form.addButton("添加自定义物品");
		if (form.getButtonSize() > 1)
			form.addButton("删除自定义物品");
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse d) {
		FormResponseSimple data = (FormResponseSimple) d;
		int ButtonID = data.getClickedButtonId();
		MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
		if (ButtonID >= kKList.size()) {
			myPlayer.basesetForm = ButtonID == kKList.size() ? new addItem(player) : new delItem(player);
			kick.PlayerDataMap.put(player.getName(), myPlayer);
			return myPlayer.basesetForm.makeMain();
		}
		Map<String, Object> map = (Map<String, Object>) Items.get(kKList.get(ButtonID));
		return MakeForm.Tip(player,
				"§6Name§f: §5" + map.get("Name") + "\n§6ID§f: §5" + map.get("ID") + "\n§6Path§f: " + map.get("Path"),
				true);
	}
}
