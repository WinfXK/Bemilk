package xiaokai.bemilk.shop;

import xiaokai.bemilk.MakeForm;
import xiaokai.bemilk.mtp.DisPlayer;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.bemilk.tool.CustomForm;
import xiaokai.bemilk.tool.SimpleForm;
import xiaokai.bemilk.tool.Tool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;

/**
 * 设置商店分页
 * 
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class setShop {
	private static Message msg = Kick.kick.Message;
	private static Kick kick = Kick.kick;

	/**
	 * 检索玩家点击的是哪一个商店分页按钮
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean start(Player player, FormResponseSimple data) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.CacheKey = myPlayer.Keys.get(data.getClickedButtonId());
		Map<String, Object> Shops = (HashMap<String, Object>) kick.ShopConfig.get("Shops");
		Map<String, Object> Item = (Map<String, Object>) Shops.get(myPlayer.CacheKey);
		myPlayer.string = (String) Item.get("Config");
		File file = new File(new File(kick.mis.getDataFolder(), Kick.ShopConfigPath), myPlayer.string);
		Config config = new Config(file, Config.YAML);
		CustomForm form = new CustomForm(Kick.kick.formID.getID(3), Tool.getColorFont("添加分页"));
		form.addInput("请输入按钮将要显示的内容", Item.get("Text"));
		form.addInput("请输入商店界面的标题", config.get("Title"));
		form.addInput("请输入商店的文本内容", config.get("Content"));
		form.addInput("进入商店的" + kick.mis.getName() + "下限", config.get("MoneyFloor"), "当上下限均为0时不适用该功能");
		form.addInput("进入商店的" + kick.mis.getName() + "上限", config.get("MoneyLimit"), "当上下限均为0时不适用该功能");
		form.addStepSlider("过滤权限模式", Kick.FilterPermissions, config.getInt("FilterPermissions"));
		form.addStepSlider("过滤模式", Kick.FilteredModel, config.getInt("FilteredModel"));
		form.addInput("请输入要过滤的玩家名称", getString(config.getList("FilterList")), "多个使用; 分割");
		form.addToggle("允许玩家创建个人商店", config.getBoolean("isMakeMyShop"));
		form.addStepSlider("按钮的图标类型", Kick.IconType, Float.valueOf(String.valueOf(Item.get("IconType"))).intValue());
		form.addInput("请输入图标的路径", Item.get("IconPath"));
		myPlayer.isMake = false;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 创建一个界面供玩家选择要修改的商店分页
	 * 
	 * @param player
	 * @return
	 */
	public static boolean MakeForm(Player player) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		Config config = kick.ShopConfig;
		Object object = config.get("Shops");
		Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) object;
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), DisPlayer.getMoney(player.getName()) };
		SimpleForm form = new SimpleForm(kick.formID.getID(4), kick.Message.getText(config.get("Title"), DsK, DsO),
				kick.Message.getText(config.get("Content"), DsK, DsO));
		List<String> Keys = new ArrayList<>();
		if (Shops.size() < 1)
			return MakeForm.Tip(player, "§4当前还没有任何一个商店分页");
		for (String ike : Shops.keySet()) {
			Map<String, Object> map = (Map<String, Object>) Shops.get(ike);
			File file = new File(new File(kick.mis.getDataFolder(), Kick.ShopConfigPath), (String) map.get("Config"));
			Config config2 = new Config(file, Config.YAML);
			String Button = msg.getText(map.get("Text"), DsK, DsO);
			String ShopTitle = msg.getText(config2.get("Title"), DsK, DsO);
			String Content = msg.getText(config2.get("Content"), DsK, DsO);
			int IconType = Integer.valueOf(String.valueOf(map.get("IconType")));
			form.addButton(
					msg.getSun("界面", "主页", "商店分页按钮格式",
							new String[] { "{Player}", "{ButtonName}", "{ShopTitle}", "{ShopContent}" },
							new String[] { player.getName(), Button, ShopTitle, Content }),
					IconType == 2 ? false : true, IconType != 0 ? (String) map.get("IconPath") : null);
			Keys.add(ike);
		}
		myPlayer.Keys = Keys;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 将List转换为文本
	 * 
	 * @param list
	 * @return
	 */
	public static String getString(List<String> list) {
		String string = "";
		for (int i = 0; i < list.size(); i++) {
			String string2 = list.get(i);
			if (string2 != null && !string2.isEmpty())
				string += string2 + (i < (list.size() + 1) ? ";" : "");
		}
		return string;
	}
}