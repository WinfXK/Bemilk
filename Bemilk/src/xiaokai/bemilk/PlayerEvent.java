package xiaokai.bemilk;

import xiaokai.bemilk.mtp.Belle;
import xiaokai.bemilk.mtp.DisPlayer;
import xiaokai.bemilk.mtp.FormID;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.mtp.MyPlayer;
import xiaokai.bemilk.set.delMyshopBlItem;
import xiaokai.bemilk.shop.addShop;
import xiaokai.bemilk.shop.delShop;
import xiaokai.bemilk.shop.delShopItem;
import xiaokai.bemilk.shop.setShop;
import xiaokai.bemilk.shop.add.Command;
import xiaokai.bemilk.shop.add.ItemEnchant;
import xiaokai.bemilk.shop.add.ItemRepair;
import xiaokai.bemilk.shop.add.ItemTradeItem;
import xiaokai.bemilk.shop.add.MyShopReceive;
import xiaokai.bemilk.shop.add.ShopOrSell;
import xiaokai.bemilk.shop.add.addShopItem;
import xiaokai.bemilk.shop.add.effect.addEffect;
import xiaokai.bemilk.tool.ItemIDSunName;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.item.Item;

/**
 * @author Winfxk
 */
public class PlayerEvent implements Listener {
	private Kick kick;

	public PlayerEvent(Kick kick) {
		this.kick = kick;
	}

	/**
	 * 表单响应事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerForm(PlayerFormRespondedEvent e) {
		FormResponse data = e.getResponse();
		int ID = e.getFormID();
		FormID f = kick.formID;
		Player player = e.getPlayer();
		if (player == null || e.wasClosed() || e.getResponse() == null
				|| (!(e.getResponse() instanceof FormResponseCustom) && !(e.getResponse() instanceof FormResponseSimple)
						&& !(e.getResponse() instanceof FormResponseModal)))
			return;
		try {
			MyPlayer myPlayer = Kick.kick.PlayerDataMap.get(player.getName());
			if (f.getID(0) == ID)
				Dispose.Main(player, (FormResponseSimple) data);
			else if (f.getID(1) == ID)
				Dispose.MoreSettings(player, (FormResponseSimple) data);
			else if (f.getID(2) == ID)
				Dispose.OpenShop(player, (FormResponseSimple) data);
			else if (f.getID(3) == ID)
				addShop.Dispose(player, (FormResponseCustom) data);
			else if (f.getID(4) == ID)
				setShop.start(player, (FormResponseSimple) data);
			else if (f.getID(5) == ID)
				delShop.start(player, (FormResponseSimple) data);
			else if (f.getID(6) == ID)
				delShop.dis(player, (FormResponseSimple) data);
			else if (f.getID(7) == ID)
				addShopItem.disShellOrSellMakeForm(player, (FormResponseSimple) data);
			else if (f.getID(8) == ID)
				ShopOrSell.disInventoryGetItem(player, (FormResponseSimple) data);
			else if (f.getID(9) == ID)
				ShopOrSell.disInventoryGetItemIsData(player, (FormResponseCustom) data);
			else if (f.getID(10) == ID)
				ShopOrSell.startAddShopItemInventory(player, (FormResponseCustom) data);
			else if (f.getID(11) == ID)
				ShopOrSell.disInputItem(player, (FormResponseCustom) data);
			else if (f.getID(12) == ID)
				ItemTradeItem.disInputItem(player, (FormResponseCustom) data);
			else if (f.getID(13) == ID)
				ItemTradeItem.disInventoryGetItem(player, (FormResponseSimple) data);
			else if (f.getID(14) == ID)
				ItemTradeItem.dismakeItemCount(player, (FormResponseCustom) data);
			else if (f.getID(15) == ID)
				ItemTradeItem.disMakeFormInventoryGetItemIsOKStop(player, (FormResponseCustom) data);
			else if (f.getID(16) == ID)
				ItemEnchant.disMakeMain(player, (FormResponseSimple) data);
			else if (f.getID(17) == ID)
				new ItemEnchant.Dis(player, (FormResponseCustom) data).disMakeForm();
			else if (f.getID(18) == ID)
				new ItemRepair(player).disMakeMain((FormResponseSimple) data);
			else if (f.getID(19) == ID)
				new ItemRepair(player).disAdd((FormResponseCustom) data);
			else if (f.getID(20) == ID)
				addShopItem.disMakeForm(player, (FormResponseSimple) data);
			else if (f.getID(21) == ID)
				ItemTradeItem.disInventoryGetItem(player, (FormResponseSimple) data);
			else if (f.getID(22) == ID)
				delShopItem.disMakeForm(player, (FormResponseSimple) data);
			else if (f.getID(23) == ID)
				delShopItem.disOK(player, (FormResponseSimple) data);
			else if (f.getID(24) == ID)
				MyShopReceive.MakeMain(player, (FormResponseSimple) data);
			else if (f.getID(25) == ID)
				MyShopReceive.InputItem(player, (FormResponseCustom) data);
			else if (f.getID(26) == ID)
				MyShopReceive.InventoryGetItem(player, (FormResponseCustom) data);
			else if (f.getID(27) == ID && myPlayer != null && myPlayer.OpenShopDis != null)
				myPlayer.OpenShopDis.disMain((FormResponseCustom) data);
			else if (f.getID(28) == ID)
				Dispose.ddisSeek(player, (FormResponseSimple) data);
			else if (f.getID(29) == ID)
				Dispose.disSeek(player, (FormResponseCustom) data);
			else if (f.getID(30) == ID)
				Dispose.disSHopSeek(player, (FormResponseCustom) data);
			else if (f.getID(31) == ID)
				addEffect.disMain(player, (FormResponseCustom) data);
			else if (f.getID(32) == ID && myPlayer != null && myPlayer.makeBaseEffect != null)
				myPlayer.makeBaseEffect.disMain((FormResponseCustom) data);
			else if (f.getID(33) == ID)
				Dispose.SettingSwitch(player, (FormResponseSimple) data);
			else if (f.getID(34) == ID && myPlayer != null && myPlayer.baseset != null)
				myPlayer.baseset.disMain(data);
			else if (f.getID(35) == ID && myPlayer != null && myPlayer.basesetForm != null)
				myPlayer.basesetForm.disMain(data);
			else if (f.getID(36) == ID)
				delMyshopBlItem.Del(player, (FormResponseSimple) data);
			else if (f.getID(37) == ID)
				Command.disMakeForm(player, (FormResponseCustom) data);
		} catch (Exception e2) {
			e2.printStackTrace();
			kick.mis.getLogger().error("ID为" + ID + "的表单数据发生错误！");
			MakeForm.Tip(player,
					kick.Message.getSon("界面", "界面显示失败", new String[] { "{Player}", "{Money}", "{Error}" },
							new Object[] { player.getName(), DisPlayer.getMoney(player.getName()),
									"ID为" + ID + "的表单数据发生错误！\n" + e2.getMessage() }));
		}
	}

	/**
	 * 玩家吃多了没事干想丢东西
	 * 
	 * @param e
	 */
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
