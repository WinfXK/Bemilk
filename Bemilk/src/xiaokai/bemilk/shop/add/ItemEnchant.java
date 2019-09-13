package xiaokai.bemilk.shop.add;

import xiaokai.bemilk.data.Message;
import xiaokai.bemilk.data.MyPlayer;
import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.shop.addItem;
import xiaokai.bemilk.tool.Tool;
import xiaokai.bemilk.tool.data.EnchantName;
import xiaokai.bemilk.tool.form.CustomForm;
import xiaokai.bemilk.tool.form.SimpleForm;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;

import me.onebone.economyapi.EconomyAPI;

/**
*@author Winfxk
*/
/**
 * 物品附魔处理类
 * 
 * @author Winfxk
 */
public class ItemEnchant {
	private static Kick kick = Kick.kick;
	private static Message msg = Kick.kick.Message;

	/**
	 * 处理附魔项目设置界面回调的数据
	 * 
	 * @author Winfxk
	 */
	public static class Dis {
		private Player player;
		private FormResponseCustom data;
		private MyPlayer myPlayer;

		/**
		 * 处理附魔项目设置界面回调的数据
		 * 
		 * @param player
		 * @param data
		 */
		public Dis(Player player, FormResponseCustom data) {
			this.player = player;
			this.data = data;
			myPlayer = kick.PlayerDataMap.get(player.getName());
		}

		/**
		 * 处理万家添加附魔项目发回的数据
		 * 
		 * @param player
		 * @param data
		 * @return
		 */
		public boolean disMakeForm() {
			switch (myPlayer.string) {
			case "MakeFormLevelRand":
				return MakeFormLevelRand();
			case "MakeFormByCustom":
				return MakeFormByCustom();
			case "MakeFormLevel":
				return MakeFormLevel();
			}
			return true;
		}

		/**
		 * 添加的是定级附魔
		 * 
		 * @return
		 */
		private boolean MakeFormLevel() {
			String string = data.getInputResponse(2);
			if (string == null || string.isEmpty())
				return MakeForm.Tip(player, "§4请输入附魔的价格");
			double Money = 0;
			if (!Tool.isInteger(string) || (Money = Double.valueOf(string)) < 1)
				return MakeForm.Tip(player, "§4附魔价格仅支持大于零的纯数字！");
			int EnchantID = data.getDropdownResponse(0).getElementID();
			int EnchantLevel = data.getDropdownResponse(1).getElementID();
			boolean isTool = data.getToggleResponse(3);
			boolean isOK;
			player.sendMessage("§6您" + ((isOK = new addItem(player, myPlayer.file).addEnchantLevel(EnchantID,
					EnchantLevel, Money, isTool)) ? "§e成功" : "§4未成功") + "§6创建一个附魔商店");
			return isOK;
		}

		/**
		 * 添加的是定级但是可以设置概率的附魔
		 * 
		 * @param player
		 * @return
		 */
		private boolean MakeFormLevelRand() {
			String string = data.getInputResponse(5);
			if (string == null || string.isEmpty())
				return MakeForm.Tip(player, "§4请输入附魔的价格");
			double Money = 0;
			if (!Tool.isInteger(string) || (Money = Double.valueOf(string)) < 1)
				return MakeForm.Tip(player, "§4附魔价格仅支持大于零的纯数字！");
			int EnchantID = data.getDropdownResponse(0).getElementID();
			int EnchantLevel = data.getDropdownResponse(1).getElementID();
			string = data.getInputResponse(2);
			if (string == null || string.isEmpty())
				return MakeForm.Tip(player, "§4请输入附魔的概率");
			double EnchantRand = 0;
			if (!Tool.isInteger(string) || (EnchantRand = Double.valueOf(string)) < 1)
				return MakeForm.Tip(player, "§4附魔概率仅支持大于零的纯数字！");
			int SBEnchantID = data.getDropdownResponse(3).getElementID();
			SBEnchantID = (SBEnchantID > (EnchantName.getNameList().size() - 1)) ? -1 : SBEnchantID;
			int SBEnchantLevel = data.getDropdownResponse(4).getElementID();
			boolean isTool = data.getToggleResponse(6);
			boolean isOK;
			player.sendMessage("§6您"
					+ ((isOK = new addItem(player, myPlayer.file).addEnchantLevelRand(EnchantID, EnchantLevel,
							EnchantRand, SBEnchantID, SBEnchantLevel, isTool, Money)) ? "§e成功" : "§4未成功")
					+ "§6创建一个附魔商店");
			return isOK;
		}

		/**
		 * 添加的是自定义附魔类型的
		 * 
		 * @param player
		 * @return
		 */
		private boolean MakeFormByCustom() {
			String string = data.getInputResponse(2);
			if (string == null || string.isEmpty())
				return MakeForm.Tip(player, "§4请输入附魔的价格");
			double Money = 0;
			if (!Tool.isInteger(string) || (Money = Double.valueOf(string)) < 1)
				return MakeForm.Tip(player, "§4附魔价格仅支持大于零的纯数字！");
			String EnchantData = data.getInputResponse(0);
			if (EnchantData == null || EnchantData.isEmpty())
				return MakeForm.Tip(player, "§4请输入附魔的数据！");
			boolean isTool = data.getToggleResponse(1);
			String[] EnchantDatas = EnchantData.split(";");
			Map<String, Map<String, Object>> map = new HashMap<>();
			for (String EnchData : EnchantDatas) {
				Map<String, Object> item = new HashMap<>();
				String[] EnchDatas = EnchData.split(">");
				item.put("EnchantID", EnchantName.UnknownToID(EnchDatas[0], -1));
				item.put("EnchantLevel", EnchDatas.length > 1
						? (Tool.isInteger(EnchDatas[1]) && Float.valueOf(EnchDatas[1]).intValue() > 0)
								? Float.valueOf(EnchDatas[1]).intValue()
								: 1
						: 1);
				item.put("EnchantRand", EnchDatas.length > 2
						? (Tool.isInteger(EnchDatas[2]) && Float.valueOf(EnchDatas[2]).intValue() > 0)
								? Float.valueOf(EnchDatas[2]).intValue()
								: 10
						: 10);
				item.put("Key", getKey(map, 1));
				map.put((String) item.get("Key"), item);
			}
			boolean isOK;
			player.sendMessage(
					"§6您" + ((isOK = new addItem(player, myPlayer.file).addEnchantByCustom(map, Money, isTool)) ? "§e成功"
							: "§4未成功") + "§6创建一个附魔商店");
			return isOK;
		}

		/**
		 * 随机获取一个不重复的附魔的项目Key值
		 * 
		 * @param map      附魔的项目集合
		 * @param JJlength Key的初始长度
		 * @return
		 */
		public static String getKey(Map<String, Map<String, Object>> map, int JJlength) {
			String string = "";
			for (int i = 0; i < JJlength; i++)
				string += Tool.getRandString("qwertyuiopasdfghjklzxcvbnm,./;*+'][=-");
			if (map.containsKey(string))
				return getKey(map, JJlength++);
			return string;
		}
	}

	/**
	 * 添加的是定级附魔
	 * 
	 * @param player
	 * @return
	 */
	private static boolean MakeFormLevel(Player player) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.string = "MakeFormLevel";
		Config config = new Config(myPlayer.file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		CustomForm form = new CustomForm(kick.formID.getID(17), kick.Message.getText(config.get("Title"), DsK, DsO));
		form.addDropdown("请选择要添加的附魔", EnchantName.getNameList());
		form.addDropdown("请选择附魔等级", new String[] { "I", "II", "III", "IV", "V" });
		form.addInput("请输入附魔的价格");
		form.addToggle("允许非工具附魔", true);
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 添加的是定级但是可以设置概率的附魔
	 * 
	 * @param player
	 * @return
	 */
	private static boolean MakeFormLevelRand(Player player) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.string = "MakeFormLevelRand";
		Config config = new Config(myPlayer.file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		CustomForm form = new CustomForm(kick.formID.getID(17), kick.Message.getText(config.get("Title"), DsK, DsO));
		form.addDropdown("请选择要添加的附魔", EnchantName.getNameList());
		form.addDropdown("请选择附魔等级", new String[] { "I", "II", "III", "IV", "V" });
		form.addInput("请输入成功比\n当输入10时，成功概率为1/10，\n当输入100时，成功概率为1/100,\n以此类推");
		List<String> list = EnchantName.getNameList();
		list.add("无");
		form.addDropdown("请选择失败后的附魔", list, list.size() - 1);
		form.addDropdown("请选择失败后的附魔等级", new String[] { "I", "II", "III", "IV", "V" });
		form.addInput("请输入附魔的价格");
		form.addToggle("允许非工具附魔", true);
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 添加的是自定义附魔类型的
	 * 
	 * @param player
	 * @return
	 */
	private static boolean MakeFormByCustom(Player player) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.string = "MakeFormByCustom";
		Config config = new Config(myPlayer.file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		CustomForm form = new CustomForm(kick.formID.getID(17), kick.Message.getText(config.get("Title"), DsK, DsO));
		form.addInput("§6请输入附魔数据，多个使用;分割\n§6例(附魔名称、附魔ID均可): \n§6附魔ID§f>§4附魔等级§f>§6概率占比§e;§6附魔ID§f>§4附魔等级§f>§6概率占比§e\n\n"
				+ getEnchantString());
		form.addToggle("允许附魔非工具物品", true);
		form.addInput("请输入附魔的价格");
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 处理主页发过来的数据，即选择的是啥玩意方式附魔
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean disMakeMain(Player player, FormResponseSimple data) {
		switch (Kick.addItemEnchantType[data.getClickedButtonId()]) {
		case "定级概率":
			return MakeFormLevelRand(player);
		case "自定义概率":
			return MakeFormByCustom(player);
		case "定级出售":
		default:
			return MakeFormLevel(player);
		}
	}

	/**
	 * 添加项目的主页
	 * 
	 * @param player
	 * @return
	 */
	public static boolean MakeMain(Player player, File file) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		Config config = new Config(file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(16), kick.Message.getText(config.get("Title"), DsK, DsO),
				Tool.getColorFont("请输入想要添加的方式"));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.string = "ShopOrSell";
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.addButtons(Kick.addItemEnchantType).sendPlayer(player);
		return true;
	}

	/**
	 * 获取一个附魔列表的字符串
	 * 
	 * @return
	 */
	public static String getEnchantString() {
		String string = "";
		List<EnchantName> EnchantS = EnchantName.getAll();
		int a = -1;
		for (EnchantName Enchant : EnchantS)
			string += "§2" + Enchant.getID() + "§f>§5" + Enchant.getName() + (((a++) - 1) % 3 == 0 ? "\n" : "  ");
		return string;
	}
}