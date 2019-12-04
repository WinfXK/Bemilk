package xiaokai.bemilk.shop.add.effect;

import xiaokai.bemilk.shop.addItem;
import xiaokai.bemilk.tool.Effectrec;
import xiaokai.bemilk.tool.Tool;

import java.io.File;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class isEffect extends makeBaseEffect {
	/**
	 * 处理添加的项目是定级定效定时长的效果项目的事件处理类
	 * 
	 * @param player
	 * @param file
	 */
	public isEffect(Player player, File file) {
		super(player, file);
	}

	@Override
	public boolean makeMain() {
		if (!isMoneyOnline())
			return false;
		form.addDropdown("§6请选择您想要上架的效果", Effectrec.getNameList());
		form.addInput("§5请输入效果的等级", 1);
		form.addInput("§6请输入效果持续的时长(秒)", 100);
		form.addInput("请输入购买该效果需要的价格");
		form.addDropdown("请选择想要使用的货币种类", kick.getMoneyType());
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponseCustom data) {
		if (!isMoneyOnline())
			return false;
		int ID = Effectrec.getID(data.getDropdownResponse(0).getElementContent());
		String string = data.getInputResponse(1);
		if (string == null || string.isEmpty())
			return Tip("§4请输入想要上架的效果等级");
		int Level = 0;
		if (!Tool.isInteger(string) || (Level = Tool.ObjectToInt(string)) < 1)
			return Tip("§4效果的等级只能为大于零的纯整数！");
		int Time = 0;
		string = data.getInputResponse(2);
		if (string == null || string.isEmpty())
			return Tip("§4请输入想要上架的效果持续时长");
		if (!Tool.isInteger(string) || (Time = Tool.ObjectToInt(string)) < 1)
			return Tip("§4效果的持续时长只能为大于零的纯整数！");
		string = data.getInputResponse(3);
		int Money = 0;
		if (string == null || string.isEmpty())
			return Tip("§4请输入想要上架的效果购买价格");
		if (!Tool.isInteger(string) || (Money = Tool.ObjectToInt(string)) < 1)
			return Tip("§4效果的购买价格只能为大于零的纯整数！");
		MoneyType = data.getDropdownResponse(4).getElementContent();
		boolean isok = new addItem(player, file, MoneyType).addEffectByisEffect(ID, Level, Time, Money);
		return Tip(
				isok ? ("§6您成功的创建了一个效果为§8" + Effectrec.getName(ID) + "§f*§5" + Level + "§6持续时长为：§4" + Time + "§6的药水效果")
						: "§4创建貌似出现了一点问题！",
				isok);
	}

}
