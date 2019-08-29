package xiaokai.bemilk.shop.open;

import java.io.File;
import java.util.Map;

import cn.nukkit.Player;
import cn.nukkit.utils.Config;

/**
 * 用来临时存储玩家打开的商店项目的对象
 * 
 * @author Winfxk
 */
public class ShopData {
	/**
	 * 打开商店项目的玩家对象
	 */
	public Player player;
	/**
	 * 打开的商店的文件对象
	 */
	public File file;
	/**
	 * 打开的项目的Key
	 */
	public String Key;
	/**
	 * 打开的商店所在的配置文件的对象
	 */
	public Config config;
	/**
	 * 打开的项目所属的数据
	 */
	public Map<String, Object> Item;
	/**
	 * 商店的项目列表
	 */
	public Map<String, Object> Shops;
	/**
	 * 打开的项目类型
	 */
	public String Type;
}
