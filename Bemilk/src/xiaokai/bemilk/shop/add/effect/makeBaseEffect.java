package xiaokai.bemilk.shop.add.effect;

import xiaokai.bemilk.MakeForm;
import xiaokai.bemilk.mtp.DisPlayer;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.bemilk.tool.CustomForm;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.utils.Config;

/**
 * @author Winfxk
 */
public abstract class makeBaseEffect {
	public Kick kick = Kick.kick;
	public Player player;
	public File file;
	public String Style, MoneyType;
	public Message msg = Kick.kick.Message;
	public String[] k;
	public Object[] d;
	public CustomForm form;

	public makeBaseEffect(Player player, File file) {
		this.file = file;
		this.player = player;
		k = new String[] { "{Player}", "{Money}" };
		d = new Object[] { player.getName(), DisPlayer.getMoney(player.getName()) };
		Config config = new Config(file, Config.YAML);
		form = new CustomForm(kick.formID.getID(32), kick.Message.getText(config.get("Title"), k, d));
	}

	/**
	 * 判断是否有货币可用
	 * 
	 * @return
	 */
	public boolean isMoneyOnline() {
		if (kick.getMoneyType().size() >= 1)
			return true;
		return Tip("§4暂无可用货币！请添加货币插件！\n§4EconomyAPI\n§4Snowmn", false);
	}

	/**
	 * 发送提示
	 * 
	 * @param string
	 * @return
	 */
	public boolean Tip(String string) {
		return Tip(string, false);
	}

	/**
	 * 发送提示
	 * 
	 * @param string
	 * @return
	 */
	public boolean Tip(String string, boolean isOK) {
		return MakeForm.Tip(player, string, isOK);
	}

	/**
	 * 构建界面
	 * 
	 * @return
	 */
	public abstract boolean makeMain();

	/**
	 * 处理界面发回的数据
	 * 
	 * @param data
	 * @return
	 */
	public abstract boolean disMain(FormResponseCustom data);
}
