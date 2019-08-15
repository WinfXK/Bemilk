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
import xiaokai.bemilk.mtp.FormID;
import xiaokai.bemilk.mtp.Kick;

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
			new Dispose(player).Main((FormResponseSimple) data);
	}
}
