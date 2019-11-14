package xiaokai.bemilk.set;

import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.bemilk.tool.SimpleForm;
import xiaokai.bemilk.tool.Tool;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseSimple;

/**
 * @author Winfxk
 */
public class MyshopBl extends Baseset {

	public MyshopBl(Player player) {
		super(player);
	}

	@Override
	public boolean makeMain() {
		SimpleForm form = new SimpleForm(FormID, Tool.getColorFont("个人商店黑名单设置"));
		form.addButton(Tool.getColorFont("添加物品")).addButton(Tool.getColorFont("删除物品")).sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse data) {
		MyPlayer myPlayer = kick.PlayerDataMap.get(player.getName());
		myPlayer.basesetForm = (((FormResponseSimple) data).getClickedButtonId() == 0 ? new addMyshopBlItem(player)
				: new delMyshopBlItem(player));
		kick.PlayerDataMap.put(player.getName(), myPlayer);
		return myPlayer.basesetForm.makeMain();
	}

}
