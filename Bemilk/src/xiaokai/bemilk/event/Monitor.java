package xiaokai.bemilk.event;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.response.FormResponseSimple;
import xiaokai.bemilk.form.Dispose;
import xiaokai.bemilk.form.Paging;
import xiaokai.bemilk.mtp.FormID;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.bemilk.shop.ShopRegulate.addShop;
import xiaokai.bemilk.shop.ShopRegulate.addShop.ItemEnchant;
import xiaokai.bemilk.shop.ShopRegulate.addShop.ItemRepair;
import xiaokai.bemilk.shop.ShopRegulate.addShop.ItemTradeItem;
import xiaokai.bemilk.shop.ShopRegulate.addShop.ShopOrSell;

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
			Paging.addShop.Dispose(player, (FormResponseCustom) data);
		else if (f.getID(4) == ID)
			Paging.setShop.start(player, (FormResponseSimple) data);
		else if (f.getID(5) == ID)
			Paging.delShop.start(player, (FormResponseSimple) data);
		else if (f.getID(6) == ID)
			Paging.delShop.dis(player, (FormResponseSimple) data);
		else if (f.getID(7) == ID)
			addShop.disShellOrSellMakeForm(player, (FormResponseSimple) data);
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
			addShop.disMakeForm(player, (FormResponseSimple) data);
	}
}
