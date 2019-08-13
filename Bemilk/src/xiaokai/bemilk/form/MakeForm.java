package xiaokai.bemilk.form;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;
import xiaokai.bemilk.mtp.Kick;
import xiaokai.tool.SimpleForm;

/**
 * @author Winfxk
 */
@SuppressWarnings("unchecked")
public class MakeForm {
	public static boolean Main(Player player) {
		Kick kick = Kick.kick;
		File file = new File(kick.mis.getDataFolder(), kick.ShopConfigName);
		Config config = new Config(file, Config.YAML);
		Map<String, Object> Shops = (config.get("Shops") == null || !(config.get("Shops") instanceof Map))
				? new HashMap<String, Object>()
				: (HashMap<String, Object>) config.get("Shops");
		SimpleForm form = new SimpleForm(kick.formID.getID(0),
				kick.Message.getText(config.get("Title"), new String[] { "{Player}" },
						new Object[] { player.getName() }),
				kick.Message.getText(config.get("Content"), new String[] { "{Player}" },
						new Object[] { player.getName() }));
		if (Shops.size() < 1)
			form.setContent(form.getContent() + "\n");
		return true;
	}

}
