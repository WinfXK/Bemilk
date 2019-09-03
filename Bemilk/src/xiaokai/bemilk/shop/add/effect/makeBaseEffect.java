package xiaokai.bemilk.shop.add.effect;

import xiaokai.bemilk.data.Message;
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
public abstract class makeBaseEffect {
	public Kick kick = Kick.kick;
	public Player player;
	public File file;
	public String Style;
	public Message msg = Kick.kick.Message;
	public String[] k;
	public Object[] d;
	public CustomForm form;

	public makeBaseEffect(Player player, File file) {
		this.file = file;
		this.player = player;
		k = new String[] { "{Player}", "{Money}" };
		d = new Object[] { player.getName(), MyMoney() };
		Config config = new Config(file, Config.YAML);
		form = new CustomForm(kick.formID.getID(32), kick.Message.getText(config.get("Title"), k, d));
	}

	/**
	 * 获取自己的金币数量
	 * 
	 * @return
	 */
	public double MyMoney() {
		return EconomyAPI.getInstance().myMoney(player);
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
