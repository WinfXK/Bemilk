package xiaokai.bemilk.shop.add.effect;

import xiaokai.bemilk.data.MyPlayer;
import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.tool.form.CustomForm;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.utils.Config;

import me.onebone.economyapi.EconomyAPI;

/**
 * @author Winfxk
 */
public class addEffect {
	private static Kick kick = Kick.kick;
	private static final String[] Style = { "定点出售", "定效果随机时间出售", "定时间随机效果出售", "完全自定义" };

	/**
	 * 构建一个界面给玩家选择要添加的药水方式
	 * 
	 * @param player
	 * @param file
	 * @return
	 */
	public static boolean makeMain(Player player, File file) {
		if (!Kick.isAdmin(player))
			return MakeForm.Tip(player,
					kick.Message.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		Config config = new Config(myPlayer.file, Config.YAML);
		String[] DsK = { "{Player}", "{Money}" };
		Object[] DsO = { player.getName(), EconomyAPI.getInstance().myMoney(player) };
		CustomForm form = new CustomForm(kick.formID.getID(31), kick.Message.getText(config.get("Title"), DsK, DsO));
		form.addDropdown("§6请选择您想要添加的药水购买方式", Style);
		myPlayer.file = file;
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		form.sendPlayer(player);
		return true;
	}

	/**
	 * 检测要添加的是什么类型，并构建处理类
	 * 
	 * @param player
	 * @param data
	 * @return
	 */
	public static boolean disMain(Player player, FormResponseCustom data) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		switch (data.getDropdownResponse(0).getElementID()) {
		case 0:
			myPlayer.makeBaseEffect = new isEffect(player, myPlayer.file);
			break;
		case 1:
			myPlayer.makeBaseEffect = new RandTime(player, myPlayer.file);
			break;
		case 2:
			myPlayer.makeBaseEffect = new RandEffect(player, myPlayer.file);
			break;
		case 3:
			myPlayer.makeBaseEffect = new CustomEffect(player, myPlayer.file);
			break;
		default:
			return MakeForm.Tip(player, "§4无法获取添加的项目类型");
		}
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		return myPlayer.makeBaseEffect.makeMain();
	}

}
