package xiaokai.bemilk.shop.add;

import xiaokai.bemilk.Bemilk;
import xiaokai.bemilk.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.bemilk.shop.addItem;
import xiaokai.bemilk.tool.CustomForm;
import xiaokai.bemilk.tool.Tool;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk 处理创建的命令类型的按钮商店
 */
public class Command {
	private static Kick kick = Kick.kick;
	private static Message msg = Kick.kick.Message;

	/**
	 * 构建创建商店的页面
	 * 
	 * @param player
	 * @param file
	 * @return
	 */
	public static boolean makeform(Player player, File file) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		CustomForm form = new CustomForm(kick.formID.getID(37), Tool.getColorFont("命令商店"));
		form.addInput("§6请输入想要执行的命令\n	{Player}：购买该商品的玩家名称\n	{Players}：当前在线玩家");
		form.addInput("§6请输入商品价格");
		form.addDropdown("§6请选择要执行命令的权限等级", new String[] { "玩家", "管理员", "控制台" });
		form.addDropdown("§6请选择想要使用的货币", kick.getMoneyType());
		form.addInput("§6请输入商店项目将会显示的内容", kick.config.get("命令商店显示内容"));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.file = file;
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 处理构建的数据
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean disMakeForm(Player player, FormResponseCustom data) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		String cmd = data.getInputResponse(0);
		if (cmd == null || cmd.isEmpty())
			return MakeForm.Tip(player, "§4请输入想要执行的命令内容！");
		String SMoney = data.getInputResponse(1);
		if (!Tool.isInteger(SMoney))
			return MakeForm.Tip(player, "§4" + Bemilk.getMoneyName() + "仅支持大于零的纯数字！");
		double Money = 0;
		try {
			Money = Double.valueOf(Tool.objToString(SMoney, "0"));
		} catch (Exception e) {
			return MakeForm.Tip(player, "§4" + Bemilk.getMoneyName() + "仅支持大于零的纯数字！");
		}
		if (Money <= 0)
			return MakeForm.Tip(player, "§4" + Bemilk.getMoneyName() + "仅支持大于零的纯数字！");
		int ID = data.getDropdownResponse(2).getElementID();
		String MoneyType = data.getDropdownResponse(3).getElementContent();
		String ButtonContent = data.getInputResponse(4);
		boolean isOK = new addItem(player, myPlayer.file, MoneyType).addCommand(cmd, ID, Money, ButtonContent);
		return MakeForm.Tip(player, "§6命令商店创建" + (isOK ? "成功" : "§4失败") + "§6！", isOK);
	}

	public static String getPermission(int Permission) {
		return Permission == 1 ? "PlayerByOP" : (Permission == 2 ? "Console" : "Player");
	}
}
