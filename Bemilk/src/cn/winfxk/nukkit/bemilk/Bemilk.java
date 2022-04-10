package cn.winfxk.nukkit.bemilk;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.event.player.PlayerRespawnEvent;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.winfxk.nukkit.winfxklib.Check;
import cn.winfxk.nukkit.winfxklib.Message;
import cn.winfxk.nukkit.winfxklib.MyBase;
import cn.winfxk.nukkit.winfxklib.tool.Config;
import cn.winfxk.nukkit.winfxklib.tool.Enchantlist;
import cn.winfxk.nukkit.winfxklib.tool.Itemlist;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Bemilk extends MyBase implements Listener {
    private Instant loadTime;
    public static final String MsgConfigName = "Message.yml";
    public static final String CommandFileName = "Command.yml";
    public static final String ShopFileName = "Shop.yml";
    public static final String ConfigFileName = "Config.yml";
    public static final String ConfigDirName = "Configs";
    public static final String ShopDirName = "Shops";
    public static final String PlayerDirName = "Players";
    public static final String FastNBT = "冰月超级无敌帅！宇宙第一帅！！！！帅呆啦~";
    public static final String[] Meta = {MsgConfigName, ConfigFileName, CommandFileName};
    public static final String[] MakeDirs = {ConfigDirName, PlayerDirName, ShopDirName};
    protected static final Map<String, MyPlayer> MyPlayers = new LinkedHashMap<>();
    public File ConfigDir;
    public static Bemilk main;
    public static Config config;
    public static Message message;
    private Itemlist itemlist = Itemlist.getItemlist();

    @EventHandler
    public void onClcik(PlayerInteractEvent event) {
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        MyPlayers.put(player.getName(), new MyPlayer(player));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        MyPlayers.remove(player.getName());
    }

    /**
     * 判断一个玩家是否拥有快捷工具
     *
     * @param player
     * @return
     */
    public boolean HaveFastTool(Player player) {
        PlayerInventory inventory = player.getInventory();
        for (Item item : inventory.getContents().values())
            if (isFastTool(item)) return true;
        return false;
    }

    /**
     * 判断一个物品是否是快捷工具
     *
     * @param item
     * @return
     */
    public boolean isFastTool(Item item) {
        CompoundTag nbt = item.getNamedTag();
        if (nbt == null) return false;
        return nbt.getString(getName()).equals(FastNBT);
    }

    /**
     * 取得一个快捷工具的Item对象
     *
     * @param player 要取得快捷工具的玩家
     * @return
     */
    public Item getFastTool(Player player) {
        String SID = config.getString("快捷工具", "指南针");
        Itemlist MyItem = itemlist.getItem(SID, true, null);
        Item item;
        if (MyItem != null) item = MyItem.getItem();
        else item = itemlist.setItem(SID, (Item) null);
        if (item == null) throw new RuntimeException("无法获取快捷工具物品对象！");
        item.setCustomName(message.getMessage("快捷工具名称", player));
        item.setLore(message.getMessage("快捷工具Lore", player));
        List<Object> list = config.getList("快捷工具附魔");
        Enchantlist enchantlist;
        for (Object obj : list) {
            enchantlist = Enchantlist.getEnchantlist().getEnchant(obj, null);
            if (enchantlist != null)
                item.addEnchantment(enchantlist.getEnchantment());
        }
        CompoundTag nbt = item.getNamedTag();
        nbt = nbt == null ? new CompoundTag() : nbt;
        nbt.putString(getName(), FastNBT);
        item.setNamedTag(nbt);
        return item;
    }

    @Override
    public void onEnable() {
        loadTime = Instant.now();
        super.onEnable();
        ConfigDir = new File(getDataFolder(), ConfigDirName);
        config = new Config(new File(ConfigDir, ConfigFileName));
        new Check(this, Meta, MakeDirs).start();
        message = new Message(this, new File(ConfigDir, MsgConfigName));
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info(message.getMessage("插件启动", "{loadTime}", (float) Duration.between(loadTime, Instant.now()).toMillis() + "ms"));
    }

    @Override
    public void onLoad() {
        getLogger().info(getName() + " Load....");
        main = this;
        super.onLoad();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        getLogger().info(message.getMessage("插件关闭", "{loadTime}", (float) Duration.between(loadTime, Instant.now()).toMillis() + "ms"));
    }

    /**
     * 返回玩家缓存对象
     *
     * @param player 玩家
     * @return 返回玩家缓存对象
     */
    public static MyPlayer getMyPlayer(Player player) {
        return MyPlayers.get(player.getName());
    }

    /**
     * 返回玩家缓存对象
     *
     * @param player 玩家名称
     * @return 返回玩家缓存对象
     */
    public static MyPlayer getMyPlayer(String player) {
        return MyPlayers.get(player);
    }

    /**
     * 返回玩家缓存对象集合
     *
     * @return 缓存对象集合
     */
    public static Map<String, MyPlayer> getMyPlayers() {
        return MyPlayers;
    }

    @Override
    public File getConfigDir() {
        return ConfigDir;
    }
}
