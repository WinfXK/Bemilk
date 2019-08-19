package xiaokai.bemilk.form;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;
import me.onebone.economyapi.EconomyAPI;
import xiaokai.bemilk.mtp.ItemID;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.bemilk.shop.Shop;
import xiaokai.tool.CustomForm;
import xiaokai.tool.SimpleForm;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class Paging {
	private static Message msg = Kick.kick.Message;
	private static Kick kick = Kick.kick;

	/**
	 * 删除商店分页
	 * 
	 * @author Winfxk
	 */
	public static class delShop {
		/**
		 * 处理玩家点击的是删除界面还是不删除
		 * 
		 * @param player
		 * @param data
		 * @return
		 */
		public static boolean dis(Player player, FormResponseSimple data) {
			if (data.getClickedButtonId() == 1) {
				MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
				Map<String, Object> Shops = (HashMap<String, Object>) kick.ShopConfig.get("Shops");
				Shops.remove(myPlayer.CacheKey);
				kick.ShopConfig.set("Shops", Shops);
				kick.ShopConfig.save();
				boolean isok = myPlayer.file.delete();
				player.sendMessage("§6删除" + (isok ? "§e成功" : "§4失败"));
				return MakeForm.Main(player);
			}
			return false;
		}

		/**
		 * 处理玩家点击的是哪一个按钮，并且创建对应的界面
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
			File file = new File(new File(kick.mis.getDataFolder(), Kick.ShopConfigPath), (String) Item.get("Config"));
			myPlayer.file = file;
			Config config = new Config(file, Config.YAML);
			String[] DsK = { "{Player}", "{Money}" };
			Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
			SimpleForm form = new SimpleForm(kick.formID.getID(6), kick.Message.getText(config.get("Title"), DsK, DsO));
			Map<String, Object> Items = (config.get("Items") == null || !(config.get("Items") instanceof Map))
					? new HashMap<String, Object>()
					: (HashMap<String, Object>) config.get("Items");
			String string = "";
			Set<String> set = config.getAll().keySet();
			for (String ike : set) {
				if (ike.equals("Items"))
					continue;
				String RandColor = Tool.getRandColor();
				string += RandColor + ike + "§f:" + RandColor + config.getString(ike) + "\n";
			}
			String itemString = "";
			if (Items.size() > 0) {
				Set<String> ItemKey = Items.keySet();
				int i = 0;
				for (String ike : ItemKey) {
					Map<String, Object> map = (Map<String, Object>) Items.get(ike);
					Map<String, Object> items = (map.get("Items") != null) && (map.get("Items") instanceof Map)
							? (HashMap<String, Object>) map.get("Items")
							: new HashMap<String, Object>();
					Set<String> temsKey = items.keySet();
					int j = 0;
					for (String kie : temsKey) {
						itemString += "§9" + ItemID.getNameByID(kie)
								+ (((i + 1) < ItemKey.size()) ? ((j + 1) < temsKey.size() ? "§f," : "") : "");
						j++;
					}
					i++;
				}
				itemString = "§f[§9" + itemString + "§f]";
			} else
				itemString = "§4没有上架任何物品";
			string += Tool.getRandColor() + "已上架物品: " + itemString + "\n\n§4您确定要删除这个分页吗？";
			form.setContent(string);
			form.addButton("§a取消");
			form.addButton("§4确定删除");
			kick.PlayerDataMap.put(player.getName(), myPlayer);
			form.sendPlayer(player);
			return true;
		}

		/**
		 * 创建一个界面供玩家选择要删除的商店分页
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
			Map<String, Object> Shops = (config.get("Shops") == null || !(config.get("Shops") instanceof Map))
					? new HashMap<String, Object>()
					: (HashMap<String, Object>) config.get("Shops");
			if (Shops.size() < 1)
				return MakeForm.Tip(player, "§4当前还没有任何一个商店分页");
			String[] DsK = { "{Player}", "{Money}" };
			Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
			SimpleForm form = new SimpleForm(kick.formID.getID(5), kick.Message.getText(config.get("Title"), DsK, DsO),
					kick.Message.getText(config.get("Content"), DsK, DsO));
			List<String> Keys = new ArrayList<String>();
			for (String ike : Shops.keySet()) {
				Map<String, Object> map = (Map<String, Object>) Shops.get(ike);
				File file = new File(new File(kick.mis.getDataFolder(), Kick.ShopConfigPath),
						(String) map.get("Config"));
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
	}

	/**
	 * 设置商店分页
	 * 
	 * @author Winfxk
	 */
	public static class setShop {
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
			form.addStepSlider("按钮的图标类型", Kick.IconType,
					Float.valueOf(String.valueOf(Item.get("IconType"))).intValue());
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
			Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<String, Object>()
					: (HashMap<String, Object>) object;
			String[] DsK = { "{Player}", "{Money}" };
			Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
			SimpleForm form = new SimpleForm(kick.formID.getID(4), kick.Message.getText(config.get("Title"), DsK, DsO),
					kick.Message.getText(config.get("Content"), DsK, DsO));
			List<String> Keys = new ArrayList<String>();
			if (Shops.size() < 1)
				return MakeForm.Tip(player, "§4当前还没有任何一个商店分页");
			for (String ike : Shops.keySet()) {
				Map<String, Object> map = (Map<String, Object>) Shops.get(ike);
				File file = new File(new File(kick.mis.getDataFolder(), Kick.ShopConfigPath),
						(String) map.get("Config"));
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

	/**
	 * 添加商店分页
	 * 
	 * @author Winfxk
	 */
	public static class addShop implements FilenameFilter {
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
					+ new File(kick.mis.getDataFolder(), Kick.ShopConfigPath).list(new Paging.addShop()).length);
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
			List<String> FilteredList = new ArrayList<String>();
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
					FilterPermissions, FilteredModel, FilteredList, isMakeMyShop, IconType, IconPath, myPlayer.string,
					Key, true, isMake);
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
			if (Arrays.asList(new File(kick.mis.getDataFolder(), Kick.ShopConfigPath).list(new Paging.addShop()))
					.contains(JJSize))
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
					? new HashMap<String, Object>()
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
}
