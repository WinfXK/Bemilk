package xiaokai.bemilk.shop;

import xiaokai.bemilk.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.bemilk.tool.CustomForm;
import xiaokai.bemilk.tool.ItemID;
import xiaokai.bemilk.tool.Tool;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

/**
*@author Winfxk
*/
/**
 * 添加商店分页
 * 
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class addShop implements FilenameFilter {
	private static Message msg = Kick.kick.Message;
	private static Kick kick = Kick.kick;

	/**
	 * 创建一个界面供玩家创建一个商店分页
	 * 
	 * @param player
	 * @return
	 */
	public static boolean MakeForm(Player player) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		CustomForm form = new CustomForm(Kick.kick.formID.getID(3), Tool.getColorFont("添加分页"));
		form.addInput("请输入按钮将要显示的内容", Tool.getRandColor() + "商店"
				+ new File(kick.mis.getDataFolder(), Kick.ShopConfigPath).list(new addShop()).length);
		form.addInput("请输入商店界面的标题");
		form.addInput("请输入商店的文本内容");
		form.addInput("进入商店的" + kick.mis.getName() + "下限", "0", "当上下限均为0时不适用该功能");
		form.addInput("进入商店的" + kick.mis.getName() + "上限", "0", "当上下限均为0时不适用该功能");
		form.addStepSlider("过滤权限模式", Kick.FilterPermissions);
		form.addStepSlider("过滤模式", Kick.FilteredModel);
		form.addInput("请输入要过滤的玩家名称", "", "多个使用; 分割");
		form.addToggle("允许玩家创建个人商店", true);
		form.addStepSlider("按钮的图标类型", Kick.IconType);
		form.addInput("请输入图标的路径", getHandItemID(player));
		myPlayer.string = null;
		myPlayer.isMake = true;
		myPlayer.CacheKey = null;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 处理玩家创建商店分页发回的数据
	 * 
	 * @param player 发回数据的玩家对象
	 * @param data   发回的数据
	 * @return
	 */
	public static boolean Dispose(Player player, FormResponseCustom data) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		String ButtonName = data.getInputResponse(0);
		if (ButtonName == null || ButtonName.isEmpty())
			ButtonName = Tool.getRandColor() + "商店" + Tool.getDate();
		String ShopTitle = data.getInputResponse(1);
		if (ShopTitle == null || ShopTitle.isEmpty())
			ShopTitle = ButtonName;
		String ShopContent = data.getInputResponse(2);
		if (ShopContent == null || ShopContent.isEmpty())
			ShopContent = "";
		String moneysString = data.getInputResponse(3);
		String moneydString = data.getInputResponse(4);
		double MoneyFloor = 0d, MoneyLimit = 0d;
		if (moneysString != null && !moneysString.isEmpty() && Tool.isInteger(moneysString) && moneydString != null
				&& !moneydString.isEmpty() && Tool.isInteger(moneydString)) {
			MoneyFloor = Double.valueOf(moneysString);
			MoneyLimit = Double.valueOf(moneydString);
			if (MoneyLimit < MoneyFloor || MoneyFloor < 0 || MoneyLimit < 0) {
				MoneyFloor = 0d;
				MoneyLimit = 0d;
			}
		}
		int FilterPermissions = data.getStepSliderResponse(5).getElementID();
		int FilteredModel = data.getStepSliderResponse(6).getElementID();
		String FilteredListString = data.getInputResponse(7);
		List<String> FilteredList = new ArrayList<>();
		if (FilteredListString != null && !FilteredListString.isEmpty()) {
			if (FilteredListString.contains(";")) {
				List<String> List = Arrays.asList(FilteredListString.split(";"));
				for (String string : List)
					if (string != null && !string.isEmpty() && !List.contains(string))
						List.add(string);
			} else
				FilteredList.add(FilteredListString);
		}
		boolean isMakeMyShop = data.getToggleResponse(8);
		int IconType = data.getStepSliderResponse(9).getElementID();
		String IconPath = ItemID.UnknownToPath(data.getInputResponse(10));
		boolean isMake = myPlayer.isMake;
		String Key = getKey(1);
		if (!isMake)
			Key = myPlayer.CacheKey;
		else
			myPlayer.string = getFileName(1);
		boolean isok = Shop.addShopWindow(player, ButtonName, ShopTitle, ShopContent, MoneyFloor, MoneyLimit,
				FilterPermissions, FilteredModel, FilteredList, isMakeMyShop, IconType, IconPath, myPlayer.string, Key,
				true, isMake);
		myPlayer.string = null;
		myPlayer.isMake = true;
		myPlayer.CacheKey = null;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		return isok;
	}

	/**
	 * 随机获取一个不重复的商店分页菜单名
	 * 
	 * @param JJLength
	 * @return
	 */
	public static String getFileName(int JJLength) {
		String JJSize = "";
		JJLength = JJLength < 1 ? 1 : JJLength;
		for (int i = 0; i < JJLength; i++)
			JJSize += Tool.getRandString();
		if (Arrays.asList(new File(kick.mis.getDataFolder(), Kick.ShopConfigPath).list(new addShop())).contains(JJSize))
			return getFileName(JJLength++);
		return JJSize + ".yml";
	}

	/**
	 * 随机获取一个不重复的按钮Key
	 * 
	 * @param JJLength 按钮Key的初始长度
	 * @return
	 */
	public static String getKey(int JJLength) {
		JJLength = JJLength < 1 ? 1 : JJLength;
		String JJSize = "";
		for (int i = 0; i < JJLength; i++)
			JJSize += Tool.getRandString("abcdefghijklmnopqrstuvwxyz~/\\|,.<>{};");
		Config config = kick.ShopConfig;
		Map<String, Object> map = (config.get("Shops") == null || !(config.get("Shops") instanceof Map))
				? new HashMap<>()
				: (HashMap<String, Object>) config.get("Shops");
		if (map.containsKey(JJSize))
			return getKey(JJLength++);
		return JJSize;
	}

	/**
	 * 获取当前玩家手上的物品ID
	 * 
	 * @param player
	 * @return
	 */
	public static String getHandItemID(Player player) {
		Item item = player.getInventory().getItemInHand();
		return item == null ? "0:0" : (item.getId() != 0 ? (item.getId() + ":" + item.getDamage()) : "1:0");
	}

	/**
	 * 文件列表过滤过滤规则方法
	 */
	@Override
	public boolean accept(File dir, String name) {
		return new File(dir, name).isFile();
	}
}