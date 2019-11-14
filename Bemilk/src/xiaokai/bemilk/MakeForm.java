package xiaokai.bemilk;

import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.bemilk.shop.Shop;
import xiaokai.bemilk.tool.CustomForm;
import xiaokai.bemilk.tool.ModalForm;
import xiaokai.bemilk.tool.SimpleForm;
import xiaokai.bemilk.tool.Tool;

import java.io.File;
import java.io.FilenameFilter;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;

import me.onebone.economyapi.EconomyAPI;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class MakeForm implements FilenameFilter {
	private static Message msg = Kick.kick.Message;
	private static Kick kick = Kick.kick;

	/**
	 * 插件属性设置
	 * 
	 * @param player
	 * @return
	 */
	public static boolean Setting(Player player) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		SimpleForm form = new SimpleForm(kick.formID.getID(33), Tool.getColorFont("系统设置"), "§6请选择您想要设置的选项");
		List<String> AdminList = new ArrayList<>();
		form.addButton(Tool.getRandColor() + "配置设置");
		AdminList.add("set");
		form.addButton(Tool.getRandColor() + "自定义物品");
		AdminList.add("item");
		form.addButton(Tool.getRandColor() + "自定义效果");
		AdminList.add("effect");
		form.addButton(Tool.getRandColor() + "个人商店物品黑名单");
		AdminList.add("msbl");
		myPlayer.ExtraKeys = AdminList;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 商店分页点开的搜索
	 * 
	 * @param player
	 * @param file
	 * @return
	 */
	public static boolean OpenShopFoSeek(Player player, File file) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.file = file;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		CustomForm form = new CustomForm(kick.formID.getID(30), msg.getSon("搜索", "标题", DsK, DsO));
		form.addInput(msg.getSon("搜索", "关键字", DsK, DsO));
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 创建一个界面给玩家搜索，这个界面是来至主页
	 * 
	 * @param player
	 * @return
	 */
	public static boolean Seek(Player player) {
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		CustomForm form = new CustomForm(kick.formID.getID(29), msg.getSon("搜索", "标题", DsK, DsO));
		form.addInput(msg.getSon("搜索", "关键字", DsK, DsO));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean accept(File dir, String name) {
		File file = new File(dir, name);
		return file.isFile();
	}

	/**
	 * 显示更多设置页面
	 * 
	 * @param player 要显示更多设置页面的玩家对象
	 * @return
	 */
	public static boolean MoreSettings(Player player) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		Config config = kick.ShopConfig;
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(1), kick.Message.getText(config.get("Title"), DsK, DsO),
				kick.Message.getText(config.get("Content"), DsK, DsO));
		Object object = config.get("Shops");
		Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) object;
		List<String> AdminList = new ArrayList<>();
		if (Shops.size() > 0) {
			AdminList.add("seek");
			form.addButton(msg.getSun("界面", "主页", "搜索按钮", DsK, DsO));
		}
		AdminList.add("mydata");
		form.addButton(msg.getSon("界面", "个人数据", DsK, DsO));
		if (Kick.isAdmin(player)) {
			AdminList.add("add");
			form.addButton(Tool.getRandColor() + "添加商店");
			if (Shops.size() > 0) {
				AdminList.add("del");
				form.addButton(Tool.getRandColor() + "删除商店");
				AdminList.add("ss");
				form.addButton(Tool.getRandColor() + "设置商店");
			}
			AdminList.add("set");
			form.addButton(Tool.getRandColor() + "系统设置");
		}
		myPlayer.ExtraKeys = AdminList;
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
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		if (myPlayer.loadTime != null
				&& Duration.between(myPlayer.loadTime, Instant.now()).toMillis() < kick.config.getDouble("屏蔽玩家双击间隔"))
			return false;
		myPlayer.loadTime = Instant.now();
		Config config = kick.ShopConfig;
		Object object = config.get("Shops");
		Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<>()
				: (HashMap<String, Object>) object;
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(0), kick.Message.getText(config.get("Title"), DsK, DsO),
				kick.Message.getText(config.get("Content"), DsK, DsO));
		List<String> Keys = new ArrayList<>();
		if (Shops.size() < 1)
			form.setContent(form.getContent() + "\n" + msg.getSun("界面", "主页", "没有商店分页时显示", DsK, DsO));
		for (String ike : Shops.keySet()) {
			Map<String, Object> map = (Map<String, Object>) Shops.get(ike);
			File file = new File(new File(kick.mis.getDataFolder(), Kick.ShopConfigPath), (String) map.get("Config"));
			Config config2 = new Config(file, Config.YAML);
			if (!Kick.isAdmin(player) && (!Shop.isOk(player, file) && kick.config.getBoolean("隐藏无权商店"))
					|| (!Shop.isOkMoney(player, file) && kick.config.getBoolean("隐藏金钱数不匹配的商店")))
				continue;
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
		List<String> AdminList = new ArrayList<>();
		if (kick.config.getBoolean("折叠选项")) {
			form.addButton(msg.getSon("界面", "更多设置按钮", DsK, DsO));
			AdminList.add("ms");
		} else {
			if (Shops.size() > 0) {
				AdminList.add("seek");
				form.addButton(msg.getSun("界面", "主页", "搜索按钮", DsK, DsO));
			}
			AdminList.add("mydata");
			form.addButton(msg.getSon("界面", "个人数据", DsK, DsO));
			if (Kick.isAdmin(player)) {
				AdminList.add("add");
				form.addButton(Tool.getRandColor() + "添加商店");
				if (Shops.size() > 0) {
					AdminList.add("del");
					form.addButton(Tool.getRandColor() + "删除商店");
					AdminList.add("ss");
					form.addButton(Tool.getRandColor() + "设置商店");
				}
				AdminList.add("set");
				form.addButton(Tool.getRandColor() + "系统设置");
			}
		}
		myPlayer.ExtraKeys = AdminList;
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
		return Tip(player, Tool.getRandColor() + kick.mis.getName(), Content, false);
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
		return Tip(player, Tool.getRandColor() + kick.mis.getName(), Content, back);
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
