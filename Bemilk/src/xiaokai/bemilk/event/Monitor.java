package xiaokai.bemilk.event;

import xiaokai.bemilk.data.FormID;
import xiaokai.bemilk.form.Dispose;
import xiaokai.bemilk.form.manage.addShop;
import xiaokai.bemilk.form.manage.delShop;
import xiaokai.bemilk.form.manage.setShop;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.shop.delShopItem;
import xiaokai.bemilk.shop.add.ItemEnchant;
import xiaokai.bemilk.shop.add.ItemRepair;
import xiaokai.bemilk.shop.add.ItemTradeItem;
import xiaokai.bemilk.shop.add.ShopOrSell;
import xiaokai.bemilk.shop.add.addShopItem;
import xiaokai.bemilk.shop.myshop.Receive;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;

/**
 * @author Winfxk
 */
public class Monitor implements Listener {
	private Kick kick;

	/**
	 * 处理玩家UI表单数据回调事件的类
	 * 
	 * @param kick
	 */
	public Monitor(Kick kick) {
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
			Receive.MakeMain(player, (FormResponseSimple) data);
		else if (f.getID(25) == ID)
			Receive.InputItem(player, (FormResponseCustom) data);
		else if (f.getID(26) == ID)
			Receive.InventoryGetItem(player, (FormResponseCustom) data);
	}
}
