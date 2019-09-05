package xiaokai.bemilk.data;
/**
 * @author Winfxk
 */

import xiaokai.bemilk.set.Baseset;
import xiaokai.bemilk.shop.add.effect.makeBaseEffect;
import xiaokai.bemilk.shop.open.BaseDis;

import java.io.File;
import java.time.Instant;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

public class MyPlayer {
	/**
	 * 玩家上次成功使用快捷菜单的时间
	 */
	public Instant loadTime;
	public Player player;
	/**
	 * 某些界面用来存储给额外按钮的key
	 */
	public List<String> ExtraKeys;
	/**
	 * 存储玩家看到的界面的按钮的Key信息
	 */
	public List<String> Keys;
	/**
	 * 玩家配置文件对象
	 */
	public Config config = null;
	/**
	 * 当前显示页面的文件对象
	 */
	public File file;
	/**
	 * 用于判断玩家到底是在创建商店分页还是修改商店分页
	 */
	public boolean isMake = false;
	/**
	 * 玩家在修改某按钮的时候临时存储按钮的Key用的
	 */
	public String CacheKey;
	/**
	 * 用来临时存储一些字符串，例如说商店分页的文件名什么的
	 */
	public String string;
	/**
	 * 判断从背包添加物品到商店的时候是不是第二或之后的界面了
	 */
	public boolean isInventoryGetItem2 = false;
	/**
	 * 从背包添加物品到商店的时候已经确认添加的物品
	 */
	public List<Item> addIsItem;
	/**
	 * 同上，这是存储物品兑换物品的时候用来当做货币兑换物品的物品列表，这是已经确定要添加的
	 */
	public List<Item> addItemTradeItems;
	/**
	 * 从背包添加物品到商店的时候显示的物品列表
	 */
	public List<Item> addIsItemList;
	/**
	 * 缓存物品对象
	 */
	public Item CacheItem;
	/**
	 * 判断是出售还是回收类型的按钮 </br>
	 * <b> true: 出售 </br>
	 * false：回收</b>
	 */
	public boolean isShopOrSell = false;
	/**
	 * 存储玩家是不是在设置兑换所需的物品列表,
	 */
	public boolean isGetItemSSXXXX = false;
	/**
	 * 存储一些临时缓存
	 */
	public int CacheInt;
	/**
	 * 存储临时Integer組
	 */
	public List<Integer> integers;
	/**
	 * 打开大开商店项目的处理类
	 */
	public BaseDis OpenShopDis;
	/**
	 * 搜索结果存储地
	 */
	public List<SeekData> SeekData;
	/**
	 * 添加药水项目的处理类
	 */
	public makeBaseEffect makeBaseEffect;
	/**
	 * 服务器配置构建类
	 */
	public Baseset baseset;

	public MyPlayer(Player player) {
		this.player = player;
		config = new Config(DisPlayer.getFile(player), Config.YAML);
	}
}
