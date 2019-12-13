package xiaokai.bemilk.shop.open.type;

import xiaokai.bemilk.MakeForm;
import xiaokai.bemilk.mtp.DisPlayer;
import xiaokai.bemilk.shop.open.BaseDis;
import xiaokai.bemilk.shop.open.ShopData;
import xiaokai.bemilk.tool.Tool;

import java.util.ArrayList;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.ConsoleCommandSender;
import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author Winfxk 命令商店的处理
 */
public class Command extends BaseDis {
	private String BaseCommand, Command;
	private int Permission;

	public Command(ShopData data) {
		super(data);
		BaseCommand = Tool.objToString(Item.get("Command"), null);
		Permission = Tool.ObjectToInt(Item.get("CommandSender"), 0);
		if (BaseCommand != null) {
			ArrayList<String> strings = new ArrayList<>();
			for (Player player2 : Server.getInstance().getOnlinePlayers().values())
				if (player2.isOnline())
					strings.add(player2.getName());
			k = new String[] { "{Player}", "{Money}", "{Players}" };
			d = new Object[] { player.getName(), Money,
					strings.get(Tool.getRand(0, strings.size() - 1)) + "等" + strings.size() + "个玩家" };
			Command = msg.getText(Command, k, d);
		}
	}

	@Override
	public boolean MakeMain() {
		if (BaseCommand == null || BaseCommand.isEmpty()) {
			kick.mis.getLogger().warning("无法获取命令商店的命令内容，请检查" + Item);
			return MakeForm.Tip(player, msg.getSun("界面", "命令商店", "无法获取命令", k, d));
		}
		String[] myKey = { "{Player}", "{BaseCommand}", "{Command}", "{Money}", "{Permission}", "{MyMoney}" };
		Object[] myData = { player.getName(), BaseCommand, Command, Money,
				xiaokai.bemilk.shop.add.Command.getPermission(Permission), MyMoney() };
		String Contxt = msg.getSun("界面", "命令商店", "内容", myKey, myData);
		form.addLabel(Contxt);
		form.sendPlayer(player);
		return true;
	}

	@Override
	public boolean disMain(FormResponseCustom data) {
		if (MyMoney() < Money)
			return MakeForm.Tip(player, msg.getSun("界面", "命令商店", "钱不够", k, d));
		DisPlayer.delMoney(player, Money);
		switch (Permission) {
		case 1:
			return Admin();
		case 2:
			return Console();
		case 0:
		default:
			return Player();
		}
	}

	/**
	 * 正常玩家执行命令
	 * 
	 * @return
	 */
	public boolean Player() {
		if (!BaseCommand.equals("{Players}")) {
			boolean isb = false;
			try {
				isb = Server.getInstance().dispatchCommand(player, msg.getText(BaseCommand,
						new String[] { "{Player}", "{Money}" }, new Object[] { player.getName(), MyMoney() }));
			} catch (Exception e) {
				player.sendMessage("执行命令时出现错误！请联系管理员。" + e.getMessage());
				e.printStackTrace();
			}
			return isb;
		}
		for (Player player2 : Server.getInstance().getOnlinePlayers().values())
			if (player2.isOnline())
				try {
					Server.getInstance().dispatchCommand(player2,
							msg.getText(BaseCommand, new String[] { "{Player}", "{Money}", "{Players}" },
									new Object[] { player.getName(), MyMoney(), player2.getName() }));
				} catch (Exception e) {
					player.sendMessage("执行循环命令时出现错误！请联系管理员。" + e.getMessage());
					e.printStackTrace();
				}
		return true;
	}

	public boolean Admin() {
		boolean isb = player.isOp();
		if (!BaseCommand.equals("{Players}")) {
			if (!player.isOp())
				player.setOp(true);
			try {
				Server.getInstance().dispatchCommand(player, msg.getText(BaseCommand,
						new String[] { "{Player}", "{Money}" }, new Object[] { player.getName(), MyMoney() }));
			} catch (Exception e) {
				player.sendMessage("执行命令时出现错误！请联系管理员。" + e.getMessage());
				e.printStackTrace();
			} finally {
				if (!isb)
					player.setOp(false);
			}
			return true;
		}
		for (Player player2 : Server.getInstance().getOnlinePlayers().values())
			if (player2.isOnline())
				try {
					Server.getInstance().dispatchCommand(player2,
							msg.getText(BaseCommand, new String[] { "{Player}", "{Money}", "{Players}" },
									new Object[] { player.getName(), MyMoney(), player2.getName() }));
				} catch (Exception e) {
					player.sendMessage("执行命令时出现错误！请联系管理员。" + e.getMessage());
					e.printStackTrace();
				} finally {
					if (!isb)
						player.setOp(false);
				}
		return true;
	}

	public boolean Console() {
		if (!BaseCommand.equals("{Players}")) {
			boolean isb = false;
			try {
				isb = Server.getInstance().dispatchCommand(new ConsoleCommandSender(), msg.getText(BaseCommand,
						new String[] { "{Player}", "{Money}" }, new Object[] { player.getName(), MyMoney() }));
			} catch (Exception e) {
				player.sendMessage("执行命令时出现错误！请联系管理员。" + e.getMessage());
				e.printStackTrace();
			}
			return isb;
		}
		for (Player player2 : Server.getInstance().getOnlinePlayers().values())
			if (player2.isOnline())
				try {
					Server.getInstance().dispatchCommand(new ConsoleCommandSender(),
							msg.getText(BaseCommand, new String[] { "{Player}", "{Money}", "{Players}" },
									new Object[] { player.getName(), MyMoney(), player2.getName() }));
				} catch (Exception e) {
					player.sendMessage("执行循环命令时出现错误！请联系管理员。" + e.getMessage());
					e.printStackTrace();
				}
		return true;
	}
}
