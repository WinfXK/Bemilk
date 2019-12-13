package xiaokai.bemilk.shop.add;

import xiaokai.bemilk.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.bemilk.tool.CustomForm;
import xiaokai.bemilk.tool.Tool;

import java.io.File;

import cn.nukkit.Player;

/**
 * @author Winfxk
 */
public class Command {
	private static Kick kick = Kick.kick;
	private static Message msg = Kick.kick.Message;

	public static boolean makeform(Player player, File file) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		CustomForm form = new CustomForm(kick.formID.getID(37), Tool.getColorFont("命令商店"));
		form.addInput("§6请输入想要执行的命令\n{Player}：购买该商品的玩家名称");
		form.addInput("§6请输入商品价格");
		form.addDropdown("§6请选择要执行命令的权限等级", new String[] { "玩家", "管理员", "控制台" });
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.file = file;
		form.sendPlayer(player);
		return false;
	}
}
