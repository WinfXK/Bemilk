package xiaokai.bemilk;

import xiaokai.bemilk.mtp.Kick;

/**
 * @author Winfxk
 */
public class MyCommand {
	private Kick kick;

	public MyCommand(Kick kick) {
		this.kick = kick;
	}

	public boolean setCommand() {
		kick.mis.getDataFolder();
		return true;
	}
}
