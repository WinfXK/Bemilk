package xiaokai.bemilk.set;

import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.tool.Tool;
import xiaokai.bemilk.tool.data.ItemID;
import xiaokai.bemilk.tool.form.CustomForm;

import java.util.ArrayList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk
 */
public class SetConfig extends Baseset {
	private String 物品修复项目图标 = "textures/gui/newgui/anvil-hammer.png";
	private String 物品附魔项目图标 = "textures/items/textures/ui/haste_effect.png";
	private String 搜索按钮图标 = "textures/gui/newgui/magnifyingGlass.png";
	private String 个人商店创建按钮图标 = "textures/gui/newgui/Friends.png";

	/**
	 * 处理服务器设置
	 * 
	 * @param player
	 */
	public SetConfig(Player player) {
		super(player);
		config = kick.config;
	}

	@Override
	public boolean makeMain() {
		CustomForm form = new CustomForm(FormID, Tool.getColorFont("服务器设置"));
		form.addInput("§6请输入快捷工具的名称或ID", ItemID.getNameByID(config.getString("快捷工具")));
		form.addToggle("§6撤销使用快捷工具触发的事件", config.getBoolean("打开撤销"));
		form.addInput("§6货币名称", config.get("货币单位"));
		form.addToggle("§6自动检测更新", config.getBoolean("检测更新"));
		form.addInput("§6自动检查更新的间隔(s)", config.getInt("检测更新间隔"));
		form.addInput("§6屏蔽玩家双击间隔(ms)", config.getInt("屏蔽玩家双击间隔"));
		form.addInput("§6定时检查快捷工具间隔(s)", config.getInt("定时检查快捷工具间隔"));
		form.addToggle("§6是否允许玩家丢弃快捷工具", config.getBoolean("是否允许玩家丢弃快捷工具"));
		form.addInput("§6插件管理员", getAdminToString());
		form.addToggle("§6管理员白名单", config.getBoolean("管理员白名单"));
		form.addToggle("§6折叠选项", config.getBoolean("折叠选项"));
		form.addToggle("§6隐藏无权商店", config.getBoolean("隐藏无权商店"));
		form.addToggle("§6隐藏金钱数不匹配的商店", config.getBoolean("隐藏金钱数不匹配的商店"));
		form.addInput("§6定时保存间隔(s)", config.getInt("定时保存间隔"));
		form.addInput("§6物品修复项目图标", config.get("物品修复项目图标"));
		form.addInput("§6物品附魔项目图标", config.get("物品附魔项目图标"));
		form.addInput("§6搜索按钮图标", config.get("搜索按钮图标"));
		form.addInput("§6个人商店创建按钮图标", config.get("个人商店创建按钮图标"));
		form.addToggle("§6限制OP使用个人商店", config.getBoolean("限制OP使用个人商店"));
		form.addToggle("§6限制创造模式使用个人商店", config.getBoolean("限制创造模式使用个人商店"));
		form.addToggle("§6玩家重生时重置玩家缓存数据", config.getBoolean("玩家重生时重置玩家缓存数据"));
		form.addToggle("§6限制创造模式使用商店", config.getBoolean("限制创造模式使用商店"));
		form.addToggle("§6重生进服等事件检测玩家快捷工具持有", config.getBoolean("重生进服等事件检测玩家快捷工具持有"));
		form.addInput("§6数据刷新间隔(s)", config.getInt("数据刷新间隔"));
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponse d) {
		FormResponseCustom data = (FormResponseCustom) d;
		config.set("快捷工具", ItemID.UnknownToID(data.getInputResponse(0)));
		config.set("打开撤销", data.getToggleResponse(1));
		config.set("货币单位", data.getInputResponse(2));
		config.set("检测更新", data.getToggleResponse(3));
		config.set("检测更新间隔", Tool.ObjectToInt(data.getInputResponse(4), 21600));
		config.set("屏蔽玩家双击间隔", Tool.ObjectToInt(data.getInputResponse(5), 500));
		config.set("定时检查快捷工具间隔", Tool.ObjectToInt(data.getInputResponse(6), 60));
		config.set("是否允许玩家丢弃快捷工具", data.getToggleResponse(7));
		config.set("管理员", getStringToList(data.getInputResponse(8)));
		config.set("管理员白名单", data.getToggleResponse(9));
		config.set("折叠选项", data.getToggleResponse(10));
		config.set("隐藏无权商店", data.getToggleResponse(11));
		config.set("隐藏金钱数不匹配的商店", data.getToggleResponse(12));
		config.set("定时保存间隔", Tool.ObjectToInt(data.getInputResponse(13), 30));
		config.set("物品修复项目图标", ItemID.UnknownToPath(data.getInputResponse(14), 物品修复项目图标));
		config.set("物品附魔项目图标", ItemID.UnknownToPath(data.getInputResponse(15), 物品附魔项目图标));
		config.set("搜索按钮图标", ItemID.UnknownToPath(data.getInputResponse(16), 搜索按钮图标));
		config.set("个人商店创建按钮图标", ItemID.UnknownToPath(data.getInputResponse(17), 个人商店创建按钮图标));
		config.set("限制OP使用个人商店", data.getToggleResponse(18));
		config.set("限制创造模式使用个人商店", data.getToggleResponse(19));
		config.set("玩家重生时重置玩家缓存数据", data.getToggleResponse(20));
		config.set("限制创造模式使用商店", data.getToggleResponse(21));
		config.set("重生进服等事件检测玩家快捷工具持有", data.getToggleResponse(22));
		config.set("数据刷新间隔", Tool.ObjectToInt(data.getInputResponse(23), 600));
		kick.config = config;
		boolean isok = config.save();
		return MakeForm.Tip(player, "§6设置保存" + (isok ? "§e成功！" : "§4失败！"), isok);
	}

	/**
	 * 将管理员文本序列转化为List
	 * 
	 * @param string
	 * @return
	 */
	public List<String> getStringToList(String string) {
		List<String> list = new ArrayList<>();
		if (string == null || string.isEmpty())
			return list;
		String[] strings = string.split(";");
		for (String s : strings)
			if (s != null && !s.isEmpty())
				list.add(s);
		return list;
	}

	/**
	 * 将管理员列表序列化为文本
	 * 
	 * @return
	 */
	public String getAdminToString() {
		String string = "";
		List<String> list = config.getStringList("管理员");
		for (String s : list)
			if (s != null && !s.isEmpty())
				string += (string.isEmpty() ? "" : ";") + s;
		return string;
	}
}
