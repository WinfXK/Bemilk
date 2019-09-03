package xiaokai.bemilk.cmd;

import xiaokai.bemilk.data.Message;
import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.shop.open.OpenShop;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.utils.Config;

import me.onebone.economyapi.EconomyAPI;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class ShopCommand extends Command {
	private Kick kick;
	private Message msg;
	private static String[] FileList;
	private static File file;
	private static ShopCommand command;

	public ShopCommand(Kick bemilk) {
		super("shop", "商店主命令", "/shop", new String[] { "shop", "商店" });
		command = this;
		kick = bemilk;
		file = new File(kick.mis.getDataFolder(), Kick.ShopConfigPath);
		reload();
		msg = kick.Message;
		commandParameters = new HashMap<>();
		commandParameters.put("打开商店主页", new CommandParameter[] {});
		commandParameters.put("打开商店分页", new CommandParameter[] { new CommandParameter("商店的配置文件名", false, FileList) });
		commandParameters.put("打开商店一个项目", new CommandParameter[] { new CommandParameter("商店的配置文件名", false, FileList),
				new CommandParameter("商店的项目Key", CommandParamType.TARGET, false) });
	}

	/**
	 * 刷新自动填充文件列表
	 */
	public static void reload() {
		FileList = file.list();
		command.commandParameters.put("打开商店分页",
				new CommandParameter[] { new CommandParameter("商店的配置文件名", false, FileList) });
	}

	@Override
	public boolean execute(CommandSender player, String commandLabel, String[] args) {
		String[] k = new String[] { "{Player}", "{Money}" };
		Object[] d = new Object[] { player.getName(), EconomyAPI.getInstance().myMoney(player.getName()) };
		if (!kick.mis.isEnabled()) {
			player.sendMessage(msg.getMessage("插件已关闭", k, d));
			return false;
		}
		if (!player.hasPermission("Bemilk.Command.Shop")) {
			player.sendMessage(msg.getMessage("无命令权限", k, d));
			return true;
		}
		if (!player.isPlayer()) {
			player.sendMessage("§4请在游戏内执行此命令!");
			return true;
		}
		if (args.length < 1)
			return MakeForm.Main((Player) player);
		String useFileName = args[0];
		if (useFileName.isEmpty()) {
			player.sendMessage(msg.getSun("命令", "Shop", "请输入文件名", k, d));
			return true;
		}
		file = new File(kick.mis.getDataFolder(), Kick.ShopConfigPath);
		FileList = file.list();
		List<String> okFileList = new ArrayList<>();
		File shopFile = new File(file, useFileName);
		if (!shopFile.exists()) {
			shopFile = new File(file, useFileName + ".yml");
			if (!shopFile.exists()) {
				for (String okString : FileList)
					if (okString.toLowerCase().equals(useFileName.toLowerCase()))
						okFileList.add(okString);
				if (okFileList.size() < 1) {
					for (String okString : FileList)
						if (okString.contains(useFileName))
							okFileList.add(okString);
					if (okFileList.size() < 1)
						for (String okString : FileList)
							if (okString.toLowerCase().contains(useFileName.toLowerCase()))
								okFileList.add(okString);
				}
				if (okFileList.size() < 1) {
					player.sendMessage(msg.getSun("命令", "Shop", "输入的文字不匹配", k, d));
					return true;
				}
				shopFile = new File(file, okFileList.get(0));
			}
		}
		if (args.length > 1) {
			Config config = new Config(shopFile, Config.YAML);
			Object object = config.get("Items");
			Map<String, Object> Shops = (object == null || !(object instanceof Map)) ? new HashMap<>()
					: (HashMap<String, Object>) object;
			String useKey = "";
			for (int i = 1; i < args.length; i++)
				useKey += (useKey.isEmpty() ? "" : " ") + args[i];
			if (useKey.isEmpty()) {
				player.sendMessage(msg.getSun("命令", "Shop", "请输入项目Key", k, d));
				return true;
			}
			if (Shops.containsKey(useKey))
				return OpenShop.Open((Player) player, shopFile, useKey);
			List<String> keys = new ArrayList<>(Shops.keySet());
			List<String> okKeys = new ArrayList<>();
			for (String Key : keys)
				if (Key.toLowerCase().equals(useKey.toLowerCase()))
					okKeys.add(Key);
			if (okKeys.size() < 1) {
				for (String Key : keys)
					if (Key.contains(useKey))
						okKeys.add(Key);
				if (okKeys.size() < 1) {
					for (String Key : keys)
						if (Key.toLowerCase().contains(useKey.toLowerCase()))
							okKeys.add(Key);
				}
			}
			if (okFileList.size() < 1) {
				player.sendMessage(msg.getSun("命令", "Shop", "找不到项目Key", k, d));
				return true;
			}
			return OpenShop.Open((Player) player, shopFile, okKeys.get(0));
		} else
			return OpenShop.ShowShop((Player) player, shopFile);
	}

}
