package xiaokai.bemilk.shop.add;

import xiaokai.bemilk.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.bemilk.shop.addItem;
import xiaokai.bemilk.tool.CustomForm;
import xiaokai.bemilk.tool.SimpleForm;
import xiaokai.bemilk.tool.Tool;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.utils.Config;

import me.onebone.economyapi.EconomyAPI;

/**
*@author Winfxk
*/
/**
 * 物品修复处理类
 * 
 * @author Winfxk
 */
public class ItemRepair {
	private Player player;
	private MyPlayer myPlayer;
	private CustomForm form;
	private FormResponseCustom data;
	private Kick kick;
	private Message msg;

	/**
	 * 处理数据
	 * 
	 * @param player
	 */
	public ItemRepair(Player player) {
		this.player = player;
		kick = Kick.kick;
		msg = Kick.kick.Message;
		myPlayer = kick.PlayerDataMap.get(player.getName());
	}

	/**
	 * 处理在输入数据界面输入后点击提交发回的数据
	 * 
	 * @param data
	 */
	public boolean disAdd(FormResponseCustom data) {
		this.data = data;
		switch (myPlayer.string) {
		case "随机增加":
			return disRandom();
		case "总量增加":
			return disSoarTo();
		case "定量增加":
		default:
			return disaddSome();
		}
	}

	private boolean disaddSome() {
		String string = data.getInputResponse(0);
		if (string == null || string.isEmpty())
			return MakeForm.Tip(player, "§4请输入将要增加的耐久");
		int Repair = 0;
		if (!Tool.isInteger(string) || (Repair = Float.valueOf(string).intValue()) < 1)
			return MakeForm.Tip(player, "§4要增加的耐久只能为大于零的纯整数！");
		string = data.getInputResponse(1);
		if (string == null || string.isEmpty())
			return MakeForm.Tip(player, "§4请输入每次使用的价格");
		int Money = 0;
		if (!Tool.isInteger(string) || (Money = Float.valueOf(string).intValue()) < 1)
			return MakeForm.Tip(player, "§4每次使用的价格只能为大于零的纯整数！");
		boolean isTool = data.getToggleResponse(2);
		boolean isOK;
		player.sendMessage("§6您"
				+ ((isOK = new addItem(player, myPlayer.file).addItemRepairSome(Repair, Money, isTool)) ? "§e成功"
						: "§4未成功")
				+ "§6创建一个耐久商店");
		return isOK;
	}

	/**
	 * 设置手持物品的特殊值为指定值
	 * 
	 * @return
	 */
	private boolean disSoarTo() {
		String string = data.getInputResponse(0);
		if (string == null || string.isEmpty())
			return MakeForm.Tip(player, "§4请输入将要设置的物品特殊值");
		int Repair = 0;
		if (!Tool.isInteger(string) || (Repair = Float.valueOf(string).intValue()) < 1)
			return MakeForm.Tip(player, "§4要设置的物品特殊值只能为大于零的纯整数！");
		string = data.getInputResponse(1);
		if (string == null || string.isEmpty())
			return MakeForm.Tip(player, "§4请输入每次使用的价格");
		int Money = 0;
		if (!Tool.isInteger(string) || (Money = Float.valueOf(string).intValue()) < 1)
			return MakeForm.Tip(player, "§4每次使用的价格只能为大于零的纯整数！");
		boolean isTool = data.getToggleResponse(2);
		boolean isOK;
		player.sendMessage("§6您"
				+ ((isOK = new addItem(player, myPlayer.file).addItemRepairSoarTo(Repair, Money, isTool)) ? "§e成功"
						: "§4未成功")
				+ "§6创建一个耐久商店");
		return isOK;
	}

	/**
	 * 随机增加或减少工具耐久
	 * 
	 * @return
	 */
	private boolean disRandom() {
		String string = data.getInputResponse(0);
		if (string == null || string.isEmpty())
			return MakeForm.Tip(player, "§4请输入随机增加耐久的最小值");
		int MinRepair = 0;
		if (!Tool.isInteger(string) || (MinRepair = Float.valueOf(string).intValue()) < 1)
			return MakeForm.Tip(player, "§4随机增加耐久的最小值只能为大于零的纯整数！");
		string = data.getInputResponse(1);
		if (string == null || string.isEmpty())
			return MakeForm.Tip(player, "§4请输入随机增加耐久的最大值");
		int MaxRepair = 0;
		if (!Tool.isInteger(string) || (MaxRepair = Float.valueOf(string).intValue()) < 1)
			return MakeForm.Tip(player, "§4随机增加耐久的最大值只能为大于零的纯整数！");
		string = data.getInputResponse(2);
		if (string == null || string.isEmpty())
			return MakeForm.Tip(player, "§4请输入成功占比");
		int RepairCount = 0;
		if (!Tool.isInteger(string) || (RepairCount = Float.valueOf(string).intValue()) < 1)
			return MakeForm.Tip(player, "§4成功占比只能为大于零的纯整数！");
		string = data.getInputResponse(3);
		if (string == null || string.isEmpty())
			return MakeForm.Tip(player, "§4请输入失败后将会减少的耐久度最小值");
		int SBMinRepair = 0;
		if (!Tool.isInteger(string) || (SBMinRepair = Float.valueOf(string).intValue()) < 0)
			return MakeForm.Tip(player, "§4失败后将会减少的耐久度最小值只能为大于等于零的纯整数！");
		string = data.getInputResponse(4);
		if (string == null || string.isEmpty())
			return MakeForm.Tip(player, "§4请输入失败后将会减少的耐久度最大值");
		int SBMaxRepair = 0;
		if (!Tool.isInteger(string) || (SBMaxRepair = Float.valueOf(string).intValue()) < 0)
			return MakeForm.Tip(player, "§4失败后将会减少的耐久度最大值只能为大于等于零的纯整数！");
		string = data.getInputResponse(5);
		if (string == null || string.isEmpty())
			return MakeForm.Tip(player, "§4请输入每次使用的价格");
		int Money = 0;
		if (!Tool.isInteger(string) || (Money = Float.valueOf(string).intValue()) < 1)
			return MakeForm.Tip(player, "§4每次使用的价格只能为大于零的纯整数！");
		boolean isTool = data.getToggleResponse(6);
		boolean isOK;
		player.sendMessage("§6您" + ((isOK = new addItem(player, myPlayer.file).addItemRepairRandom(MinRepair,
				MaxRepair, RepairCount, SBMinRepair, SBMaxRepair, Money, isTool)) ? "§e成功" : "§4未成功") + "§6创建一个耐久商店");
		return isOK;
	}

	/**
	 * 随机增加或减少工具耐久
	 * 
	 * @return
	 */
	private void Random() {
		form.addInput("请输入随机增加耐久的最小值");
		form.addInput("请输入随机增加耐久的最大值");
		form.addInput("请输入成功占比", 100);
		form.addInput("失败后将会减少的耐久度最小值（小于等于零时不启用)", 0);
		form.addInput("失败后将会减少的耐久度最大值（小于等于零时不启用)", 0);
		form.addInput("请输入每次使用的价格");
		form.addToggle("允许非工具使用", false);
	}

	/**
	 * 将工具的特殊值设置为
	 * 
	 * @return
	 */
	private void SoarTo() {
		form.addInput("请输入将要设置的物品特殊值");
		form.addInput("请输入每次使用的价格");
		form.addToggle("允许非工具使用", false);
	}

	/**
	 * 购买后获得一段修复值
	 * 
	 * @return
	 */
	private void addSome() {
		form.addInput("请输入将要增加的耐久");
		form.addInput("请输入每次使用的价格");
		form.addToggle("允许非工具使用", false);
	}

	/**
	 * 处理主页发回的数据
	 * 
	 * @param data
	 * @return
	 */
	public boolean disMakeMain(FormResponseSimple data) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		myPlayer.string = Kick.addItemRepairType[data.getClickedButtonId()];
		Config config = new Config(myPlayer.file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		form = new CustomForm(kick.formID.getID(19), kick.Message.getText(config.get("Title"), DsK, DsO));
		switch (myPlayer.string) {
		case "随机增加":
			Random();
			break;
		case "总量增加":
			SoarTo();
			break;
		case "定量增加":
		default:
			addSome();
			break;
		}
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 物品修复功能主页
	 * 
	 * @param player
	 * @return
	 */
	static boolean MakeMain(Player player, File file) {
		Kick kick = Kick.kick;
		Message msg = kick.Message;
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.file = file;
		Config config = new Config(file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		SimpleForm form = new SimpleForm(kick.formID.getID(18), kick.Message.getText(config.get("Title"), DsK, DsO),
				Tool.getColorFont("请输入想要添加的商店类型"));
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.addButtons(Kick.addItemRepairType).sendPlayer(player);
		return true;
	}
}