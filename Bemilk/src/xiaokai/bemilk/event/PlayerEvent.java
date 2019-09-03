package xiaokai.bemilk.event;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.item.Item;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;

import xiaokai.bemilk.data.DisPlayer;
import xiaokai.bemilk.data.MyPlayer;
import xiaokai.bemilk.form.MakeForm;
import xiaokai.bemilk.mtp.Belle;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.tool.data.ItemIDSunName;

/**
 * @author Winfxk
 */
public class PlayerEvent implements Listener {
	private Kick kick;

	public PlayerEvent(Kick kick) {
		this.kick = kick;
	}

	@EventHandler
	public void onSB(PlayerDropItemEvent e) {
		Item item = e.getItem();
		if (kick.config.getBoolean("是否允许玩家丢弃快捷工具") || !Belle.isMaterials(item))
			return;
		e.setCancelled();
		Player player = e.getPlayer();
		player.sendMessage(kick.Message.getMessage("撤销丢掉快捷工具的提示", new String[] { "{Player}", "{ItemName}", "{ItemID}" },
				new Object[] { player.getName(), ItemIDSunName.getIDByName(item.getId(), item.getDamage()),
						item.getId() + ":" + item.getDamage() }));
	}

	/**
	 * 玩家脑残了砸碎点击东西
	 * 
	 * @param e
	 */
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player player = e.getPlayer();
		if (Belle.isMaterials(e.getItem())) {
			MakeForm.Main(player);
			if (kick.config.getBoolean("打开撤销"))
				e.setCancelled();
		}
	}

	/**
	 * 玩家脑残了点击东西
	 * 
	 * @param e
	 */
	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		Action ac = e.getAction();
		if ((ac == Action.LEFT_CLICK_AIR || ac == Action.LEFT_CLICK_BLOCK || ac == Action.RIGHT_CLICK_AIR
				|| ac == Action.RIGHT_CLICK_BLOCK) && Belle.isMaterials(e.getItem())) {
			MakeForm.Main(player);
			if (kick.config.getBoolean("打开撤销"))
				e.setCancelled();
		}
	}

	/**
	 * 玩家滚蛋事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
		if (myPlayer != null && myPlayer.config != null)
			myPlayer.config.save();
		if (kick.PlayerDataMap.containsKey(player.getName()))
			kick.PlayerDataMap.remove(player.getName());
	}

	/**
	 * 玩家进♂来事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		if (!kick.PlayerDataMap.containsKey(player.getName()))
			kick.PlayerDataMap.put(player.getName(), new MyPlayer(player));
		if (!DisPlayer.isConfig(player)) {
			kick.mis.getLogger().info("§6未检测到玩家§9" + player.getName() + "§6的数据！正在创建...");
			DisPlayer.initializePlayerConfig(player);
		}
		DisPlayer.inspect(player.getName());
	}

	/**
	 * 监听玩家嗝了屁重新破壳的事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerSpawn(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		if (!kick.PlayerDataMap.containsKey(player.getName()))
			kick.PlayerDataMap.put(player.getName(), new MyPlayer(player));
		if (kick.config.getBoolean("重生进服等事件检测玩家快捷工具持有"))
			Belle.exMaterials(player);
		if (kick.config.getBoolean("玩家重生时重置玩家缓存数据")) {
			kick.PlayerDataMap.get(player.getName()).config.save();
			kick.PlayerDataMap.put(player.getName(), new MyPlayer(player));
		}
	}
}
