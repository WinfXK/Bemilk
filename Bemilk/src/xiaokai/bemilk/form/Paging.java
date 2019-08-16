package xiaokai.bemilk.form;

import cn.nukkit.Player;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.Message;
import xiaokai.tool.CustomForm;
import xiaokai.tool.Tool;

/**
 * @author Winfxk
 */
public class Paging {
	private static Message msg = Kick.kick.Message;

	/**
	 * 删除商店分页
	 * 
	 * @author Winfxk
	 */
	public static class delShop {
		/**
		 * 创建一个界面供玩家选择要删除的商店分页
		 * 
		 * @param player
		 * @return
		 */
		public static boolean MakeForm(Player player) {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player,
						msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
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
		 * 创建一个界面供玩家选择要修改的商店分页
		 * 
		 * @param player
		 * @return
		 */
		public static boolean MakeForm(Player player) {
			if (!Kick.isAdmin(player))
				return MakeForm.Tip(player,
						msg.getMessage("权限不足", new String[] { "{Player}" }, new Object[] { player.getName() }));
			return true;
		}
	}

	/**
	 * 添加商店分页
	 * 
	 * @author Winfxk
	 */
	public static class addShop {
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
			CustomForm form = new CustomForm(Kick.kick.formID.getID(3), Tool.getColorFont("添加分页"));
			form.addInput("请输入按钮将要显示的内容");
			form.addInput("请输入商店界面的标题");
			form.addInput("请输入商店的文本内容");
			return true;
		}
	}
}
