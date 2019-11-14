package xiaokai.bemilk.set;

import xiaokai.bemilk.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.bemilk.shop.delShopItem;
import xiaokai.bemilk.tool.ItemID;
import xiaokai.bemilk.tool.SimpleForm;
import xiaokai.bemilk.tool.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.item.Item;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class delMyshopBlItem extends BasesetForm {
	List<String> Keys;

	public delMyshopBlItem(Player player) {
		super(player);
	}

	@Override
	public boolean makeMain() {
		Map<String, Object> map2, map = kick.MyShopBlacklist.getAll();
		if (map.size() < 1)
			return MakeForm.Tip(player, "您还咩有添加任何一个物品为黑名单！");
		SimpleForm form = new SimpleForm(kick.formID.getID(35), "删除自定义物品设置", "请点击您想要删除的黑名单项目");
		Keys = new ArrayList<>(map.keySet());
		Item item;
		for (Object obj : map.values()) {
			map2 = obj != null && (obj instanceof Map) ? (HashMap<String, Object>) obj : new HashMap<>();
			if (map2.size() < 0)
				continue;
			item = Tool.loadItem((Map<String, Object>) map2.get("Item"));
			form.addButton("§6" + ItemID.getName(item) + "§4(§9" + ItemID.getID(item) + "§4)§f>§8" + item.getCount()
					+ "§e:§8" + (Tool.ObjToBool(map2.get("Skip")) ? "忽略NBt" : "不忽略NBt"));
		}
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse d) {
		String Key = Keys.get(((FormResponseSimple) d).getClickedButtonId());
		Map<String, Object> map = (Map<String, Object>) kick.MyShopBlacklist.get(Key);
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.CacheKey = Key;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		new SimpleForm(kick.formID.getID(36), "警告",
				"§e确定要删除这个黑名单项目吗？\n\n" + delShopItem.MapTosString(map, "", Tool.getRandColor()))
						.addButton(Tool.getRandColor() + "确定").addButton(Tool.getRandColor() + "取消").sendPlayer(player);
		return true;
	}

	/**
	 * 玩家确定时候要删除黑名单物品
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean Del(Player player, FormResponseSimple data) {
		if (data.getClickedButtonId() != 0)
			return false;
		MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
		Kick.kick.MyShopBlacklist.remove(myPlayer.CacheKey);
		boolean kick = Kick.kick.MyShopBlacklist.save();
		return MakeForm.Tip(player, "§6删除" + (kick ? "§e成功" : "§4失败") + "!", kick);
	}
}
