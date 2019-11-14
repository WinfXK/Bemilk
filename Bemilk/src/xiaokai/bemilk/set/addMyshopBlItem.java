package xiaokai.bemilk.set;

import xiaokai.bemilk.MakeForm;
import xiaokai.bemilk.mtp.Belle;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.tool.CustomForm;
import xiaokai.bemilk.tool.ItemID;
import xiaokai.bemilk.tool.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.item.Item;
import cn.nukkit.plugin.Plugin;

/**
 * @author Winfxk
 */
public class addMyshopBlItem extends BasesetForm {
	private List<Item> items = new ArrayList<>();

	public addMyshopBlItem(Player player) {
		super(player);
	}

	@Override
	public boolean makeMain() {
		CustomForm form = new CustomForm(kick.formID.getID(35), "添加个人商店黑名单");
		Map<Integer, Item> Contents = player.getInventory().getContents();
		List<String> list = new ArrayList<>();
		Plugin kis = Server.getInstance().getPluginManager().getPlugin("Knickers");
		for (Item item : Contents.values()) {
			if (Belle.isMaterials(item)
					|| (kis != null && kis.isEnabled() && xiaokai.knickers.mtp.Belle.isMaterials(item))
					|| Kick.isBL(item))
				continue;
			items.add(item);
			list.add("§6" + ItemID.getName(item) + "§4(§9" + ItemID.getID(item) + "§4)§f>§8" + item.getCount());
		}
		if (items.size() < 1)
			return MakeForm.Tip(player, "您的背包无任何可以添加黑名单的物品！");
		form.addDropdown("请选择想要添加黑名单的物品", list, 0);
		form.addToggle("忽略Nbt", false);
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse d) {
		FormResponseCustom data = (FormResponseCustom) d;
		Item item = items.get(data.getDropdownResponse(0).getElementID());
		boolean isOK = data.getToggleResponse(1);
		Map<String, Object> map = new HashMap<>();
		String Key = getKey(1);
		map.put("Skip", isOK);
		map.put("Key", Key);
		map.put("Item", Tool.saveItem(item));
		kick.MyShopBlacklist.set(Key, map);
		kick.MyShopBlacklist.save();
		return MakeForm.Tip(player, "黑名单添加成功！", true);
	}

	public String getKey(int Length) {
		String key = "";
		for (int i = 0; i < Length; i++)
			key += Tool.getRandString("qwertyuiop[]asdfghjkl;'\\zxcvbnm,./0123456789-+=()*&^%$#@!~");
		if (kick.MyShopBlacklist.exists(key))
			return getKey(Length++);
		return key;
	}
}
