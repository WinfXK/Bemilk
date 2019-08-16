package xiaokai.bemilk.form;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import me.onebone.economyapi.EconomyAPI;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.tool.ModalForm;
import xiaokai.tool.SimpleForm;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class MakeForm {
	private static Message msg = Kick.kick.Message;

	public static boolean Setting(Player player) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		return true;
	}

	/**
	 * 打开一个商店分页
	 * 
	 * @param player 要显示上点分页的玩家对象
	 * @param file   要显示的商店分页的文件对象
	 * @return
	 */
	public static boolean OpenShop(Player player, File file) {
		Kick kick = Kick.kick;
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		Config config = new Config(file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(2), kick.Message.getText(config.get("Title"), DsK, DsO),
				kick.Message.getText(config.get("Content"), DsK, DsO));
		
		return true;
	}

	/**
	 * 显示更多设置页面
	 * 
	 * @param player 要显示更多设置页面的玩家对象
	 * @return
	 */
	public static boolean MoreSettings(Player player) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		Kick kick = Kick.kick;
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		Config config = new Config(myPlayer.file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(1), kick.Message.getText(config.get("Title"), DsK, DsO),
				kick.Message.getText(config.get("Content"), DsK, DsO));
		List<String> AdminList = new ArrayList<String>();
		AdminList.add("add");
		form.addButton(Tool.getRandColor() + "添加商店");
		AdminList.add("del");
		form.addButton(Tool.getRandColor() + "删除商店");
		AdminList.add("ss");
		form.addButton(Tool.getRandColor() + "设置商店");
		AdminList.add("set");
		form.addButton(Tool.getRandColor() + "系统设置");
		myPlayer.AdminKeys = AdminList;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 商店主页
	 * 
	 * @param player 要显示商店主页的玩家对象
	 * @return
	 */
	public static boolean Main(Player player) {
		Kick kick = Kick.kick;
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		if (myPlayer.loadTime != null
				&& Duration.between(myPlayer.loadTime, Instant.now()).toMillis() < kick.config.getDouble("屏蔽玩家双击间隔"))
			return false;
		myPlayer.loadTime = Instant.now();
		File file = new File(kick.mis.getDataFolder(), kick.ShopConfigName);
		Config config = new Config(file, Config.YAML);
		Map<String, Object> Shops = (config.get("Shop") == null || !(config.get("Shop") instanceof Map))
				? new HashMap<String, Object>()
				: (HashMap<String, Object>) config.get("Shops");
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(0), kick.Message.getText(config.get("Title"), DsK, DsO),
				kick.Message.getText(config.get("Content"), DsK, DsO));
		List<String> Keys = new ArrayList<String>();
		if (Shops.size() < 1)
			form.setContent(form.getContent() + "\n" + msg.getSun("界面", "主页", "没有商店分页时显示", DsK, DsO));
		for (String ike : Shops.keySet()) {
			Keys.add(ike);
			Map<String, Object> map = (Map<String, Object>) Shops.get(ike);
			Config config2 = new Config(
					new File(new File(kick.mis.getDataFolder(), Kick.ShopConfigPath), (String) map.get("Config")),
					Config.YAML);
			String Button = msg.getText(map.get("Text"), DsK, DsO);
			String ShopTitle = msg.getText(config2.get("Title"), DsK, DsO);
			String Content = msg.getText(config2.get("Content"), DsK, DsO);
			form.addButton(
					msg.getSun("界面", "主页", "商店分页按钮格式",
							new String[] { "{Player}", "{ButtonName}", "{ShopTitle}", "{ShopContent}" },
							new String[] { player.getName(), Button, ShopTitle, Content }),
					map.get("IconType").equals("2"), (String) map.get("IconPath"));
		}
		myPlayer.AdminKeys = null;
		if (Kick.isAdmin(player)) {
			List<String> AdminList = new ArrayList<String>();
			if (kick.config.getBoolean("折叠选项")) {
				form.addButton("更多设置");
				AdminList.add("ms");
			} else {
				AdminList.add("add");
				form.addButton(Tool.getRandColor() + "添加商店");
				AdminList.add("del");
				form.addButton(Tool.getRandColor() + "删除商店");
				AdminList.add("ss");
				form.addButton(Tool.getRandColor() + "设置商店");
				AdminList.add("set");
				form.addButton(Tool.getRandColor() + "系统设置");
			}
			myPlayer.AdminKeys = AdminList;
		}
		myPlayer.file = file;
		myPlayer.Keys = Keys;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Content 弹窗的内容
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Content) {
		return Tip(player, Tool.getRandColor() + Kick.kick.mis.getName(), Content, false);
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Content 弹窗的内容
	 * @param back    返回的布尔值
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Content, boolean back) {
		return Tip(player, Tool.getRandColor() + Kick.kick.mis.getName(), Content, back);
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Title   弹窗的标题
	 * @param Content 弹窗的内容
	 * @param back    返回的布尔值
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Title, String Content, boolean back) {
		return Tip(player, Title, Content, back, true);
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Title   弹窗的标题
	 * @param Content 弹窗的内容
	 * @param back    返回的布尔值
	 * @param Modal   是否是Modal型弹窗
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Title, String Content, boolean back, boolean Modal) {
		return Tip(player, Title, Content, "确定", "取消", back, Modal);
	}

	/**
	 * 显示一个弹窗
	 * 
	 * @param player  要显示弹窗的玩家对象
	 * @param Title   弹窗的标题
	 * @param Content 弹窗的内容
	 * @param Button1 弹窗的第一个按钮文本内容
	 * @param Button2 弹窗的第二个按钮文本内容
	 * @param back    返回的布尔值
	 * @param Modal   是否是Modal型弹窗
	 * @return <b>back</b>
	 */
	public static boolean Tip(Player player, String Title, String Content, String Button1, String Button2, boolean back,
			boolean Modal) {
		if (Modal) {
			ModalForm form = new ModalForm(Tool.getRand(), Title, Button1, Button2);
			form.setContent(Content);
			form.sendPlayer(player);
		} else {
			SimpleForm form = new SimpleForm(Tool.getRand(), Title, Content);
			form.addButton(Button1).addButton(Button2);
			form.sendPlayer(player);
		}
		return back;
	}
}
