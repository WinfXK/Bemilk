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
import xiaokai.bemilk.shop.ShopRegulate.addShop.ShopOrSell;

/**
 * @author Winfxk
 */
public class Monitor implements Listener {
	private Kick kick;

	public Monitor(Kick kick) {
		this.kick = kick;
	}

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
			ShopOrSell.disShellOrSellMakeForm(player, (FormResponseSimple) data);
		else if (f.getID(8) == ID)
			ShopOrSell.disInventoryGetItem(player, (FormResponseSimple) data);
		else if (f.getID(9) == ID)
			ShopOrSell.disInventoryGetItemIsData(player, (FormResponseCustom) data);
	}
}
